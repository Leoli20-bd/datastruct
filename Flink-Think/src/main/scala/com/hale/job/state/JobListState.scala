package com.hale.job.state

import org.apache.flink.api.common.functions.{RichMapFunction, RichReduceFunction}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}

/**
 * @ClassName: JobListState
 * @Author: haleli
 * @Date: 22:00
 * @ProjectName: Flink-Think
 * @Description: ${Desc}
 * @Version: ${Version}
 * */
object JobListState {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    env.fromElements("a", "b", "c", "d", "e", "f", "g", "aa", "bb", "cc","a","b")
      .map(x => (1, x))
      .keyBy(_._1)
      .map(new MyListState)
      .print()
      .setParallelism(1)

    env.execute(this.getClass.getSimpleName)


  }


}

class MyListState extends RichMapFunction[(Int, String), (String, Int)] {
  var state: ListState[String] = _


  override def open(parameters: Configuration): Unit = {
    state = getRuntimeContext.getListState(new ListStateDescriptor[String]("count_list", createTypeInformation[String]))
  }

  override def map(in: (Int, String)): (String, Int) = {
     state.add(in._2)

    var tmp = ""
    var count = 0;
    state.get().forEach(x => {
      tmp += x + "->"
      count += 1
    })

    (tmp, count)

  }
}
