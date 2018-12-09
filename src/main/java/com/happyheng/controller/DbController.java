package com.happyheng.controller;

import com.alibaba.fastjson.JSON;
import com.happyheng.db.pool.DbConnectionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by happyheng on 2018/12/9.
 */
@RequestMapping("/db")
@RestController
public class DbController {


    @Autowired
    private DbConnectionPool dbConnectionPool;


    @RequestMapping("/query")
    public String queryDb() {


        String querySql = "SELECT * FROM user";
        List<Map<String, String>> result = new ArrayList<>();
        try {
            result = dbConnectionPool.executeSql(querySql);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return JSON.toJSONString(result);
    }


}
