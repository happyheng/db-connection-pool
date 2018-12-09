package com.happyheng.db.pool.thread;

import com.happyheng.db.bean.ExecuteSqlBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 *
 * Created by happyheng on 2018/12/6.
 */
public class ConnectionRunnable implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(ConnectionRunnable.class);

    private Connection connection;
    private Object waitObject;
    private ConnectionCallBack callBack;

    public ConnectionRunnable(Connection connection, Object waitObject, ConnectionCallBack callBack) {
        this.connection = connection;
        this.waitObject = waitObject;
        this.callBack = callBack;
    }

    @Override
    public void run() {

        // 不停获取数据，如果未获取到，wait
        while (!Thread.currentThread().isInterrupted()) {

            ExecuteSqlBean sqlBean = callBack.getExecuteSqlBean();
            while (sqlBean == null) {
                synchronized (waitObject) {
                    try {

                        logger.info(Thread.currentThread().getName() +  " run 开始wait");

                        waitObject.wait();

                        logger.info(Thread.currentThread().getName() +  " run 结束wait");

                        sqlBean = callBack.getExecuteSqlBean();

                        logger.info(Thread.currentThread().getName() +  " run 获取到sqlBean");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            logger.info(Thread.currentThread().getName() +  " run 开始执行sqlBean");

            // 开始执行
            List<Map<String, String>> result = doExecuteJob(sqlBean.getSql());
            sqlBean.setResult(result);

            logger.info(Thread.currentThread().getName() +  " run 结束执行sqlBean");

            // 执行完成后，回调连接池，已执行完成
            callBack.onExecuteSuccess(sqlBean);
        }


        // 关闭连接
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private List<Map<String, String>> doExecuteJob(String sql) {

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            List<Map<String, String>> executeResult = new LinkedList<>();
            while (resultSet.next()) {
                Map<String, String> columnMap = new HashMap<>();
                ResultSetMetaData metaData =  resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i < columnCount + 1 ; i++) {

                    columnMap.put(metaData.getColumnName(i), resultSet.getString(i));
                }

                executeResult.add(columnMap);
            }


            /**
             * 模拟数据库慢查询
             */
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            statement.close();
            return executeResult;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }



     public interface ConnectionCallBack{

         ExecuteSqlBean getExecuteSqlBean();

         void onExecuteSuccess(ExecuteSqlBean executeSqlBean);

    }



}
