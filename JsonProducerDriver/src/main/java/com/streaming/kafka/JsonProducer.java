package com.streaming.kafka;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.google.gson.Gson;

public class JsonProducer {
	
	
	
	public static void main(String[] args)throws Exception {
        
		
		if(args.length < 2)
		{
			System.err.println("Wrong no of arguments passed \nUsage:<kafka broker> <topicname> ");
			System.exit(1);
		}
		
        String topicName = args[1];

        System.out.println("Producer running");
        
        // map to hold the total sales per store
        Map<Integer, Float> mp = new HashMap<>();
        
        Random randomGenerator = new Random();

        int receiptno  = 1;

        //Configure the Producer
        Properties configProperties = new Properties();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,args[0]);
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.ByteArraySerializer");
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

        org.apache.kafka.clients.producer.Producer<String, String> producer = new KafkaProducer<String, String>(configProperties);

        Gson gson = new Gson();
        
        Thread.sleep(10000);

        	while(true)
        	{
        		// define item1 with random values
	            Item item1 = new Item();
	            item1.setItem_id(randomGenerator.nextInt(10));
	            item1.setQuantity(randomGenerator.nextInt(5));
	            item1.setTotal_price_paid(roundTwoDecimals(randomGenerator.nextFloat()*10));
	            
        		// define item2 with random values
	            Item item2 = new Item();
	            item2.setItem_id(randomGenerator.nextInt(10));
	            item2.setQuantity(randomGenerator.nextInt(5));
	            item2.setTotal_price_paid(roundTwoDecimals(randomGenerator.nextFloat()*10));
	            
	           
	            
	            List<Item> items = new ArrayList<Item>();
	            items.add(item1);
	            items.add(item2);
	            
	            // define receipt with random values
	            
	            Receipt receipt = new Receipt();
	            receipt.setCustomer_id(randomGenerator.nextInt(20));
	            receipt.setItems(items);
	            receipt.setReceipt_id(receiptno);
	            receiptno++;
	            // only 5 stores
	            receipt.setStore_id(randomGenerator.nextInt(5));
	            
	            
	            ProducerRecord<String, String> rec = new ProducerRecord<String, String>(topicName,gson.toJson(receipt));
	            // write receipt record to the topic
	            producer.send(rec);  
	            
	            Integer key = receipt.getStore_id();
	            Float val1 = receipt.getItems().get(0).getTotal_price_paid();
	            Float val2 = receipt.getItems().get(1).getTotal_price_paid();
	            
	            
	            if(mp.containsKey(key))
	            	mp.put(key, val1 + val2 + mp.get(key));
	            else
	            	mp.put(key, val1 + val2);
	            
	            System.out.println(receipt.getStore_id() + "\t" + receipt.getItems().get(0).getTotal_price_paid() + "\t" + receipt.getItems().get(1).getTotal_price_paid());
	            
	            // sleep for one sec after sending 1 record to the topic
	            Thread.sleep(1000);
	            
	            // producer sends max 100 records
	            if(receiptno == 100)
	            	break;
	            
        	}
        	
        	// only 5 stores are there
        	// print store and total sales data
        	// it should match the data produced by the spark streaming app
        	for(int i=0;i<=4;i++) {
        		
				System.out.println(i + "\t" + mp.get(i));
			}
        	
            producer.close();
            System.out.println("Producer closed");
    }
	
	 static float roundTwoDecimals(float d) {
		  DecimalFormat twoDForm = new DecimalFormat("#.##");
		  return Float.valueOf(twoDForm.format(d));
		}
}

