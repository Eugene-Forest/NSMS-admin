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
package org.springblade.nsms.dto;

import org.springblade.nsms.entity.Expectation;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 护士助手的排班期望表数据传输对象实体类
 *
 * @author Blade
 * @since 2022-04-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExpectationDTO extends Expectation implements Comparable<ExpectationDTO>{
	private static final long serialVersionUID = 1L;

	/**
	 * 护理人员的职位类型
	 */
	private Integer postType;

	public ExpectationDTO(Expectation expectation,Integer postType) {
		this.setId(expectation.getId());
		this.setPriority(expectation.getPriority());
		this.setStartDate(expectation.getStartDate());
		this.setEndDate(expectation.getEndDate());
		this.setNurseSid(expectation.getNurseSid());
		this.setExpectationType(expectation.getExpectationType());
		this.setDayNumber(expectation.getDayNumber());
		this.setActualState(expectation.getActualState());
		this.setReferenceSid(expectation.getReferenceSid());
		this.postType = postType;
	}

	/**
	 * 优先级比较
	 * @param o the object to be compared.
	 * @return
	 */
	@Override
	public int compareTo(ExpectationDTO o) {
		if (this.getPriority().compareTo(o.getPriority())>0){
			return 1;
		}else if (this.getPriority().compareTo(o.getPriority())<0){
			return -1;
		}else {
			return 0;
		}
	}

	/**
	 * 优先级比较(逆序)
	 * @param o the object to be compared.
	 * @return
	 */
	public int compareToReverse(ExpectationDTO o) {
		if (this.getPriority().compareTo(o.getPriority())>0){
			return -1;
		}else if (this.getPriority().compareTo(o.getPriority())<0){
			return 1;
		}else {
			return 0;
		}
	}
}
