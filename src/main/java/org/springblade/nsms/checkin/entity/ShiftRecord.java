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
package org.springblade.nsms.checkin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import org.springblade.core.mp.base.BaseEntity;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.rewrite.FoundationEntity;

/**
 * 交班记录表实体类
 *
 * @author Blade
 * @since 2022-03-14
 */
@Data
@TableName("com_shift_record")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ShiftRecord对象", description = "交班记录表")
public class ShiftRecord extends FoundationEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 申请人
     */
    @ApiModelProperty(value = "申请人")
    private Long applicantSid;
    /**
     * 被请求人
     */
    @ApiModelProperty(value = "被请求人")
    private Long beRequestedSid;
    /**
     * 交班原因
     */
    @ApiModelProperty(value = "交班原因")
    private String changeResult;
    /**
     * 交班日期
     */
    @ApiModelProperty(value = "交班日期")
    private LocalDate changeDate;
    /**
     * 交班班次
     */
    @ApiModelProperty(value = "交班班次")
    private Integer changeShift;
    /**
     * 申请状态
     */
    @ApiModelProperty(value = "申请状态")
    private Integer applicationStatus;
    /**
     * 审批意见
     */
    @ApiModelProperty(value = "审批意见")
    private String approvalOpinion;
    /**
     * 审批人id
     */
    @ApiModelProperty(value = "审批人id")
    private Long approver;


}
