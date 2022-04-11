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
import org.springblade.nsms.vo.ShiftRecordVO;
import org.springblade.nsms.mapper.ShiftRecordMapper;
import org.springblade.nsms.service.IShiftRecordService;
import org.springblade.rewrite.FoundationServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

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
		if (shiftRecord.getApplicationStatus()<=0||shiftRecord.getApplicationStatus()>3) {
			throw new RuntimeException("请确认提交的审核状态是否正确！");
		}
		//获取源数据
		ShiftRecord originShiftRecord=baseMapper.selectById(shiftRecord.getId());
		//判断是否能够或需要审核
		if (originShiftRecord.getApplicationStatus()>0&&originShiftRecord.getApplicationStatus()<=3){
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
		if (shiftRecord.getApplicationStatus()<=3||shiftRecord.getApplicationStatus()>5) {
			throw new RuntimeException("请确认提交的审核状态是否正确！");
		}
		//获取源数据
		ShiftRecord originShiftRecord=baseMapper.selectById(shiftRecord.getId());
		//判断是否能够或需要审核
		if (originShiftRecord.getApplicationStatus()>3&&originShiftRecord.getApplicationStatus()<=5){
			//将换班记录改为未审核状态
			//能过被护士长审核的换班都是被同意的
			originShiftRecord.setApplicationStatus(2);
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
		if (shiftRecord.getApplicationStatus()<=0||shiftRecord.getApplicationStatus()>=3) {
			throw new RuntimeException("请确认提交的审核状态是否正确！");
		}
		//获取源数据
		ShiftRecord originShiftRecord=baseMapper.selectById(shiftRecord.getId());
		if (originShiftRecord.getApplicationStatus()==0){
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
		if (shiftRecord.getApplicationStatus()==0) {
			throw new RuntimeException("请确认提交的审核状态是否正确！");
		}
		//获取源数据
		ShiftRecord originShiftRecord=baseMapper.selectById(shiftRecord.getId());
		if (originShiftRecord.getApplicationStatus()==0){
			throw new RuntimeException("反审商议失败！请确认换班记录的状态。");
		}else {
			originShiftRecord.setApplicationStatus(0);
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
