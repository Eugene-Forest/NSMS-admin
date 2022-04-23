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

	/**
	 * 期望实现状态： 已实现
	 */
	public static Integer ACTUAL_STATE_SUCCESS=1;

	/**
	 * 期望实现状态： 待实现
	 */
	public static Integer ACTUAL_STATE_WAIT=0;

	/**
	 * 期望实现状态： 未实现
	 */
	public static Integer ACTUAL_STATE_FAILURE=2;


	/**
	 * 逻辑删除的标识符： 已被逻辑删除
	 */
	public static Integer RECORD_IS_DELETED=1;

	/**
	 * 逻辑删除的标识符： 未被逻辑删除
	 */
	public static Integer RECORD_IS_NOT_DELETED=0;


	/**
	 * 换班申请审核状态： 待商议
	 */
	public static Integer EXCHANGE_APPROVAL_STATUS_PENDING=0;

	/**
	 * 换班申请审核状态： 被申请人不同意
	 */
	public static Integer EXCHANGE_APPROVAL_STATUS_DISAGREE=1;

	/**
	 * 换班申请审核状态： 被申请人同意
	 */
	public static Integer EXCHANGE_APPROVAL_STATUS_AGREE=2;

	/**
	 * 换班申请审核状态： 待审核
	 */
	public static Integer EXCHANGE_APPROVAL_STATUS_PENDING_CHECK=3;

	/**
	 * 换班申请审核状态： 护士长驳回
	 */
	public static Integer EXCHANGE_APPROVAL_STATUS_REJECT=4;

	/**
	 * 换班申请审核状态： 护士长同意
	 */
	public static Integer EXCHANGE_APPROVAL_STATUS_PASS=5;

	/**
	 * 请假申请审核状态： 未审核
	 */
	public static Integer APPROVAL_STATUS_PENDING=0;


	/**
	 * 请假申请审核状态： 驳回
	 */
	public static Integer APPROVAL_STATUS_REJECT=1;


	/**
	 * 请假申请审核状态： 同意
	 */
	public static Integer APPROVAL_STATUS_PASS=2;
}

