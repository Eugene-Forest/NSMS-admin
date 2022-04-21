package org.springblade.nsms.tools;

/**
 * 常量工具类；此类用来存储标记NSMS系统的使用的常量
 * @author Eugene-Forest
 * @date 2022/4/20
 **/
public class Constant {

	/**
	 * 排班配置记录的状态值:未使用
	 */
	public static Integer REFERENCE_CONFIG_STATE_UNUSED=0;
	/**
	 * 排班配置记录的状态值:期望添加
	 */
	public static Integer REFERENCE_CONFIG_STATE_ADD_EXPECTATION=1;
	/**
	 * 排班配置记录的状态值:待排班
	 */
	public static Integer REFERENCE_CONFIG_STATE_WAIT_FOR_SCHEDULING=2;

	/**
	 * 排班配置记录的状态值:排班完成
	 */
	public static Integer REFERENCE_CONFIG_STATE_COMPLETE_SCHEDULING=3;


	/**
	 * 排班期望：夜班天数期望
	 */
	public static Integer EXPECTATION_TYPE_NIGHT_NUMBER=0;
	/**
	 * 排班期望：日班天数期望
	 */
	public static Integer EXPECTATION_TYPE_DAY_NUMBER=1;
	/**
	 * 排班期望：夜班期望
	 */
	public static Integer EXPECTATION_TYPE_NIGHT_SHIFT=10;
	/**
	 * 排班期望：日班期望
	 */
	public static Integer EXPECTATION_TYPE_DAY_SHIFT=11;
	/**
	 * 排班期望：假期期望
	 */
	public static Integer EXPECTATION_TYPE_VACATION=20;



}
