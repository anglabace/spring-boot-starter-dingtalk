package com.dingtalk.spring.boot.event;

import com.alibaba.fastjson.JSONObject;

public interface BpmsInstanceChangeEventHandler {

	public boolean supports(JSONObject response);
	
	public void onBpmsInstanceChanged(JSONObject response);
	
}
