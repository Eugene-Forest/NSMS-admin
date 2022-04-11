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
package org.springblade.nsms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sun.istack.NotNull;
import org.mapstruct.Mapper;
import org.springblade.nsms.entity.NurseInfo;
import org.springblade.nsms.vo.NurseInfoVO;

import java.util.List;

/**
 * 护士档案  Mapper 接口
 *
 * @author Blade
 * @since 2022-03-14
 */
@Mapper
public interface NurseInfoMapper extends BaseMapper<NurseInfo> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param nurseInfo
	 * @return
	 */
	List<NurseInfoVO> selectNurseInfoPage(IPage page, NurseInfoVO nurseInfo);


	/**
	 * 通过用户id获取对应的护士信息
	 *
	 * @param userId 用户id
	 */
	NurseInfo getNurseInfoByUserId(@NotNull String userId);


	/**
	 * 获取同部门的同事的信息--id--name
	 * @return
	 */
	List<NurseInfo> selectCoWorkerFromSameDept(@NotNull Long deptId,@NotNull String tenantId, Long myId);

	/**
	 * 获取同部门的护士长的信息--id--name
	 * @return
	 */
	List<NurseInfo> selectHeadNurseFromSameDept(@NotNull Long deptId,@NotNull String tenantId,Long myId);
}