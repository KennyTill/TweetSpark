import org.apache.spark.streaming._
import org.apache.spark.storage.StorageLevel

import scala.io.Source
import scala.collection.mutable.HashMap
import java.io.{File, FileNotFoundException}

import org.apache.log4j.Logger
import org.apache.log4j.Level

import sys.process.stringSeqToProcess
/**
  * Created on 3/30/16.
  */
object HelperMethods {
  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
  Logger.getLogger("org.apache.spark.streaming.NetworkInputTracker").setLevel(Level.INFO)

  def configureTwitter(): Unit = {

    //define the file and attempt to access it
    val file = new File("configurationKeys.txt")
    if (!file.exists){
      throw new FileNotFoundException()("Twitter Configuration Not Found!")
    }

    //read in the file
    val lines = Source.fromFile(file.toString).getLines.filter(_.trim.size > 0 ).toSeq

    //map the lines provided in the configuration file
    val pairs = lines.map(line => {
      val splits = line.split("=")
      if (splits.size != 2){
        throw new Exception("Could not parse configuration file, line is incorrectly formatted [%s]".format(line))
      }
      //return to pairs here.
      (splits(0).trim, splits(1).trim)
    })

    //setup keys and read into system properties at runtime
    val map = new HashMap[String, String] ++= pairs
    val configurationKeys = Seq("consumerKey", "consumerSecret", "accessToken", "accessTokenSecret")
    println("Configuring Twitter API...")
    configurationKeys.foreach(key => {
      if (!map.contains(key)){
        throw new Exception("Could not set key for [%s]".format(key))
      }
      val fullKey = "twitter4j.oauth.%s".format(key)
      System.setProperty(fullKey, map(key))
      println("Key %s set as %s".format(fullKey,map(key)))
    })
    println()
  }


  //would typically be used for setting up a cluster location
  //this function is here for future use
  def getSparkURL(): String = {
    "Local[2]"
  }

}
