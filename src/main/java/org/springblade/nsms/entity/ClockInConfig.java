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
import org.springblade.core.mp.base.BaseEntity;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 打卡配置表实体类
 *
 * @author Blade
 * @since 2022-04-11
 */
@Data
@TableName("com_clock_in_config")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ClockInConfig对象", description = "打卡配置表")
public class ClockInConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 科室id
     */
    @ApiModelProperty(value = "科室id")
    private Long deptId;
    /**
     * 打卡用的路由物理地址
     */
    @ApiModelProperty(value = "打卡用的路由物理地址")
    private String macAddress;
    /**
     * 打卡用的gps地址
     */
    @ApiModelProperty(value = "打卡用的gps地址")
    private String gpsAddress;
    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用")
    private Integer state;
    /**
     * 打卡字符串
     */
    @ApiModelProperty(value = "打卡字符串")
    private String message;
    /**
     * 打卡日期/数据有效期
     */
    @ApiModelProperty(value = "打卡日期/数据有效期")
    private LocalDateTime clockInDate;


}
