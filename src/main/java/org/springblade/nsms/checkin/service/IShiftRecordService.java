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
package org.springblade.nsms.checkin.service;

import org.springblade.nsms.checkin.entity.ShiftRecord;
import org.springblade.nsms.checkin.vo.ShiftRecordVO;
import org.springblade.core.mp.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.rewrite.FoundationService;

/**
 * 换班记录表 服务类
 *
 * @author Blade
 * @since 2022-03-14
 */
public interface IShiftRecordService extends FoundationService<ShiftRecord> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param shiftRecord
	 * @return
	 */
	IPage<ShiftRecordVO> selectShiftRecordPage(IPage<ShiftRecordVO> page, ShiftRecordVO shiftRecord);

	/**
	 * 审核换班记录
	 * @param shiftRecord 换班记录
	 * @return
	 */
	boolean checkInShiftRecord(ShiftRecord shiftRecord);

	/**
	 * 反审换班记录/撤销换班记录的审核通过
	 * @param shiftRecord 换班记录
	 * @return
	 */
	boolean recheckInShiftRecord(ShiftRecord shiftRecord);


	/**
	 * 审核同事的换班申请
	 * @param shiftRecord 换班申请记录
	 * @return
	 */
	boolean conferShiftExchange(ShiftRecord shiftRecord);

	/**
	 * 反审同事的换班申请
	 * @param shiftRecord 换班申请记录
	 * @return
	 */
	boolean reConferShiftExchange(ShiftRecord shiftRecord);

}
