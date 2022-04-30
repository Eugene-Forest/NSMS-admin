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
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.nsms.dto.*;
import org.springblade.nsms.entity.Expectation;
import org.springblade.nsms.entity.NurseInfo;
import org.springblade.nsms.entity.SchedulingReference;
import org.springblade.nsms.entity.StaffTime;
import org.springblade.nsms.mapper.SchedulingReferenceMapper;
import org.springblade.nsms.service.ISchedulingReferenceService;
import org.springblade.nsms.tools.Constant;
import org.springblade.nsms.tools.SchedulingUtil;
import org.springblade.nsms.tools.ServiceImplUtil;
import org.springblade.nsms.vo.SchedulingReferenceVO;
import org.springblade.nsms.wrapper.SchedulingReferenceWrapper;
import org.springblade.rewrite.FoundationServiceImpl;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
					.eq("state", Constant.SCHEDULING_REFERENCE_CONFIG_ADD_EXPECTATION));
		return schedulingReferences
			.stream()
			.map(x->SchedulingReferenceWrapper.build().resolveEntityForSelect(x))
			.collect(Collectors.toList());
	}

	/**
	 * 改变排班配置表的状态为待排班、期望输入、或未启用
	 *
	 * @param schedulingReferenceVO
	 * @return
	 */
	@Override
	public boolean changeReferenceConfigState(SchedulingReferenceVO schedulingReferenceVO) {
		//todo 身份验证
		//排班前的状态变换只能在前三种情况之下： 未启用、期望录入、待排班
		if (
			!schedulingReferenceVO.getState().equals(Constant.SCHEDULING_REFERENCE_CONFIG_NOT_ENABLED)
			&& !schedulingReferenceVO.getState().equals(Constant.SCHEDULING_REFERENCE_CONFIG_ADD_EXPECTATION)
			&& !schedulingReferenceVO.getState().equals(Constant.SCHEDULING_REFERENCE_CONFIG_WAITING_FOR_SCHEDULING)
		){
			throw new RuntimeException("更改的状态值异常，请重新提交！");
		}
		//获取源数据
		SchedulingReference origin=baseMapper.selectById(
			schedulingReferenceVO.getId()
		);
		if (origin==null){
			throw new RuntimeException("数据异常，请重新提交");
		}
		//判断配置原状态是否时排班前
		if (
			!origin.getState().equals(Constant.SCHEDULING_REFERENCE_CONFIG_NOT_ENABLED)
				&& !origin.getState().equals(Constant.SCHEDULING_REFERENCE_CONFIG_ADD_EXPECTATION)
				&& !origin.getState().equals(Constant.SCHEDULING_REFERENCE_CONFIG_WAITING_FOR_SCHEDULING)
		){
			throw new RuntimeException("更改的状态值异常，请重新提交！");
		}
		origin.setState(schedulingReferenceVO.getState());
		return this.saveOrUpdate(origin);
	}

	/**
	 * 改变排班配置表的状态为待排班
	 *
	 * @param schedulingReferenceVO
	 * @return
	 */
	@Override
	public boolean recheckReferenceConfigState(SchedulingReferenceVO schedulingReferenceVO) {
		//todo 改变排班配置表的状态为待排班
		//获取源数据
		SchedulingReference origin=baseMapper.selectById(
			schedulingReferenceVO.getId()
		);
		if (origin==null){
			throw new RuntimeException("数据异常，请重新提交");
		}
		//判断配置原状态是否围为排班结果
		if (
			!origin.getState().equals(Constant.SCHEDULING_REFERENCE_CONFIG_SCHEDULING_FAILURE)
			&& !origin.getState().equals(Constant.SCHEDULING_REFERENCE_CONFIG_SCHEDULING_SUCCESS)
		){
			throw new RuntimeException("数据异常的状态值异常，请重新提交！");
		}
		//改为待排班并更新
		origin.setState(Constant.SCHEDULING_REFERENCE_CONFIG_WAITING_FOR_SCHEDULING);
		return this.saveOrUpdate(origin);
	}

	/**
	 * 排班
	 *
	 * @param schedulingReferenceVO
	 * @return
	 */
	@Override
	public boolean scheduling(SchedulingReferenceVO schedulingReferenceVO) {
		//获取排班配置的源数据
		SchedulingReference origin=baseMapper.selectById(
			schedulingReferenceVO.getId()
		);
		if (origin==null){
			throw new RuntimeException("排班配置的数据异常");
		}
		//获取必要的基础信息，科室人员信息，科室人员期望
		List<NurseDTO> nurseDTOList=
			SpringUtil.getContext().getBean(NurseInfoServiceImpl.class).selectAllBaseNurseFromSampDept()
				.stream().map(x-> new NurseDTO(x.getId(),x.getCategory())).collect(Collectors.toList());
		Map<Long,Integer> nursesPostTypeMap=new HashMap<>();
		nurseDTOList.forEach(x->{
			nursesPostTypeMap.put(x.getNurseId(),x.getPostType());
		});
		List<ExpectationDTO> expectationDTOList=
			SpringUtil.getContext().getBean(ExpectationServiceImpl.class).list(
				Condition.getQueryWrapper(new Expectation())
					.eq("tenant_id", ServiceImplUtil.getUserTenantId())
					.eq("create_dept", ServiceImplUtil.getNurseDeptFromUser())
					.eq("is_deleted", 0)
					.eq("reference_sid", origin.getId())
			).stream().map(x->new ExpectationDTO(x,nursesPostTypeMap.get(x.getNurseSid()))).collect(Collectors.toList());

		//创建排班姐结果对象
		ScheduleTable scheduleTable=new ScheduleTable(schedulingReferenceVO);

		//获取排班前一天的排班结果数据，并将其转换为 shiftPlanDTO
		List<PersonDTO> personDTOs=SpringUtil.getContext().getBean(StaffTimeServiceImpl.class).list(
			Condition.getQueryWrapper(new StaffTime())
				.eq("yearmonth", SchedulingUtil.lastDay(schedulingReferenceVO.getStartDate()))
				.eq("create_dept", schedulingReferenceVO.getDepartmentId())
		).stream()
			.filter(x->!x.getCategory().equals(Constant.SHIFT_TYPE_OF_DAY))
			.map(x->new PersonDTO(x.getNurseSid(),x.getCategory()))
			.collect(Collectors.toList());
		ShiftPlanDTO shiftPlanDTO=new ShiftPlanDTO();
		shiftPlanDTO.nightShiftsAdd(personDTOs);

		//执行排班算法
		SchedulingUtil.scheduling(nurseDTOList, expectationDTOList,
			scheduleTable,shiftPlanDTO);


		return false;
	}


}
