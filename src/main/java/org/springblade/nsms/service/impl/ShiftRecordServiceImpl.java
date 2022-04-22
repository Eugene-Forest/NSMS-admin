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

import org.springblade.nsms.entity.ShiftRecord;
import org.springblade.nsms.tools.Constant;
import org.springblade.nsms.vo.ShiftRecordVO;
import org.springblade.nsms.mapper.ShiftRecordMapper;
import org.springblade.nsms.service.IShiftRecordService;
import org.springblade.rewrite.FoundationServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.ArrayList;
import java.util.List;

/**
 * 换班记录表 服务实现类
 *
 * @author Blade
 * @since 2022-03-14
 */
@Service
public class ShiftRecordServiceImpl extends FoundationServiceImpl<ShiftRecordMapper, ShiftRecord> implements IShiftRecordService {

	@Override
	public IPage<ShiftRecordVO> selectShiftRecordPage(IPage<ShiftRecordVO> page, ShiftRecordVO shiftRecord) {
		return page.setRecords(baseMapper.selectShiftRecordPage(page, shiftRecord));
	}

	/**
	 * 审核换班记录
	 *
	 * @param shiftRecord 换班记录
	 * @return
	 */
	@Override
	public boolean checkInShiftRecord(ShiftRecord shiftRecord) {
		//验证传入的实体是否符合规范
		verifyEntity(shiftRecord);
		//护士长审核后的的换班数据的状态必须是 通过/驳回 两种状态
		if ((!shiftRecord.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_PASS))
			&& (!shiftRecord.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_REJECT))) {
			throw new RuntimeException("请确认提交的审核状态是否正确！");
		}
		//获取源数据
		ShiftRecord originShiftRecord=baseMapper.selectById(shiftRecord.getId());
		//判断是否能够或需要审核	；护士长审核的换班数据只能是 被申请人同意/待审核 两种状态
		if (originShiftRecord.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_PENDING_CHECK)
		|| originShiftRecord.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_AGREE)){
			originShiftRecord.setApplicationStatus(shiftRecord.getApplicationStatus());
			originShiftRecord.setApprovalOpinion(shiftRecord.getApprovalOpinion());
			return baseMapper.updateById(originShiftRecord)==1?true:false;
		}else {
			throw new RuntimeException("审核失败！请确认换班记录的状态。");
		}
	}

	/**
	 * 反审换班记录/撤销换班记录的审核通过
	 *
	 * @param shiftRecord 换班记录
	 * @return
	 */
	@Override
	public boolean recheckInShiftRecord(ShiftRecord shiftRecord) {
		//验证传入的实体是否符合规范
		verifyEntity(shiftRecord);
		//护士长反审后的的换班数据状态只能是 被申请人同意/待审核 两种状态
//		if ((!shiftRecord.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_PENDING_CHECK))
//			&& (!shiftRecord.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_AGREE))) {
//			throw new RuntimeException("请确认提交的审核状态是否正确！");
//		}
		//获取源数据
		ShiftRecord originShiftRecord=baseMapper.selectById(shiftRecord.getId());
		//判断是否能够或需要审核
		//护士长反审的换班数据的状态必须是 通过/驳回 两种状态
		if (originShiftRecord.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_REJECT)
			|| originShiftRecord.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_PASS)){
			//将换班记录改为未审核状态
			//能过被护士长审核的换班都是被同意的
			originShiftRecord.setApplicationStatus(Constant.EXCHANGE_APPROVAL_STATUS_PENDING_CHECK);
			originShiftRecord.setApprovalOpinion(shiftRecord.getApprovalOpinion());
			return baseMapper.updateById(originShiftRecord)==1?true:false;
		}else {
			throw new RuntimeException("反审失败！请确认换班记录是否处于审核过的状态。");
		}
	}

	/**
	 * 审核同事的换班申请
	 *
	 * @param shiftRecord 换班申请记录
	 * @return
	 */
	@Override
	public boolean conferShiftExchange(ShiftRecord shiftRecord) {
		//验证传入的实体是否符合规范
		verifyEntity(shiftRecord);
		//传入的交班的数据的状态必须为 被申请人同意/不同意两种
		if ((!shiftRecord.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_AGREE))
			&& (!shiftRecord.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_DISAGREE))) {
			throw new RuntimeException("请确认提交的审核状态是否正确！");
		}
		//获取源数据
		ShiftRecord originShiftRecord=baseMapper.selectById(shiftRecord.getId());
		if (originShiftRecord.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_PENDING)){
			originShiftRecord.setApplicationStatus(shiftRecord.getApplicationStatus());
			return baseMapper.updateById(originShiftRecord)==1?true:false;
		}else {
			throw new RuntimeException("审核商议失败！请确认换班记录的状态。");
		}
	}

	/**
	 * 反审同事的换班申请
	 *
	 * @param shiftRecord 换班申请记录
	 * @return
	 */
	@Override
	public boolean reConferShiftExchange(ShiftRecord shiftRecord) {
		//验证传入的实体是否符合规范
		verifyEntity(shiftRecord);
//		if (shiftRecord.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_PENDING)) {
//			throw new RuntimeException("请确认提交的审核状态是否正确！");
//		}
		//获取源数据
		ShiftRecord originShiftRecord=baseMapper.selectById(shiftRecord.getId());
		//交班的数据的状态必须为 被申请人同意/不同意两种
		if (
			(!shiftRecord.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_AGREE))
				&& (!shiftRecord.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_DISAGREE))
		){
			throw new RuntimeException("反审商议失败！请确认换班记录的状态。");
		}else {
			originShiftRecord.setApplicationStatus(Constant.EXCHANGE_APPROVAL_STATUS_PENDING);
			return baseMapper.updateById(originShiftRecord)==1?true:false;
		}
	}


	private boolean verifyEntity(ShiftRecord shiftRecord){
		if (shiftRecord==null||shiftRecord.getId()==null||shiftRecord.getApplicationStatus()==null){
			throw new RuntimeException("请确认提交的数据是否正确！");
		}
		return true;
	}


}
