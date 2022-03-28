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
package org.springblade.nsms.checkin.service.impl;

import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.nsms.checkin.entity.LeaveRecord;
import org.springblade.nsms.checkin.vo.LeaveRecordVO;
import org.springblade.nsms.checkin.mapper.LeaveRecordMapper;
import org.springblade.nsms.checkin.service.ILeaveRecordService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.rewrite.FoundationServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 请假记录表 服务实现类
 *
 * @author Blade
 * @since 2022-03-14
 */
@Service
public class LeaveRecordServiceImpl extends FoundationServiceImpl<LeaveRecordMapper, LeaveRecord> implements ILeaveRecordService {

	@Override
	public IPage<LeaveRecordVO> selectLeaveRecordPage(IPage<LeaveRecordVO> page, LeaveRecordVO leaveRecord) {
		return page.setRecords(baseMapper.selectLeaveRecordPage(page, leaveRecord));


	}

	/**
	 * 审核请假记录
	 *
	 * @param leaveRecord 请假记录
	 * @return
	 */
	@Override
	public boolean checkInLeaveRecord(LeaveRecord leaveRecord) {
		//step1：通过传入的请假记录从数据库中获取源数据
		LeaveRecord originLeaveRecord = baseMapper.selectById(leaveRecord.getId());
		//step2: 判断源数据是否已经审批
		if (originLeaveRecord.getApprovalStatus()==2){
			return true;
		}else {
			originLeaveRecord.setApprovalStatus(2);
			originLeaveRecord.setApprovalOpinion(leaveRecord.getApprovalOpinion());
			return baseMapper.updateById(originLeaveRecord)==1?true:false;
		}
	}

	/**
	 * 反审请假记录/撤销请假记录的审核通过
	 *
	 * @param leaveRecord 请假记录
	 * @return
	 */
	@Override
	public boolean recheckInLeaveRecord(LeaveRecord leaveRecord) {
		//step1：通过传入的请假记录从数据库中获取源数据
		LeaveRecord originLeaveRecord = baseMapper.selectById(leaveRecord.getId());
		//step2: 判断源数据是否处于审核完成的状态
		if (originLeaveRecord.getApprovalStatus()!=2){
			//如果不处于审核状态则出错
			throw new RuntimeException("反审失败！请确认请假记录是否处于通过状态。");
		}else {
			originLeaveRecord.setApprovalStatus(2);
			originLeaveRecord.setApprovalOpinion(leaveRecord.getApprovalOpinion());
			return baseMapper.updateById(originLeaveRecord)==1?true:false;
		}
	}
}
