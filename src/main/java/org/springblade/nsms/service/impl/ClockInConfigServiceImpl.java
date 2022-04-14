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
import org.apache.commons.lang3.RandomStringUtils;
import org.springblade.nsms.entity.ClockInConfig;
import org.springblade.nsms.mapper.ClockInConfigMapper;
import org.springblade.nsms.service.IClockInConfigService;
import org.springblade.nsms.tools.QRCodeUtil;
import org.springblade.nsms.tools.ServiceImplUtil;
import org.springblade.nsms.vo.ClockInConfigVO;
import org.springblade.nsms.vo.RQInfoVO;
import org.springblade.rewrite.FoundationServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

/**
 * 打卡配置表 服务实现类
 *
 * @author Blade
 * @since 2022-04-11
 */
@Service
public class ClockInConfigServiceImpl extends FoundationServiceImpl<ClockInConfigMapper, ClockInConfig> implements IClockInConfigService {

	@Override
	public IPage<ClockInConfigVO> selectClockInConfigPage(IPage<ClockInConfigVO> page, ClockInConfigVO clockInConfig) {
		return page.setRecords(baseMapper.selectClockInConfigPage(page, clockInConfig));
	}

	/**
	 * 获取 Base64 格式的二维码
	 *
	 * @return
	 */
	@Override
	public RQInfoVO getBase64QRCodeRandomly() {
		//随机获取长度为20的字符串
		String message= RandomStringUtils.randomAlphanumeric(20);
		return getBase64QRByMessage(message);
	}

	@Override
	public RQInfoVO getBase64QRByMessage(String message) {
		RQInfoVO rqInfoVO=new RQInfoVO();
		rqInfoVO.setMessage(message);
		rqInfoVO.setRq(QRCodeUtil.getBase64QRCode(message));
		return rqInfoVO;
	}

	/**
	 * 通过视图层实体保存或更新配置记录
	 *
	 * @param clockInConfigVO 视图层实体
	 * @return
	 */
	@Override
	public boolean saveOrUpdate(ClockInConfigVO clockInConfigVO) {
		//从视图层实体中提取必要信息：有效时长，打卡信息，部门，启用状态
		ClockInConfig clockInConfig=new ClockInConfig();
		//获取截止时间
		Date now=new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.MINUTE, clockInConfigVO.getEffectiveTime());
		//赋值
		clockInConfig.setClockInDate(cal.getTime());
		clockInConfig.setState(clockInConfigVO.getState());
		clockInConfig.setMessage(clockInConfigVO.getMessage());
		clockInConfig.setDeptId(clockInConfigVO.getDeptId());
		return saveOrUpdate(clockInConfig);
	}


}
