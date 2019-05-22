package com.dingtalk.spring.boot.event;

import org.springframework.biz.context.event.EnhancedEvent;

import com.alibaba.fastjson.JSONObject;

/**
 */
@SuppressWarnings("serial")
public class BpmsInstanceChangeEvent extends EnhancedEvent<JSONObject> {
	
	public BpmsInstanceChangeEvent(Object source, JSONObject response) {
		super(source, response);
	}
	
}
