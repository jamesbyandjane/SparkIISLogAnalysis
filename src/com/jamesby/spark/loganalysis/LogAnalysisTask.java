package com.jamesby.spark.loganalysis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;

import com.jamesby.spark.utils.SparkConstants;

import kafka.serializer.StringDecoder;
import scala.Tuple2;

public class LogAnalysisTask implements java.io.Serializable{
	private static final long serialVersionUID = 1L;

	public void analysis() throws InterruptedException {
	    SparkConf sparkConf = new SparkConf().setAppName("LogAnalysisTask");
	    sparkConf.setMaster(SparkConstants._MASTER);
	    JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(2));
	    jssc.checkpoint("hdfs://hdfs1:9000/spark/checkpointiis");
	    //jssc.checkpoint("hdfs://hdfs1:9000/spark/checkpointtest");
	     
	    HashSet<String> _TopicsSet = new HashSet<String>();
	    _TopicsSet.add("topiciislog");//可支持多个Topic
	    //_TopicsSet.add("topictest1");//可支持多个Topic

	    Map<String, String> _KafkaParams = new HashMap<String, String>();
	    _KafkaParams.put("metadata.broker.list", "server2:9092");//支持多个broker
	    _KafkaParams.put("group.id", "iislogspark");
	    _KafkaParams.put("fetch.message.max.bytes", "5120000");
	    //_KafkaParams.put("auto.offset.reset", "smallest");//smallest and largest
    
	    
	    JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(
	            jssc,
	            String.class,
	            String.class,
	            StringDecoder.class,
	            StringDecoder.class,
	            _KafkaParams,
	            _TopicsSet
	        );
	    
	    JavaDStream<IISLogEvent> eventList = messages.map(new Function<Tuple2<String, String>, IISLogEvent>() {
			private static final long serialVersionUID = 1L;
			@Override
	        public IISLogEvent call(Tuple2<String, String> tuple2) {
	          String[] array = tuple2._2().split(" ");
	          
	          IISLogEvent event = new IISLogEvent();
	          if (array!=null && array.length>=14) {
		          event.setDate(array[0]); 
		          event.setTime(array[0]+" "+array[1].substring(0, 2)); //统计
		          event.setsIp(array[2]);
		          event.setCsMethod(array[3]);
		          event.setCsUriStem(array[4]);//统计
		          event.setCsUriQuery(array[5]); 
		          event.setsPort(array[6]); 
		          event.setCsUsername(array[7]); 
		          event.setcIp(array[8]);//统计
		          event.setCsUserAgent(array[9]); 
		          event.setCsReferer(array[10]); 
		          event.setScStatus(array[11]); //统计
		          event.setScSubstatus(array[12]); 
		          event.setScWin32Status(array[13]); 
		          event.setTimeTaken(array[14]);
	          }else {
	        	  event.setTime("NONE");
	        	  event.setCsUriStem("NONE");
	        	  event.setScStatus("NONE");
	          }
	          return event;
	        }
	      });

	    JavaPairDStream<Tuple2<String,String>, Integer> ipKeyPairList = eventList.mapToPair(new PairFunction<IISLogEvent,Tuple2<String,String>,Integer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<Tuple2<String,String>, Integer> call(IISLogEvent t) throws Exception {
				Tuple2<String,String> key = new Tuple2<String,String>(t.getTime(),t.getcIp());
				return new Tuple2<Tuple2<String,String>, Integer>(key,1);
			}
	    	
	    });
	    
	    JavaPairDStream<Tuple2<String,String>, Integer> urlKeyPairList = eventList.mapToPair(new PairFunction<IISLogEvent,Tuple2<String,String>,Integer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<Tuple2<String,String>, Integer> call(IISLogEvent t) throws Exception {
				Tuple2<String,String> key = new Tuple2<String,String>(t.getTime(),t.getCsUriStem());
				return new Tuple2<Tuple2<String,String>, Integer>(key,1);
			}
	    	
	    });
	    
	    JavaPairDStream<Tuple2<String,Tuple2<String,String>>, Integer> statusKeyPairList = eventList.mapToPair(new PairFunction<IISLogEvent,Tuple2<String,Tuple2<String,String>>,Integer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Tuple2<Tuple2<String,Tuple2<String,String>>, Integer> call(IISLogEvent t) throws Exception {
				Tuple2<String,Tuple2<String,String>> key = new Tuple2<String,Tuple2<String,String>>(t.getTime(),new Tuple2<String,String>(t.getcIp(),t.getScStatus()));
				return new Tuple2<Tuple2<String,Tuple2<String,String>>, Integer>(key,1);
			}
	    	
	    });	    
	    
	    ipKeyPairList.reduceByKeyAndWindow(new Function2<Integer,Integer,Integer>(){
			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1+v2;
			}
	    },Duration.apply(1000*10)).print();
	    
	    urlKeyPairList.reduceByKeyAndWindow(new Function2<Integer,Integer,Integer>(){
			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1+v2;
			}
	    },Duration.apply(1000*10)).print();
	    
	    statusKeyPairList.reduceByKeyAndWindow(new Function2<Integer,Integer,Integer>(){
			private static final long serialVersionUID = 1L;

			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1+v2;
			}
	    },Duration.apply(1000*10)).print();
	    
	    	
	    
	    jssc.start();
		jssc.awaitTermination();

	}
}
