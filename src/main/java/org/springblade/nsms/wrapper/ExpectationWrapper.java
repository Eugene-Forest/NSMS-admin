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
package org.springblade.nsms.wrapper;

import lombok.AllArgsConstructor;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.nsms.entity.Expectation;
import org.springblade.nsms.tools.Constant;
import org.springblade.nsms.vo.ExpectationVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 护士助手的排班期望表包装类,返回视图层所需的字段
 *
 * @author Blade
 * @since 2022-04-18
 */
public class ExpectationWrapper extends BaseEntityWrapper<Expectation, ExpectationVO>  {

    public static ExpectationWrapper build() {
        return new ExpectationWrapper();
    }

	@Override
	public ExpectationVO entityVO(Expectation expectation) {
		ExpectationVO expectationVO = BeanUtil.copy(expectation, ExpectationVO.class);
		//对日期范围字段进行封装赋值
////		先对期望类型进行判断
//		if (expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_NUMBER) ||
//			expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_NUMBER)){
//			return expectationVO;
//		}
		//数据验证
		List<String> dateList=new ArrayList<>();
		if (expectation.getStartDate()!=null){
			dateList.add(DateUtil.format(expectation.getStartDate(),"yyyy-MM-dd HH:mm:ss"));
		}
		if (expectation.getEndDate()!=null){
			dateList.add(DateUtil.format(expectation.getEndDate(),"yyyy-MM-dd HH:mm:ss"));
		}
		expectationVO.setDateRange(dateList);
		return expectationVO;
	}

}
