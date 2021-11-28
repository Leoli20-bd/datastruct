package com.hale.job.state

import org.apache.flink.api.common.functions.{RichMapFunction, RichReduceFunction}
import org.apache.flink.api.common.state.{ReducingState, ReducingStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

/**
 * @ClassName: JobReduceState
 * @Author: haleli
 * @Date: 22:19
 * @ProjectName: Flink-Think
 * @Description: ${Desc}
 * @Version: ${Version}
 * */
object JobReduceState {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment


    val sourceDS: DataStream[String] = env.fromElements("a", "b", "c", "d", "e", "f", "a", "c", "e")

    sourceDS.map(x => (x, 1))
      .keyBy(x => x._1)
      .map(new MyReducingState)
      .print("")
      .setParallelism(1)


    env.execute(this.getClass.getSimpleName)

  }

}

class MyReducingState extends RichMapFunction[(String, Int), (String, Int)] {
  var state: ReducingState[(String, Int)] = _

  override def open(parameters: Configuration): Unit = {
    state = getRuntimeContext.getReducingState(new ReducingStateDescriptor[(String, Int)]("state", (x1, x2) => {
      (x1._1, x1._2 + x2._2)
    }, createTypeInformation[(String, Int)]))
  }

  override def map(in: (String, Int)): (String, Int) = {
    state.add(in)
    state.get()
  }
}
