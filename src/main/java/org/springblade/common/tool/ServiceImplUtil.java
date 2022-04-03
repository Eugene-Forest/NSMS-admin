package org.springblade.common.tool;

import lombok.SneakyThrows;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.nsms.base.entity.NurseInfo;
import org.springblade.nsms.base.service.INurseInfoService;
import org.springblade.rewrite.FoundationEntity;

import java.util.Date;

/**
 * @author Eugene-Forest
 * @date 2022/4/3
 **/
public class ServiceImplUtil {


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
		INurseInfoService iNurseInfoService=SpringBeanUtil.getBean(INurseInfoService.class);
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

	@SneakyThrows
	public static <T extends FoundationEntity> void resolveEntityByBladeUser(T entity,BladeUser user) {
		try {
			Date now = DateUtil.now();
			//判断是更新还是添加
			if (entity.getId() == null) {
				if (user != null) {
					entity.setCreateUser(user.getUserId());
					entity.setUpdateUser(user.getUserId());
					//添加判断以适应测试用的账户可能会出现的部门id为空的情况
					if (user.getDeptId()!=null){
						entity.setCreateDept(Long.valueOf(user.getDeptId()));
					}
				}

				if (entity.getStatus() == null) {
					entity.setStatus(1);
				}

				entity.setCreateTime(now);
				entity.setIsDeleted(0);
				assert user != null;
				entity.setTenantId(user.getTenantId());
			} else if (user != null) {
				entity.setUpdateUser(user.getUserId());
				entity.setUpdateTime(now);
			}

		} catch (Throwable var8) {
			throw new RuntimeException("处理业务类的基础类[FoundationEntity]时出现错误");
		}
	}

}
