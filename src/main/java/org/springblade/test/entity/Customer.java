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
package org.springblade.test.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import org.springblade.core.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 客户档案 实体类
 *
 * @author Blade
 * @since 2021-11-18
 */
@Data
@TableName("c_b_customer")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Customer对象", description = "客户档案 ")
public class Customer extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 客户编号
     */
    @ApiModelProperty(value = "客户编号")
    private String customerId;
    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    /**
     * 客户性质
     */
    @ApiModelProperty(value = "客户性质")
    private Long customerProperty;
    /**
     * 公司电话
     */
    @ApiModelProperty(value = "公司电话")
    private String customerPhone;
    /**
     * 公司地址
     */
    @ApiModelProperty(value = "公司地址")
    private String customerAddress;
    /**
     * 负责人名称
     */
    @ApiModelProperty(value = "负责人名称")
    private String customerPrincipal;
    /**
     * 负责人电话
     */
    @ApiModelProperty(value = "负责人电话")
    private String customerPrincipalPhone;
    /**
     * 所属行业
     */
    @ApiModelProperty(value = "所属行业")
    private Long industry;
    /**
     * 主营业务
     */
    @ApiModelProperty(value = "主营业务")
    private String customerMainBusiness;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;


}
