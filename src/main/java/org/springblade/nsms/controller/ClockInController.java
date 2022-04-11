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
import org.springblade.nsms.entity.ClockIn;
import org.springblade.nsms.vo.ClockInVO;
import org.springblade.nsms.wrapper.ClockInWrapper;
import org.springblade.nsms.service.IClockInService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 打卡记录表 控制器
 *
 * @author Blade
 * @since 2022-04-11
 */
@RestController
@AllArgsConstructor
@RequestMapping("nsms/clockin")
@Api(value = "打卡记录表", tags = "打卡记录表接口")
public class ClockInController extends BladeController {

	private IClockInService clockInService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入clockIn")
	public R<ClockInVO> detail(ClockIn clockIn) {
		ClockIn detail = clockInService.getOne(Condition.getQueryWrapper(clockIn));
		return R.data(ClockInWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 打卡记录表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入clockIn")
	public R<IPage<ClockInVO>> list(ClockIn clockIn, Query query) {
		IPage<ClockIn> pages = clockInService.page(Condition.getPage(query), Condition.getQueryWrapper(clockIn));
		return R.data(ClockInWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 打卡记录表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入clockIn")
	public R<IPage<ClockInVO>> page(ClockInVO clockIn, Query query) {
		IPage<ClockInVO> pages = clockInService.selectClockInPage(Condition.getPage(query), clockIn);
		return R.data(pages);
	}

	/**
	 * 新增 打卡记录表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入clockIn")
	public R save(@Valid @RequestBody ClockIn clockIn) {
		return R.status(clockInService.save(clockIn));
	}

	/**
	 * 修改 打卡记录表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入clockIn")
	public R update(@Valid @RequestBody ClockIn clockIn) {
		return R.status(clockInService.updateById(clockIn));
	}

	/**
	 * 新增或修改 打卡记录表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入clockIn")
	public R submit(@Valid @RequestBody ClockIn clockIn) {
		return R.status(clockInService.saveOrUpdate(clockIn));
	}

	
	/**
	 * 删除 打卡记录表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(clockInService.deleteLogic(Func.toLongList(ids)));
	}

	
}
