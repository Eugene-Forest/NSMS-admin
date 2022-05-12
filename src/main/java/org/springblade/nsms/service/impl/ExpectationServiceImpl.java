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

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.microsoft.schemas.office.visio.x2012.main.FaceNamesType;
import org.springblade.common.tool.SpringBeanUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.nsms.entity.Expectation;
import org.springblade.nsms.entity.NurseInfo;
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
import java.util.*;
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
			//判断期望的时间区间是否在排班配置的时间区间内
			Date startDate=DateUtil.parse(dateList.get(0), DateUtil.PATTERN_DATE);
			Date endDate=DateUtil.parse(dateList.get(1), DateUtil.PATTERN_DATE);
			expectation.setStartDate(startDate);
			expectation.setEndDate(endDate);
			expectation.setNurseSid(ServiceImplUtil.getNurseIdFromUser());
			expectation.setActualState(Constant.ACTUAL_STATE_WAIT);
			if (!ServiceImplUtil.compareDateIsBetweenOrNot(
				schedulingReference.getStartDate(),
				schedulingReference.getEndDate(),
				startDate)){
				throw new Exception("期望的时间区间不在排班配置时间范围内！");
			}
			if (!ServiceImplUtil.compareDateIsBetweenOrNot(
				schedulingReference.getStartDate(),
				schedulingReference.getEndDate(),
				endDate)){
				throw new Exception("期望的时间区间不在排班配置时间范围内！");
			}
			//获取本人在此排班配置中添加了的记录
			NurseInfo nurseInfo=ServiceImplUtil.getNurseInfoFromUser();
			List<Expectation> originList=this.list(
				Condition.getQueryWrapper(new Expectation())
				.eq("tenant_id", nurseInfo.getTenantId())
					.eq("create_dept", nurseInfo.getDepartment())
					.eq("create_user", nurseInfo.getUserId())
					.eq("reference_sid", schedulingReference.getId())
					.eq("is_deleted", 0));
			//todo 判断是添加还是编辑，并对其时间区间以及期望合理性进行校验
			//天数期望每种类型只能存在一种，并且所有天数期望之和小于等于排班时间天数
			if (checkExpectation(originList, expectation, schedulingReference)){
				// 满足以上判断说明可以添加或修改
				return this.saveOrUpdate(expectation);
			}else {
				return false;
			}
		}catch (Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}


	/**
	 * 期望的判断
	 * @param expectationList
	 * @param target
	 * @return
	 */
	private boolean checkExpectation(
		final List<Expectation> expectationList,Expectation target,SchedulingReference reference
	){
		//获取除了自身之外的期望
		List<Expectation> expectations=expectationList.stream().filter(
			x->!x.getId().equals(target.getId())
		).collect(Collectors.toList());
		//如果期源期望数组为空，那么就不用校验
		if (expectations.size()==0){
			return true;
		}
		//排班配置区间天数
		int schedulingDayNumber= (int) (DateUtil.between(reference.getStartDate(),reference.getEndDate()).toDays()+1);
		//日班日期天数期望天数之和
		int dayShiftNumber=getDayNumberFromExpectations(
			expectationList.stream().filter(x->x.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_SHIFT)).collect(Collectors.toList()));
		//日班日期天数期望天数之和
		int nightShiftNumber=getDayNumberFromExpectations(
			expectationList.stream().filter(x->x.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_SHIFT)).collect(Collectors.toList()));
		//假期日期天数期望天数之和
		int vacationNumber=getDayNumberFromExpectations(
			expectationList.stream().filter(x->x.getExpectationType().equals(Constant.EXPECTATION_TYPE_VACATION)).collect(Collectors.toList()));
		//日班天数期望天数
		int dayNumber=0;
		Optional<Expectation> dayNumberExpectationOptional=expectationList.stream().filter(x->x.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_NUMBER)).findFirst();
		if (dayNumberExpectationOptional.isPresent()){
			dayNumber=dayNumberExpectationOptional.get().getDayNumber();
		}
		//夜班天数期望
		int nightNumber=0;
		Optional<Expectation> nightNumberExpectationOptional=expectationList.stream().filter(x->x.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_NUMBER)).findFirst();
		if (nightNumberExpectationOptional.isPresent()){
			nightNumber=nightNumberExpectationOptional.get().getDayNumber();
		}

		int targetNumber=0;
		if (target.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_NUMBER)){
			//需要判断在源期望数组中还存在同类型的期望（针对新建时）
			for (Expectation expectation:expectations){
				if (expectation.getExpectationType().equals(target.getExpectationType())){
					throw new RuntimeException("已经存在夜班天数期望！");
				}
			}
			targetNumber=target.getDayNumber();
			//日班、夜班天数期望天数之和加上假期期望天数之和要小于等于排班配置的时间范
			int remainNumber=schedulingDayNumber-vacationNumber-dayNumber;
			if (targetNumber>remainNumber){
				throw new RuntimeException("剩余可被期望为夜班的天数为："+remainNumber+"天");
			}
			//夜班日期期望总天数小于等于夜班天数期望
			if (targetNumber<nightShiftNumber){
				throw new RuntimeException("已经存在夜班日期期望，所以夜班天数期望需要满足的最小值为："+nightShiftNumber);
			}
			return true;
		}else if (target.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_NUMBER)){
			for (Expectation expectation:expectations){
				if (expectation.getExpectationType().equals(target.getExpectationType())){
					throw new RuntimeException("已经存在日班天数期望！");
				}
			}
			targetNumber=target.getDayNumber();
			//日班、夜班天数期望天数之和加上假期期望天数之和要小于等于排班配置的时间范
			int remainNumber=schedulingDayNumber-vacationNumber-nightNumber;
			if (targetNumber>remainNumber){
				throw new RuntimeException("剩余可被期望为日班的天数为："+remainNumber+"天");
			}
			//夜班日期期望总天数小于等于夜班天数期望
			if (targetNumber<dayShiftNumber){
				throw new RuntimeException("已经存在日班日期期望，所以日班天数期望需要满足的最小值为："+nightShiftNumber);
			}
			return true;
		}else{
			targetNumber=getDayNumberFromExpectation(target);
			//再进行日期重合判断前，先判断天数是否符合
			//日期天数之和小于等于总天数
			int remainNumber=schedulingDayNumber-(dayShiftNumber+nightShiftNumber+vacationNumber);
			if (targetNumber>remainNumber){
				throw new RuntimeException("剩余可被添加期望的天数为："+remainNumber);
			}
			if (target.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_SHIFT)){
				//日期期望天数小于对应班次的天数期望的天数
				if (targetNumber>(dayNumber-dayShiftNumber)&&dayNumber!=0){
					throw new RuntimeException("已经存在日班天数期望！剩余可被指定具体日期的天数为"+(dayNumber-dayShiftNumber));
				}
			}
			if (target.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_SHIFT)){
				//日期期望天数小于对应班次的天数期望的天数
				if (targetNumber>(nightNumber-nightShiftNumber)&&nightNumber!=0){
					throw new RuntimeException("已经存在夜班天数期望！剩余可被指定具体日期的天数为"+(nightNumber-nightShiftNumber));
				}
			}
			//日期期望判断是否由日期重合
			List<Expectation> dateExpectations=expectations.stream().filter(
				x->x.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_SHIFT)
				||x.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_SHIFT)
				||x.getExpectationType().equals(Constant.EXPECTATION_TYPE_VACATION)
			).collect(Collectors.toList());
			//并判断与期望时间是否冲突
			for (Expectation expectation:dateExpectations){
				boolean flag=ServiceImplUtil.compareDateIsBetweenOrNot(
					expectation.getStartDate(),expectation.getEndDate(),target.getStartDate());
				if (flag){
					throw new RuntimeException("添加的期望的日期与现存的日期期望时间冲突");
				}
				flag=ServiceImplUtil.compareDateIsBetweenOrNot(
					expectation.getStartDate(),expectation.getEndDate(),target.getEndDate());
				if (flag){
					throw new RuntimeException("添加的期望的日期与现存的日期期望时间冲突");
				}
			}
			return true;
		}
	}

	public int getDayNumberFromExpectations(List<Expectation> targets){
		int dayNumber=0;
		for (Expectation expectation:targets){
			dayNumber=dayNumber+getDayNumberFromExpectation(expectation);
		}
		return dayNumber;
	}

	public int getDayNumberFromExpectation(Expectation target){
		return (int)(DateUtil.between(target.getStartDate(),target.getEndDate()).toDays()+1);
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
