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
package org.springblade.nsms.service;

import org.springblade.nsms.entity.NurseInfo;
import org.springblade.nsms.vo.NurseInfoVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.rewrite.FoundationService;

import java.util.List;

/**
 * 护士档案  服务类
 *
 * @author Blade
 * @since 2022-03-14
 */
public interface INurseInfoService extends FoundationService<NurseInfo> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param nurseInfo
	 * @return
	 */
	IPage<NurseInfoVO> selectNurseInfoPage(IPage<NurseInfoVO> page, NurseInfoVO nurseInfo);


	/**
	 * 通过用户id获取对应的护士信息
	 */
	NurseInfo getNurseInfoByUserId(String userId);

	/**
	 * 获取同部门的同事的信息--id--name
	 * @return
	 */
	List<NurseInfo> selectCoWorkerFromSameDept();

	/**
	 * 获取同部门的护士长的信息--id--name
	 * @return
	 */
	List<NurseInfo> selectHeadNurseFromSameDept();

	/**
	 * 获取同部门的护士助手的信息--id--name
	 * @return
	 */
	List<NurseInfo> selectNursesFromSameDept();

	/**
	 * 获取同部门的护士长的信息--id--name
	 * @return
	 */
	List<NurseInfo> selectHeadNursesFromSameDept();

}