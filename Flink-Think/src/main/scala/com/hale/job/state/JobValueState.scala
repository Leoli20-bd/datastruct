package com.hale.job.state

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}

/**
 * @ClassName: JobValueState
 * @Author: haleli
 * @Date: 20:20
 * @ProjectName: Flink-Think
 * @Description: ${Desc}
 * @Version: ${Version}
 * */
object JobValueState {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val sourceDS: DataStream[String] = env.fromElements("a", "b", "c", "d", "e", "f", "g", "h", "a", "b")

    sourceDS
      .keyBy(x => x)
      .map(new MyMapState)
      .print("result : ")
      .setParallelism(1)


    env.execute(this.getClass.getSimpleName)
  }


}

class MyMapState extends RichMapFunction[String, String] {
  var state: ValueState[String] = _


  override def open(parameters: Configuration): Unit = {
    state = getRuntimeContext.getState(new ValueStateDescriptor[String]("value", createTypeInformation[String]))
  }

  override def map(in: String): String = {
   state.update(in)

    if (state.value().contains(in)) {
      s"$in : exit"
    } else {
      in
    }

  }
}
