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
package org.springblade.nsms.base.entity;

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

import java.util.Date;

/**
 * 护士档案 实体类
 *
 * @author Blade
 * @since 2022-03-14
 */
@Data
@TableName("com_nurse_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "NurseInfo对象", description = "护士档案 ")
public class NurseInfo extends FoundationEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String name;
    /**
     * 年龄
     */
    @ApiModelProperty(value = "年龄")
    private Integer age;
    /**
     * 男女性别
     */
    @ApiModelProperty(value = "男女性别")
    private Integer gender;
    /**
     * 出生年月日
     */
	@DateTimeFormat(
		pattern = "yyyy-MM-dd"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd"
	)
    @ApiModelProperty(value = "出生年月日")
    private Date birthday;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String telephone;
    /**
     * 职工号
     */
    @ApiModelProperty(value = "职工号")
    private String wNo;
    /**
     * 职位
     */
    @ApiModelProperty(value = "职位")
    private Long position;
    /**
     * 部门
     */
    @ApiModelProperty(value = "部门")
	@JsonSerialize(using = ToStringSerializer.class)
    private Long department;
    /**
     * 就职状态
     */
    @ApiModelProperty(value = "就职状态")
    private Long workingCondition;
	/**
	 * 对应用户id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "对应用户id")
	private Long userId;

}
