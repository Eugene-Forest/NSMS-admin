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

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.nsms.entity.ClockIn;
import org.springblade.nsms.mapper.ClockInMapper;
import org.springblade.nsms.service.IClockInService;
import org.springblade.nsms.vo.ClockInVO;
import org.springblade.rewrite.FoundationServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 打卡记录表 服务实现类
 *
 * @author Blade
 * @since 2022-04-11
 */
@Service
public class ClockInServiceImpl extends FoundationServiceImpl<ClockInMapper, ClockIn> implements IClockInService {

	@Override
	public IPage<ClockInVO> selectClockInPage(IPage<ClockInVO> page, ClockInVO clockIn) {
		return page.setRecords(baseMapper.selectClockInPage(page, clockIn));
	}

}
