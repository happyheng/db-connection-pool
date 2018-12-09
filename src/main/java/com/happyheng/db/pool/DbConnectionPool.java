package com.happyheng.db.pool;

import com.alibaba.fastjson.JSON;
import com.happyheng.db.bean.ExecuteSqlBean;
import com.happyheng.db.factory.ConnectionFactory;
import com.happyheng.db.pool.thread.ConnectionRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * Created by happyheng on 2018/12/6.
 */
public class DbConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(DbConnectionPool.class);

    /**
     * 核心数量
     */
    private int coreNum;

    /**
     * 执行的Runnable等待的Object
     */
    private final Object executeRunnableWaitObject = new Object();

    /**
     * 存储需要行过的sql的Vector
     */
    private Vector<ExecuteSqlBean> sqlBeanVector = new Vector<>();

    /**
     * 最大执行sql的Id
     */
    private AtomicLong maxSqlId = new AtomicLong();


    public DbConnectionPool(int coreNum, ConnectionFactory connectionFactory) {
        this.coreNum = coreNum;

        initConnectionThread(connectionFactory);
    }


    private ConnectionRunnable.ConnectionCallBack connectionCallBack = new ConnectionRunnable.ConnectionCallBack() {
        @Override
        public synchronized ExecuteSqlBean getExecuteSqlBean() {

            logger.info(Thread.currentThread().getName() +  " getExecuteSqlBean 开始执行");

            if (sqlBeanVector.size() == 0) {
                logger.info(Thread.currentThread().getName() +  " getExecuteSqlBean 为空");
                return null;
            }

            ExecuteSqlBean executeSqlBean =  sqlBeanVector.get(0);
            sqlBeanVector.remove(executeSqlBean);
            logger.info(Thread.currentThread().getName() +  " 获取到 executeSqlBean");
            return executeSqlBean;
        }

        @Override
        public void onExecuteSuccess(ExecuteSqlBean executeSqlBean) {

            System.out.println(Thread.currentThread().getName() + " executeId为 " + executeSqlBean.getId() + " 的sql任务完成，执行结果为\n " + JSON.toJSONString(executeSqlBean.getResult()));

            // 执行完成后，需要将对应的waitObject上wait的线程notifyAll
            Object executeWaitObject = executeSqlBean.getExecuteWaitObject();
            synchronized (executeWaitObject) {

                System.out.println(Thread.currentThread().getName() + " executeWaitObject开始通知");
                executeWaitObject.notifyAll();
                System.out.println(Thread.currentThread().getName() + " executeWaitObject结束通知");
            }
        }
    };


    /**
     * 初始化连接池线程数量
     */
    private void initConnectionThread(ConnectionFactory connectionFactory) {
        for (int i = 0; i< coreNum; i++) {
            Connection connection = connectionFactory.makeConnection();
            Thread thread = new Thread(new ConnectionRunnable(connection, executeRunnableWaitObject, connectionCallBack), "db-connection-thread-" + i);
            thread.start();
        }
    }


    public List<Map<String, String>> executeSql(String sql) throws InterruptedException {

        logger.info("executeSql 开始执行");

        maxSqlId.addAndGet(1);
        final Object executeWaitObject = new Object();

        logger.info("executeSql maxSqlId为" + maxSqlId);

        // 获取到最大id之后，生成 ExecuteSqlBean ，然后通知连接池等待的执行线程，开始获取 ExecuteSqlBean 并执行
        ExecuteSqlBean sqlBean = new ExecuteSqlBean(maxSqlId.get(), sql, executeWaitObject);
        sqlBeanVector.add(sqlBean);
        synchronized (executeRunnableWaitObject) {
            logger.info("executeSql notifyAll before");

            executeRunnableWaitObject.notifyAll();

            logger.info("executeSql notifyAll end");
        }

        // 开始阻塞在执行等待Object中
        synchronized (executeWaitObject) {
            logger.info("executeSql executeWaitObject 开始wait");

            executeWaitObject.wait();

            logger.info("executeSql executeWaitObject 结束wait");
        }

        // wait之后，说明已经执行完成，从直接从sqlBean中获取即可
        return sqlBean.getResult();
    }

}
