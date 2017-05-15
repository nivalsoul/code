package com.nivalsoul.code.kafka;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import kafka.javaapi.producer.Producer;
import kafka.message.CompressionCodec;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.DefaultEncoder;
import kafka.serializer.StringEncoder;
import oracle.net.aso.r;

public class KafkaProducer {
	private final Producer<Object, Object> producer;
    public final static String TOPIC = "mytopic";

    private KafkaProducer(){
        Properties props = new Properties();
        //此处配置的是kafka的端口
        props.put("metadata.broker.list", "172.16.80.70:19092,172.16.80.71:19092,172.16.80.72:19092");

        //配置value的序列化类
        //props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("serializer.class", DefaultEncoder.class.getName());
        //配置key的序列化类
        props.put("key.serializer.class", "kafka.serializer.DefaultEncoder");
        //指定压缩格式：默认0表示不压缩，1表示用gzip压缩，2表示用snappy压缩
        props.put("compression.codec", "0");

        //request.required.acks
        //0, which means that the producer never waits for an acknowledgement from the broker (the same behavior as 0.7). This option provides the lowest latency but the weakest durability guarantees (some data will be lost when a server fails).
        //1, which means that the producer gets an acknowledgement after the leader replica has received the data. This option provides better durability as the client waits until the server acknowledges the request as successful (only messages that were written to the now-dead leader but not yet replicated will be lost).
        //-1, which means that the producer gets an acknowledgement after all in-sync replicas have received the data. This option provides the best durability, we guarantee that no messages will be lost as long as at least one in sync replica remains.
        props.put("request.required.acks","1");

        producer = new Producer<Object, Object>(new ProducerConfig(props));
    }

    void produce() {
        int messageNo = 1;
        final int COUNT = 10;

        while (messageNo < COUNT) {
        	Object key = messageNo;
        	Object data = "hello kafka message " + key;
        	data = data.toString().getBytes();
            producer.send(new KeyedMessage<Object, Object>(TOPIC, key.toString().getBytes() ,data));
            System.out.println(data);
            messageNo ++;
        }
    	
    	/*String info = "172.16.50.12:1521:ORCL";
		String user = "GA_TESTER1";//GA_TESTER1
		String password = "123456";//123456
		String sql = "select * from ORDERS_150 where rownum<100001";
		
		Connection con = null;
	    ResultSet result = null;
	    try
	    {
	    	long start = System.currentTimeMillis();
	        Class.forName("oracle.jdbc.driver.OracleDriver");
	        System.out.println("开始尝试连接数据库: "+info);
	        String url = "jdbc:oracle:thin:@"+info;
	        con = DriverManager.getConnection(url, user, password);
	        Statement st = con.createStatement();
	        System.out.println("连接成功！");
	        
	        result = st.executeQuery(sql);
	        ResultSetMetaData rsmd = result.getMetaData();
	        int nrCols = rsmd.getColumnCount();
	        List<KeyedMessage<Object, Object>> list = Lists.newArrayList();
	        int batchSize = 50000;
	        while (result.next()){
	        	Map<String, Object> row = Maps.newHashMap();
	        	for (int i = 1; i <= nrCols; i++) {
	        		row.put(rsmd.getColumnName(i), result.getObject(i));
	        	}
	        	KeyedMessage<Object, Object> msg = new KeyedMessage<Object, Object>(TOPIC, 
	        			JSON.toJSONString(row).getBytes());
	        	if(list.size()==batchSize){
        			producer.send(list);
        			list.clear();
        		}
	        	list.add(msg);
	        }
	        if(list.size()>0){
    			producer.send(list);
    			list.clear();
    		}
	        System.out.println(System.currentTimeMillis()-start);
	            
	    }catch (Exception e){
	        e.printStackTrace();
	    }*/
    }

    public static void main( String[] args )
    {
        new KafkaProducer().produce();
    }
}
