package com.dingtalk.spring.boot.event;

import com.alibaba.fastjson.JSONObject;

public interface BpmsTaskChangeEventHandler {

	public boolean supports(JSONObject response);
	
	public void onBpmsTaskChanged(JSONObject response);
	
}
