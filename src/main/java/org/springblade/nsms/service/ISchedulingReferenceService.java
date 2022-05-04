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
import org.springblade.nsms.dto.ScheduleTable;
import org.springblade.nsms.entity.SchedulingReference;
import org.springblade.nsms.vo.SchedulingReferenceVO;
import org.springblade.rewrite.FoundationService;

import java.util.List;

/**
 * 排班依据表 服务类
 *
 * @author Blade
 * @since 2022-04-18
 */
public interface ISchedulingReferenceService extends FoundationService<SchedulingReference> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param schedulingReference
	 * @return
	 */
	IPage<SchedulingReferenceVO> selectSchedulingReferencePage(IPage<SchedulingReferenceVO> page, SchedulingReferenceVO schedulingReference);
	boolean saveOrUpdateEntity(SchedulingReferenceVO schedulingReferenceVO);

	/**
	 * 作为一个护士助手选择期望对应的排班配置的结果
	 * @return
	 */
	List<SchedulingReferenceVO> selectByUserDept();

	/**
	 * 改变排班配置表的状态为待排班、期望输入、或未启用
	 * @param schedulingReferenceVO
	 * @return
	 */
	boolean changeReferenceConfigState(SchedulingReferenceVO schedulingReferenceVO);

	/**
	 * 改变排班配置表的状态为待排班
	 * @param schedulingReferenceVO
	 * @return
	 */
	boolean recheckReferenceConfigState(SchedulingReferenceVO schedulingReferenceVO);

	/**
	 * 排班
	 * @param schedulingReferenceVO
	 * @return
	 */
	boolean scheduling(SchedulingReferenceVO schedulingReferenceVO);
}
