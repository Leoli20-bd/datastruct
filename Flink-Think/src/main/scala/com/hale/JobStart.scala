package com.hale

import com.hale.comm.BaseEnvironment
import com.hale.utils.ParameterUtils
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

import java.util

/**
 * @ClassName: JobStart
 * @Author: haleli
 * @Date: 23:24
 * @ProjectName: Flink-Think
 * @Description: ${Desc}
 * @Version: ${Version}
 * */
object JobStart extends BaseEnvironment{
  def main(args: Array[String]): Unit = {
    val path = "commonConfig.yaml"

    val map: util.HashMap[String, String] = ParameterUtils.init(path)
    val tool: ParameterTool = ParameterTool.fromMap(map)
    print(tool.toMap)

    val env: StreamExecutionEnvironment = super.init(tool)
    execute(tool,env)



  }

  override def execute(tool: ParameterTool, env: StreamExecutionEnvironment): Unit = {
    env.socketTextStream("localhost", 9999)
      .flatMap(_.split(","))
      .map(x => {
        if (x.equalsIgnoreCase("error")) {
          throw new Exception("error msg")
        }
        (x, 1)
      })
      .keyBy(_._1)
      .sum(1)
      .print("wc : ")
      .setParallelism(1)

    env.execute("wc")
  }
}
