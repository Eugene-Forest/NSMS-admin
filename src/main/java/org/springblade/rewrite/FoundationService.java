package org.springblade.rewrite;

import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Eugene-Forest
 * @date 2022/3/15
 **/
public interface FoundationService<T> extends IService<T> {
	boolean deleteLogic(@NotEmpty List<Long> ids);

	boolean changeStatus(@NotEmpty List<Long> ids, Integer status);
}
