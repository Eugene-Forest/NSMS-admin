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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.nsms.entity.StaffTime;
import org.springblade.nsms.service.IStaffTimeService;
import org.springblade.nsms.tools.ServiceImplUtil;
import org.springblade.nsms.vo.StaffTimeVO;
import org.springblade.nsms.wrapper.StaffTimeWrapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 人员安排表，排班表 控制器
 *
 * @author Blade
 * @since 2022-04-18
 */
@RestController
@AllArgsConstructor
@RequestMapping("nsms/stafftime")
@Api(value = "人员安排表，排班表", tags = "人员安排表，排班表接口")
public class StaffTimeController extends BladeController {

	private IStaffTimeService staffTimeService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入staffTime")
	public R<StaffTimeVO> detail(StaffTime staffTime) {
		StaffTime detail = staffTimeService.getOne(Condition.getQueryWrapper(staffTime));
		return R.data(StaffTimeWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 人员安排表，排班表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入staffTime")
	public R<IPage<StaffTimeVO>> list(StaffTime staffTime, Query query) {
		IPage<StaffTime> pages = staffTimeService.page(Condition.getPage(query), Condition.getQueryWrapper(staffTime));
		return R.data(StaffTimeWrapper.build().pageVO(pages));
	}


	/**
	 * 自定义分页 人员安排表，排班表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入staffTime")
	public R<IPage<StaffTimeVO>> page(StaffTimeVO staffTime, Query query) {
		IPage<StaffTimeVO> pages = staffTimeService.selectStaffTimePage(Condition.getPage(query), staffTime);
		return R.data(pages);
	}

	/**
	 * 新增 人员安排表，排班表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入staffTime")
	public R save(@Valid @RequestBody StaffTime staffTime) {
		return R.status(staffTimeService.save(staffTime));
	}

	/**
	 * 修改 人员安排表，排班表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入staffTime")
	public R update(@Valid @RequestBody StaffTime staffTime) {
		return R.status(staffTimeService.updateById(staffTime));
	}

	/**
	 * 新增或修改 人员安排表，排班表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入staffTime")
	public R submit(@Valid @RequestBody StaffTime staffTime) {
		return R.status(staffTimeService.saveOrUpdate(staffTime));
	}


	/**
	 * 删除 人员安排表，排班表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(staffTimeService.deleteLogic(Func.toLongList(ids)));
	}


	@GetMapping("/calender")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "获取日历显示的排班数据", notes = "传入staffTime")
	public R calender(@RequestParam String date) {

		return R.data(staffTimeService.calendar(date));
	}

//	@GetMapping("/calender")
//	@ApiOperationSupport(order = 8)
//	@ApiOperation(value = "获取日历显示的排班数据", notes = "传入staffTime")
//	public R calender(@RequestParam(value = "start") String startDate,
//					  @RequestParam(value = "end") String endDate) {
//		return R.data(staffTimeService.calendar(startDate,endDate));
//	}

}
