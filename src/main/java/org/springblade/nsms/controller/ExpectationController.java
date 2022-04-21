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
package org.springblade.nsms.controller;

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
import org.springblade.nsms.entity.Expectation;
import org.springblade.nsms.vo.ExpectationVO;
import org.springblade.nsms.wrapper.ExpectationWrapper;
import org.springblade.nsms.service.IExpectationService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 护士助手的排班期望表 控制器
 *
 * @author Blade
 * @since 2022-04-18
 */
@RestController
@AllArgsConstructor
@RequestMapping("nsms/expectation")
@Api(value = "护士助手的排班期望表", tags = "护士助手的排班期望表接口")
public class ExpectationController extends BladeController {

	private IExpectationService expectationService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入expectation")
	public R<ExpectationVO> detail(Expectation expectation) {
		Expectation detail = expectationService.getOne(Condition.getQueryWrapper(expectation));
		return R.data(ExpectationWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 护士助手的排班期望表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入expectation")
	public R<IPage<ExpectationVO>> list(Expectation expectation, Query query) {
		IPage<Expectation> pages = expectationService.page(Condition.getPage(query), Condition.getQueryWrapper(expectation));
		return R.data(ExpectationWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 护士助手的排班期望表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入expectation")
	public R<IPage<ExpectationVO>> page(ExpectationVO expectation, Query query) {
		IPage<ExpectationVO> pages = expectationService.selectExpectationPage(Condition.getPage(query), expectation);
		return R.data(pages);
	}

	/**
	 * 新增 护士助手的排班期望表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入expectation")
	public R save(@Valid @RequestBody Expectation expectation) {
		return R.status(expectationService.save(expectation));
	}

	/**
	 * 修改 护士助手的排班期望表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入expectation")
	public R update(@Valid @RequestBody Expectation expectation) {
		return R.status(expectationService.updateById(expectation));
	}

	/**
	 * 新增或修改 护士助手的排班期望表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入expectation")
	public R submit(@Valid @RequestBody Expectation expectation) {
		return R.status(expectationService.saveOrUpdate(expectation));
	}


	/**
	 * 删除 护士助手的排班期望表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(expectationService.deleteLogic(Func.toLongList(ids)));
	}



	@GetMapping("/getPriority")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R getPriority(@ApiParam(value = "目标排班配置id", required = true) @RequestParam String referenceSid) {
		return R.data(expectationService.getPriority(referenceSid));
	}
}
