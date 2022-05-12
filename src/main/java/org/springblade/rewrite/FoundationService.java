package org.springblade.rewrite;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.SecureUtil;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Eugene-Forest
 * @date 2022/3/15
 **/
public interface FoundationService<T> extends IService<T> {
	boolean deleteLogic(@NotEmpty List<T> ids);

	boolean changeStatus(@NotEmpty List<Long> ids, Integer status);

	/**
	 * 重写：分页查询
	 */

}
