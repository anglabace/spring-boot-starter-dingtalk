/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.dingtalk.spring.boot;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiCallBackDeleteCallBackRequest;
import com.dingtalk.api.request.OapiCallBackRegisterCallBackRequest;
import com.dingtalk.api.response.OapiCallBackDeleteCallBackResponse;
import com.dingtalk.api.response.OapiCallBackRegisterCallBackResponse;
import com.dingtalk.spring.boot.api.DingTalkEncryptException;
import com.dingtalk.spring.boot.api.DingTalkEncryptor;
import com.dingtalk.spring.boot.api.DingTalkTemplate;
import com.dingtalk.spring.boot.api.DingtalkConstant;

@Configuration
@ConditionalOnClass(DefaultDingTalkClient.class)
@ConditionalOnProperty(prefix = DingtalkProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties(DingtalkProperties.class)
public class DingTalkAutoConfiguration implements ApplicationContextAware, CommandLineRunner {
	
	protected final Logger logger = LoggerFactory.getLogger(DingTalkAutoConfiguration.class);
	private ApplicationContext applicationContext;
	
	@Bean
	@ConditionalOnMissingBean
	public DingTalkTemplate dingTalkTemplate() {
		return new DingTalkTemplate();
	}
	
	@Bean
	public DingTalkEncryptor dingTalkEncryptor(DingtalkProperties dingtalkProperties) throws DingTalkEncryptException {
		return new DingTalkEncryptor(dingtalkProperties.getToken(), dingtalkProperties.getEncodingAesKey(),
				dingtalkProperties.getCorpId());
	}
	
	@Autowired
	private DingTalkTemplate dingTalkTemplate;
	@Autowired
	private DingtalkProperties dingtalkProperties;
	

	@Override
	public void run(String... args) throws Exception {
		 
		String accessToken = dingTalkTemplate.getAccessToken(dingtalkProperties.getAppKey(), dingtalkProperties.getAppSecret());
		
        // 先删除企业已有的回调
        DingTalkClient client = new DefaultDingTalkClient(DingtalkConstant.DELETE_CALLBACK);
        OapiCallBackDeleteCallBackRequest request = new OapiCallBackDeleteCallBackRequest();
        request.setHttpMethod("GET");
        OapiCallBackDeleteCallBackResponse deleteCallBackResponse = client.execute(request, accessToken);
        logger.info("注册回调 deleteCallBackResponse={}",JSONObject.toJSONString(deleteCallBackResponse));
        
        // 重新为企业注册回调
        client = new DefaultDingTalkClient(DingtalkConstant.REGISTER_CALLBACK);
        OapiCallBackRegisterCallBackRequest registerRequest = new OapiCallBackRegisterCallBackRequest();
        registerRequest.setUrl(dingtalkProperties.getCallbackUrlHost() + "/dingtalk/callback");
        registerRequest.setAesKey(dingtalkProperties.getEncodingAesKey());
        registerRequest.setToken(dingtalkProperties.getToken());
        registerRequest.setCallBackTag(Arrays.asList("bpms_instance_change", "bpms_task_change"));
        
        OapiCallBackRegisterCallBackResponse registerResponse = client.execute(registerRequest, accessToken);
        logger.info("注册回调 registerResponse={}",JSONObject.toJSONString(registerResponse));
        if (registerResponse.isSuccess()) {
            System.out.println("回调注册成功了！！！");
        }
		
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}



}
