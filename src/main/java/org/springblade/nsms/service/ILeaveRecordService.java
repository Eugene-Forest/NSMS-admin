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

import org.springblade.nsms.entity.LeaveRecord;
import org.springblade.nsms.vo.LeaveRecordVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.rewrite.FoundationService;

/**
 * 请假记录表 服务类
 *
 * @author Blade
 * @since 2022-03-14
 */
public interface ILeaveRecordService extends FoundationService<LeaveRecord> {

	/**
	 * 申请请假/新建一条请假记录
	 * @param leaveRecord 请假记录
	 * @return
	 */
	boolean applyForLeave(LeaveRecord leaveRecord);

	/**
	 * 更新一条请假记录
	 * @param leaveRecord 请假记录
	 * @return
	 */
	boolean updateForLeave(LeaveRecord leaveRecord);

	/**
	 * 申请或更新一条请假记录
	 * @param leaveRecord 请假记录
	 * @return
	 */
	boolean applyOrUpdateForLeave(LeaveRecord leaveRecord);

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param leaveRecord
	 * @return
	 */
	IPage<LeaveRecordVO> selectLeaveRecordPage(IPage<LeaveRecordVO> page, LeaveRecordVO leaveRecord);

	/**
	 * 审核请假记录
	 * @param leaveRecord 请假记录
	 * @return
	 */
	boolean checkInLeaveRecord(LeaveRecord leaveRecord);

	/**
	 * 反审请假记录/撤销请假记录的审核通过
	 * @param leaveRecord 请假记录
	 * @return
	 */
	boolean recheckInLeaveRecord(LeaveRecord leaveRecord);


}