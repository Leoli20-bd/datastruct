package utils

import com.hale.utils.ParameterUtils
import org.scalatest.funsuite.AnyFunSuite

import java.util


/**
 * @ClassName: UtilsTest
 * @Author: haleli
 * @Date: 23:53
 * @ProjectName: Flink-Think
 * @Description: ${Desc}
 * @Version: ${Version}
 * */
class UtilsTest extends AnyFunSuite   {
  test("utils"){
    val map: util.HashMap[String, String] = ParameterUtils.init("commonConfig.yaml")
    print(map)
  }

}
