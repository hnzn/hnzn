package com.jq.service;

import com.jq.dao.TestDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hnznw on 2017/12/16.
 */
@Service
public class TestServer {

    @Autowired
    private TestDAO testDAO;

    public List getTest(){
        return testDAO.getTest();
    }

}
