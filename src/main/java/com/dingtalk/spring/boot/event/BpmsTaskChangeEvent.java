package com.dingtalk.spring.boot.event;

import org.springframework.biz.context.event.EnhancedEvent;

import com.alibaba.fastjson.JSONObject;

/**
 */
@SuppressWarnings("serial")
public class BpmsTaskChangeEvent extends EnhancedEvent<JSONObject> {
	
	public BpmsTaskChangeEvent(Object source, JSONObject response) {
		super(source, response);
	}
	
}
