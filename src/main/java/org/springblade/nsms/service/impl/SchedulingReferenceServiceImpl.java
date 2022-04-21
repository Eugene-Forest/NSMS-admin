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

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.support.Condition;
import org.springblade.nsms.entity.NurseInfo;
import org.springblade.nsms.entity.SchedulingReference;
import org.springblade.nsms.mapper.SchedulingReferenceMapper;
import org.springblade.nsms.service.ISchedulingReferenceService;
import org.springblade.nsms.tools.Constant;
import org.springblade.nsms.tools.ServiceImplUtil;
import org.springblade.nsms.vo.SchedulingReferenceVO;
import org.springblade.nsms.wrapper.SchedulingReferenceWrapper;
import org.springblade.rewrite.FoundationServiceImpl;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 排班依据表 服务实现类
 *
 * @author Blade
 * @since 2022-04-18
 */
@Service
public class SchedulingReferenceServiceImpl extends FoundationServiceImpl<SchedulingReferenceMapper, SchedulingReference> implements ISchedulingReferenceService {

	@Override
	public IPage<SchedulingReferenceVO> selectSchedulingReferencePage(IPage<SchedulingReferenceVO> page, SchedulingReferenceVO schedulingReference) {
		return page.setRecords(baseMapper.selectSchedulingReferencePage(page, schedulingReference));
	}

	@Override
	public boolean saveOrUpdateEntity(SchedulingReferenceVO schedulingReferenceVO) {
		try {
			//对添加或修改的数据进行逻辑验证
			List<String> dateList=schedulingReferenceVO.getDateRange();
			if (dateList.size()==2){
				//解析封装并赋值
				schedulingReferenceVO.setStartDate(DateFormat.getDateInstance().parse(dateList.get(0)));
				schedulingReferenceVO.setEndDate(DateFormat.getDateInstance().parse(dateList.get(1)));
				if (schedulingReferenceVO.getEndDate().before(schedulingReferenceVO.getStartDate())){
					throw new Exception("日期区间错误！");
				}
				//针对一些字段进行初始化
				if (schedulingReferenceVO.getId()==null){
					//新建时需要对部门以及审批状态进行初始化
					schedulingReferenceVO.setDepartmentId(ServiceImplUtil.getNurseInfoFromUser().getDepartment());
					schedulingReferenceVO.setState(0);
				}
				return this.saveOrUpdate(schedulingReferenceVO);
			}
			return false;
		}catch (Exception e){
			throw new RuntimeException("请确保提交的数据符合规范。");
		}
	}

	/**
	 * 作为一个护士助手选择期望对应的排班配置的结果
	 *
	 * @return
	 */
	@Override
	public List<SchedulingReferenceVO> selectByUserDept() {
		NurseInfo nurseInfo=ServiceImplUtil.getNurseInfoFromUser();
		List<SchedulingReference> schedulingReferences=
			this.list(
				Condition.getQueryWrapper(new SchedulingReference())
					.eq("department_id", nurseInfo.getDepartment())
					.eq("tenant_id", nurseInfo.getTenantId())
					.eq("state", Constant.REFERENCE_CONFIG_STATE_ADD_EXPECTATION));
		return schedulingReferences
			.stream()
			.map(x->SchedulingReferenceWrapper.build().resolveEntityForSelect(x))
			.collect(Collectors.toList());
	}


}
