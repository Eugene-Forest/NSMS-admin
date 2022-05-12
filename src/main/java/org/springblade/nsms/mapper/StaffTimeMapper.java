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
package org.springblade.nsms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springblade.nsms.entity.NurseInfo;
import org.springblade.nsms.entity.StaffTime;
import org.springblade.nsms.vo.StaffTimeVO;

import java.util.Date;
import java.util.List;

/**
 * 人员安排表，排班表 Mapper 接口
 *
 * @author Blade
 * @since 2022-04-18
 */
public interface StaffTimeMapper extends BaseMapper<StaffTime> {

	/**
	 * 自定义分页
	 * @return
	 */
	List<StaffTime> selectStaffTimePage(IPage page, NurseInfo nurseInfo);

	/**
	 * 逻辑删除时间区间内的排班结果
	 * @param start 开始时间
	 * @param end 结束时间
	 * @param tenantId 租户id
	 * @param deptId 部门id
	 * @return
	 */
	boolean deleteSchedulingResultByTimeInterval(@Param("start") Date start,
												 @Param("end") Date end,
												 @Param("tenantId") String tenantId,
												 @Param("deptId") String deptId);

}
