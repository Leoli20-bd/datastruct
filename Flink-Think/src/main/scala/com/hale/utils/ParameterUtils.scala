package com.hale.utils

import org.apache.commons.lang3.StringUtils
import org.slf4j.{Logger, LoggerFactory}
import org.yaml.snakeyaml.Yaml

import java.io.InputStream
import java.util

/**
 * @ClassName: ParameterUtils
 * @Author: haleli
 * @Date: 23:32
 * @ProjectName: Flink-Think
 * @Description: ${Desc}
 * @Version: ${Version}
 * */
object ParameterUtils {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  var map: util.HashMap[String, String] = _

  //if map is null ,init map
  def init(configPath: String): util.HashMap[String, String] = {
    var in: InputStream = null
    try {
      if (configPath == null || StringUtils.isBlank(configPath)) {
        throw new Exception(s"config path is null")
      }

      if (map == null) {
        in = this.getClass.getClassLoader.getResourceAsStream(configPath)
        val yaml = new Yaml()
        map = yaml.loadAs(in, classOf[util.HashMap[String, String]])
      }

    } catch {
      case e: Exception => e.printStackTrace()
      case _ => logger.error(s"parse config exception")
    } finally {
      if (in != null) {
        in.close()
      }
    }
    map
  }
}
