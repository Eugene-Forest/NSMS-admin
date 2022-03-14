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

/**
 * 交班记录表 服务类
 *
 * @author Blade
 * @since 2022-03-14
 */
public interface IShiftRecordService extends BaseService<ShiftRecord> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param shiftRecord
	 * @return
	 */
	IPage<ShiftRecordVO> selectShiftRecordPage(IPage<ShiftRecordVO> page, ShiftRecordVO shiftRecord);

}
