package org.springblade.nsms.dto;

import org.springblade.nsms.entity.Expectation;
import org.springblade.nsms.tools.Constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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


	public void addVacationExpectation(final ExpectationDTO expectation,List<NurseDTO> nurseDTOList){
		PersonDTO personDTO=new PersonDTO(expectation);
		//加入之前判断是否存在本人的其他期望
		if (getNightShiftIds().contains(expectation.getNurseSid())){
			replaceOrNot(nightShifts, vacationList, nurseDTOList, expectation,personDTO);
		}else if (getDayShiftIds().contains(expectation.getNurseSid())){
			replaceOrNot(dayShifts, vacationList, nurseDTOList, expectation,personDTO);
		}else if (getVacationIds().contains(expectation.getNurseSid())){
			replaceOrNot(vacationList, vacationList, nurseDTOList, expectation,personDTO);
		}
		//如果都不存在所有班次列表，那么说明此次添加是初次添加
		dealWithNumberOfShift(expectation, nurseDTOList);
		this.vacationList.add(personDTO);
	}

	public void replaceOrNot(List<PersonDTO> originShiftList,List<PersonDTO> targetShiftList,
							 List<NurseDTO> nurseDTOList,ExpectationDTO expectation,
							 PersonDTO personDTO){
		//加入之前判断是否存在本人的其他期望，默认：即使存在，也只存在一个班次的排班计划中
		//如果存在，那么比较两个期望的优先级
		Optional<PersonDTO> target=originShiftList.stream()
			.filter(x->!x.getNurseId().equals(expectation.getNurseSid()))
			.findFirst();
		if (target.isPresent()){
			PersonDTO targetPlan=target.get();
			if (targetPlan.getPriority()<expectation.getPriority()){
				//新加入的期望优先级高于已经添加的，所以要把原来的期望去掉，然后再加入新期望
				originShiftList.remove(targetPlan);
				reduceNumberOfShift(targetPlan, nurseDTOList);

				dealWithNumberOfShift(expectation, nurseDTOList);
				targetShiftList.add(personDTO);
			}
		}
	}



	/**
	 * 通过期望添加日班安排
	 * @param expectation
	 */
	public void addDayShift(final ExpectationDTO expectation,List<NurseDTO> nurseDTOList){
		PersonDTO personDTO=new PersonDTO(expectation);
		//加入之前判断是否存在本人的其他期望
		if (getNightShiftIds().contains(expectation.getNurseSid())){
			replaceOrNot(nightShifts, dayShifts, nurseDTOList, expectation,personDTO);
		}else if (getDayShiftIds().contains(expectation.getNurseSid())){
			replaceOrNot(dayShifts, dayShifts, nurseDTOList, expectation,personDTO);
		}else if (getVacationIds().contains(expectation.getNurseSid())){
			replaceOrNot(vacationList, dayShifts, nurseDTOList, expectation,personDTO);
		}
		//如果都不存在所有班次列表，那么说明此次添加是初次添加
		dealWithNumberOfShift(expectation, nurseDTOList);
		this.dayShifts.add(personDTO);
	}

	/**
	 * 添加日班安排
	 * @param person
	 */
	public void addDayShift(final PersonDTO person,List<NurseDTO> nurseDTOList){
		PersonDTO personDTO=new PersonDTO(person);
		dealWithNumberOfShift("日班",person.getNurseId(), nurseDTOList);
		this.dayShifts.add(personDTO);
	}

	/**
	 * 通过期望添加夜班安排
	 * @param expectation
	 */
	public void addNightShift(final ExpectationDTO expectation,List<NurseDTO> nurseDTOList){
		PersonDTO personDTO=new PersonDTO(expectation);
		//加入之前判断是否存在本人的其他期望
		if (getNightShiftIds().contains(expectation.getNurseSid())){
			replaceOrNot(nightShifts, nightShifts, nurseDTOList, expectation,personDTO);
		}else if (getDayShiftIds().contains(expectation.getNurseSid())){
			replaceOrNot(dayShifts, nightShifts, nurseDTOList, expectation,personDTO);
		}else if (getVacationIds().contains(expectation.getNurseSid())){
			replaceOrNot(vacationList, nightShifts, nurseDTOList, expectation,personDTO);
		}
		//如果都不存在所有班次列表，那么说明此次添加是初次添加
		dealWithNumberOfShift(expectation, nurseDTOList);
		this.nightShifts.add(personDTO);
	}

	/**
	 * 添加夜班安排
	 * @param person
	 */
	public void addNightShift(final PersonDTO person,List<NurseDTO> nurseDTOList){
		PersonDTO personDTO=new PersonDTO(person);
		dealWithNumberOfShift("夜班",person.getNurseId(), nurseDTOList);
		this.nightShifts.add(personDTO);
	}

	/**
	 * 期望注入，直接添加入即可
	 * @param expectation 期望
	 */
	public void addShift(final ExpectationDTO expectation,List<NurseDTO> nurseDTOList){

		if (expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_SHIFT)
		|| expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_NUMBER)) {
			addDayShift(expectation,nurseDTOList);
		}
		else if (expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_SHIFT)
		||expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_NUMBER))
		{
			addNightShift(expectation,nurseDTOList);
		}

	}


	/**
	 * 定向清除职位为 postType 的夜班安排
	 */
	public void nightShiftFilterByPostType(final List<PersonDTO> targetLists,List<NurseDTO> nurseDTOList){
		List<PersonDTO> personDTOList=new ArrayList<>();
		List<Long> targetIds=targetLists.stream().map(PersonDTO::getNurseId).collect(Collectors.toList());
		nightShifts.forEach(x->{
			if (targetIds.contains(x.getNurseId())){
				personDTOList.add(x);
				//同时减去排班夜班天数
				reduceNumberOfShift("夜班",x.getNurseId(), nurseDTOList);
			}
		});
		nightShifts.removeAll(personDTOList);
	}

	/**
	 * 向夜班安排中添加人员安排列表
	 * @param personDTOList
	 */
	public void nightShiftsAdd(List<PersonDTO> personDTOList,List<NurseDTO> nurseDTOList){
		nightShifts.addAll(personDTOList);
		personDTOList.forEach(x->{
			dealWithNumberOfShift("夜班",x.getNurseId(), nurseDTOList);
		});
	}

	/**
	 * 用来初始化排班前日禁忌表的方法
	 * @param personDTOList
	 */
	public void nightShiftsAdd(List<PersonDTO> personDTOList){
		nightShifts.addAll(personDTOList);
	}

	/**
	 * 用来初始化排班前日禁忌表的方法
	 * @param personDTOList
	 */
	public void dayShiftsAdd(List<PersonDTO> personDTOList){
		dayShifts.addAll(personDTOList);
	}

	/**
	 * 向日班安排中添加人员安排列表
	 * @param personDTOList
	 */
	public void dayShiftsAdd(List<PersonDTO> personDTOList,List<NurseDTO> nurseDTOList){
		dayShifts.addAll(personDTOList);
		personDTOList.forEach(x->{
			dealWithNumberOfShift("日班",x.getNurseId(), nurseDTOList);
		});
	}


	public void reduceNumberOfShift(String shiftType,Long nurseSid,List<NurseDTO> nurseDTOList){
		nurseDTOList.forEach(x->{
			if (x.getNurseId().equals(nurseSid)){
				if ("日班".equals(shiftType)){
					x.dayShiftNumberReduce();
				}else if ("夜班".equals(shiftType)){
					x.nightShiftNumberReduce();
				}
			}
		});
	}
	public void dealWithNumberOfShift(String shiftType,Long nurseSid,List<NurseDTO> nurseDTOList){
		nurseDTOList.forEach(x->{
			if (x.getNurseId().equals(nurseSid)){
				if ("日班".equals(shiftType)){
					x.dayShiftNumberAdd();
				}else if ("夜班".equals(shiftType)){
					x.nightShiftNumberAdd();
				}
			}
		});
	}

	/**
	 * 排班计划班次计数器
	 * @param expectation
	 * @param nurseDTOList
	 */
	public void dealWithNumberOfShift(ExpectationDTO expectation,List<NurseDTO> nurseDTOList){
		nurseDTOList.forEach(x->{
			if (x.getNurseId().equals(expectation.getNurseSid())){
				if (expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_SHIFT)
					|| expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_NUMBER)){
					x.nightShiftNumberAdd();
				}else if (expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_SHIFT)
					|| expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_NUMBER)){
					x.dayShiftNumberAdd();
				}else if (expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_VACATION)){
					x.vacationDayNumberAdd();
				}
			}
		});
	}

	public void reduceNumberOfShift(PersonDTO personDTO,List<NurseDTO> nurseDTOList){
		nurseDTOList.forEach(x->{
			if (x.getNurseId().equals(personDTO.getNurseId())){
				if (personDTO.getType().equals(Constant.EXPECTATION_TYPE_NIGHT_SHIFT)
					|| personDTO.getType().equals(Constant.EXPECTATION_TYPE_NIGHT_NUMBER)){
					x.nightShiftNumberReduce();
				}else if (personDTO.getType().equals(Constant.EXPECTATION_TYPE_DAY_SHIFT)
					|| personDTO.getType().equals(Constant.EXPECTATION_TYPE_DAY_NUMBER)){
					x.dayShiftNumberReduce();
				}else if (personDTO.getType().equals(Constant.EXPECTATION_TYPE_VACATION)){
					x.vacationDayNumberReduce();
				}
			}
		});
	}

		public List<Long> getDayShiftIds(){
		return this.dayShifts.stream().map(PersonDTO::getNurseId).collect(Collectors.toList());
	}
	public List<Long> getNightShiftIds(){
		return this.nightShifts.stream().map(PersonDTO::getNurseId).collect(Collectors.toList());
	}
	public List<Long> getVacationIds(){
		return this.vacationList.stream().map(PersonDTO::getNurseId).collect(Collectors.toList());
	}
}
