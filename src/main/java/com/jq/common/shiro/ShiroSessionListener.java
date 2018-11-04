package com.jq.common.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;

public class ShiroSessionListener extends SessionListenerAdapter{

	@Override
	public void onStart(Session session) {
		System.out.println("会话开始");
	}

	@Override
	public void onStop(Session session) {
		System.out.println("会话结束");
	}

	@Override
	public void onExpiration(Session session) {
		System.out.println("会话异常");
	}

	
	
}
