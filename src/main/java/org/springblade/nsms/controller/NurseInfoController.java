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
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.nsms.entity.NurseInfo;
import org.springblade.nsms.vo.NurseInfoVO;
import org.springblade.nsms.wrapper.NurseInfoWrapper;
import org.springblade.nsms.service.INurseInfoService;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 护士档案  控制器
 *
 * @author Blade
 * @since 2022-03-14
 */
@RestController
@AllArgsConstructor
@RequestMapping("nsms/nurseinfo")
@Api(value = "护士档案 ", tags = "护士档案 接口")
public class NurseInfoController extends BladeController {

	private INurseInfoService nurseInfoService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入nurseInfo")
	public R<NurseInfoVO> detail(NurseInfo nurseInfo) {
		NurseInfo detail = nurseInfoService.getOne(Condition.getQueryWrapper(nurseInfo));
		return R.data(NurseInfoWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 护士档案
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入nurseInfo")
	public R<IPage<NurseInfoVO>> list(NurseInfo nurseInfo, Query query) {
		//todo: 针对管理员的权限进行改造
		BladeUser user = SecureUtil.getUser();
		IPage<NurseInfo> pages ;
		if (user.getTenantId()!=null){
			pages = nurseInfoService.page(Condition.getPage(query),
				Condition.getQueryWrapper(nurseInfo).
					eq("tenant_id",user.getTenantId()).
					eq("create_dept", user.getDeptId()));
			return R.data(NurseInfoWrapper.build().pageVO(pages));
		}else {
			throw new RuntimeException("请确认此账号是否属于租户！");
		}
	}


	/**
	 * 自定义分页 护士档案
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入nurseInfo")
	public R<IPage<NurseInfoVO>> page(NurseInfoVO nurseInfo, Query query) {
		IPage<NurseInfoVO> pages = nurseInfoService.selectNurseInfoPage(Condition.getPage(query), nurseInfo);
		return R.data(pages);
	}

	/**
	 * 新增 护士档案
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入nurseInfo")
	public R save(@Valid @RequestBody NurseInfo nurseInfo) {
		return R.status(nurseInfoService.save(nurseInfo));
	}

	/**
	 * 修改 护士档案
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入nurseInfo")
	public R update(@Valid @RequestBody NurseInfo nurseInfo) {
		return R.status(nurseInfoService.updateById(nurseInfo));
	}

	/**
	 * 新增或修改 护士档案
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入nurseInfo")
	public R submit(@Valid @RequestBody NurseInfo nurseInfo) {
		return R.status(nurseInfoService.saveOrUpdate(nurseInfo));
	}


	/**
	 * 删除 护士档案
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(nurseInfoService.deleteLogic(Func.toLongList(ids)));
	}


	@GetMapping("/selectCoWorker")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "同部门同事下拉列表")
	public R  selectCoWorkerFromSameDept(){
		return R.data(nurseInfoService.selectCoWorkerFromSameDept());
	}


	@GetMapping("/selectHeadNurse")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "同部门护士长下拉列表")
	public R  selectHeadNurseFromSameDept(){
		return R.data(nurseInfoService.selectHeadNurseFromSameDept());
	}


	@GetMapping("/selectNurses")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "同部门护士助手下拉列表")
	public R  selectNursesFromSameDept(){
		return R.data(nurseInfoService.selectNursesFromSameDept());
	}

	@GetMapping("/selectHeadNurses")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "同部门护士长下拉列表")
	public R  selectHeadNurses(){
		return R.data(nurseInfoService.selectHeadNursesFromSameDept());
	}

}
