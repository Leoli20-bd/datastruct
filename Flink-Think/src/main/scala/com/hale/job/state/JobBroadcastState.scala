package com.hale.job.state

import com.hale.pojo.IntervalJoinExample.{ActionLog, LocaltionInfo}
import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.api.common.state.{MapStateDescriptor, ReadOnlyBroadcastState}
import org.apache.flink.streaming.api.datastream.BroadcastStream
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.scala._


/**
 * @ClassName: JobBroadcastState
 * @Author: haleli
 * @Date: 21:50
 * @ProjectName: Flink-Think
 * @Description: 广播状态，可能出现广播之前连接到数据，导致无法获取到状态
 * @Version: ${Version}
 * */
object JobBroadcastState {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    env.setRestartStrategy(
      RestartStrategies.fixedDelayRestart(5, 3000L))

    val localDesc = new MapStateDescriptor[String, LocaltionInfo]("localtioninfo", createTypeInformation[String], createTypeInformation[LocaltionInfo])

    val localInfoDS: DataStream[LocaltionInfo] = env.fromElements(
      LocaltionInfo("10001", "湖北省", "武汉市", "中国", "中国湖北武汉"),
      LocaltionInfo("10002", "湖北省", "黄冈市", "中国", "中国湖北黄冈"),
      LocaltionInfo("10003", "湖北省", "荆州市", "中国", "中国湖北荆州"),
      LocaltionInfo("20001", "上海市", "浦东新区", "中国", "中国上海浦东"),
      LocaltionInfo("20002", "上海市", "普陀区", "中国", "中国上海普陀"),
      LocaltionInfo("30001", "北京市", "朝阳区", "中国", "中国北京朝阳")

    ).setParallelism(1)


    val localBroadcastBS: BroadcastStream[LocaltionInfo] = localInfoDS.broadcast(localDesc)

    /**
     * jack_10d01, 10001
     * fafa_d001, 10002
     * rose_100d1, 10003
     * cdaf_1cd001, 20001
     * cdk_10s01, 20002
     * ck_10d01, 30001
     */
    /* val actionDS: DataStream[ActionLog] = env.socketTextStream("localhost", 9999)
       .map(x => {
         val sp: Array[String] = x.split(",")
         ActionLog(sp(0), sp(1))
       })*/


    val actionDS: DataStream[ActionLog] = env.readTextFile("/Users/haleli/myworld/test_data/action.txt")
      .map(x => {
        val sp: Array[String] = x.split(",")
        ActionLog(sp(0), sp(1))
      })
      .rebalance


    actionDS.connect(localBroadcastBS)
      .process(new MyBraodcastProcessFunction)
      .print("")

    env.execute(this.getClass.getSimpleName)

  }


}

class MyBraodcastProcessFunction extends BroadcastProcessFunction[ActionLog, LocaltionInfo, (String, LocaltionInfo)] {
  val localDesc = new MapStateDescriptor[String, LocaltionInfo]("localtioninfo", createTypeInformation[String], createTypeInformation[LocaltionInfo])

  override def processBroadcastElement(in2: LocaltionInfo, context: BroadcastProcessFunction[ActionLog, LocaltionInfo, (String, LocaltionInfo)]#Context, collector: Collector[(String, LocaltionInfo)]): Unit = {
    context.getBroadcastState(localDesc).put(in2.localId, in2)

  }


  override def processElement(in1: ActionLog, readOnlyContext: BroadcastProcessFunction[ActionLog, LocaltionInfo, (String, LocaltionInfo)]#ReadOnlyContext, collector: Collector[(String, LocaltionInfo)]): Unit = {
    val state: ReadOnlyBroadcastState[String, LocaltionInfo] = readOnlyContext.getBroadcastState(localDesc)
    if (state.contains(in1.localId)) {
      collector.collect(in1.userId, state.get(in1.localId))
    } else {
      collector.collect(in1.userId, null)
    }

  }
}

