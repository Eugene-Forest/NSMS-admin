package org.springblade.nsms.tools;

import lombok.SneakyThrows;
import org.apache.poi.ss.formula.functions.T;
import org.springblade.common.tool.SpringBeanUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.system.entity.Post;
import org.springblade.modules.system.service.impl.PostServiceImpl;
import org.springblade.nsms.entity.NurseInfo;
import org.springblade.nsms.service.INurseInfoService;
import org.springblade.rewrite.FoundationEntity;

import java.util.Date;

/**
 * @author Eugene-Forest
 * @date 2022/4/3
 **/
public class ServiceImplUtil {

	public static Integer getUserPostType(){
		NurseInfo nurseInfo=getNurseInfoFromUser();
		Post post=SpringBeanUtil.getApplicationContext().getBean(PostServiceImpl.class)
			.getOne(Condition.getQueryWrapper(new Post())
				.eq("tenant_id", nurseInfo.getTenantId())
				.eq("id", nurseInfo.getPosition()));
		return post.getCategory();
	}


	public static <T extends FoundationEntity> String getUserTenantId() {
		BladeUser user =  SecureUtil.getUser();
		if (user!=null){
			return user.getTenantId();
		}else {
			throw new RuntimeException("账号异常！");
		}
	}

	public static <T extends FoundationEntity> Long getUserId() {
		BladeUser user =  SecureUtil.getUser();
		if (user!=null){
			return user.getUserId();
		}else {
			throw new RuntimeException("账号异常！");
		}
	}

	/**
	 * 获取用户对应的账号id
	 * @param <T>
	 * @return
	 */
	public static <T extends FoundationEntity> Long getNurseIdFromUser() {
		BladeUser user =  SecureUtil.getUser();
		if (user!=null){
			return getNurseIdFromUser(user);
		}else {
			throw new RuntimeException("账号异常！");
		}
	}

	/**
	 * 获取用户对应的账号id
	 * @param user
	 * @param <T>
	 * @return
	 */
	public static <T extends FoundationEntity> Long getNurseIdFromUser(BladeUser user) {
		INurseInfoService iNurseInfoService= SpringBeanUtil.getBean(INurseInfoService.class);
		NurseInfo nurseInfo=iNurseInfoService.getNurseInfoByUserId(Func.toStr(user.getUserId()));
		if (nurseInfo==null){
			throw new RuntimeException("请确认账号是否关联了护士信息");
		}
		return nurseInfo.getId();
	}


	/**
	 * 获取账号对应的护士信息
	 * @param <T>
	 * @return
	 */
	public static <T extends FoundationEntity> NurseInfo getNurseInfoFromUser() {
		BladeUser user =  SecureUtil.getUser();
		if (user!=null){
			return getNurseInfoFromUser(user);
		}else {
			throw new RuntimeException("账号异常！");
		}
	}

	/**
	 * 获取账号对应的护士的所在部门
	 */
	public static <T extends FoundationEntity> Long getNurseDeptFromUser(BladeUser user){
		INurseInfoService iNurseInfoService= SpringBeanUtil.getBean(INurseInfoService.class);
		NurseInfo nurseInfo=iNurseInfoService.getNurseInfoByUserId(Func.toStr(user.getUserId()));
		if (nurseInfo==null){
			throw new RuntimeException("请确认账号是否关联了护士信息");
		}
		return nurseInfo.getDepartment();
	}

	/**
	 *  获取账号对应的护士的所在部门
	 */
	public static <T extends FoundationEntity> Long getNurseDeptFromUser(){
		BladeUser user =  SecureUtil.getUser();
		if (user!=null){
			return getNurseDeptFromUser(user);
		}else {
			throw new RuntimeException("账号异常！");
		}
	}

	/**
	 * 获取账号对应的护士信息
	 * @param user
	 * @param <T>
	 * @return
	 */
	public static <T extends FoundationEntity> NurseInfo getNurseInfoFromUser(BladeUser user) {
		INurseInfoService iNurseInfoService=SpringBeanUtil.getBean(INurseInfoService.class);
		NurseInfo nurseInfo=iNurseInfoService.getNurseInfoByUserId(Func.toStr(user.getUserId()));
		if (nurseInfo==null){
			throw new RuntimeException("请确认账号是否关联了护士信息");
		}
		return nurseInfo;
	}


	public static <T extends FoundationEntity> void resolveEntity(T entity) {
		BladeUser user = SecureUtil.getUser();
		resolveEntityByBladeUser(entity,user);
	}

	public static <T extends FoundationEntity> void resolveEntityByBladeUser(T entity,BladeUser user) {
		try {
			Date now = DateUtil.now();
			//判断是更新还是添加
			if (user==null){
				throw new RuntimeException("找不到用户对应的信息！");
			}
			//判断是更新还是添加
			if (entity.getId() == null) {
				//当操作为更新时
				entity.setCreateUser(user.getUserId());
				//添加判断以适应测试用的账户可能会出现的部门id为空的情况
				if (user.getDeptId()!=null){
					entity.setCreateDept(Long.valueOf(user.getDeptId()));
				}
				entity.setCreateTime(now);
				entity.setIsDeleted(0);
				entity.setStatus(0);
				entity.setTenantId(user.getTenantId());
			} else {
				entity.setUpdateUser(user.getUserId());
				entity.setUpdateTime(now);
				//将此纪录的系统状态加1以表示此纪录被更新
				entity.setStatus(entity.getStatus()+1);
			}

		} catch (Exception var8) {
			throw new RuntimeException("处理业务类的基础类[FoundationEntity]时出现错误");
		}
	}

}
