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
package org.springblade.nsms.base.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.common.tool.ServiceImplUtil;
import org.springblade.nsms.base.entity.NurseInfo;
import org.springblade.nsms.base.mapper.NurseInfoMapper;
import org.springblade.nsms.base.service.INurseInfoService;
import org.springblade.nsms.base.vo.NurseInfoVO;
import org.springblade.rewrite.FoundationServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 护士档案  服务实现类
 *
 * @author Blade
 * @since 2022-03-14
 */
@Service
public class NurseInfoServiceImpl extends FoundationServiceImpl<NurseInfoMapper, NurseInfo> implements INurseInfoService {

	@Resource
	private NurseInfoMapper nurseInfoMapper;

	@Override
	public IPage<NurseInfoVO> selectNurseInfoPage(IPage<NurseInfoVO> page, NurseInfoVO nurseInfo) {
		return page.setRecords(baseMapper.selectNurseInfoPage(page, nurseInfo));
	}

	/**
	 * 通过用户id获取对应的护士信息
	 *
	 * @param userId 用户id
	 */
	@Override
	public NurseInfo getNurseInfoByUserId(String userId) {
		if (userId==null||userId.isEmpty()){
			return null;
		}else {
			return baseMapper.getNurseInfoByUserId(userId);
		}
	}

	/**
	 * 获取同部门的同事的信息--id--name
	 *
	 * @return
	 */
	@Override
	public List<NurseInfo> selectCoWorkerFromSameDept() {
		//获取此用户的信息
		NurseInfo nurseInfo=ServiceImplUtil.getNurseInfoFromUser();
		return nurseInfoMapper.selectCoWorkerFromSameDept(nurseInfo.getDepartment(), nurseInfo.getTenantId(),nurseInfo.getId());
	}

	/**
	 * 获取同部门的护士长的信息--id--name
	 *
	 * @return
	 */
	@Override
	public List<NurseInfo> selectHeadNurseFromSameDept() {
		//获取此用户的信息
		NurseInfo nurseInfo=ServiceImplUtil.getNurseInfoFromUser();
		return nurseInfoMapper.selectHeadNurseFromSameDept(nurseInfo.getDepartment(), nurseInfo.getTenantId(),nurseInfo.getId());
	}

	/**
	 * 获取同部门的护士助手的信息--id--name
	 *
	 * @return
	 */
	@Override
	public List<NurseInfo> selectNursesFromSameDept() {
		//获取此用户的信息
		NurseInfo nurseInfo=ServiceImplUtil.getNurseInfoFromUser();
		return nurseInfoMapper.selectCoWorkerFromSameDept(nurseInfo.getDepartment(), nurseInfo.getTenantId(), null);
	}

	/**
	 * 获取同部门的护士长的信息--id--name
	 *
	 * @return
	 */
	@Override
	public List<NurseInfo> selectHeadNursesFromSameDept() {
		//获取此用户的信息
		NurseInfo nurseInfo=ServiceImplUtil.getNurseInfoFromUser();
		return nurseInfoMapper.selectHeadNurseFromSameDept(nurseInfo.getDepartment(), nurseInfo.getTenantId(), null);
	}

}
