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
import org.springblade.core.mp.base.BaseEntity;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
public class NurseInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
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
    private String gender;
    /**
     * 出生年月日
     */
    @ApiModelProperty(value = "出生年月日")
    private LocalDate birthday;
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
    private Integer position;
    /**
     * 部门
     */
    @ApiModelProperty(value = "部门")
    private Integer department;
    /**
     * 就职状态
     */
    @ApiModelProperty(value = "就职状态")
    private Integer workingCondition;


}
