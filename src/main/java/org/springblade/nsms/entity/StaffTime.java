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
import org.springblade.core.mp.base.BaseEntity;
import java.time.LocalDate;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.rewrite.FoundationEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 人员安排表，排班表实体类
 *
 * @author Blade
 * @since 2022-04-18
 */
@Data
@TableName("com_staff_time")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "StaffTime对象", description = "人员安排表，排班表")
public class StaffTime extends FoundationEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;
    /**
     * 班次类别
     */
    @ApiModelProperty(value = "班次类别")
    private Integer category;
    /**
     * 工作时间的年月
     */
    @ApiModelProperty(value = "工作时间的年月")
    @TableField("yearMonth")
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	private Date yearmonth;
	/**
     * 护士id
     */
	@ApiModelProperty(value = "护士id")
	@JsonSerialize(
		using = ToStringSerializer.class
	)
    private Long nurseSid;
    /**
     * 排班依据id
     */
    @ApiModelProperty(value = "排班依据id")
	@JsonSerialize(
		using = ToStringSerializer.class
	)
    private Long referenceSid;
    /**
     * 工作状态；如请假
     */
    @ApiModelProperty(value = "工作状态；如请假")
    private Integer workStatus;


}
