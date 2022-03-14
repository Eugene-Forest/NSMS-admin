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

/**
 * 请假记录表实体类
 *
 * @author Blade
 * @since 2022-03-14
 */
@Data
@TableName("com_leave_record")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "LeaveRecord对象", description = "请假记录表")
public class LeaveRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 请假原因
     */
    @ApiModelProperty(value = "请假原因")
    private String leaveResult;
    /**
     * 请假日期
     */
    @ApiModelProperty(value = "请假日期")
    private LocalDate leaveDate;
    /**
     * 请假班次
     */
    @ApiModelProperty(value = "请假班次")
    private Integer leaveShift;
    /**
     * 请假类别
     */
    @ApiModelProperty(value = "请假类别")
    private Integer leaveType;
    /**
     * 申请人id
     */
    @ApiModelProperty(value = "申请人id")
    private Long nurseSid;
    /**
     * 审批状态
     */
    @ApiModelProperty(value = "审批状态")
    private Integer approvalStatus;
    /**
     * 审批意见
     */
    @ApiModelProperty(value = "审批意见")
    private String approvalOpinion;
    /**
     * 审批人
     */
    @ApiModelProperty(value = "审批人")
    private Long approver;


}
