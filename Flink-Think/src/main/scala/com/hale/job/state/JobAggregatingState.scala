package com.hale.job.state

import org.apache.flink.api.common.functions.{AggregateFunction, RichMapFunction}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.api.common.state.{AggregatingState, AggregatingStateDescriptor}

/**
 * @ClassName: JobAggregatingState
 * @Author: haleli
 * @Date: 23:18
 * @ProjectName: Flink-Think
 * @Description: ${Desc}
 * @Version: ${Version}
 * */
object JobAggregatingState {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val sourceDS: DataStream[String] = env.fromElements("a", "b", "c", "d", "e", "f", "b", "d", "f")

    sourceDS.map(x => (x, 1))
      .keyBy(x=>x._1)
      .map(new MyAggregatingState)
      .print()
      .setParallelism(1)


  }

}

class MyAggregatingState extends RichMapFunction[(String, Int), (String, Int)] {

  var state: AggregatingState[(String, Int), (String, Int)] = _

  override def open(parameters: Configuration): Unit = {
    val desc = new AggregatingStateDescriptor[(String, Int), Int, (String, Int)]("state", new MyAggregateMethod, createTypeInformation[Int])
    state = getRuntimeContext.getAggregatingState(desc)

  }

  override def map(in: (String, Int)): (String, Int) = {
    state.add(in)
    state.get()
  }
}

class MyAggregateMethod extends AggregateFunction[(String, Int), Int, (String, Int)] {
  override def createAccumulator(): Int = 1

  override def add(in: (String, Int), acc: Int): Int = {
    in._2 + acc
  }

  override def getResult(acc: Int): (String, Int) = {
    ("", acc)
  }

  override def merge(acc: Int, acc1: Int): Int = {
    acc1 + acc
  }
}
