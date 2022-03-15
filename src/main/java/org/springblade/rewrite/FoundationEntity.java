package org.springblade.rewrite;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author Eugene-Forest
 * @date 2022/3/15
 **/
public class FoundationEntity implements Serializable {
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty("主键id")
	@TableId(
		value = "id",
		type = IdType.ASSIGN_ID
	)
	private Long id;
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty("创建人")
	private Long createUser;
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty("创建时间")
	private Date createTime;
	@ApiModelProperty("创建部门")
	private Long createDept;
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty("更新人")
	private Long updateUser;
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty("更新时间")
	private Date updateTime;
	@ApiModelProperty("业务状态")
	private Integer status;
	@TableLogic
	@ApiModelProperty("是否已删除")
	private Integer isDeleted;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Long getCreateDept() {
		return createDept;
	}

	public void setCreateDept(Long createDept) {
		this.createDept = createDept;
	}

	protected boolean canEqual(final Object other) {
		return other instanceof FoundationEntity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FoundationEntity that = (FoundationEntity) o;
		return id.equals(that.id) && Objects.equals(createUser, that.createUser) && Objects.equals(createTime, that.createTime) && Objects.equals(createDept, that.createDept) && Objects.equals(updateUser, that.updateUser) && Objects.equals(updateTime, that.updateTime) && Objects.equals(status, that.status) && Objects.equals(isDeleted, that.isDeleted);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, createUser, createTime, createDept, updateUser, updateTime, status, isDeleted);
	}

	@Override
	public String toString() {
		return "FoundationEntity{" +
			"id=" + id +
			", createUser=" + createUser +
			", createTime=" + createTime +
			", createDept=" + createDept +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			", status=" + status +
			", isDeleted=" + isDeleted +
			'}';
	}
}
