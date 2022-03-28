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

import org.springblade.nsms.checkin.entity.LeaveRecord;
import org.springblade.nsms.checkin.entity.ShiftRecord;
import org.springblade.nsms.checkin.vo.ShiftRecordVO;
import org.springblade.nsms.checkin.mapper.ShiftRecordMapper;
import org.springblade.nsms.checkin.service.IShiftRecordService;
import org.springblade.core.mp.base.BaseServiceImpl;
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
		//获取源数据
		ShiftRecord originShiftRecord=baseMapper.selectById(shiftRecord.getId());
		//判断是否能够或需要审核
		if (originShiftRecord.getApplicationStatus()!=0){
			return true;
		}else {
			//审核的结果会有2种
			originShiftRecord.setApplicationStatus(shiftRecord.getApplicationStatus());
			originShiftRecord.setApprovalOpinion(shiftRecord.getApprovalOpinion());
			return baseMapper.updateById(originShiftRecord)==1?true:false;
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
		//todo 所有都需要确认其对应的操作的值是否为 null
		//获取源数据
		ShiftRecord originShiftRecord=baseMapper.selectById(shiftRecord.getId());
		//判断是否能够或需要审核
		if (originShiftRecord.getApplicationStatus()==0){
			throw new RuntimeException("反审失败！请确认换班记录是否处于审核过的状态。");
		}else {
			// todo 需要判断普通护士和护士长的身份，以此来区别和判断反审的结果
//			if (originShiftRecord.getApplicationStatus()>2&&身份是护士长)

			//将换班记录改为未审核状态
			originShiftRecord.setApplicationStatus(0);
			originShiftRecord.setApprovalOpinion(shiftRecord.getApprovalOpinion());
			return baseMapper.updateById(originShiftRecord)==1?true:false;
		}
	}

	/**
	 * 同意同事的换班申请
	 *
	 * @param shiftRecord 换班申请记录
	 * @return
	 */
	@Override
	public boolean agreeWithShiftExchange(ShiftRecord shiftRecord) {

		return false;
	}

	/**
	 * 取消同意同事的换班申请
	 *
	 * @param shiftRecord 换班申请记录
	 * @return
	 */
	@Override
	public boolean disagreeWithShiftExchange(ShiftRecord shiftRecord) {
		return false;
	}


}
