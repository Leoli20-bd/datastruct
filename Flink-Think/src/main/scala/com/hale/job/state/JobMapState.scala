package com.hale.job.state

import com.hale.pojo.IntervalJoinExample.OrderList
import org.apache.flink.api.common.functions. RichMapFunction
import org.apache.flink.api.common.state.{MapState, MapStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._


/**
 * @ClassName: JobState
 * @Author: haleli
 * @Date: 19:10
 * @ProjectName: Flink-Think
 * @Description: 更加订单信息，实时计算当天的销售额
 * @Version: ${Version}
 * */
object JobMapState {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val sourceDS: DataStream[OrderList] = env.fromElements(
      OrderList("10001", "2021-11-08 10:23:05", 100.0, "Done"),
      OrderList("10002", "2021-11-08 10:23:05", 200.0, "Done"),
      OrderList("10003", "2021-11-08 10:23:05", 200.0, "Done"),
      OrderList("10001", "2021-11-08 10:23:05", 100.0, "Quite"),
      OrderList("10004", "2021-11-08 10:23:05", 400.0, "Done"),
      OrderList("10005", "2021-11-08 10:23:05", 200.0, "Done"),
      OrderList("10006", "2021-11-08 10:23:05", 500.2, "Pending")
    ).setParallelism(1)

    val value: KeyedStream[OrderList, String] = sourceDS.keyBy(_.orderTime.substring(0,10))

    value.map(new MyState)
      .print("sum : ")


    env.execute(this.getClass.getSimpleName)


  }

  class MyState extends RichMapFunction[OrderList, Double] {
    var state: MapState[String, Double] = _


    override def open(parameters: Configuration): Unit = {
      state = getRuntimeContext.getMapState(new MapStateDescriptor[String, Double]("sum", createTypeInformation[String],createTypeInformation[Double]))

    }

    override def map(in: OrderList): Double = {
      if(in.orderStatus.equals("Done")){
        state.put(in.orderId,in.amount)
      }else if(in.orderStatus.equals("Quite")){
        if(state.contains(in.orderId)){
          state.remove(in.orderId)
        }
      }

      var sum = 0.0
      state.values().forEach(x=>{
        sum += x
      })

      sum
    }
  }

}
