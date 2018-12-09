package com.happyheng.db.bean;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by happyheng on 2018/12/6.
 */
public class ExecuteSqlBean {

    /**
     * 唯一id
     */
    private long id;

    /**
     * 执行的sql
     */
    private String sql;

    /**
     * 执行时线程wait的对象
     */
    private Object executeWaitObject;

    /**
     * 执行后的结果
     */
    private List<Map<String, String>> result;

    public ExecuteSqlBean(long id, String sql, Object executeWaitObject) {
        this.id = id;
        this.sql = sql;
        this.executeWaitObject = executeWaitObject;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object getExecuteWaitObject() {
        return executeWaitObject;
    }

    public void setExecuteWaitObject(Object executeWaitObject) {
        this.executeWaitObject = executeWaitObject;
    }

    public List<Map<String, String>> getResult() {
        return result;
    }

    public void setResult(List<Map<String, String>> result) {
        this.result = result;
    }
}
