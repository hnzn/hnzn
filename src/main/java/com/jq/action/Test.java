package com.jq.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jq.service.TestServer;
import com.jq.service.mq.MQProducer;
import com.jq.util.TokenManager;

/**
 * Created by hnznw on 2017/12/11.
 */
@RestController
public class Test {

    @Autowired
    private TestServer testServer;
    @Autowired
    private MQProducer mqProducer;

    @GetMapping("/test")
    public List getWord(){

        return testServer.
                getTest
                ();
    }
    
    @GetMapping("/wel")
    public String getWel(){

        return "你好";
    }
    
    @GetMapping("/pMsg")
    public String pMsg(@RequestParam("msg") String msg) {
    	mqProducer.createMsg(msg);
    	return "成功";
    }
    
    


    
    public static void main(String[] args) {
        /*Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        System.exit(0);*/

        Class c = Test.class;

        System.out.println(c.getResource("/"));

        System.out.println(c.getClassLoader().getResource(("/")));

        System.out.println(c.getPackage());

        System.out.println(c.getDeclaredFields()[0].getType().getPackage());

        
        Integer i = 1;
        
        System.out.println((Object)i);
        System.out.println(Integer.valueOf(1));
        System.out.println(i == Integer.valueOf(1));
        		
        
    }
}
