package org.springblade.rewrite;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Eugene-Forest
 * @date 2022/3/15
 **/
@Validated
public class FoundationServiceImpl<M extends BaseMapper<T>, T extends FoundationEntity> extends ServiceImpl<M, T> implements FoundationService<T> {


	@Override
	@Transactional(
		rollbackFor = {Exception.class}
	)
	public boolean deleteLogic(@NotEmpty List<Long> ids) {
		//通过Secure工具类提供的方法，获取于请求中的用户信息
		BladeUser user = SecureUtil.getUser();
		List<T> list = new ArrayList();
		ids.forEach((id) -> {
			T entity = BeanUtil.newInstance(this.currentModelClass());
			if (user != null) {
				entity.setUpdateUser(user.getUserId());
			}

			entity.setUpdateTime(DateUtil.now());
			entity.setId(id);
			list.add(entity);
		});
		return super.updateBatchById(list) && super.removeByIds(ids);
	}


	@Override
	public boolean changeStatus(@NotEmpty List<Long> ids, Integer status) {
		BladeUser user = SecureUtil.getUser();
		List<T> list = new ArrayList();
		ids.forEach((id) -> {
			T entity = BeanUtil.newInstance(this.currentModelClass());
			if (user != null) {
				entity.setUpdateUser(user.getUserId());
			}

			entity.setUpdateTime(DateUtil.now());
			entity.setId(id);
			entity.setStatus(status);
			list.add(entity);
		});
		return super.updateBatchById(list);
	}

	@Override
	public boolean save(T entity) {
		this.resolveEntity(entity);
		//添加对主键的生成
		return super.save(entity);
	}

	@Override
	public boolean saveBatch(Collection<T> entityList, int batchSize) {
		entityList.forEach(this::resolveEntity);
		return super.saveBatch(entityList, batchSize);
	}

	@Override
	public boolean updateById(T entity) {
		this.resolveEntity(entity);
		return super.updateById(entity);
	}

	@Override
	public boolean updateBatchById(Collection<T> entityList, int batchSize) {
		entityList.forEach(this::resolveEntity);
		return super.updateBatchById(entityList, batchSize);
	}

	@Override
	public boolean saveOrUpdate(T entity) {
		return entity.getId() == null ? this.save(entity) : this.updateById(entity);
	}

	@Override
	public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
		entityList.forEach(this::resolveEntity);
		return super.saveOrUpdateBatch(entityList, batchSize);
	}



	@SneakyThrows
	private void resolveEntity(T entity) {
		try {
			BladeUser user = SecureUtil.getUser();
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

//			Field field = ReflectUtil.getField(entity.getClass(), "tenantId");
//			if (ObjectUtil.isNotEmpty(field)) {
//				Method getTenantId = ClassUtil.getMethod(entity.getClass(), "getTenantId");
//				String tenantId = String.valueOf(getTenantId.invoke(entity));
//				Method setTenantId = ClassUtil.getMethod(entity.getClass(), "setTenantId");
//				if (ObjectUtil.isEmpty(tenantId)) {
//					setTenantId.invoke(entity, null);
//				}else{
//					setTenantId.invoke(entity, tenantId);
//				}
//			}

		} catch (Throwable var8) {
			throw new RuntimeException("处理业务类的基础类[FoundationEntity]时出现错误");
		}
	}
}
