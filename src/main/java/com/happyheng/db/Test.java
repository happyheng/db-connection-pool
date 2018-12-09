package com.happyheng.db;

import com.happyheng.db.factory.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 *
 * Created by happyheng on 2018/12/5.
 */
@Service
public class Test {

    @Autowired
    private ConnectionFactory connectionFactory;

    private static final Logger logger = LoggerFactory.getLogger(Test.class);


    @PostConstruct
    public void test() {
//
//         new Thread(()->{
//
//             try {
//                 Thread.sleep(1000);
//             } catch (InterruptedException e) {
//                 e.printStackTrace();
//             }
//
//             Connection connection = connectionFactory.makeConnection();
//
//             try {
//                 Statement statement = connection.createStatement();
//                 int result = statement.executeUpdate("insert into user (name,age) values ('lisi', 18)");
//
//                 logger.info("result -- " + result);
//
//                 statement.close();
//
//
//
//                 Statement queryStatement = connection.createStatement();
//                 ResultSet resultSet = queryStatement.executeQuery("SELECT * FROM user");
//                 List<Map<String, String>> executeResult = new LinkedList<>();
//                 while (resultSet.next()) {
//
//                     Map<String, String> columnMap = new HashMap<>();
//                     ResultSetMetaData metaData =  resultSet.getMetaData();
//                     int columnCount = metaData.getColumnCount();
//                     for (int i = 1; i < columnCount + 1 ; i++) {
//
//                         columnMap.put(metaData.getColumnName(i), resultSet.getString(i));
//                     }
//
//                     executeResult.add(columnMap);
//                     System.out.println("\n\n\n\n\n");
//                 }
//                 System.out.println("结果为" + JSON.toJSONString(executeResult));
//                 queryStatement.close();
//
//                 connection.close();
//             } catch (SQLException e) {
//                 e.printStackTrace();
//             }
//
//         }).start();

    }



}
