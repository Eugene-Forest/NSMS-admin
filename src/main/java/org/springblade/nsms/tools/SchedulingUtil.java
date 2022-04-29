package org.springblade.nsms.tools;

import org.springblade.core.tool.utils.DateUtil;
import org.springblade.nsms.dto.*;
import org.springblade.nsms.entity.Expectation;
import org.springblade.nsms.entity.SchedulingReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 排班算法
 *
 * @author Eugene-Forest
 * @date 2022/4/23
 **/
public class SchedulingUtil {

	/**
	 * 一些排班规则的前置说明：
	 * 1.一般来说，如果没有特殊要求，护士上完夜班后的次日休息（优先级低于期望）
	 * 2.护士在同一天没办法同时上日班和夜班（优先级高于期望）
	 * 3.排班期望的依据优先级进行排班结果填充，直到达到上限要求
	 * 4.要考虑到排班区间前一天的排班情况，并依据规则1
	 *
	 * 排班期望的说明：
	 *
	 *  *** 期望的实现之间互不影响， todo 这需要在期望添加时的业务逻辑中添加期望校验
	 *
	 * 1.对于日班天数期望
	 *   - 当期望天数小于排班天数时，进行随机分布计算，并对此期望标记为实现
	 *   - 当期望天数等于排班天数时，直接进行赋值，并对此期望标记为实现
	 *   - 当期望天数大于排班天数时，对此期望标记为异常
	 *
	 * 2.对于夜班天数期望
	 *
	 *
	 *
	 * 需要对根据计算不同规模的排班情况的科室人数的最小规模计算：
	 * 假设排班配置的
	 *
	 * 对于先进行期望注入，再进行最小要求实现的弊端：
	 * 1.
	 */


	public static boolean scheduling(final List<NurseDTO> nurses,
									 final List<ExpectationDTO> expectationDTOS,
									 ScheduleTable scheduleTable,
									 final ShiftPlanDTO lastSchedule){
		//首先，进行实际排班人数和计划排班人数规模的校验
		nurseNumberScaleCheck(nurses, scheduleTable.getSchedulingReference());
		//然后进行所有护理人员的期望注入
		nurses.forEach(x->{
			//获取某护理人员的所有期望
			List<ExpectationDTO> someoneExpectations=expectationDTOS.stream().filter(
				y->!y.getNurseSid().equals(x.getNurseId())
			).collect(Collectors.toList());
			//完成某护理人员的期望注入
			dateShiftExpectationScheduling(someoneExpectations, scheduleTable);
			dayNumberExpectationScheduling(someoneExpectations, scheduleTable);
			nightNumberExpectationScheduling(someoneExpectations, scheduleTable);
		});
		//满足最小的排班人数规模的要求下的最大人员安排实现
		if (minRuleScheduling(scheduleTable, nurses, lastSchedule)){
			return true;
		}
		return false;
	}



	/**
	 * 部门排班的人数规模校验
	 * @param nurses 部门的护士和助手id
	 * @param schedulingReference 目标排班配置表
	 * @return
	 */
	public static boolean nurseNumberScaleCheck(List<NurseDTO> nurses,
												SchedulingReference schedulingReference){
		//排班最小规模的计算规则：假设日班护士和助手的最小人数分别为：a,b；夜班的分别为c,d；
		//则满足排班前置规则【1】的情况下的最小规模护士助手人数为 a+b+2*(c+d)
		//护士最小数为：a+2*c ,助手最小数为 b+2*d
		int a=schedulingReference.getDayNurseNumber();
		int b=schedulingReference.getDayAssistantNumber();
		int c=schedulingReference.getDayNurseNumber();
		int d=schedulingReference.getNightAssistantNumber();
		int minTotal=(a+b+2*(c+d));
		if (nurses.size()<minTotal){
			throw new RuntimeException("排班配置所需的最小人数"+minTotal+"未达到");
		}
		//计算出其中的护士的人数
		Integer nurseNumber= (int) nurses.stream().filter(x -> !x.getPostType().equals(Constant.POST_TYPE_NURSE)).count();
		if (nurseNumber<(a+2*c)){
			throw new RuntimeException("排班配置所需护士的最小人数"+(a+2*c)+"未达到");
		}
		Integer assistantNumber=(int) nurses.stream().filter(x-> !x.getPostType().equals(Constant.POST_TYPE_ASSISTANT)).count();
		if (assistantNumber<(b+2*d)){
			throw new RuntimeException("排班配置所需护士的最小人数"+(b+2*d)+"未达到");
		}
		return true;
	}

	/**
	 * 某人的日期期望注入实现
	 */
	public static void dateShiftExpectationScheduling(
		final List<ExpectationDTO> expectationDTOS,
		ScheduleTable scheduleTable
	){
		//日期期望注入；只要保证符合期望标准，那么就可以直接注入

		for (ExpectationDTO expectationDTO : expectationDTOS) {
			if (expectationDTO.getExpectationType().equals(Constant.EXPECTATION_TYPE_VACATION)){
				//假期期望需要单独存储
				for (Date date=expectationDTO.getStartDate();date.compareTo(expectationDTO.getEndDate())<=0;nextDay(date)){
					scheduleTable.getShiftPlanDTOList().get(date).addVacationExpectation(expectationDTO);
				}
			}else if (
				expectationDTO.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_SHIFT)
				|| expectationDTO.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_SHIFT)
			){
				for (Date date=expectationDTO.getStartDate();date.compareTo(expectationDTO.getEndDate())<=0;nextDay(date)){
					scheduleTable.getShiftPlanDTOList().get(date).addShift(expectationDTO);
				}
			}
		}


	}


	/**
	 * 对某人的日班天数期望注入的实现
	 * @param expectations 某人在此次排班中的全部期望
	 * @param scheduleTable 排班结果表
	 */
	public static void dayNumberExpectationScheduling(
		final List<ExpectationDTO> expectations,
		ScheduleTable scheduleTable
	){
		List<ExpectationDTO> dayNumberExpectations=expectations.stream().filter(
			x->!x.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_NUMBER)
		).collect(Collectors.toList());
		//判断天数期望是否为空，若为空结束实现
		if (dayNumberExpectations.size() == 0){
			return;
		}else if (dayNumberExpectations.size()!=1){
			//异常，对于同一种类型的天数期望来说，只能存在一条
			throw new RuntimeException("日班天数期望条数异常。对于天数期望来说，只能同时存在一条");
		}
		//找出此人的所有其他日期类型的期望，（日班、夜班、假期日期期望）
		List<ExpectationDTO> dayShiftDateExpectations=expectations.stream().filter(
			x->!x.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_SHIFT)
		).collect(Collectors.toList());
		List<ExpectationDTO> allDateExpectations=expectations.stream().filter(
			x->
				!x.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_SHIFT)
			&& !x.getExpectationType().equals(Constant.EXPECTATION_TYPE_VACATION)
			&& !x.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_SHIFT)
		).collect(Collectors.toList());

		//计算日班日期期望的天数之和
		List<Date> dayDateShift=new ArrayList<>();
		Integer total=computedDateExpectations(dayShiftDateExpectations,dayDateShift );
		//获取实现了日班日期期望后的剩余天数
		Integer remainNumber=dayNumberExpectations.get(0).getDayNumber()-total;

		if (remainNumber==0)
		{
			//日班日期期望正好实现日班天数期望，结束日班天数期望注入
			return;
		}
		else if ((remainNumber<0)){
			throw new RuntimeException("日期期望和天数期望有冲突，请检查期望是否合理！");
		}
		//避开 日期期望实现剩余的天数期望
		List<Date> allDateShift=new ArrayList<>();
		computedDateExpectations(allDateExpectations, allDateShift);

		//通过获取配置的克隆日期以避免循环导致的变化
		for (Date date=scheduleTable.getCloneStartDate();date.compareTo(scheduleTable.getCloneEndDate())<=0;nextDay(date)){
			if (!allDateShift.contains(date)){
				if (remainNumber>0){
					//如果天数期望还有剩余，且还存在可安排的日期，则向其添加
					scheduleTable.getShiftPlanDTOList().get(date).addShift(dayNumberExpectations.get(0));
					remainNumber=remainNumber-1;
				}else {
					break;
				}
			}
		}
	}

	/**
	 * 对某人的夜班天数期望注入的实现
	 * @param expectations 某人在此次排班中的全部期望
	 * @param scheduleTable 排班结果表
	 */
	public static void nightNumberExpectationScheduling(
		final List<ExpectationDTO> expectations,
		ScheduleTable scheduleTable
	){
		List<ExpectationDTO> nightNumberExpectations=expectations.stream().filter(
			x->!x.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_NUMBER)
		).collect(Collectors.toList());
		//判断天数期望是否为空，若为空结束实现
		if (nightNumberExpectations.size() == 0){
			return;
		}else if (nightNumberExpectations.size()!=1){
			//异常，对于同一种类型的天数期望来说，只能存在一条
			throw new RuntimeException("夜班天数期望条数异常。对于天数期望来说，只能同时存在一条");
		}
		//找出此人的所有其他日期类型的期望，（日班、夜班、假期日期期望）
		List<ExpectationDTO> nightShiftDateExpectations=expectations.stream().filter(
			x->!x.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_SHIFT)
		).collect(Collectors.toList());
		List<ExpectationDTO> allDateExpectations=expectations.stream().filter(
			x->
				!x.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_SHIFT)
					&& !x.getExpectationType().equals(Constant.EXPECTATION_TYPE_VACATION)
					&& !x.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_SHIFT)
		).collect(Collectors.toList());

		//计算日班日期期望的天数之和
		List<Date> dayDateShift=new ArrayList<>();
		Integer total=computedDateExpectations(nightShiftDateExpectations,dayDateShift );
		//获取实现了日班日期期望后的剩余天数
		Integer remainNumber=nightNumberExpectations.get(0).getDayNumber()-total;

		if (remainNumber==0)
		{
			//夜班日期期望正好实现日班天数期望，结束夜班天数期望注入
			return;
		}
		else if ((remainNumber<0)){
			throw new RuntimeException("日期期望和天数期望有冲突，请检查期望是否合理！");
		}
		//避开 日期期望实现剩余的天数期望
		List<Date> allDateShift=new ArrayList<>();
		computedDateExpectations(allDateExpectations, allDateShift);

		//通过获取配置的克隆日期以避免循环导致的变化
		for (Date date=scheduleTable.getCloneStartDate();date.compareTo(scheduleTable.getCloneEndDate())<=0;nextDay(date)){
			if (!allDateShift.contains(date)){
				if (remainNumber>0){
					//如果天数期望还有剩余，且还存在可安排的日期，则向其添加
					scheduleTable.getShiftPlanDTOList().get(date).addShift(nightNumberExpectations.get(0));
					remainNumber=remainNumber-1;
				}else {
					break;
				}
			}
		}
	}

	/**
	 * 满足最小的排班人数规模的要求下的最大人员安排实现
	 * @param scheduleTable 排班结果表
	 * @param personDTOList 人员信息表
	 */
	public static boolean minRuleScheduling(
		ScheduleTable scheduleTable,
		final List<NurseDTO> personDTOList,
		final ShiftPlanDTO lastSchedule
	){
		boolean flag=true;
		//完成最小规则注入,判断夜班是否满足要求，如果不满足则添加或移除
		for (Date date=scheduleTable.getCloneStartDate();
		date.compareTo(scheduleTable.getCloneEndDate())<=0;
		nextDay(date)){
			//获取本日的现有人员安排
			ShiftPlanDTO shiftPlanDTO=scheduleTable.getShiftPlanDTOList().get(date);
			//分别目前获取护士助手的人员安排
			List<PersonDTO> assistantDTOs=shiftPlanDTO.getNightShiftAssistantDTOs();
			List<PersonDTO> nurseDTOs=shiftPlanDTO.getNightShiftNurseDTOs();


			if (nurseDTOs.size()>scheduleTable.getNightNurseNumber()){
				//如果护士夜班班次规模大于预计规模,进行期望优先级比较，同时这种情况下不需要考虑前一天的影响，我们默认认为班次日期期望优先级大于夜班后休息一天的规则
				nurseDTOs=getPersonDTOsByPriority(nurseDTOs, scheduleTable.getNightNurseNumber());
				shiftPlanDTO.nightShiftFilterByPostType(Constant.POST_TYPE_NURSE);
				shiftPlanDTO.nightShiftsAdd(nurseDTOs);
			}else if (nurseDTOs.size()<scheduleTable.getNightNurseNumber()){
				//如果小于，需要添加，这个时候，
				// 我们要考虑在满足假期期望和当日排班禁忌表的情况下能否满足最小规模，
				// 如果不能，那么将从假期期望中取人；
				// 如果还不能，从当日排班禁忌表中取人；
				// 如果还不能，则说明不能满足，标记此次排班失败

				//获取假期期望和生成当日排班禁忌表
				List<Long> vacationNurses=shiftPlanDTO.getVacationNurses();
				List<Long> tabooNurses;
				//如果是排班第一天，那么需要通过排班前一天的夜班结果获取今日禁忌表
				if (date.equals(scheduleTable.getCloneStartDate())){
					tabooNurses=lastSchedule.getNightShiftNurses();
				}else {
					tabooNurses=scheduleTable.getShiftPlanDTOList().get(lastDay(date)).getNightShiftNurses();
				}
				//将所有护士中去除掉 已经是夜班的，已经是日班的，请假的，禁忌表中的
				//然后判断剩余的人是否能满足要求，如果满足即完成
				if (
					nightShiftMinInjection(
						shiftPlanDTO,personDTOList ,
						tabooNurses, vacationNurses,
						scheduleTable.getNightShiftPersonNumberOfPostType(Constant.POST_TYPE_NURSE),
						Constant.POST_TYPE_NURSE)
				){
					//todo 如果返回真，则说明注入成功
					flag=flag&&true;
				}else {
					//todo 如果失败,则说明此次排班失败
					flag= false;
				}
			}
			//如果助手夜班班次规模大于预计规模
			if (assistantDTOs.size()>scheduleTable.getNightAssistantNumber()){
				assistantDTOs=getPersonDTOsByPriority(assistantDTOs, scheduleTable.getNightAssistantNumber());
				shiftPlanDTO.nightShiftFilterByPostType(Constant.POST_TYPE_ASSISTANT);
				shiftPlanDTO.nightShiftsAdd(assistantDTOs);
			}else if (assistantDTOs.size()<scheduleTable.getNightAssistantNumber()){

				//获取假期期望和生成当日排班禁忌表
				List<Long> vacationAssistants=shiftPlanDTO.getVacationAssistants();
				List<Long> tabooAssistants;
				//如果是排班第一天，那么需要通过排班前一天的夜班结果获取今日禁忌表
				if (date.equals(scheduleTable.getCloneStartDate())){
					tabooAssistants=lastSchedule.getNightShiftAssistants();
				}else {
					tabooAssistants=scheduleTable.getShiftPlanDTOList().get(lastDay(date)).getNightShiftAssistants();
				}
				//将所有中去除掉 已经是夜班的，已经是日班的，请假的，禁忌表中的
				//然后判断剩余的人是否能满足要求，如果满足即完成
				if (
					nightShiftMinInjection(
						shiftPlanDTO,personDTOList ,
						tabooAssistants, vacationAssistants,
						scheduleTable.getNightShiftPersonNumberOfPostType(Constant.POST_TYPE_ASSISTANT),
						Constant.POST_TYPE_ASSISTANT)
				){
					//todo 如果返回真，则说明注入成功
					flag=flag&&true;
				}else {
					//todo 如果失败,则说明此次排班失败
					flag= false;
				}
			}
			//如果刚好满足夜班规模的要求，那么不需要进行期望移除或人员添加，结束夜班班次的最小规则注入

			//开始日班的最小规模注入:

			//分别目前获取护士助手的人员安排
			assistantDTOs=shiftPlanDTO.getDayShiftAssistantDTOs();
			nurseDTOs=shiftPlanDTO.getDayShiftNurseDTOs();
			//获取假期期望和生成当日排班禁忌表
			List<Long> vacationAssistants=shiftPlanDTO.getVacationAssistants();
			List<Long> vacationNurses=shiftPlanDTO.getVacationNurses();
			List<Long> tabooAssistants;
			//如果是排班第一天，那么需要通过排班前一天的夜班结果获取今日禁忌表
			if (date.equals(scheduleTable.getCloneStartDate())){
				tabooAssistants=lastSchedule.getNightShiftAssistants();
			}else {
				tabooAssistants=scheduleTable.getShiftPlanDTOList().get(lastDay(date)).getNightShiftAssistants();
			}
			List<Long> tabooNurses;
			//如果是排班第一天，那么需要通过排班前一天的夜班结果获取今日禁忌表
			if (date.equals(scheduleTable.getCloneStartDate())){
				tabooNurses=lastSchedule.getNightShiftNurses();
			}else {
				tabooNurses=scheduleTable.getShiftPlanDTOList().get(lastDay(date)).getNightShiftNurses();
			}
			//		已经是夜班的，已经是日班的
			List<Long> dayShiftPersonDTOList=shiftPlanDTO.getDayShiftAssistants();
			List<Long> nightShiftPersonDTOList=shiftPlanDTO.getNightShiftAssistants();

			//日班在最大注入的情况下尽可能实现假期期望、禁忌表的情况下的闲置人员日班安排
//			如果日班连最小注入都无法实现，那么说明这就是它的最大注入
			if(assistantDTOs.size()>=scheduleTable.getDayAssistantNumber()){
				//如果助手日班列表人数大于等于目标规模，则说明人数充裕满足最小要求，直接进行最大日班人数填充
				//将所有护理人员中去除掉 已经是夜班的，已经是日班的，请假的，禁忌表中的
				//然后判断剩余的人是否能满足要求，如果满足即完成
				List<Long> remainPersonDTOs=personDTOList.stream()
					.filter(x->x.getPostType().equals(Constant.POST_TYPE_ASSISTANT))
					.map(NurseDTO::getNurseId).filter(
					x-> {
						if (tabooAssistants.contains(x)){
							return false;
						}else if (vacationAssistants.contains(x)){
							return false;
						}else if (dayShiftPersonDTOList.contains(x)){
							return false;
						}else if (nightShiftPersonDTOList.contains(x)){
							return false;
						}else {
							return true;
						}
					}
				).collect(Collectors.toList());
				//如果还有剩余的闲置人员，则全部加入日班安排表中
				remainPersonDTOs.forEach(x->{
					shiftPlanDTO.addDayShift(new PersonDTO(x,Constant.POST_TYPE_ASSISTANT));
				});
				flag=flag&&true;
			}else {
				//如果助手日班列表人数小于目标规模，则说明人数不足，需要进行最小要求注入
				if (
					dayShiftMinOrMaxInjection(
						shiftPlanDTO,
						personDTOList,
						tabooAssistants,
						vacationAssistants,
						scheduleTable.getDayAssistantNumber(),
						Constant.POST_TYPE_ASSISTANT
					)
				){
					flag=flag&&true;
				}else {
					flag=flag&&false;
				}
			}

			if (nurseDTOs.size()>=scheduleTable.getDayNurseNumber()){
//将所有护理人员中去除掉 已经是夜班的，已经是日班的，请假的，禁忌表中的
				//然后判断剩余的人是否能满足要求，如果满足即完成
				List<Long> remainPersonDTOs=personDTOList.stream()
					.filter(x->x.getPostType().equals(Constant.POST_TYPE_NURSE))
					.map(NurseDTO::getNurseId).filter(
						x-> {
							if (tabooNurses.contains(x)){
								return false;
							}else if (vacationNurses.contains(x)){
								return false;
							}else if (dayShiftPersonDTOList.contains(x)){
								return false;
							}else if (nightShiftPersonDTOList.contains(x)){
								return false;
							}else {
								return true;
							}
						}
					).collect(Collectors.toList());
				//如果还有剩余的闲置人员，则全部加入日班安排表中
				remainPersonDTOs.forEach(x->{
					shiftPlanDTO.addDayShift(new PersonDTO(x,Constant.POST_TYPE_NURSE));
				});
				flag=flag&&true;
			}else {
				//如果护士日班列表人数小于目标规模，则说明人数不足，需要进行最小要求注入
				if (
					dayShiftMinOrMaxInjection(
						shiftPlanDTO,
						personDTOList,
						tabooNurses,
						vacationNurses,
						scheduleTable.getDayNurseNumber(),
						Constant.POST_TYPE_NURSE
					)
				){
					flag=flag&&true;
				}else {
					flag=flag&&false;
				}
			}
		}
		return flag;
	}


	/**
	 * 日班满足最小规则下的最大注入实现；当可以被安排的闲置人员超过目标数时，则进行最大注入；反之进行最小规则注入
	 * @param shiftPlanDTO 本日排班结果表
	 * @param allPersonDTOList 所有人的信息
	 * @param tabooPersonDTOList 禁忌表
	 * @param vacationPersonDTOList 假期表
	 * @param numberOfSetting 人数规模
	 * @param postType 人员类型
	 * @return
	 */
	public static boolean dayShiftMinOrMaxInjection(
		ShiftPlanDTO shiftPlanDTO,
		final List<NurseDTO> allPersonDTOList,
		final List<Long> tabooPersonDTOList,
		final List<Long> vacationPersonDTOList,
		int numberOfSetting,
		final Integer postType
	){
		//获取科室的全部的护士、助手
		List<Long> personDTOs=
			allPersonDTOList.stream()
				.filter(x->x.getPostType().equals(postType))
				.map(NurseDTO::getNurseId)
				.collect(Collectors.toList());
//		已经是夜班的，已经是日班的
		List<Long> dayShiftPersonDTOList;
		List<Long> nightShiftPersonDTOList;
		if (postType.equals(Constant.POST_TYPE_NURSE)){
			dayShiftPersonDTOList=shiftPlanDTO.getDayShiftNurses();
			nightShiftPersonDTOList=shiftPlanDTO.getNightShiftNurses();
		}else {
			dayShiftPersonDTOList=shiftPlanDTO.getDayShiftAssistants();
			nightShiftPersonDTOList=shiftPlanDTO.getNightShiftAssistants();
		}

		//将所有护理人员中去除掉 已经是夜班的，已经是日班的，请假的，禁忌表中的
		//然后判断剩余的人是否能满足要求，如果满足即完成
		List<Long> remainPersonDTOs=personDTOs.stream().filter(
			x-> {
				if (tabooPersonDTOList.contains(x)){
					return false;
				}else if (vacationPersonDTOList.contains(x)){
					return false;
				}else if (dayShiftPersonDTOList.contains(x)){
					return false;
				}else if (nightShiftPersonDTOList.contains(x)){
					return false;
				}else {
					return true;
				}
			}
		).collect(Collectors.toList());
		//日班最小实现与夜班最小实现的区别
		numberOfSetting=numberOfSetting-dayShiftPersonDTOList.size();
		if (remainPersonDTOs.size()>=numberOfSetting){
			//人员充足，全部加入以实现最大日班人数填充
			remainPersonDTOs.forEach(x->{
				shiftPlanDTO.addDayShift(new PersonDTO(x,postType));
			});
			return true;
		}else {
			//剩余人员不足/逐步添加
			//首先将所有剩余人员加入
			for (Long remainPersonDTO : remainPersonDTOs) {
				shiftPlanDTO.addNightShift(new PersonDTO(remainPersonDTO,postType));
				numberOfSetting=numberOfSetting-1;
				if (numberOfSetting<=0){
					return true;
				}
			}
			// 如果不能，那么将从假期期望中取人；
			//todo 如果没有完成，则根据假期期望优先级先后忽略看是否可以实现
			for (Long vacationPersonDTO : vacationPersonDTOList) {
				shiftPlanDTO.addNightShift(new PersonDTO(vacationPersonDTO,postType));
				numberOfSetting=numberOfSetting-1;
				if (numberOfSetting<=0){
					return true;
				}
			}
			// 如果还不能，从当日排班禁忌表中取人；
			for (Long tabooPersonDTO : tabooPersonDTOList) {
				shiftPlanDTO.addNightShift(new PersonDTO(tabooPersonDTO,postType));
				numberOfSetting=numberOfSetting-1;
				if (numberOfSetting<=0){
					return true;
				}
			}
			// 如果还不能，则说明不能满足，标记此次排班失败
			return false;
		}
	}

	/**
	 * 夜班最小规则注入实现
	 * @param shiftPlanDTO 本日排班结果表
	 * @param allPersonDTOList 所有人的信息
	 * @param tabooPersonDTOList 禁忌表
	 * @param vacationPersonDTOList 假期表
	 * @param numberOfSetting 人数规模
	 * @param postType 人员类型
	 * @return
	 */
	public static boolean nightShiftMinInjection(
		ShiftPlanDTO shiftPlanDTO,
		final List<NurseDTO> allPersonDTOList,
		final List<Long> tabooPersonDTOList,
		final List<Long> vacationPersonDTOList,
		int numberOfSetting,
		final Integer postType
	){
		//获取科室的全部的护士、助手
		List<Long> personDTOs=
			allPersonDTOList.stream()
				.filter(x->x.getPostType().equals(postType))
				.map(NurseDTO::getNurseId)
				.collect(Collectors.toList());
//		已经是夜班的，已经是日班的
		List<Long> dayShiftPersonDTOList;
		List<Long> nightShiftPersonDTOList;
		if (postType.equals(Constant.POST_TYPE_NURSE)){
			dayShiftPersonDTOList=shiftPlanDTO.getDayShiftNurses();
			nightShiftPersonDTOList=shiftPlanDTO.getNightShiftNurses();
		}else {
			dayShiftPersonDTOList=shiftPlanDTO.getDayShiftAssistants();
			nightShiftPersonDTOList=shiftPlanDTO.getNightShiftAssistants();
		}

		//将所有护理人员中去除掉 已经是夜班的，已经是日班的，请假的，禁忌表中的
		//然后判断剩余的人是否能满足要求，如果满足即完成
		List<Long> remainPersonDTOs=personDTOs.stream().filter(
			x-> {
				if (tabooPersonDTOList.contains(x)){
					return false;
				}else if (vacationPersonDTOList.contains(x)){
					return false;
				}else if (dayShiftPersonDTOList.contains(x)){
					return false;
				}else if (nightShiftPersonDTOList.contains(x)){
					return false;
				}else {
					return true;
				}
			}
		).collect(Collectors.toList());
		numberOfSetting=numberOfSetting-nightShiftPersonDTOList.size();
		if (remainPersonDTOs.size()>=numberOfSetting){
			//人员充足，遍历加入
			//todo 可优化
			for (Long remainPersonDTO : remainPersonDTOs) {
				shiftPlanDTO.addNightShift(new PersonDTO(remainPersonDTO,postType));
				numberOfSetting=numberOfSetting-1;
				if (numberOfSetting<=0){
					break;
				}
			}
			return true;
		}else {
			//剩余人员不足/逐步添加
			//首先将所有剩余人员加入
			for (Long remainPersonDTO : remainPersonDTOs) {
				shiftPlanDTO.addNightShift(new PersonDTO(remainPersonDTO,postType));
				numberOfSetting=numberOfSetting-1;
				if (numberOfSetting<=0){
					return true;
				}
			}
			// 如果不能，那么将从假期期望中取人；
			//todo 如果没有完成，则根据假期期望优先级先后忽略看是否可以实现
			for (Long vacationPersonDTO : vacationPersonDTOList) {
				shiftPlanDTO.addNightShift(new PersonDTO(vacationPersonDTO,postType));
				numberOfSetting=numberOfSetting-1;
				if (numberOfSetting<=0){
					return true;
				}
			}
			// 如果还不能，从当日排班禁忌表中取人；
			for (Long tabooPersonDTO : tabooPersonDTOList) {
				shiftPlanDTO.addNightShift(new PersonDTO(tabooPersonDTO,postType));
				numberOfSetting=numberOfSetting-1;
				if (numberOfSetting<=0){
					return true;
				}
			}
			// 如果还不能，则说明不能满足，标记此次排班失败
			return false;
		}
	}


	/**
	 * 	获取 numberOfGetting 个 origins 数组中的对象；其中 origins.size()>numberOfGetting
	 * @param origins
	 * @param numberOfGetting
	 * @return
	 */
	public static List<PersonDTO> getPersonDTOsByPriority(
		final List<PersonDTO> origins , int numberOfGetting
	){
		//创建用来存储计算结果的对象
		List<PersonDTO> targets=new ArrayList<>();
		//依据优先级先后向结果中顺序添加

		//获取其中优先级数最小的（数值越小优先级越高）
		for (int i=1;i<Integer.MAX_VALUE;i++){
			if (numberOfGetting<=0){
				break;
			}
			int finalI = i;
			List<PersonDTO> personDTOS=origins.stream().filter(x->x.getPriority()== finalI).collect(Collectors.toList());
			if (personDTOS.size()>0&&personDTOS.size()<=numberOfGetting){
				//如果过滤出的期望安排的数量小于或等于需要的数量，则直接加入目标队列中
				targets.addAll(personDTOS);
				numberOfGetting=numberOfGetting-personDTOS.size();
			}else if (personDTOS.size()>numberOfGetting){
				//如果过滤出的期望安排数量多余目标数量，则进行随机获取目标数个
				targets.addAll(randomGetPersonDTOs(personDTOS, numberOfGetting));
				//这种情况下目标数量必定清零
				numberOfGetting=0;
			}
		}
		return targets;
	}

	/**
	 * 随机获取 numberOfGetting 个 origins 数组中的对象；其中 origins.size()>numberOfGetting
	 * @param origins
	 * @param numberOfGetting
	 * @return
	 */
	public static List<PersonDTO> randomGetPersonDTOs(
		final List<PersonDTO> origins ,int numberOfGetting
	){
		List<PersonDTO> targets=new ArrayList<>();
		for (int i=0;i<numberOfGetting;i++){
			int targetNumber=getRandomNumber(origins.size());
			targets.add(origins.get(targetNumber));
			origins.remove(targetNumber);
		}
		return targets;
	}

	public static Random random=new Random();

	/**
	 * 随机获取 [0,size) 内的整数
	 * @param size
	 * @return
	 */
	public static int getRandomNumber(int size){
		return random.nextInt(size);
	}
	public static Integer computedDateExpectations(
		final List<ExpectationDTO> expectationDTOS,
		List<Date> tabooDate
	){
		expectationDTOS.forEach(x->{
			for (Date date=x.getStartDate();date.compareTo(x.getEndDate())<=0;nextDay(date)){
				tabooDate.add(date);
			}
		});
		return tabooDate.size();
	}



//	/**
//	 * 遍历加入实现期望注入
//	 * @param scheduleTable
//	 * @param expectation
//	 * @param lastSchedule
//	 */
//	public static void addTraversal(
//		ScheduleTable scheduleTable,
//		ExpectationDTO expectation,
//		ShiftPlanDTO lastSchedule
//	){
//		//ps:对于期望注入中期望之间的相互影响默认没有，即各个期望之间是相互独立且互不影响的（需要在期望添加中实现相关的逻辑判断以实现）
//		//只有在期望之间相互不影响的情况下，才能使得期望实现的时候可以不用考虑除了夜班期望的最大规模的外的条件
//
//		//对于日班、夜班的日期期望的实现，默认是遍历期望日期实现
//		if (expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_SHIFT)){
//			//对于日班日期期望，直接添加入即可
//			//获取期望时间区间，并通过此事件区间的时间获取对应的排班表
//			for (Date date=expectation.getStartDate();date.compareTo(expectation.getEndDate())<=0;nextDay(date)){
//				scheduleTable.getShiftPlanDTOList().get(date).addDayShift(expectation);
//			}
//		}
//		else if (expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_SHIFT)){
//			//对于夜班日期期望，
//			for (Date date=expectation.getStartDate();date.compareTo(expectation.getEndDate())<=0;nextDay(date)){
//				scheduleTable.getShiftPlanDTOList().get(date).addNightShift(expectation);
//			}
//		}
//		else if (expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_DAY_NUMBER) ||
//			expectation.getExpectationType().equals(Constant.EXPECTATION_TYPE_NIGHT_NUMBER)){
//			//展示将天数期望的实现定为遍历实现，加入前判断本日此人是否已经被安排
//			//还要考虑假期期望
//			//todo
//		}
//	}


	/**
	 * 获取此次排班前一天时间
	 * @param schedulingReference
	 * @return
	 */
	public static Date getLastSchedulingEndDay(
		SchedulingReference schedulingReference
	){
		return DateUtil.plusDays(schedulingReference.getStartDate(), -1L);
	}

	/**
	 * 获取明天的时间
	 * @param date
	 * @return
	 */
	public static Date nextDay(Date date){
		return DateUtil.plusDays(date, 1L);
	}

	/**
	 * 获取昨日的时间
	 * @param date
	 */
	public static Date lastDay(Date date){
		return DateUtil.plusDays(date, -1L);
	}

}
