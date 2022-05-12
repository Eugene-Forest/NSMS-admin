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
import org.springblade.nsms.entity.ClockInConfig;
import org.springblade.nsms.entity.LeaveRecord;
import org.springblade.nsms.entity.NurseInfo;
import org.springblade.nsms.tools.Constant;
import org.springblade.nsms.tools.ServiceImplUtil;
import org.springblade.nsms.wrapper.ShiftRecordWrapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.nsms.entity.ShiftRecord;
import org.springblade.nsms.vo.ShiftRecordVO;
import org.springblade.nsms.service.IShiftRecordService;
import org.springblade.core.boot.ctrl.BladeController;

import java.util.List;
import java.util.stream.Collectors;

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
	@ApiOperation(value = "护士、助手等基础角色的分页", notes = "传入shiftRecord")
	public R<IPage<ShiftRecordVO>> list(ShiftRecord shiftRecord, Query query) {
		BladeUser user = SecureUtil.getUser();

		if (user.getTenantId() != null) {
			//获取用户对应的护士信息
			NurseInfo nurseInfo= ServiceImplUtil.getNurseInfoFromUser();
			shiftRecord.setTenantId(nurseInfo.getTenantId());
			shiftRecord.setCreateDept(nurseInfo.getDepartment());
			//处理筛选条件
			IPage<ShiftRecord> pages = shiftRecordService.page(Condition.getPage(query),
				Condition.getQueryWrapper(shiftRecord)
					.eq("be_requested_sid",nurseInfo.getId())
					.or(qw->qw.eq("create_user", nurseInfo.getUserId())));
			pages.getRecords().stream().map(x->{
				//我们在封装是需要注意，如果此纪录是本人创建那么可以状态替换为本人的状态
				//当本人作为被申请人时，此纪录不应当时本人创建的
				if (x.getBeRequestedSid().equals(nurseInfo.getId())){
					if (x.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_AGREE)){
						x.setApplicationStatus(Constant.EXCHANGE_APPROVAL_STATUS_I_AGREE);
					}else if (x.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_DISAGREE)){
						x.setApplicationStatus(Constant.EXCHANGE_APPROVAL_STATUS_I_DISAGREE);
					}else if (x.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_PENDING)){
						x.setApplicationStatus(Constant.EXCHANGE_APPROVAL_STATUS_I_PENDING);
					}
				}
				return x;
			}).collect(Collectors.toList());
			return R.data(ShiftRecordWrapper.build().pageVO(pages));
		}
		else {
			throw new RuntimeException("请确认此账号是否属于租户！");
		}
	}

	@GetMapping("/listForApproval")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "护士长的角色的分页", notes = "传入shiftRecord")
	public R<IPage<ShiftRecordVO>> listForApproval(ShiftRecord shiftRecord, Query query) {
		BladeUser user = SecureUtil.getUser();

		if (user.getTenantId() != null) {
			//获取用户对应的护士信息
			NurseInfo nurseInfo= ServiceImplUtil.getNurseInfoFromUser();
			shiftRecord.setTenantId(nurseInfo.getTenantId());
			shiftRecord.setCreateDept(nurseInfo.getDepartment());
			//处理筛选条件
			IPage<ShiftRecord> pages = shiftRecordService.page(Condition.getPage(query),
				Condition.getQueryWrapper(shiftRecord)
					.eq("approver",nurseInfo.getId()));
			return R.data(ShiftRecordWrapper.build().pageVO(pages));
		}
		else {
			throw new RuntimeException("请确认此账号是否属于租户！");
		}
	}





	/**
	 * 自定义分页 换班记录表
	 */
//	@GetMapping("/page")
//	@ApiOperationSupport(order = 3)
//	@ApiOperation(value = "分页", notes = "传入shiftRecord")
//	public R<IPage<ShiftRecordVO>> page(ShiftRecordVO shiftRecord, Query query) {
//		IPage<ShiftRecordVO> pages = shiftRecordService.selectShiftRecordPage(Condition.getPage(query), shiftRecord);
//		return R.data(pages);
//	}

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
		ShiftRecord origin=shiftRecordService.getById(shiftRecord.getId());
		if (origin.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_PENDING)){
			return R.status(shiftRecordService.updateById(shiftRecord));
		}else {
			throw new RuntimeException("请确认此申请的审核状态！");
		}
	}

	/**
	 * 新增或修改 换班记录表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入shiftRecord")
	public R submit(@Valid @RequestBody ShiftRecord shiftRecord) {
		if (shiftRecord.getId()!=null){
			ShiftRecord origin=shiftRecordService.getById(shiftRecord.getId());
			if (!origin.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_PENDING)){
				throw new RuntimeException("请刷新页面并请确认被选中的申请的审核状态！");
			}
		}
		return R.status(shiftRecordService.saveOrUpdate(shiftRecord));
	}


	/**
	 * 删除 换班记录表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@Valid @RequestBody List<ShiftRecord> objectList) {
		objectList.forEach(x->{
			ShiftRecord origin=shiftRecordService.getById(x.getId());
			if (!origin.getApplicationStatus().equals(Constant.EXCHANGE_APPROVAL_STATUS_PENDING)){
				throw new RuntimeException("请刷新页面并请确认被选中的申请的审核状态！");
			}
		});
		return R.status(shiftRecordService.deleteLogic(objectList));
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
