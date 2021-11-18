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
package org.springblade.test.controller;

import io.swagger.annotations.Api;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import javax.validation.Valid;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.test.entity.Customer;
import org.springblade.test.vo.CustomerVO;
import org.springblade.test.wrapper.CustomerWrapper;
import org.springblade.test.service.ICustomerService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 客户档案  控制器
 *
 * @author Blade
 * @since 2021-11-18
 */
@RestController
@AllArgsConstructor
@RequestMapping("/material/customer")
@Api(value = "客户档案 ", tags = "客户档案 接口")
public class CustomerController extends BladeController {

	private ICustomerService customerService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入customer")
	public R<CustomerVO> detail(Customer customer) {
		Customer detail = customerService.getOne(Condition.getQueryWrapper(customer));
		return R.data(CustomerWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 客户档案
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入customer")
	public R<IPage<CustomerVO>> list(Customer customer, Query query) {
		IPage<Customer> pages = customerService.page(Condition.getPage(query), Condition.getQueryWrapper(customer));
		return R.data(CustomerWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 客户档案
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入customer")
	public R<IPage<CustomerVO>> page(CustomerVO customer, Query query) {
		IPage<CustomerVO> pages = customerService.selectCustomerPage(Condition.getPage(query), customer);
		return R.data(pages);
	}

	/**
	 * 新增 客户档案
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入customer")
	public R save(@Valid @RequestBody Customer customer) {
		return R.status(customerService.save(customer));
	}

	/**
	 * 修改 客户档案
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入customer")
	public R update(@Valid @RequestBody Customer customer) {
		return R.status(customerService.updateById(customer));
	}

	/**
	 * 新增或修改 客户档案
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入customer")
	public R submit(@Valid @RequestBody Customer customer) {
		return R.status(customerService.saveOrUpdate(customer));
	}


	/**
	 * 删除 客户档案
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(customerService.deleteLogic(Func.toLongList(ids)));
	}


}
