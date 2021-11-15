package com.hale.comm

import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.environment.CheckpointConfig


/**
 * @ClassName: BaseEnvironment
 * @Author: haleli
 * @Date: 23:29
 * @ProjectName: Flink-Think
 * @Description: ${Desc}
 * @Version: ${Version}
 * */
trait BaseEnvironment {
  private val flink_restart_try_times = "flink.restart.try.times"
  private val flink_restart_interval = "flink.restart.interval"
  private val flink_checkpoint_interval = "flink.checkpoint.interval"
  private val flink_checkpoint_timeout = "flink.checkpoint.timeout"
  private val flink_checkpoint_MinPauseBetweenCheckpoints = "flink.checkpoint.MinPauseBetweenCheckpoints"
  private val flink_checkpoint_store_path = "flink.checkpoint.store.path"

  def init(tool: ParameterTool): StreamExecutionEnvironment = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    env.getConfig.setGlobalJobParameters(tool.mergeWith(ParameterTool.fromSystemProperties()))

    //set checkpoint config
    val config: CheckpointConfig = env.getCheckpointConfig
    config.setMaxConcurrentCheckpoints(1)
    config.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
    env.setStateBackend(new FsStateBackend(tool.get(flink_checkpoint_store_path, "file:///Users/haleli/myworld/data")))
    config.setCheckpointInterval(tool.get(flink_checkpoint_interval, "30000").toLong)
    config.setCheckpointTimeout(tool.get(flink_checkpoint_timeout, "30000").toLong)
    config.setMinPauseBetweenCheckpoints(0)

    //set restart stagy
    env.setRestartStrategy(
      RestartStrategies.fixedDelayRestart(tool.get(flink_restart_try_times, "5").toInt,
        tool.get(flink_restart_interval, "30000").toLong))

    env
  }

  def execute(tool: ParameterTool, env: StreamExecutionEnvironment)

}
