package org.springblade.nsms.dto;

import org.springblade.nsms.tools.Constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用来存储一天内的两班制排班结果的对象
 * @author Eugene-Forest
 * @date 2022/4/24
 **/
public class ShiftPlanDTO implements Serializable {

	/**
	 * 用来标记日期
	 */
//	Date today;
	/**
	 * 日班安排
	 */
	private List<PersonDTO> dayShifts;

	/**
	 * 夜班安排
	 */
	private List<PersonDTO> nightShifts;


	private List<PersonDTO> vacationList;


	public ShiftPlanDTO() {
		dayShifts=new ArrayList<>();
		nightShifts=new ArrayList<>();
		vacationList=new ArrayList<>();
	}

	public List<PersonDTO> getDayShifts() {
		return dayShifts;
	}

	public List<PersonDTO> getNightShifts() {
		return nightShifts;
	}

	public List<PersonDTO> getVacationList() {
		return vacationList;
	}


	public void addVacationExpectation(final ExpectationDTO expectation){
		PersonDTO personDTO=new PersonDTO(expectation);
		this.vacationList.add(personDTO);
	}

	/**
	 * 获取假期护士id列表
	 * @return
	 */
	public List<Long> getVacationNurses(){
		if (vacationList.isEmpty()){
			return null;
		}else {
			return vacationList.stream().filter(x->
				!x.getPostType().equals(Constant.POST_TYPE_NURSE)
			).map(PersonDTO::getNurseId).collect(Collectors.toList());
		}
	}

	/**
	 * 获取假期护士安排表
	 * @return
	 */
	public List<PersonDTO> getVacationNurseDTOs(){
		if (vacationList.isEmpty()){
			return null;
		}else {
			return vacationList.stream().filter(x->
				!x.getPostType().equals(Constant.POST_TYPE_NURSE)
			).collect(Collectors.toList());
		}
	}

	/**
	 * 获取假期助手id列表
	 * @return
	 */
	public List<Long> getVacationAssistants(){
		if (vacationList.isEmpty()){
			return null;
		}else {
			return vacationList.stream().filter(x->
				!x.getPostType().equals(Constant.POST_TYPE_ASSISTANT)
			).map(PersonDTO::getNurseId).collect(Collectors.toList());
		}
	}

	/**
	 * 获取假期助手安排表
	 * @return
	 */
	public List<PersonDTO> getVacationAssistantDTOs(){
		if (vacationList.isEmpty()){
			return null;
		}else {
			return vacationList.stream().filter(x->
				!x.getPostType().equals(Constant.POST_TYPE_ASSISTANT)
			).collect(Collectors.toList());
		}
	}

	/**
	 * 获取日班护士id列表
	 * @return
	 */
	public List<Long> getDayShiftNurses(){
		if (this.dayShifts.isEmpty()){
			return null;
		}else {
			return this.dayShifts.stream()
				.filter(x->!x.getPostType().equals(Constant.POST_TYPE_NURSE))
				.map(PersonDTO::getNurseId)
				.collect(Collectors.toList());
		}
	}

	/**
	 * 获取日班护士安排表
	 * @return
	 */
	public List<PersonDTO> getDayShiftNurseDTOs(){
		if (this.dayShifts.isEmpty()){
			return null;
		}else {
			return this.dayShifts.stream()
				.filter(x->!x.getPostType().equals(Constant.POST_TYPE_NURSE))
				.collect(Collectors.toList());
		}
	}

	/**
	 * 获取夜班护士人数
	 * @return
	 */
	public int getNightShiftNursesNumber(){
		List<Long> nightShiftNurses=getNightShiftNurses();
		if (nightShiftNurses==null){
			return 0;
		}else {
			return nightShiftNurses.size();
		}
	}

	/**
	 * 获取夜班护士id列表
	 * @return
	 */
	public List<Long> getNightShiftNurses(){
		if (this.nightShifts.isEmpty()){
			return null;
		}else {
			return this.nightShifts.stream()
				.filter(x->!x.getPostType().equals(Constant.POST_TYPE_NURSE))
				.map(PersonDTO::getNurseId)
				.collect(Collectors.toList());
		}
	}

	/**
	 * 获取夜班护士安排表
	 * @return
	 */
	public List<PersonDTO> getNightShiftNurseDTOs(){
		if (this.nightShifts.isEmpty()){
			return null;
		}else {
			return this.nightShifts.stream()
				.filter(x->!x.getPostType().equals(Constant.POST_TYPE_NURSE))
				.collect(Collectors.toList());
		}
	}


	/**
	 * 获取日班助手id列表
	 * @return
	 */
	public List<Long> getDayShiftAssistants(){
		if (this.dayShifts.isEmpty()){
			return null;
		}else {
			return this.dayShifts.stream()
				.filter(x->!x.getPostType().equals(Constant.POST_TYPE_ASSISTANT))
				.map(PersonDTO::getNurseId)
				.collect(Collectors.toList());
		}
	}

	/**
	 * 获取日班助手安排表
	 * @return
	 */
	public List<PersonDTO> getDayShiftAssistantDTOs(){
		if (this.dayShifts.isEmpty()){
			return null;
		}else {
			return this.dayShifts.stream()
				.filter(x->!x.getPostType().equals(Constant.POST_TYPE_ASSISTANT))
				.collect(Collectors.toList());
		}
	}

	/**
	 * 获取夜班助手数
	 * @return
	 */
	public int getNightShiftAssistantsNumber(){
		List<Long> nightShiftAssistantIds=getNightShiftAssistants();
		if (nightShiftAssistantIds==null){
			return 0;
		}else {
			return  nightShiftAssistantIds.size();
		}
	}

	/**
	 * 获取夜班助手id列表
	 * @return
	 */
	public List<Long> getNightShiftAssistants(){
		if (this.nightShifts.isEmpty()){
			return null;
		}else {
			return this.nightShifts.stream()
				.filter(x->!x.getPostType().equals(Constant.POST_TYPE_ASSISTANT))
				.map(PersonDTO::getNurseId)
				.collect(Collectors.toList());
		}
	}

	/**
	 * 获取夜班助手安排表
	 * @return
	 */
	public List<PersonDTO> getNightShiftAssistantDTOs(){
		if (this.nightShifts.isEmpty()){
			return null;
		}else {
			return this.nightShifts.stream()
				.filter(x->!x.getPostType().equals(Constant.POST_TYPE_ASSISTANT))
				.collect(Collectors.toList());
		}
	}

	/**
	 * 通过期望添加日班安排
	 * @param expectation
	 */
	public void addDayShift(final ExpectationDTO expectation){
		PersonDTO personDTO=new PersonDTO(expectation);
		this.dayShifts.add(personDTO);
	}

	/**
	 * 添加日班安排
	 * @param person
	 */
	public void addDayShift(final PersonDTO person){
		PersonDTO personDTO=new PersonDTO(person);
		this.dayShifts.add(personDTO);
	}

	/**
	 * 通过期望添加夜班安排
	 * @param expectation
	 */
	public void addNightShift(final ExpectationDTO expectation){
		PersonDTO personDTO=new PersonDTO(expectation);
		this.nightShifts.add(personDTO);
	}

	/**
	 * 添加夜班安排
	 * @param person
	 */
	public void addNightShift(final PersonDTO person){
		PersonDTO personDTO=new PersonDTO(person);
		this.nightShifts.add(personDTO);
	}

	/**
	 * 期望注入，直接添加入即可
	 * @param expectation 期望
	 */
	public void addShift(final ExpectationDTO expectation){
		if (expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_SHIFT)
		|| expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_NUMBER)) {
			addDayShift(expectation);
		}
		else if (expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_SHIFT)
		||expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_NUMBER))
		{
			addNightShift(expectation);
		}

	}


	/**
	 * 定向清除职位为 postType 的夜班安排
	 */
	public void nightShiftFilterByPostType(Integer postType){
		nightShifts=nightShifts.stream().
			filter(x->!x.getPostType().equals(Constant.POST_TYPE_NURSE))
			.collect(Collectors.toList());
	}

	/**
	 * 向夜班安排中添加人员安排列表
	 * @param personDTOList
	 */
	public void nightShiftsAdd(List<PersonDTO> personDTOList){
		nightShifts.addAll(personDTOList);
	}
}