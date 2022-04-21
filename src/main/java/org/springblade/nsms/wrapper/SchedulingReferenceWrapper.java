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
import org.springblade.nsms.entity.SchedulingReference;
import org.springblade.nsms.vo.SchedulingReferenceVO;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 排班依据表包装类,返回视图层所需的字段
 *
 * @author Blade
 * @since 2022-04-18
 */
public class SchedulingReferenceWrapper extends BaseEntityWrapper<SchedulingReference, SchedulingReferenceVO>  {

    public static SchedulingReferenceWrapper build() {
        return new SchedulingReferenceWrapper();
    }

	@Override
	public SchedulingReferenceVO entityVO(SchedulingReference schedulingReference) {
		SchedulingReferenceVO schedulingReferenceVO = BeanUtil.copy(schedulingReference, SchedulingReferenceVO.class);
		//对日期范围字段进行封装赋值
		List<String> dateList=new ArrayList<>();
		dateList.add(DateUtil.format(schedulingReference.getStartDate(),"yyyy-MM-dd HH:mm:ss"));
		dateList.add(DateUtil.format(schedulingReference.getEndDate(),"yyyy-MM-dd HH:mm:ss"));
		schedulingReferenceVO.setDateRange(dateList);
		return schedulingReferenceVO;
	}

	public SchedulingReferenceVO resolveEntityForSelect(SchedulingReference schedulingReference){
		SchedulingReferenceVO schedulingReferenceVO = this.entityVO(schedulingReference);
		//数据验证
		if (schedulingReference.getEndDate()==null||schedulingReference.getStartDate()==null){
			return schedulingReferenceVO;
		}
		//对标题字段进行封装赋值
		String title="";
		title+=DateUtil.format(schedulingReferenceVO.getStartDate(),"yyyy-MM-dd");
		title+=" 至 ";
		title+=DateUtil.format(schedulingReferenceVO.getEndDate(),"yyyy-MM-dd");
		title+="时间内的排班";
		schedulingReferenceVO.setTitle(title);
		return schedulingReferenceVO;
	}

}
