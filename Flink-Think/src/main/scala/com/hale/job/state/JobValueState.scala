package com.hale.job.state

import org.apache.flink.api.common.functions.{RichMapFunction, RichReduceFunction}
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

    val sourceDS = env.fromElements(
      ("1", 1000.0, "done"),
      ("2", 1000.0, "done"),
      ("3", 4000.0, "done"),
      ("4", 2000.0, "done"),
      ("5", 9000.0, "done"),
      ("1", 1000.0, "cancel"),
    )

    sourceDS
      .keyBy(x => x._1)
      .reduce(new MyReduceState)
      .print("result")
      .setParallelism(1)

    env.execute(this.getClass.getSimpleName)
  }


}

class MyReduceState extends RichReduceFunction[(String, Double, String)] {
  var state: ValueState[(String, Double, String)] = _


  override def open(parameters: Configuration): Unit = {
    state = getRuntimeContext.getState(new ValueStateDescriptor[(String, Double, String)]("value", createTypeInformation[(String, Double, String)]))
  }

  override def reduce(t: (String, Double, String), t1: (String, Double, String)): (String, Double, String) = {
    var sum = t
    state.update(t1)
    if (state.value()._3.equals("done")) {
      sum = (t._1, t._2 + t1._2, t._3)
    } else if (state.value()._3.equals("cancel"))
    {
      sum = (t._1, t._2 - t1._2, t._3)
    }
    sum
  }
}
