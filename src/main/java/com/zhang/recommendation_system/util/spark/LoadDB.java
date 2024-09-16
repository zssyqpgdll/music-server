package com.zhang.recommendation_system.util.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.Properties;

public class LoadDB implements Serializable {

    private final String DB_URL = "jdbc:mysql://localhost:3306/music_rs?serverTimezone=Asia/Shanghai";
    private final String DB_USERNAME = "root";
    private final String DB_PASSWORD = "123456";

    private SparkConf sparkConf;
    private JavaSparkContext javaSparkContext;
//    private HiveContext hiveContext;
    private SQLContext sqlContext;

    /*
    *   初始化Load
    *   创建sparkContext, sqlContext, hiveContext
    * */
    public LoadDB() {
        initSparckContext();
        initSQLContext();
//        initHiveContext();
    }

    /*
    *   创建sparkContext
    * */
    private void initSparckContext() {
        String warehouseLocation = System.getProperty("user.dir");
        sparkConf = new SparkConf()
                .setAppName("from-to-mysql")
                .set("spark.sql.warehouse.dir", warehouseLocation)
                .setMaster("local");
//                        .setMaster("yarn-client");
        javaSparkContext = new JavaSparkContext(sparkConf);
    }

    /*
    *   创建hiveContext
    *   用于读取Hive中的数据
    * */
//    private void initHiveContext() {
//        hiveContext = new HiveContext(javaSparkContext);
//    }

    /*
    *   创建sqlContext
    *   用于读写MySQL中的数据
    * */
    private void initSQLContext() {
        sqlContext = new SQLContext(javaSparkContext);
    }

    /*
    *   使用spark-sql从hive中读取数据, 然后写入mysql对应表.
    * */
//    public void hive2db() {
//        String url = "jdbc:mysql://10.93.84.53:3306/big_data?characterEncoding=UTF-8";
//        String table = "accounts";
//        Properties props = new Properties();
//        props.put("user", "root");
//        props.put("password", "1234");
//        String query = "select * from gulfstream_test.accounts where year=2017 and month=10 and day=23";
//        DataFrame rows = hiveContext.sql(query).select("id", "order_id", "status", "count");;
//        rows.write().mode(SaveMode.Append).jdbc(url, table, props);
//    }

    public Properties getDBProperties(){
        Properties props = new Properties();
        props.put("user", DB_USERNAME);
        props.put("password", DB_PASSWORD);
        return props;
    }

    /*
    *   使用spark-sql从db中读取数据, 处理后再回写到db
    * */
    public void db2db() {
        String fromTable = "singer";
//        String toTable = "accountsPart";

        DataFrame rows = sqlContext.read().jdbc(DB_URL, fromTable, getDBProperties()).where("1=1");
//        DataFrame rows = sqlContext.sql("select  *  from  cemap.member");
        JavaRDD<Row> testRdd= rows.toJavaRDD();
        System.out.println(rows.toJavaRDD());
//        rows.write().mode(SaveMode.Append).jdbc(url, toTable, props);
        System.out.println(testRdd.collect().size());
//        testRdd.foreach(element -> {
//            System.out.println(element.toString());
//            }
//        );
    }

    public static void main(String[] args) {
//        LoadDB loadDB = new LoadDB();
        System.out.println(" ---------------------- start hive2db ------------------------");
        System.out.println(" ---------------------- finish hive2db ------------------------");
        System.out.println(" ---------------------- start db2db ------------------------");
//        loadDB.db2db();

        Sparkdao dao = new Sparkdao();
        Integer singers = dao.count("singer");
        System.out.println("singers :" + singers);
        Integer singers2 = dao.count("singer");
        System.out.println("singers2 :" + singers2);
        System.out.println(" ---------------------- finish db2db ------------------------");
    }
}