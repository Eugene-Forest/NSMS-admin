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

import org.springblade.nsms.tools.Constant;
import org.springblade.nsms.tools.ServiceImplUtil;
import org.springblade.common.tool.SpringBeanUtil;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.nsms.entity.LeaveRecord;
import org.springblade.nsms.entity.NurseInfo;
import org.springblade.nsms.mapper.LeaveRecordMapper;
import org.springblade.nsms.service.ILeaveRecordService;
import org.springblade.nsms.service.INurseInfoService;
import org.springblade.nsms.vo.LeaveRecordVO;
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

	/**
	 * 申请请假/新建一条请假记录
	 *
	 * @param leaveRecord 请假记录
	 * @return
	 */
	@Override
	public boolean applyForLeave(LeaveRecord leaveRecord) {
		//根据此纪录获取用户信息并保存
		//获取用户对应的护士信息（管理员默认对应一个名为同名的护士信息）
		BladeUser user = SecureUtil.getUser();
		if (user==null){
			throw new RuntimeException("请确认账号是否正常");
		}else {
			INurseInfoService iNurseInfoService=SpringBeanUtil.getBean(INurseInfoService.class);
			NurseInfo nurseInfo=iNurseInfoService.getNurseInfoByUserId(Func.toStr(user.getUserId()));
			if (nurseInfo==null){
				throw new RuntimeException("请确认账号是否关联了护士信息");
			}
			leaveRecord.setNurseSid(nurseInfo.getId());
			//将获取到的用户信息写入请假记录中，并执行默认的保存方法
			ServiceImplUtil.resolveEntityByBladeUser(leaveRecord, user);
			return baseMapper.insert(leaveRecord)==1?true:false;
		}
	}

	/**
	 * 更新一条请假记录
	 *
	 * @param leaveRecord 请假记录
	 * @return
	 */
	@Override
	public boolean updateForLeave(LeaveRecord leaveRecord) {
		//将获取到的用户信息写入请假记录中，并执行默认的保存方法
		ServiceImplUtil.resolveEntity(leaveRecord);
		return baseMapper.updateById(leaveRecord)==1?true:false;
	}

	/**
	 * 申请或更新一条请假记录
	 *
	 * @param leaveRecord 请假记录
	 * @return
	 */
	@Override
	public boolean applyOrUpdateForLeave(LeaveRecord leaveRecord) {
		if (leaveRecord.getId()==null){
			return applyForLeave(leaveRecord);
		}else {
			return updateForLeave(leaveRecord);
		}
	}

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
		//被审核后的状态必定为 驳回/通过 良种之一
		if(!leaveRecord.getApprovalStatus().equals(Constant.APPROVAL_STATUS_PASS)
			&& !leaveRecord.getApprovalStatus().equals(Constant.APPROVAL_STATUS_REJECT)){
			throw new RuntimeException("请确认审核状态是否准确！");
		}
		//step1：通过传入的请假记录从数据库中获取源数据
		LeaveRecord originLeaveRecord = baseMapper.selectById(leaveRecord.getId());
		//step2: 判断源数据是否已经审批
		if (originLeaveRecord.getApprovalStatus().equals(Constant.APPROVAL_STATUS_PENDING)){
			originLeaveRecord.setApprovalStatus(leaveRecord.getApprovalStatus());
			originLeaveRecord.setApprovalOpinion(leaveRecord.getApprovalOpinion());
			//添加审核人的信息
			originLeaveRecord.setApprover(ServiceImplUtil.getNurseIdFromUser());
			return baseMapper.updateById(originLeaveRecord)==1?true:false;
		}else {
			throw new RuntimeException("此请假已经被审批！请反审再进行审批。");
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
		if (originLeaveRecord.getApprovalStatus().equals(Constant.APPROVAL_STATUS_PASS)
			|| originLeaveRecord.getApprovalStatus().equals(Constant.APPROVAL_STATUS_REJECT)){
			originLeaveRecord.setApprover(ServiceImplUtil.getNurseIdFromUser());
			originLeaveRecord.setApprovalOpinion(leaveRecord.getApprovalOpinion());
			originLeaveRecord.setApprovalStatus(Constant.APPROVAL_STATUS_PENDING);
			return baseMapper.updateById(originLeaveRecord)==1?true:false;
		}else {
			//如果不处于审核完成的状态则出错
			throw new RuntimeException("反审失败！请确认请假记录是否处于未被审核状态。");
		}
	}
}
