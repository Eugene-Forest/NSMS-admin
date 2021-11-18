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
package org.springblade.test.service.impl;

import org.springblade.test.entity.Customer;
import org.springblade.test.vo.CustomerVO;
import org.springblade.test.mapper.CustomerMapper;
import org.springblade.test.service.ICustomerService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 客户档案  服务实现类
 *
 * @author Blade
 * @since 2021-11-18
 */
@Service
public class CustomerServiceImpl extends BaseServiceImpl<CustomerMapper, Customer> implements ICustomerService {

	@Override
	public IPage<CustomerVO> selectCustomerPage(IPage<CustomerVO> page, CustomerVO customer) {
		return page.setRecords(baseMapper.selectCustomerPage(page, customer));
	}

}
