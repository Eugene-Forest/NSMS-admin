package org.springblade.rewrite;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.nsms.entity.Expectation;
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
			entity.setStatus(entity.getStatus()+1);
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
		//在更新前先进行记录的系统状态验证
		T origin=super.getById(entity.getId());
		if (origin.getStatus().equals(entity.getStatus())){
			this.resolveEntity(entity);
			return super.updateById(entity);
		}else {
			throw new RuntimeException("此数据已被更新，请刷新数据后再进行编辑！");
		}
	}

	@Override
	public boolean updateBatchById(Collection<T> entityList, int batchSize) {
		//在更新前先进行记录的系统状态验证
		entityList.forEach(x->{
			//在更新前先进行记录的系统状态验证
			T origin=super.getById(x.getId());
			if (!origin.getStatus().equals(x.getStatus())){
				throw new RuntimeException("此数据已被更新，请刷新数据后再进行编辑！");
			}
		});
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

	private void resolveEntity(T entity) {
		try {
			BladeUser user = SecureUtil.getUser();
			Date now = DateUtil.now();
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
		}catch (Exception e){
			throw new RuntimeException(e.getMessage());
		}

	}
}
