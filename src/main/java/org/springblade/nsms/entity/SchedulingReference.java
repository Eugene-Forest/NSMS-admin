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
package org.springblade.nsms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.rewrite.FoundationEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

/**
 * 排班依据表实体类
 *
 * @author Blade
 * @since 2022-04-18
 */
@Data
@TableName("com_scheduling_reference")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SchedulingReference对象", description = "排班依据表")
public class SchedulingReference extends FoundationEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 科室编号
     */
    @ApiModelProperty(value = "科室编号")
	@JsonSerialize(
		using = ToStringSerializer.class
	)
    private Long departmentId;
    /**
     * 排班期间的假期天数
     */
    @ApiModelProperty(value = "排班期间的假期天数")
    private Integer vacationTimes;
    /**
     * 夜班护士数
     */
    @ApiModelProperty(value = "夜班护士数")
    private Integer nightNurseNumber;
    /**
     * 夜班助手数
     */
    @ApiModelProperty(value = "夜班助手数")
    private Integer nightAssistantNumber;
    /**
     * 排班月份参照数据
     */
    @ApiModelProperty(value = "排班月份参照数据")
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
    private Date schedulingMonth;
    /**
     * 日班最小护士数
     */
    @ApiModelProperty(value = "日班最小护士数")
    private Integer dayNurseNumber;
    /**
     * 日班最小助手数
     */
    @ApiModelProperty(value = "日班最小助手数")
    private Integer dayAssistantNumber;
    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期")
	@DateTimeFormat(
		pattern = "yyyy-MM-dd"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd"
	)
    private Date startDate;
    /**
     * 结束日期
     */
    @ApiModelProperty(value = "结束日期")
	@DateTimeFormat(
		pattern = "yyyy-MM-dd"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd"
	)
    private Date endDate;
    /**
     * 采用状态
     */
    @ApiModelProperty(value = "采用状态")
    private Integer state;
    /**
     * 成功排班后被采用的期望数据的id
     */
    @ApiModelProperty(value = "成功排班后被采用的期望数据的id")
    private String expectationSids;


}
