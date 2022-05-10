package org.springblade.nsms.dto;

/**
 * @author Eugene-Forest
 * @date 2022/4/25
 **/
public class NurseDTO implements Comparable<NurseDTO> {

	/**
	 * id
	 */
	private Long nurseId;

	/**
	 * 职位类型
	 */
	private Integer postType;

	/**
	 * 排班的日班天数
	 */
	private Integer dayShiftNumber=0;

	/**
	 * 排班的夜班天数
	 */
	private Integer nightShiftNumber=0;

	private Integer allShiftNumber=0;

	/**
	 * 排班的假期天数
	 */
	private Integer vacationDayNumber=0;

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

	public int getDayShiftNumber() {
		return dayShiftNumber;
	}

	public void setDayShiftNumber(int dayShiftNumber) {
		this.dayShiftNumber = dayShiftNumber;
	}

	public int getNightShiftNumber() {
		return nightShiftNumber;
	}

	public void setNightShiftNumber(int nightShiftNumber) {
		this.nightShiftNumber = nightShiftNumber;
	}

	public int getVacationDayNumber() {
		return vacationDayNumber;
	}

	public void setVacationDayNumber(int vacationDayNumber) {
		this.vacationDayNumber = vacationDayNumber;
	}

	public int getAllShiftNumber(){
		return this.allShiftNumber;
	}

	/**
	 * 自减1
	 */
	public void dayShiftNumberReduce(){
		this.allShiftNumber=this.allShiftNumber-1;
		this.dayShiftNumber=this.dayShiftNumber-1;
	}

	/**
	 * 自加1
	 */
	public void dayShiftNumberAdd(){
		this.allShiftNumber=this.allShiftNumber+1;
		this.dayShiftNumber=this.dayShiftNumber+1;
	}

	/**
	 * 自减1
	 */
	public void nightShiftNumberReduce(){
		this.allShiftNumber=this.allShiftNumber-1;
		this.nightShiftNumber=this.nightShiftNumber-1;
	}
	/**
	 * 自加1
	 */
	public void nightShiftNumberAdd(){
		this.allShiftNumber=this.allShiftNumber+1;
		this.nightShiftNumber=this.nightShiftNumber+1;
	}

	/**
	 * 自减1
	 */
	public void vacationDayNumberReduce(){
		this.vacationDayNumber=this.vacationDayNumber-1;
	}
	/**
	 * 自加1
	 */
	public void vacationDayNumberAdd(){
		this.vacationDayNumber=this.vacationDayNumber+1;
	}


	/**
	 * 优先级比较
	 * @param o the object to be compared.
	 * @return
	 */
	@Override
	public int compareTo(NurseDTO o) {
		if (this.nightShiftNumber.compareTo(o.getNightShiftNumber())>0){
			return 1;
		}else if (this.nightShiftNumber.compareTo(o.getNightShiftNumber())<0){
			return -1;
		}else {
			if (this.dayShiftNumber.compareTo(o.getDayShiftNumber())>0){
				return 1;
			}else if (this.dayShiftNumber.compareTo(o.getDayShiftNumber())<0){
				return -1;
			}else {
				return 0;
			}
		}
//		if (this.allShiftNumber.compareTo(o.getAllShiftNumber())>0){
//			return 1;
//		}else if (this.allShiftNumber.compareTo(o.getAllShiftNumber())<0){
//			return -1;
//		}else {
//
//		}
	}
}
