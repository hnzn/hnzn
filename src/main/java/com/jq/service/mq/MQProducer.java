package com.jq.service.mq;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

@Component
public class MQProducer  {
	
	private static final String QUEUE_NAME = "test";
	
	@Autowired
	private Channel channel;
	
	public void createMsg(String msg){
		try {
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
