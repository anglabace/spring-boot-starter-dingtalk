package com.dingtalk.spring.boot.endpoint;

import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.spring.boot.api.DingTalkEncryptor;
import com.dingtalk.spring.boot.api.DingTalkEventType;
import com.dingtalk.spring.boot.event.BpmsInstanceChangeEvent;
import com.dingtalk.spring.boot.event.BpmsInstanceChangeEventHandler;
import com.dingtalk.spring.boot.event.BpmsTaskChangeEvent;
import com.dingtalk.spring.boot.event.BpmsTaskChangeEventHandler;

@Validated
@RestController
@RequestMapping("/dingtalk")
public class DingtalkCallBackEndpoint implements ApplicationEventPublisherAware {

	protected final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    protected DingTalkEncryptor dingTalkEncryptor;
    @Autowired(required = false)
	private List<BpmsTaskChangeEventHandler> bpmsTaskChangeEventHandlers;
	@Autowired(required = false)
	private List<BpmsInstanceChangeEventHandler> bpmsInstanceChangeEventHandlers;
    
    /**
     * https://open-doc.dingtalk.com/microapp/serverapi2/skn8ld#-5
     */
    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> callback(@RequestParam(value = "signature", required = false) String signature,
                                        @RequestParam(value = "timestamp", required = false) String timestamp,
                                        @RequestParam(value = "nonce", required = false) String nonce,
                                        @RequestBody(required = false) JSONObject json) {
        String params = " signature:" + signature + " timestamp:" + timestamp + " nonce:" + nonce + " json:" + json;
        logger.info("process callback params！{}" , params);
        try {
            //从post请求的body中获取回调信息的加密数据进行解密处理
            String encryptMsg = json.getString("encrypt");
            String plainText = dingTalkEncryptor.getDecryptMsg(signature, timestamp, nonce, encryptMsg);
            logger.info("process callback decryptMsg:{}",plainText);
           
            JSONObject response = JSONObject.parseObject(plainText);
    		/*
    			事件类型
    	    	bpms_task_change :  审批任务开始，结束，转交
    	    	bpms_instance_change：审批实例开始，结束
    		 */
            String eventType = response.getString("EventType");
            // bpms_task_change :  审批任务开始，结束，转交
            if (DingTalkEventType.BPMS_TASK_CHANGE.equals(eventType)) {
            	if (CollectionUtils.isEmpty(bpmsTaskChangeEventHandlers)) {
            		getEventPublisher().publishEvent(new BpmsTaskChangeEvent(this, response));
        		} else {

        			boolean isMatched = false;
        			for (BpmsTaskChangeEventHandler eventHandler : bpmsTaskChangeEventHandlers) {
        				if (eventHandler != null && eventHandler.supports(response)) {
        					eventHandler.onBpmsTaskChanged(response);
        					isMatched = true;
        					break;
        				}
        			}
    				if (!isMatched) {
    					getEventPublisher().publishEvent(new BpmsTaskChangeEvent(this, response));
    				}
        		}
            } 
            // bpms_instance_change：审批实例开始，结束
            else if (DingTalkEventType.BPMS_INSTANCE_CHANGE.equals(eventType)) {
            	
            	if (CollectionUtils.isEmpty(bpmsInstanceChangeEventHandlers)) {
            		getEventPublisher().publishEvent(new BpmsInstanceChangeEvent(this, response));
        		} else {

        			boolean isMatched = false;
        			for (BpmsInstanceChangeEventHandler eventHandler : bpmsInstanceChangeEventHandlers) {
        				if (eventHandler != null && eventHandler.supports(response)) {
        					eventHandler.onBpmsInstanceChanged(response);
        					isMatched = true;
        					break;
        				}
        			}
    				if (!isMatched) {
    					getEventPublisher().publishEvent(new BpmsInstanceChangeEvent(this, response));
    				}
        		}
            	
            } else {
                // 其他类型事件处理
                logger.info("其他类型事件处理: " + plainText);
            }
            
            // 返回success的加密信息表示回调处理成功
            Map<String, String> encryptedMap = dingTalkEncryptor.getEncryptedMap(DingTalkEventType.CALLBACK_RESPONSE_SUCCESS, System.currentTimeMillis(), dingTalkEncryptor.getRandomStr(8));
            logger.info("process callback return success encryptedMap:{}",JSONObject.toJSONString(encryptedMap));
            return encryptedMap;
        } catch (Exception e) {
            //失败的情况，应用的开发者应该通过告警感知，并干预修复
            logger.error("process callback failed！" + params, e);
            return null;
        }
    }

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;
	}

	public ApplicationEventPublisher getEventPublisher() {
		return eventPublisher;
	}
}
