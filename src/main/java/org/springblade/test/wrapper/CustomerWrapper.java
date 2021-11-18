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
package org.springblade.test.wrapper;

import lombok.AllArgsConstructor;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.test.entity.Customer;
import org.springblade.test.vo.CustomerVO;

/**
 * 客户档案 包装类,返回视图层所需的字段
 *
 * @author Blade
 * @since 2021-11-18
 */
public class CustomerWrapper extends BaseEntityWrapper<Customer, CustomerVO>  {

    public static CustomerWrapper build() {
        return new CustomerWrapper();
    }

	@Override
	public CustomerVO entityVO(Customer customer) {
		CustomerVO customerVO = BeanUtil.copy(customer, CustomerVO.class);

		return customerVO;
	}

}
