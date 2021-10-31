package com.hale.job.join

import org.apache.flink.api.common.functions.CoGroupFunction
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.util.Collector

import java.lang


/**
 * @ClassName: CoprocessJoin
 * @Author: haleli
 * @Date: 12:20
 * @ProjectName: Flink-Think
 * @Description: ${Desc}
 * @Version: ${Version}
 * */
object CoprocessJoin {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val oneStream = env.fromElements(
      (1, 1 * 1000),
      (2, 2 * 1000))
      .assignAscendingTimestamps(_._2)

    val otherStream = env.fromElements(
      (1, 1 * 1000 + 1),
      (2, 1 * 1000 + 2),
      (3, 8 * 1000 + 1))
      .assignAscendingTimestamps(_._2)

    oneStream.coGroup(otherStream)
      .where(_._1)
      .equalTo(_._1)
      .window(TumblingEventTimeWindows.of(Time.seconds(2)))
      /*.trigger()
      .evictor()*/
      .apply(new MyCoGroup)
      .print()


    env.execute(this.getClass.getSimpleName)
  }

  class MyCoGroup extends CoGroupFunction[(Int, Int), (Int, Int), (Int, Int, Int, Int)] {
    override def coGroup(iterable: lang.Iterable[(Int, Int)], iterable1: lang.Iterable[(Int, Int)], collector: Collector[(Int, Int, Int, Int)]): Unit = {
      iterable.forEach(x => {
        iterable1.forEach(y => {
          //两边的流join上了
          collector.collect(x._1, x._2, y._1, y._2)
        })
        //两边的流没有join上
        collector.collect(x._1, x._2, -1, -1)
      })
    }
  }


}
