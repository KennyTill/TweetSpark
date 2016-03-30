import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.StreamingContext._
import HelperMethods._
/**
  * Created on 3/29/16.
  */
object TweetSpark {
  def main(args: Array[String]): Unit = {

    //need spark url for master of cluster
    val sparkUrl = HelperMethods.getSparkURL()
    val sparkConfig = new SparkConf().setMaster(sparkUrl).setAppName("TwitterSpark")
    val scc = new StreamingContext(sparkConfig, Seconds(10))
    val stream = TwitterUtils.createStream(scc, None)

  }
}
