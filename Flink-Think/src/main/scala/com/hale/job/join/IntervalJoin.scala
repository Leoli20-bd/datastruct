package com.hale.job.join

import com.hale.pojo.IntervalJoinExample.{UserBrowseLog, UserClickLog}
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction
import org.apache.flink.streaming.api.scala.{KeyedStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

/**
 * @ClassName: IntervalJoin
 * @Author: haleli
 * @Date: 22:28
 * @ProjectName: Flink-Think
 * @Description: 基于时间的双流join
 * @Version: ${Version}
 *
 * */
object IntervalJoin {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment


    val clickLogDS: KeyedStream[UserClickLog, String] = env.fromElements(
      UserClickLog("jack", "1500", "click", "page_1"),
      UserClickLog("jack", "2000", "click", "page_1"))
      .assignAscendingTimestamps(_.eventTime.toLong * 1000)
      .keyBy(_.userId)

    val browserLogDs: KeyedStream[UserBrowseLog, String] = env.fromElements(
      UserBrowseLog("jack", "1000", "browser", "product_1", "1"),
      UserBrowseLog("jack", "1500", "browser", "product_1", "1"),
      UserBrowseLog("jack", "1501", "browser", "product_1", "1"),
      UserBrowseLog("jack", "2000", "browser", "product_1", "1"))
      .assignAscendingTimestamps(_.eventTime.toLong * 1000)
      .keyBy(_.userId)

    clickLogDS.intervalJoin(browserLogDs)
      .between(Time.minutes(-10), Time.minutes(0))
      .process(new MyIntervalJoin)
      .print()

    env.execute(this.getClass.getSimpleName)

  }

  class MyIntervalJoin extends ProcessJoinFunction[UserClickLog, UserBrowseLog, String] {
    override def processElement(in1: UserClickLog, in2: UserBrowseLog,
                                context: ProcessJoinFunction[UserClickLog, UserBrowseLog, String]#Context, collector: Collector[String]): Unit = {
      collector.collect(in1 + "==>" + in2)
    }

  }

}
