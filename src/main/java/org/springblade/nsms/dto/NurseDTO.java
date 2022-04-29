package org.springblade.nsms.dto;

/**
 * @author Eugene-Forest
 * @date 2022/4/25
 **/
public class NurseDTO {

	private Long nurseId;

	private Integer postType;

	public NurseDTO() {
	}

	public NurseDTO(Long nurseId, Integer postType) {
		this.nurseId = nurseId;
		this.postType = postType;
	}

	public Long getNurseId() {
		return nurseId;
	}

	public void setNurseId(Long nurseId) {
		this.nurseId = nurseId;
	}

	public Integer getPostType() {
		return postType;
	}

	public void setPostType(Integer postType) {
		this.postType = postType;
	}
}
