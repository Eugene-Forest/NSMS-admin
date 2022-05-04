package org.springblade.nsms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.apache.xmlbeans.impl.jam.xml.TunnelledException;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.nsms.entity.SchedulingReference;
import org.springblade.nsms.tools.Constant;
import org.springblade.nsms.tools.SchedulingUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

/**
 * @author Eugene-Forest
 * @date 2022/4/25
 **/
public class ScheduleTable {


	private Map<Date,ShiftPlanDTO> shiftPlanDTOList;

	/**
	 *
	 */
	private List<NurseDTO> allPersonBaseInfo;

	/**
	 * 排班期间的假期天数
	 */
	@ApiModelProperty(value = "排班期间的假期天数")
	private Integer vacationTimes;
	/**
	 * 夜班护士数
	 */
	@ApiModelProperty(value = "夜班护士数")
	private Integer nightNurseNumber;
	/**
	 * 夜班助手数
	 */
	@ApiModelProperty(value = "夜班助手数")
	private Integer nightAssistantNumber;

	@ApiModelProperty(value = "日班最小护士数")
	private Integer dayNurseNumber;
	/**
	 * 日班最小助手数
	 */
	@ApiModelProperty(value = "日班最小助手数")
	private Integer dayAssistantNumber;


	/**
	 * 开始日期
	 */
	@ApiModelProperty(value = "开始日期")
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	private Date startDate;
	/**
	 * 结束日期
	 */
	@ApiModelProperty(value = "结束日期")
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	private Date endDate;


	/**
	 * 排班是否成功
	 */
	private boolean status=true;


	private Set<ExpectationDTO> failureExpectation;

	public ScheduleTable(
		final Map<Date,ShiftPlanDTO> shiftPlanDTOList,
		final Integer vacationTimes,
		final Integer nightNurseNumber,
		final Integer nightAssistantNumber,
		final Integer dayNurseNumber,
		final Integer dayAssistantNumber) {
		this.shiftPlanDTOList = shiftPlanDTOList;
		this.vacationTimes = vacationTimes;
		this.nightNurseNumber = nightNurseNumber;
		this.nightAssistantNumber = nightAssistantNumber;
		this.dayNurseNumber = dayNurseNumber;
		this.dayAssistantNumber = dayAssistantNumber;

		this.failureExpectation=new HashSet<>();
	}

	public ScheduleTable() {
		this.shiftPlanDTOList = new HashMap<>();
		this.failureExpectation=new HashSet<>();
	}

	/**
	 * 通过排班配置表初始化排班结果对象
	 * @param schedulingReference
	 */
	public ScheduleTable(final SchedulingReference schedulingReference) {
		this.shiftPlanDTOList = new HashMap<>();
		this.failureExpectation=new HashSet<>();

		this.vacationTimes=schedulingReference.getVacationTimes();
		this.dayAssistantNumber=schedulingReference.getDayAssistantNumber();
		this.dayNurseNumber=schedulingReference.getDayNurseNumber();
		this.nightAssistantNumber=schedulingReference.getNightAssistantNumber();
		this.nightNurseNumber=schedulingReference.getNightNurseNumber();
		this.startDate=schedulingReference.getStartDate();
		this.endDate=schedulingReference.getEndDate();
	}

	public SchedulingReference getSchedulingReference(){
		SchedulingReference schedulingReference=new SchedulingReference();
		schedulingReference.setVacationTimes(this.vacationTimes);
		schedulingReference.setDayAssistantNumber(this.dayAssistantNumber);
		schedulingReference.setDayNurseNumber(this.dayNurseNumber);
		schedulingReference.setNightAssistantNumber(this.nightAssistantNumber);
		schedulingReference.setNightNurseNumber(this.nightNurseNumber);
		schedulingReference.setStartDate(this.startDate);
		schedulingReference.setEndDate(this.endDate);
		return schedulingReference;
	}

	public void setBySchedulingReference(final SchedulingReference schedulingReference){
		this.vacationTimes=schedulingReference.getVacationTimes();
		this.dayAssistantNumber=schedulingReference.getDayAssistantNumber();
		this.dayNurseNumber=schedulingReference.getDayNurseNumber();
		this.nightAssistantNumber=schedulingReference.getNightAssistantNumber();
		this.nightNurseNumber=schedulingReference.getNightNurseNumber();
		this.startDate=schedulingReference.getStartDate();
		this.endDate=schedulingReference.getEndDate();
	}

	public Map<Date,ShiftPlanDTO> getShiftPlanDTOList() {
		return shiftPlanDTOList;
	}

	public Integer getVacationTimes() {
		return vacationTimes;
	}

	public void setVacationTimes(final Integer vacationTimes) {
		this.vacationTimes = vacationTimes;
	}

	public Integer getNightNurseNumber() {
		return nightNurseNumber;
	}

	public void setNightNurseNumber(final Integer nightNurseNumber) {
		this.nightNurseNumber = nightNurseNumber;
	}

	public Integer getNightAssistantNumber() {
		return nightAssistantNumber;
	}

	public void setNightAssistantNumber(final Integer nightAssistantNumber) {
		this.nightAssistantNumber = nightAssistantNumber;
	}

	public Integer getDayNurseNumber() {
		return dayNurseNumber;
	}

	public void setDayNurseNumber(final Integer dayNurseNumber) {
		this.dayNurseNumber = dayNurseNumber;
	}

	public Integer getDayAssistantNumber() {
		return dayAssistantNumber;
	}

	public void setDayAssistantNumber(final Integer dayAssistantNumber) {
		this.dayAssistantNumber = dayAssistantNumber;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getCloneStartDate() {
		return DateUtil.parse(DateUtil.formatDate(startDate), "yyyy-MM-dd");
	}

	public Date getCloneEndDate() {
		return DateUtil.parse(DateUtil.formatDate(endDate), "yyyy-MM-dd");
	}

	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Set<ExpectationDTO> getFailureExpectation() {
		return failureExpectation;
	}

	public void setFailureExpectation(final Set<ExpectationDTO> failureExpectation) {
		this.failureExpectation = failureExpectation;
	}

	/**
	 * 用来确认夜班人数是否满足人数要求
	 * @param expectationDTO
	 * @param targetDate
	 * @return
	 */
	public boolean checkPersonNumber(final ExpectationDTO expectationDTO,final Date targetDate){
		//确认班次
		if (expectationDTO.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_NUMBER)
			|| expectationDTO.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_SHIFT)){

			//确认职位
			if (expectationDTO.getPostType().equals(Constant.POST_TYPE_NURSE)){
				if (this.nightNurseNumber<
					SchedulingUtil.getPersonDTOsFromShiftPlanDTO(
						this.shiftPlanDTOList.get(targetDate),
						Constant.POST_TYPE_NURSE,"夜班").size()){
					return true;
				}else {
					//人数满足，不需要添加
					return false;
				}
			}else if (expectationDTO.getPostType().equals(Constant.POST_TYPE_ASSISTANT))
			{
				if (this.nightAssistantNumber<
					SchedulingUtil.getPersonDTOsFromShiftPlanDTO(
						this.shiftPlanDTOList.get(targetDate),
						Constant.POST_TYPE_ASSISTANT,"夜班").size()){
					return true;
				}else {
					//人数满足，不需要添加
					return false;
				}
			}

		}
		return false;
	}


	public Integer getNightShiftPersonNumberOfPostType(Integer postType){
		if (postType.equals(Constant.POST_TYPE_NURSE)){
			return nightNurseNumber;
		} else if (postType.equals(Constant.POST_TYPE_ASSISTANT)) {
			return nightAssistantNumber;
		}else {
			throw new RuntimeException("人员类型错误，无法获取夜班人数");
		}
	}
}
