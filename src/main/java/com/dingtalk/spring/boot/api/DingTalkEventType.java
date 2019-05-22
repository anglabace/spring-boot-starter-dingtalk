package com.dingtalk.spring.boot.api;

public class DingTalkEventType {

	/**
	 * 创建套件后，验证回调URL创建有效事件（第一次保存回调URL之前）
	 */
	public static final String CHECK_URL = "check_url";

	/**
	 * 审批任务回调
	 */
	public static final String BPMS_TASK_CHANGE = "bpms_task_change";

	/**
	 * 审批实例回调
	 */
	public static final String BPMS_INSTANCE_CHANGE = "bpms_instance_change";

	/**
	 * 相应钉钉回调时的值
	 */
	public static final String CALLBACK_RESPONSE_SUCCESS = "success";

}
