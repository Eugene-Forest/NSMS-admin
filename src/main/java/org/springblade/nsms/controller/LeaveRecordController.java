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
import org.springblade.nsms.entity.LeaveRecord;
import org.springblade.nsms.entity.NurseInfo;
import org.springblade.nsms.entity.ShiftRecord;
import org.springblade.nsms.tools.Constant;
import org.springblade.nsms.tools.ServiceImplUtil;
import org.springblade.nsms.vo.LeaveRecordVO;
import org.springblade.nsms.wrapper.LeaveRecordWrapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.nsms.service.ILeaveRecordService;
import org.springblade.core.boot.ctrl.BladeController;

import java.util.List;

/**
 * 请假记录表 控制器
 *
 * @author Blade
 * @since 2022-03-14
 */
@RestController
@AllArgsConstructor
@RequestMapping("nsms/leaverecord")
@Api(value = "请假记录表", tags = "请假记录表接口")
public class LeaveRecordController extends BladeController {

	private ILeaveRecordService leaveRecordService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入leaveRecord")
	public R<LeaveRecordVO> detail(LeaveRecord leaveRecord) {
		LeaveRecord detail = leaveRecordService.getOne(Condition.getQueryWrapper(leaveRecord));
		return R.data(LeaveRecordWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 请假记录表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "助手以及护士分页", notes = "传入leaveRecord")
	public R<IPage<LeaveRecordVO>> list(LeaveRecord leaveRecord, Query query) {
		BladeUser user = SecureUtil.getUser();
		//todo: 需要针对管理员用户与普通用户以区别,同时针对不同角色的用户有不同的筛选条件(即：数据权限功能)
		//对于根管理员有所有的数据查看权限，对于医院管理员有对应医院的所有数据的查看权限
		//对于用户，若是科室护士长则有对应科室的所有数据的查看权限，对于护士或助手则只有其对应或相关的数据的查看权限
		//对于适应后期添加用户角色以适应数据权限的查看机制的设计！！

		IPage<LeaveRecord> pages;
		if (user.getTenantId()!=null){
			pages = leaveRecordService.page(Condition.getPage(query),
				Condition.getQueryWrapper(leaveRecord).
					eq("tenant_id",user.getTenantId()).
					eq("create_dept", user.getDeptId()).
					eq("create_user",user.getUserId())
					);
			return R.data(LeaveRecordWrapper.build().pageVO(pages));
		}else {
			throw new RuntimeException("请确认此账号是否属于租户！");
		}
	}

	@GetMapping("/listForApproval")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "护士长的分页", notes = "传入leaveRecord")
	public R<IPage<LeaveRecordVO>> listForApproval(LeaveRecord leaveRecord, Query query) {
		NurseInfo nurseInfo = ServiceImplUtil.getNurseInfoFromUser();
		//todo: 需要针对管理员用户与普通用户以区别,同时针对不同角色的用户有不同的筛选条件(即：数据权限功能)
		//对于根管理员有所有的数据查看权限，对于医院管理员有对应医院的所有数据的查看权限
		//对于用户，若是科室护士长则有对应科室的所有数据的查看权限，对于护士或助手则只有其对应或相关的数据的查看权限
		//对于适应后期添加用户角色以适应数据权限的查看机制的设计！！

		IPage<LeaveRecord> pages;
		if (nurseInfo.getTenantId()!=null){
			pages = leaveRecordService.page(Condition.getPage(query),
				Condition.getQueryWrapper(leaveRecord).
					eq("tenant_id",nurseInfo.getTenantId()).
					eq("create_dept", nurseInfo.getDepartment())
					.eq("approver", nurseInfo.getId()));
			return R.data(LeaveRecordWrapper.build().pageVO(pages));
		}else {
			throw new RuntimeException("请确认此账号是否属于租户！");
		}
	}


	/**
	 * 自定义分页 请假记录表
	 */
//	@GetMapping("/page")
//	@ApiOperationSupport(order = 3)
//	@ApiOperation(value = "分页", notes = "传入leaveRecord")
//	public R<IPage<LeaveRecordVO>> page(LeaveRecordVO leaveRecord, Query query) {
//		IPage<LeaveRecordVO> pages = leaveRecordService.selectLeaveRecordPage(Condition.getPage(query), leaveRecord);
//		return R.data(pages);
//	}

	/**
	 * 新增 请假记录表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入leaveRecord")
	public R save(@Valid @RequestBody LeaveRecord leaveRecord) {
		return R.status(leaveRecordService.applyForLeave(leaveRecord));
	}

	/**
	 * 修改 请假记录表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入leaveRecord")
	public R update(@Valid @RequestBody LeaveRecord leaveRecord) {
		LeaveRecord origin=leaveRecordService.getById(leaveRecord.getId());
		if (!origin.getApprovalStatus().equals(Constant.APPROVAL_STATUS_PENDING)){
			throw new RuntimeException("请刷新页面并请确认被选中的申请的审核状态！");
		}
		return R.status(leaveRecordService.updateForLeave(leaveRecord));
	}

	/**
	 * 新增或修改 请假记录表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入leaveRecord")
	public R submit(@Valid @RequestBody LeaveRecord leaveRecord) {
		return R.status(leaveRecordService.applyOrUpdateForLeave(leaveRecord));
	}


	/**
	 * 删除 请假记录表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		List<Long> idList=Func.toLongList(ids);
		idList.forEach(x->{
			LeaveRecord origin=leaveRecordService.getById(x);
			if (!origin.getApprovalStatus().equals(Constant.APPROVAL_STATUS_PENDING)){
				throw new RuntimeException("请刷新页面并请确认被选中的申请的审核状态！");
			}
		});
		return R.status(leaveRecordService.deleteLogic(Func.toLongList(ids)));
	}

	/**
	 * 审核 请假记录表
	 */
	@PostMapping("/checkIn")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "审核请假", notes = "传入ids")
	public R checkIn(@Valid @RequestBody LeaveRecord leaveRecord) {
		return R.status(leaveRecordService.checkInLeaveRecord(leaveRecord));
	}


	/**
	 * 反审 请假记录表
	 */
	@PostMapping("/recheckIn")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "反审请假", notes = "传入ids")
	public R recheckIn(@Valid @RequestBody LeaveRecord leaveRecord) {
		return R.status(leaveRecordService.recheckInLeaveRecord(leaveRecord));
	}
}
