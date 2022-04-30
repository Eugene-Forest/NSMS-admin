package org.springblade.nsms.tools;

/**
 * 常量工具类；此类用来存储标记NSMS系统的使用的常量,同时也是字典
 * @author Eugene-Forest
 * @date 2022/4/20
 **/
public class Constant {

	/**
	 * 班次类别：日班
	 */
	public static Integer SHIFT_TYPE_OF_DAY=0;

	/**
	 * 班次类别：夜班
	 */
	public static Integer SHIFT_TYPE_OF_NIGHT=1;


	/**
	 * 【expectation_type】排班期望：夜班天数期望
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
	 * 期望实现状态： 待实现
	 */
	public static Integer ACTUAL_STATE_WAIT=0;

	/**
	 * 期望实现状态： 未实现
	 */
	public static Integer ACTUAL_STATE_FAILURE=1;


	/**
	 * 期望实现状态： 已实现
	 */
	public static Integer ACTUAL_STATE_SUCCESS=2;


	/**
	 * 期望实现状态： 异常
	 */
	public static Integer ACTUAL_STATE_ERROR=3;


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


	/**
	 * 排班配置采用状态： 未启用
	 */
	public static Integer SCHEDULING_REFERENCE_CONFIG_NOT_ENABLED=0;

	/**
	 * 排班配置采用状态： 期望录入
	 */
	public static Integer SCHEDULING_REFERENCE_CONFIG_ADD_EXPECTATION=1;

	/**
	 * 排班配置采用状态： 待排班
	 */
	public static Integer SCHEDULING_REFERENCE_CONFIG_WAITING_FOR_SCHEDULING=2;

	/**
	 * 排班配置采用状态： 排班失败
	 */
	public static Integer SCHEDULING_REFERENCE_CONFIG_SCHEDULING_FAILURE=3;

	/**
	 * 排班配置采用状态： 排班完成
	 */
	public static Integer SCHEDULING_REFERENCE_CONFIG_SCHEDULING_SUCCESS=4;


	/**
	 * 字典【post_category】岗位类型： 系统管理员
	 */
	public static Integer POST_TYPE_SYSTEM_ADMIN=0;

	/**
	 * 字典【post_category】岗位类型： 租户管理员
	 */
	public static Integer POST_TYPE_TENANT_ADMIN=1;

	/**
	 * 字典【post_category】岗位类型： 总护士长
	 */
	public static Integer POST_TYPE_CHIEF_HEAD_NURSE=10;

	/**
	 * 字典【post_category】岗位类型： 科室护士长
	 */
	public static Integer POST_TYPE_DEPT_HEAD_NURSE=11;

	/**
	 * 字典【post_category】岗位类型： 一般护士
	 */
	public static Integer POST_TYPE_NURSE=12;

	/**
	 * 字典【post_category】岗位类型： 一般助手
	 */
	public static Integer POST_TYPE_ASSISTANT=13;



}

