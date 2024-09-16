package com.zhang.recommendation_system.util.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.springframework.stereotype.Component;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import java.util.Properties;
import scala.Tuple2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class Sparkdao {
    //配置数据库
    private final String DB_URL = "jdbc:mysql://localhost:3306/music_recommend?serverTimezone=Asia/Shanghai";
    private final String DB_USERNAME = "root";
    private final String DB_PASSWORD = "zssyqpgdll88990";

    private SparkConf sparkConf;
    private JavaSparkContext javaSparkContext;
    private SQLContext sqlContext;

    //获取数据库参数
    public Properties getDBProperties(){
        Properties props = new Properties();
        props.put("user", DB_USERNAME);
        props.put("password", DB_PASSWORD);
        return props;
    }

    /*
     *   初始化Load
     *   创建sparkContext, sqlContext
     * */
    public  Sparkdao() {
        initSparckContext();
        initSQLContext();
    }

    /*
     *   创建sparkContext
     * */
    private void initSparckContext() {
        //获取数据存放位置
        String warehouseLocation = System.getProperty("user.dir");
        sparkConf = new SparkConf()
                .setAppName("from-to-mysql")//设置spark应用程序名称
                .set("spark.sql.warehouse.dir", warehouseLocation)//指定数据的存放位置
                .setMaster("local");//本地工作,使用本机的所有处理器创建工作节点
        //任何Spark程序都是SparkContext开始的，SparkContext的初始化需要一个SparkConf对象，SparkConf包含了Spark集群配置的各种参数
        javaSparkContext = new JavaSparkContext(sparkConf);
    }

    /*
     *   创建sqlContext
     *   用于读写MySQL中的数据
     * */
    private void initSQLContext() {
        sqlContext = new SQLContext(javaSparkContext);
    }

    //利用spark计算表的数量
    public Integer count(String table) {
        //使用参数Properties写好的数据库的用户名和密码去连接并读取数据库中的table表,返回结果是DataFrame类型
        System.out.println("查询: " + table);
        DataFrame rows = sqlContext.read().jdbc(DB_URL, table, getDBProperties()).where("1=1");
        JavaRDD<Row> testRdd= rows.toJavaRDD();//DataFrame类型转为JavaRDD
        return testRdd.collect().size();//collect()查询是一个行动操作，将RDD类型的数据转化为数组，同时会从spark集群即本地拉取数据到driver端
    }

    public Integer countLog(String type) {
        //读取日志表中指定的类型,比如登陆、播放、收藏等等
        DataFrame rows = sqlContext.read().jdbc(DB_URL, "tb_log", getDBProperties()).
                where("opt ='" + type + "'");
        JavaRDD<Row> testRdd= rows.toJavaRDD();
        return testRdd.collect().size();
    }
}
