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

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.rewrite.FoundationEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 护士助手的排班期望表实体类
 *
 * @author Blade
 * @since 2022-04-18
 */
@Data
@TableName("com_expectation")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Expectation对象", description = "护士助手的排班期望表")
public class Expectation extends FoundationEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 优先级
     */
    @ApiModelProperty(value = "优先级")
    private Integer priority;
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
     * 申请人id
     */
    @ApiModelProperty(value = "申请人id")
	@JsonSerialize(
		using = ToStringSerializer.class
	)
    private Long nurseSid;
    /**
     * 期望类型
     */
    @ApiModelProperty(value = "期望类型")
    private Integer expectationType;
    /**
     * 夜班/夜班天数
     */
    @ApiModelProperty(value = "夜班/日班天数")
    private Integer dayNumber;
    /**
     * 实现状态；实现或未实现
     */
    @ApiModelProperty(value = "实现状态；实现或未实现")
    private Integer actualState;
    /**
     * 对应的排班依据表id
     */
    @ApiModelProperty(value = "对应的排班依据表id")
	@JsonSerialize(
		using = ToStringSerializer.class
	)
    private Long referenceSid;


}
