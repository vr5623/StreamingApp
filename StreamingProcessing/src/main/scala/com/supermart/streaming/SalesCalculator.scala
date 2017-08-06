package com.supermart.streaming

import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.functions.from_json
import org.apache.spark.sql.types._
import org.apache.spark.sql.types.FloatType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.streaming.Trigger


object SalesCalculator  {
    def main(args: Array[String]): Unit = {
      
     
     Logger.getLogger("org").setLevel(Level.ERROR)
     
     // Check if correct no of arguments are passed
     if (args.length < 4) {
        System.err.println("No of argumets must be 4 \nUsage:  <bootstrap-servers> " +
        "<subscribe-type> <topics> <Spark Master URL>")
        System.exit(1)
    }

    // Assign the values from the arguments
    val Array(bootstrapServers, subscribeType, topics, master) = args
     

         
    // Create or get an existing Spark Session object
     val spark = SparkSession
                 .builder()
                 .appName("SalesCalculator")
                 .master(master)
                 .getOrCreate()
      
      try {

          import spark.implicits._
      
            
          // Define the schema of the data obtained from the kafka topic to be parsed
          val schema = StructType(Seq(StructField("store_id",IntegerType), 
              StructField("receipt_id",IntegerType), 
              StructField("customer_id",IntegerType), StructField("items",ArrayType(
              StructType(Seq(StructField("item_id",IntegerType), 
              StructField("quantity",IntegerType),
              StructField("total_price_paid",FloatType) ))   
              ))))
              
              
      
          // Create DataSet representing the stream of sales records from kafka
          val df = spark
            .readStream
            .format("kafka")
            .option("kafka.bootstrap.servers", bootstrapServers)
            .option(subscribeType, topics)
            .load()
      
            // Extract the data from the topic which is now stored in data frame rows
            val rows = df.selectExpr("cast (value as string) as json").select(from_json($"json", schema=schema).as("data"))
            
            
            //Expand the array column items so that a row containing n items
            // will now be exploded to n rows
            // now there will be one to one mapping between the store id and each item sold
            val explodedRow = rows.withColumn("itemsdetail", explode($"data.items"))
            
            // Define aggregation to be performed which is total sales per store
            val tuple = explodedRow.groupBy("data.store_id").sum("itemsdetail.total_price_paid").as("sales_per_store")
            
            // start the query
            val query = tuple.writeStream
            .outputMode("complete")   // sales record of all the stores is displayed each time the query is executed
            .queryName("table")
            .format("console")    // print output to console
            .trigger(Trigger.ProcessingTime(30000))   // query is triggered after every 30 seconds
            .start()
      
            query.awaitTermination(86000000)  // stop querying almost after 24 hours from the start 
      }
      catch
      {
          case a: Exception => println("Session ended")
      }
      
      finally
      {
        spark.close()   // close the spark session
      }
    
}
}
