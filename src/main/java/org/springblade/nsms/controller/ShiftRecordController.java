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
import org.springblade.nsms.wrapper.ShiftRecordWrapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.nsms.entity.ShiftRecord;
import org.springblade.nsms.vo.ShiftRecordVO;
import org.springblade.nsms.service.IShiftRecordService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 换班记录表 控制器
 *
 * @author Blade
 * @since 2022-03-14
 */
@RestController
@AllArgsConstructor
@RequestMapping("nsms/shiftrecord")
@Api(value = "换班记录表", tags = "换班记录表接口")
public class ShiftRecordController extends BladeController {

	private IShiftRecordService shiftRecordService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入shiftRecord")
	public R<ShiftRecordVO> detail(ShiftRecord shiftRecord) {
		ShiftRecord detail = shiftRecordService.getOne(Condition.getQueryWrapper(shiftRecord));
		return R.data(ShiftRecordWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 换班记录表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入shiftRecord")
	public R<IPage<ShiftRecordVO>> list(ShiftRecord shiftRecord, Query query) {
		IPage<ShiftRecord> pages = shiftRecordService.page(Condition.getPage(query), Condition.getQueryWrapper(shiftRecord));
		return R.data(ShiftRecordWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 换班记录表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入shiftRecord")
	public R<IPage<ShiftRecordVO>> page(ShiftRecordVO shiftRecord, Query query) {
		IPage<ShiftRecordVO> pages = shiftRecordService.selectShiftRecordPage(Condition.getPage(query), shiftRecord);
		return R.data(pages);
	}

	/**
	 * 新增 换班记录表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入shiftRecord")
	public R save(@Valid @RequestBody ShiftRecord shiftRecord) {
		return R.status(shiftRecordService.save(shiftRecord));
	}

	/**
	 * 修改 换班记录表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入shiftRecord")
	public R update(@Valid @RequestBody ShiftRecord shiftRecord) {
		return R.status(shiftRecordService.updateById(shiftRecord));
	}

	/**
	 * 新增或修改 换班记录表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入shiftRecord")
	public R submit(@Valid @RequestBody ShiftRecord shiftRecord) {
		return R.status(shiftRecordService.saveOrUpdate(shiftRecord));
	}


	/**
	 * 删除 换班记录表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(shiftRecordService.deleteLogic(Func.toLongList(ids)));
	}

	/**
	 * 审核 换班记录表
	 */
	@PostMapping("/checkIn")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "审核换班", notes = "传入ids")
	public R checkIn(@Valid @RequestBody ShiftRecord shiftRecord) {
		return R.status(shiftRecordService.checkInShiftRecord(shiftRecord));
	}


	/**
	 * 反审 换班记录表
	 */
	@PostMapping("/recheckIn")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "反审换班", notes = "传入ids")
	public R recheckIn(@Valid @RequestBody  ShiftRecord shiftRecord) {
		return R.status(shiftRecordService.recheckInShiftRecord(shiftRecord));
	}


	/**
	 * 审核同事换班申请
	 */
	@PostMapping("/confer")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "商议换班", notes = "传入ids")
	public R agreeWithShiftExchange(@Valid @RequestBody ShiftRecord shiftRecord){
		return R.status(shiftRecordService.conferShiftExchange(shiftRecord));
	}


	/**
	 * 反审同事换班申请
	 */
	@PostMapping("/reConfer")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "撤销商议换班", notes = "传入ids")
	public R disagreeWithShiftExchange(@Valid @RequestBody ShiftRecord shiftRecord){
		return R.status(shiftRecordService.reConferShiftExchange(shiftRecord));
	}

}
