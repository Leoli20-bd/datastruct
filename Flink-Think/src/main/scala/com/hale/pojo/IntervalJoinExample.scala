package com.hale.pojo

/**
 * @ClassName: InterverJoinExample
 * @Author: haleli
 * @Date: 22:22
 * @ProjectName: Flink-Think
 * @Description: ${Desc}
 * @Version: ${Version}
 * */
object IntervalJoinExample {

  //浏览日志
  case class UserClickLog(userId: String, eventTime: String, eventType: String, pageId: String)

  //点击日志
  case class UserBrowseLog(userId: String, eventTime: String, eventType: String, productId: String, productType: String)


}
