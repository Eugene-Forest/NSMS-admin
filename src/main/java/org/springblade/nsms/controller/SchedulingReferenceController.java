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
import org.springblade.nsms.entity.SchedulingReference;
import org.springblade.nsms.vo.SchedulingReferenceVO;
import org.springblade.nsms.wrapper.SchedulingReferenceWrapper;
import org.springblade.nsms.service.ISchedulingReferenceService;
import org.springblade.core.boot.ctrl.BladeController;

import java.util.List;

/**
 * 排班依据表 控制器
 *
 * @author Blade
 * @since 2022-04-18
 */
@RestController
@AllArgsConstructor
@RequestMapping("nsms/schedulingreference")
@Api(value = "排班依据表", tags = "排班依据表接口")
public class SchedulingReferenceController extends BladeController {

	private ISchedulingReferenceService schedulingReferenceService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入schedulingReference")
	public R<SchedulingReferenceVO> detail(SchedulingReference schedulingReference) {
		SchedulingReference detail = schedulingReferenceService.getOne(Condition.getQueryWrapper(schedulingReference));
		return R.data(SchedulingReferenceWrapper.build().resolveEntityForSelect(detail));
	}

	/**
	 * 分页 排班依据表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入schedulingReference")
	public R<IPage<SchedulingReferenceVO>> list(SchedulingReference schedulingReference, Query query) {
		IPage<SchedulingReference> pages = schedulingReferenceService.page(Condition.getPage(query), Condition.getQueryWrapper(schedulingReference));
		return R.data(SchedulingReferenceWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 排班依据表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入schedulingReference")
	public R<IPage<SchedulingReferenceVO>> page(SchedulingReferenceVO schedulingReference, Query query) {
		IPage<SchedulingReferenceVO> pages = schedulingReferenceService.selectSchedulingReferencePage(Condition.getPage(query), schedulingReference);
		return R.data(pages);
	}

	/**
	 * 新增 排班依据表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入schedulingReference")
	public R save(@Valid @RequestBody SchedulingReference schedulingReference) {
		return R.status(schedulingReferenceService.save(schedulingReference));
	}

	/**
	 * 修改 排班依据表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入schedulingReference")
	public R update(@Valid @RequestBody SchedulingReference schedulingReference) {
		return R.status(schedulingReferenceService.updateById(schedulingReference));
	}

	/**
	 * 新增或修改 排班依据表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入schedulingReference")
	public R submit(@Valid @RequestBody SchedulingReferenceVO schedulingReferenceVO) {
		return R.status(schedulingReferenceService.saveOrUpdateEntity(schedulingReferenceVO));
	}


	/**
	 * 删除 排班依据表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@Valid @RequestBody List<SchedulingReference> objectList) {
		return R.status(schedulingReferenceService.deleteLogic(objectList));
	}

	@GetMapping("/select")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "作为一个护士助手选择期望对应的排班配置的下拉选择数据", notes = "传入ids")
	public R select() {
		return R.data(schedulingReferenceService.selectByUserDept());
	}


	@PostMapping("/changeReferenceConfigState")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "改变排班配置表的状态为待排班、期望输入、或未启用")
	public R changeReferenceConfigState(
		@Valid @RequestBody SchedulingReferenceVO schedulingReferenceVO) {
		return R.data(schedulingReferenceService.changeReferenceConfigState(schedulingReferenceVO));
	}

	@PostMapping("/recheckReferenceConfigState")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "改变排班配置表的状态为待排班")
	public R recheckReferenceConfigState(
		@Valid @RequestBody SchedulingReferenceVO schedulingReferenceVO) {
		//todo 当状态从排班失败、排班成功转为待排班时，将原来排班结果的数据逻辑删除
		return R.status(schedulingReferenceService.recheckReferenceConfigState(schedulingReferenceVO));
	}


	@PostMapping("/scheduling")
	@ApiOperationSupport(order = 11)
	@ApiOperation(value = "排班", notes = "传入id")
	public R scheduling(@Valid @RequestBody SchedulingReferenceVO schedulingReferenceVO) {
		return R.data(schedulingReferenceService.scheduling(schedulingReferenceVO));
	}

}
