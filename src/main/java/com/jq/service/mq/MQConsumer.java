package com.jq.service.mq;

import java.io.IOException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

@Component
public class MQConsumer implements InitializingBean {
	
	private static final String QUEUE_NAME = "test";
	
	private Consumer consumer;
	
	@Autowired
	private Channel channel;
	
	private Consumer createConsumer() {
		if(null != consumer) {
			return consumer;
		}
		Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Customer Received '" + message + "'");
            }
        };
		return consumer;
	}
	
	public void receiveMsg(){
		 try {
				channel.basicConsume(QUEUE_NAME, true, createConsumer());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}       
	}
	

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("run--MQConsumer---");
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("run");
				receiveMsg();
			}
		}).start();*/
		if(channel == null) {
			return;
		}
		receiveMsg();
	}
}
