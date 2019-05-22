package com.dingtalk.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(DingtalkProperties.PREFIX)
public class DingtalkProperties {

	public static final String PREFIX = "dingtalk";

	/**
	 * 企业内部开发：程序客户端ID，登录开发者后台可查看
	 */
	private String agentId;
	/**
	 * 企业内部开发：应用的唯一标识key，登录开发者后台，点击应用管理，进入应用详情可见
	 */
	private String appKey;
	/**
	 * 企业内部开发：应用的密钥，登录开发者后台，点击应用管理，进入应用详情可见
	 */
	private String appSecret;
	/**
	 * 企业corpid, 需要修改成开发者所在企业
	 */
	private String corpId;
    /**
     * 数据加密密钥。用于回调数据的加密，长度固定为43个字符，从a-z, A-Z, 0-9共62个字符中选取,您可以随机生成
     */
    public String encodingAesKey;

    /**
     * 加解密需要用到的token，企业可以随机填写。如 "12345"
     */
    public String token = "12345";
    /**
     * 回调host
     */
    public String callbackUrlHost;

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getEncodingAesKey() {
		return encodingAesKey;
	}

	public void setEncodingAesKey(String encodingAesKey) {
		this.encodingAesKey = encodingAesKey;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCallbackUrlHost() {
		return callbackUrlHost;
	}

	public void setCallbackUrlHost(String callbackUrlHost) {
		this.callbackUrlHost = callbackUrlHost;
	}
	
	

}
