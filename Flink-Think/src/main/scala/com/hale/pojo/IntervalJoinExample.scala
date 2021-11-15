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

  //订单信息
  case class OrderList(orderId: String, orderTime: String, amount: Double, orderStatus: String)

  //地域信息
  case class LocaltionInfo(localId: String, province: String, city: String, county: String, name: String)

  //用户行为日志
  case class ActionLog(userId: String, localId: String)
}
