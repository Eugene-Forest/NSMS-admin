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
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.nsms.entity.Expectation;
import org.springblade.nsms.mapper.ExpectationMapper;
import org.springblade.nsms.service.IExpectationService;
import org.springblade.nsms.tools.ServiceImplUtil;
import org.springblade.nsms.vo.ExpectationVO;
import org.springblade.rewrite.FoundationServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 护士助手的排班期望表 服务实现类
 *
 * @author Blade
 * @since 2022-04-18
 */
@Service
public class ExpectationServiceImpl extends FoundationServiceImpl<ExpectationMapper, Expectation> implements IExpectationService {


	@Override
	public IPage<ExpectationVO> selectExpectationPage(IPage<ExpectationVO> page, ExpectationVO expectation) {
		return page.setRecords(baseMapper.selectExpectationPage(page, expectation));
	}

	/**
	 * 获取调用人的对于某排班配置表的期望的目前优先级
	 *
	 * @param referenceSid
	 */
	@Override
	public Integer getPriority(String referenceSid) {
		Integer maxPriority=baseMapper.getPriority(referenceSid,Func.toStr(ServiceImplUtil.getUserId()));
		if (maxPriority==null){
			return 1;
		}
		return (maxPriority+1);
	}

}
