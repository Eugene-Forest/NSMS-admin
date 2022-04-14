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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.rewrite.FoundationEntity;

/**
 * 打卡记录表实体类
 *
 * @author Blade
 * @since 2022-04-11
 */
@Data
@TableName("com_clock_in")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ClockIn对象", description = "打卡记录表")
public class ClockIn extends FoundationEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 打卡类型
     */
    @ApiModelProperty(value = "打卡类型")
    private Integer type;
    /**
     * ip地址
     */
    @ApiModelProperty(value = "ip地址")
    private String ipAddress;
    /**
     * 打卡状态
     */
    @ApiModelProperty(value = "打卡状态")
    private Integer state;
    /**
     * gps 地址
     */
    @ApiModelProperty(value = "gps 地址")
    private String gpsAddress;
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;
    /**
     * 打卡字符串信息
     */
    @ApiModelProperty(value = "打卡字符串信息")
    private String message;


}
