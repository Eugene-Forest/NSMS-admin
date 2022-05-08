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
package org.springblade.nsms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.modules.system.entity.User;
import org.springblade.modules.system.service.impl.UserServiceImpl;
import org.springblade.nsms.dto.NurseInfoDTO;
import org.springblade.nsms.tools.ServiceImplUtil;
import org.springblade.nsms.entity.NurseInfo;
import org.springblade.nsms.mapper.NurseInfoMapper;
import org.springblade.nsms.service.INurseInfoService;
import org.springblade.nsms.vo.NurseInfoVO;
import org.springblade.rewrite.FoundationServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	 * 对护士信息进行新加或更改操作时，同时对用户信息进行操作
	 *
	 * @param nurseInfo
	 * @return
	 */
	@Override
	public boolean saveOrUpdateWithUser(NurseInfoVO nurseInfo) {
		boolean flag=true;
		if (nurseInfo.getId()==null){
			//我们护士信息中的手机号来生成账户名
			//todo 对于用户表的账户，我们需要保证其唯一性，所以在自动创建时需要注意先校验是否存在同名账户以免出错
			User user=new User();

			//自动数字编号
			user.setAccount(nurseInfo.getTelephone());
			user.setPassword("123456");
			//todo 如何保证并发时不会出现创建按账户的问题
			//获取本医院中的 account 的最大值,然后加一

			user.setName(nurseInfo.getName());
			user.setRealName(nurseInfo.getName());
			user.setSex(nurseInfo.getGender());
			user.setBirthday(nurseInfo.getBirthday());
			user.setDeptId(Func.toStr(nurseInfo.getDepartment()));
			//职位
			user.setPostId(Func.toStr(nurseInfo.getPosition()));
			//角色
			user.setRoleId(Func.toStr(nurseInfo.getRoleId()));
			//需要注意的是，system 包下的服务是没有被复写的，所以在进行操作是需要对实体进行添加信息
			BladeUser bladeUser = SecureUtil.getUser();

			Date date=new Date();
			user.setCreateUser(bladeUser.getUserId());
			user.setCreateTime(date);
			user.setTenantId(bladeUser.getTenantId());
			flag=flag&&SpringUtil.getContext().getBean(UserServiceImpl.class).saveOrUpdate(user);
			//this.saveOrUpdate(xx)执行之后，nurseInfo对象会被同步为最终插入数据库中的数据；可能是此方法的特性

			nurseInfo.setUserId(user.getId());
			//todo 数据在保存时需要校验是否合理
			flag=flag&&this.saveOrUpdate(nurseInfo);
		}else {
			//todo 数据在保存时需要校验是否合理
			 flag =this.saveOrUpdate(nurseInfo);
		}

		return flag;
	}

	/**
	 * 对护士信息进行删除操作时，同时对用户信息进行操作
	 *
	 * @param nurseInfo
	 * @return
	 */
	@Override
	public boolean deleteWithUser(NurseInfo nurseInfo) {

		boolean flag=this.removeById(nurseInfo);
		flag=flag&&SpringUtil.getContext().getBean(UserServiceImpl.class).removeById(nurseInfo.getUserId());

		return flag;
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
	 * 获取同部门的所有护士助手的信息--id--name
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
	 * 获取同部门的所有护士长的信息--id--name
	 *
	 * @return
	 */
	@Override
	public List<NurseInfo> selectHeadNursesFromSameDept() {
		//获取此用户的信息
		NurseInfo nurseInfo=ServiceImplUtil.getNurseInfoFromUser();
		return nurseInfoMapper.selectHeadNurseFromSameDept(nurseInfo.getDepartment(), nurseInfo.getTenantId(), null);
	}

	@Override
	public List<NurseInfo> selectAllFromSameDept() {
		NurseInfo nurseInfo=ServiceImplUtil.getNurseInfoFromUser();
		return baseMapper.selectAllFromSameDept(nurseInfo.getDepartment(), nurseInfo.getTenantId());
	}


	@Override
	public List<NurseInfoDTO> selectAllBaseNurseFromSampDept() {
		NurseInfo nurseInfo=ServiceImplUtil.getNurseInfoFromUser();
		return baseMapper.selectAllBaseNurseFromSampDept(nurseInfo.getDepartment(), nurseInfo.getTenantId());
	}

	/**
	 * 获取本人的id以及名字
	 *
	 * @return
	 */
	@Override
	public Map<String, String> getUserIdAndName() {
		Map<String,String> infoMap=new HashMap<>();
		NurseInfo nurseInfo=ServiceImplUtil.getNurseInfoFromUser();
		infoMap.put("id", Func.toStr(nurseInfo.getId()));
		infoMap.put("name", nurseInfo.getName());
		return infoMap;
	}


}
