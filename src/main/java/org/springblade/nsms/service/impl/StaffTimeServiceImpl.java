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

import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.nsms.dto.NurseInfoDTO;
import org.springblade.nsms.entity.NurseInfo;
import org.springblade.nsms.entity.StaffTime;
import org.springblade.nsms.tools.Constant;
import org.springblade.nsms.tools.ServiceImplUtil;
import org.springblade.nsms.vo.NurseInfoVO;
import org.springblade.nsms.vo.StaffTimeVO;
import org.springblade.nsms.mapper.StaffTimeMapper;
import org.springblade.nsms.service.IStaffTimeService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.nsms.wrapper.StaffTimeWrapper;
import org.springblade.rewrite.FoundationServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.*;

/**
 * 人员安排表，排班表 服务实现类
 *
 * @author Blade
 * @since 2022-04-18
 */
@Service
public class StaffTimeServiceImpl extends FoundationServiceImpl<StaffTimeMapper, StaffTime> implements IStaffTimeService {

	@Override
	public IPage<StaffTimeVO> selectStaffTimePage(IPage<StaffTimeVO> page, StaffTimeVO staffTime) {
		return page.setRecords(baseMapper.selectStaffTimePage(page, staffTime));
	}

	@Override
	public List<StaffTimeVO> calenderDefault(String startDate,String endDate) {

		Date start=DateUtil.parse(startDate,DateUtil.PATTERN_DATE);
		Date end=DateUtil.parse(endDate,DateUtil.PATTERN_DATE);
		//获取到查询数据，默认返回查询日期所在的月份的数据
		List<StaffTime> staffTimeList =
			this.list(
				Condition.getQueryWrapper(new StaffTime())
					.eq("create_dept", ServiceImplUtil.getNurseDeptFromUser())
					.eq("tenant_id", ServiceImplUtil.getUserTenantId())
					.between("shift_date",start,end)
			);

		List<NurseInfoDTO> nurseInfoList= SpringUtil.getContext().getBean(NurseInfoServiceImpl.class).selectAllBaseNurseFromSampDept();
		Map<Long,String> nurseInfoMap=new HashMap<>();
		for (NurseInfoDTO nurseInfoDTO:nurseInfoList){
			nurseInfoMap.put(nurseInfoDTO.getId(), nurseInfoDTO.getName());
		}
		List<StaffTimeVO> staffTimeVOList=new ArrayList<>();
		for (StaffTime staffTime:staffTimeList){
			StaffTimeVO staffTimeVO=StaffTimeWrapper.build().entityVO(staffTime);
			if (staffTime.getPostType().equals(Constant.POST_TYPE_NURSE)){
				staffTimeVO.setTitle(nurseInfoMap.get(staffTime.getNurseSid())+"[护士]");
			}else if (staffTime.getPostType().equals(Constant.POST_TYPE_ASSISTANT)){
				staffTimeVO.setTitle(nurseInfoMap.get(staffTime.getNurseSid())+"[助手]");
			}
			staffTimeVOList.add(staffTimeVO);
		}
		return staffTimeVOList;
	}

	@Override
	public List<StaffTimeVO> calendar(String date) {
		//获取排班月份的天数
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(DateUtil.parse(date,DateUtil.PATTERN_DATE));
		//设置为当月的第一天
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		Date startDate=calendar.getTime();

		calendar.set(Calendar.DAY_OF_MONTH, calendar.getMaximum(Calendar.DAY_OF_MONTH));

		Date endDate=calendar.getTime();
		//获取到查询数据，默认返回查询日期所在的月份的数据
		List<StaffTime> staffTimeList =
			this.list(
				Condition.getQueryWrapper(new StaffTime())
					.eq("create_dept", ServiceImplUtil.getNurseDeptFromUser())
					.eq("tenant_id", ServiceImplUtil.getUserTenantId())
					.between("shift_date",startDate,endDate)
			);

		List<NurseInfoDTO> nurseInfoList= SpringUtil.getContext().getBean(NurseInfoServiceImpl.class).selectAllBaseNurseFromSampDept();
		Map<Long,String> nurseInfoMap=new HashMap<>();
		for (NurseInfoDTO nurseInfoDTO:nurseInfoList){
			nurseInfoMap.put(nurseInfoDTO.getId(), nurseInfoDTO.getName());
		}
		List<StaffTimeVO> staffTimeVOList=new ArrayList<>();
		for (StaffTime staffTime:staffTimeList){
			StaffTimeVO staffTimeVO=StaffTimeWrapper.build().entityVO(staffTime);
			if (staffTime.getPostType().equals(Constant.POST_TYPE_NURSE)){
				staffTimeVO.setTitle(nurseInfoMap.get(staffTime.getNurseSid())+"[护士]");
			}else if (staffTime.getPostType().equals(Constant.POST_TYPE_ASSISTANT)){
				staffTimeVO.setTitle(nurseInfoMap.get(staffTime.getNurseSid())+"[助手]");
			}
			staffTimeVOList.add(staffTimeVO);
		}
		return staffTimeVOList;
	}

}
