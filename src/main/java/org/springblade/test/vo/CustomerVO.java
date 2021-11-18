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
package org.springblade.test.vo;

import org.springblade.test.entity.Customer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 客户档案 视图实体类
 *
 * @author Blade
 * @since 2021-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CustomerVO对象", description = "客户档案 ")
public class CustomerVO extends Customer {
	private static final long serialVersionUID = 1L;

}
