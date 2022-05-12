/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblade.nsms.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.common.tool.SpringBeanUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.nsms.entity.Expectation;
import org.springblade.nsms.entity.SchedulingReference;
import org.springblade.nsms.mapper.ExpectationMapper;
import org.springblade.nsms.service.IExpectationService;
import org.springblade.nsms.tools.Constant;
import org.springblade.nsms.tools.ServiceImplUtil;
import org.springblade.nsms.vo.ExpectationVO;
import org.springblade.rewrite.FoundationEntity;
import org.springblade.rewrite.FoundationServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * 护士助手的排班期望表 服务实现类
 *
 * @author Blade
 * @since 2022-04-18
 */
@Service
public class ExpectationServiceImpl extends FoundationServiceImpl<ExpectationMapper, Expectation> implements IExpectationService {


	@Override
	public IPage<ExpectationVO> selectExpectationPage(IPage<ExpectationVO> page, ExpectationVO expectation) {
		return page.setRecords(baseMapper.selectExpectationPage(page, expectation));
	}

	/**
	 * 获取调用人的对于某排班配置表的期望的目前优先级
	 *
	 * @param referenceSid
	 */
	@Override
	public Integer getPriority(String referenceSid) {
		Integer maxPriority=baseMapper.getPriority(referenceSid,Func.toStr(ServiceImplUtil.getUserId()));
		if (maxPriority==null){
			return 1;
		}
		return (maxPriority+1);
	}

	@Override
	public boolean saveOrUpdateExpectationVO(ExpectationVO expectation) {
		try {
			//在添加或更新前先进行对排班配置表的状态
			SchedulingReference schedulingReference=
				SpringBeanUtil.getApplicationContext()
					.getBean(SchedulingReferenceServiceImpl.class).getById(expectation.getReferenceSid());
			if (!schedulingReference.getState().equals(Constant.SCHEDULING_REFERENCE_CONFIG_ADD_EXPECTATION)){
				throw new RuntimeException("排班配置表状态已改变，请刷新期望录入界面！");
			}
			List<String> dateList=expectation.getDateRange();
			if (dateList==null||dateList.size()!=2){
				throw new Exception("提交的数据异常");
			}
			expectation.setStartDate(DateFormat.getDateInstance().parse(dateList.get(0)));
			expectation.setEndDate(DateFormat.getDateInstance().parse(dateList.get(1)));
			expectation.setNurseSid(ServiceImplUtil.getNurseIdFromUser());
			expectation.setActualState(Constant.ACTUAL_STATE_WAIT);

			//todo 判断是添加还是编辑，并对其时间区间以及期望合理性进行校验
			//天数期望每种类型只能存在一种，并且所有天数期望之和小于等于排班时间天数

			//如果日期期望需要判断天数期望以及排班区间的天数限制

			boolean state=this.saveOrUpdate(expectation);
			return state;
		}catch (Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public boolean deleteExpectationVO(List<Expectation> expectationList) {
		List<Long> ids=expectationList.stream().map(FoundationEntity::getId).collect(Collectors.toList());
		//删除期望之前需要确认其不会影响
		Expectation origin;
		if (ids!=null&&ids.get(0)!=null){
			origin=baseMapper.selectById(ids.get(0));
		}else {
			throw new RuntimeException("需要被删除的数据的id为空");
		}
		if (origin==null){
			throw new RuntimeException("需要被删除的数据id有误");
		}
		//在添加或更新前先进行对排班配置表的状态
		SchedulingReference schedulingReference=
			SpringBeanUtil.getApplicationContext()
				.getBean(SchedulingReferenceServiceImpl.class).getById(origin.getReferenceSid());
		if (!schedulingReference.getState().equals(Constant.SCHEDULING_REFERENCE_CONFIG_ADD_EXPECTATION)){
			throw new RuntimeException("排班配置表状态已改变，请刷新期望录入界面！");
		}

		//删除期望需要注意对后续期望的影响
//		先获取现有的本人的此次排班的所有的期望数据
		List<Expectation> originList=baseMapper.selectList(
			Condition.getQueryWrapper(new Expectation())
				.eq("tenant_id", ServiceImplUtil.getUserTenantId())
				.eq("create_user", ServiceImplUtil.getUserId())
				.eq("reference_sid", origin.getReferenceSid())
				.eq("is_deleted", Constant.RECORD_IS_NOT_DELETED)
		);
		//判断是不是所有的待删除数据都是在originList中
		List<Long> originIds=originList.stream().map(FoundationEntity::getId).collect(Collectors.toList());
		if (!originIds.containsAll(ids)){
			throw new RuntimeException("需要被删除的数据id有误,重新确认");
		}
		boolean flag=true;
		// 删除
		flag=flag&&deleteLogic(expectationList);
		//删除选中的部分，将剩余的部分由小到大重新编号
		originList=originList.stream().filter(x->!(ids.contains(x.getId()))).collect(Collectors.toList());
		//升序排序
		originList=originList.stream().sorted(Comparator.comparing(Expectation::getPriority)).collect(Collectors.toList());
		//按顺序保存
		for(int i=0;i<originList.size();i++){
			Expectation expectation=originList.get(i);
			expectation.setPriority(i+1);
			boolean s=this.saveOrUpdate(expectation);
			//todo 需要一些判断是否成功执行这些更新操作
			if (s){
				continue;
			}else {
				flag=false;
				//抛出异常事务回滚
				break;
			}
		}
		return flag;
	}


	/**
	 * 对数据进行了简单处理的分页查询；（数据权限功能待添加）
	 *
	 * @param queryWrapper
	 * @param expectation
	 * @return
	 */
	@Override
	public IPage<ExpectationVO> selectExpectationPage(Wrapper<ExpectationVO> queryWrapper, ExpectationVO expectation) {
		//需要对调用此接口的人进行身份验证，并通过其不同级别的身份进行不同的查询
		//目前有两种数据查询的区别：全查询以及相关数据查询
		//对于普通员工只有相关数据查询的权限，而对于护士长以及以上的身份人有全查询权限
		//再进行细分：护士长以及以上的身份
		//科室护士长只能看其所在部门的所有数据
		//总护士长及其以上（管理员）的身份可以看所有的数据
		//todo 关于数据权限部分的实现依据情况用框架的注解或代码逻辑实现

		//实现对数据的处理

		return null;
	}

}
