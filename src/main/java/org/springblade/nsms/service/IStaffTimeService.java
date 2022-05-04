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
package org.springblade.nsms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.nsms.entity.StaffTime;
import org.springblade.nsms.vo.StaffTimeVO;
import org.springblade.rewrite.FoundationService;

import java.util.List;

/**
 * 人员安排表，排班表 服务类
 *
 * @author Blade
 * @since 2022-04-18
 */
public interface IStaffTimeService extends FoundationService<StaffTime> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param staffTime
	 * @return
	 */
	IPage<StaffTimeVO> selectStaffTimePage(IPage<StaffTimeVO> page, StaffTimeVO staffTime);

//	List<StaffTimeVO> calendar(String startDate,String endDate);

	List<StaffTimeVO> calendar(String today);
}
