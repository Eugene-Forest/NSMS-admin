package org.springblade.nsms.dto;

/**
 * @author Eugene-Forest
 * @date 2022/4/24
 **/
public class PersonDTO {

	/**
	 * 护理人员id
	 */
	Long nurseId;


	/**
	 * 护理人员类型
	 */
	Integer postType;

	/**
	 * 期望id
	 */
	Long expectationId;

	/**
	 * 期望优先级
	 */
	Integer priority;

	/**
	 * 期望类型
	 */
	Integer type;

	public PersonDTO(final PersonDTO personDTO) {
		this.nurseId = personDTO.getNurseId();
		this.postType = personDTO.getType();
	}

	public PersonDTO(final Long nurseId,final Integer postType) {
		this.nurseId = nurseId;
		this.postType = postType;
	}

	public PersonDTO(final ExpectationDTO expectationDTO) {
		this.nurseId = expectationDTO.getNurseSid();
		this.postType = expectationDTO.getPostType();
		this.priority=expectationDTO.getPriority();
		this.type=expectationDTO.getExpectationType();
		this.expectationId=expectationDTO.getId();
	}

	public PersonDTO(
		final Long nurseId,
		final Integer postType,
		final Long expectationId,
		final Integer priority,
		final Integer type) {
		this.nurseId = nurseId;
		this.postType = postType;
		this.expectationId = expectationId;
		this.priority = priority;
		this.type = type;
	}

	public Long getNurseId() {
		return nurseId;
	}

	public void setNurseId(final Long nurseId) {
		this.nurseId = nurseId;
	}

	public Long getExpectationId() {
		return expectationId;
	}

	public void setExpectationId(final Long expectationId) {
		this.expectationId = expectationId;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(final Integer priority) {
		this.priority = priority;
	}

	public Integer getType() {
		return type;
	}

	public void setType(final Integer type) {
		this.type = type;
	}

	public Integer getPostType() {
		return postType;
	}

	public void setPostType(Integer postType) {
		this.postType = postType;
	}
}
