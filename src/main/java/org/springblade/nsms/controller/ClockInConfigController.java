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
import org.springblade.nsms.entity.ClockInConfig;
import org.springblade.nsms.vo.ClockInConfigVO;
import org.springblade.nsms.wrapper.ClockInConfigWrapper;
import org.springblade.nsms.service.IClockInConfigService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 打卡配置表 控制器
 *
 * @author Blade
 * @since 2022-04-11
 */
@RestController
@AllArgsConstructor
@RequestMapping("nsms/clockinconfig")
@Api(value = "打卡配置表", tags = "打卡配置表接口")
public class ClockInConfigController extends BladeController {

	private IClockInConfigService clockInConfigService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入clockInConfig")
	public R<ClockInConfigVO> detail(ClockInConfig clockInConfig) {
		ClockInConfig detail = clockInConfigService.getOne(Condition.getQueryWrapper(clockInConfig));
		return R.data(ClockInConfigWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 打卡配置表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入clockInConfig")
	public R<IPage<ClockInConfigVO>> list(ClockInConfig clockInConfig, Query query) {
		IPage<ClockInConfig> pages = clockInConfigService.page(Condition.getPage(query), Condition.getQueryWrapper(clockInConfig));
		return R.data(ClockInConfigWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 打卡配置表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入clockInConfig")
	public R<IPage<ClockInConfigVO>> page(ClockInConfigVO clockInConfig, Query query) {
		IPage<ClockInConfigVO> pages = clockInConfigService.selectClockInConfigPage(Condition.getPage(query), clockInConfig);
		return R.data(pages);
	}

	/**
	 * 新增 打卡配置表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入clockInConfig")
	public R save(@Valid @RequestBody ClockInConfig clockInConfig) {
		return R.status(clockInConfigService.save(clockInConfig));
	}

	/**
	 * 修改 打卡配置表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入clockInConfig")
	public R update(@Valid @RequestBody ClockInConfig clockInConfig) {
		return R.status(clockInConfigService.updateById(clockInConfig));
	}

	/**
	 * 新增或修改 打卡配置表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入clockInConfig")
	public R submit(@Valid @RequestBody ClockInConfig clockInConfig) {
		return R.status(clockInConfigService.saveOrUpdate(clockInConfig));
	}

	
	/**
	 * 删除 打卡配置表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(clockInConfigService.deleteLogic(Func.toLongList(ids)));
	}

	
}
