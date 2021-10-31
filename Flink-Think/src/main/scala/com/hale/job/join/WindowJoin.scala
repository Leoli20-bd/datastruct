package com.hale.job.join

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

/**
 * @ClassName: WindowJoin
 * @Author: haleli
 * @Date: 11:40
 * @ProjectName: Flink-Think
 * @Description: 将两条输入流的元素分配到公共窗口中，并在窗口中完成join
 * @Version: ${Version}
 * */
object WindowJoin {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val oneStream = env.fromElements(
      (1, 1*1000),
      (2, 2*1000))
      .assignAscendingTimestamps(_._2)

    val otherStream = env.fromElements(
      (1, 1*1000+1),
      (2, 1*1000+2),
      (3, 3*1000+1))
      .assignAscendingTimestamps(_._2)

    oneStream.join(otherStream)
      .where(_._1)
      .equalTo(_._1)
      .window(TumblingEventTimeWindows.of(Time.seconds(3)))
      /*.trigger()
      .evictor()*/
      .apply((e1, e2) => {
        s"$e1 ****> $e2"
      })
      .print()


    env.execute(this.getClass.getSimpleName)
  }

}
