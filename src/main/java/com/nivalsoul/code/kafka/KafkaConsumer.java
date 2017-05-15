package com.nivalsoul.code.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

public class KafkaConsumer {
	private static final String TOPIC = "hh01160951GAYS_KAKOUXINXI";
	private final ConsumerConnector consumer;

    private KafkaConsumer() {
        Properties props = new Properties();
        //zookeeper 配置
//        props.put("zookeeper.connect", "172.16.50.21:12181");
        props.put("zookeeper.connect", "172.16.80.70:2181,172.16.80.71:2181,172.16.80.72:2181");

        //group 代表一个消费组,必须要使用别的组名称， 如果生产者和消费者都在同一组，则不能访问同一组内的topic数据  
        props.put("group.id", "test-group7");

        //zk连接超时
        props.put("zookeeper.session.timeout.ms", "4000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "smallest");
        //序列化类
        props.put("serializer.class", "kafka.serializer.StringEncoder");

        ConsumerConfig config = new ConsumerConfig(props);

        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
    }

    void consume() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(TOPIC, new Integer(1));// 一次从主题中获取一个数据

        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());

        Map<String, List<KafkaStream<String, String>>> consumerMap = 
                consumer.createMessageStreams(topicCountMap,keyDecoder,valueDecoder);
        KafkaStream<String, String> stream = consumerMap.get(TOPIC).get(0);
        ConsumerIterator<String, String> it = stream.iterator();
        int i = 0;
        while (it.hasNext()) {
			MessageAndMetadata<String, String> r = it.next();
			i++;
			if(i%100 == 0){
				System.out.println(r.key() + "==" + r.message());
			}
		}
        System.out.println("total:"+ i);
    }

    public static void main(String[] args) {
        new KafkaConsumer().consume();
    }
}
