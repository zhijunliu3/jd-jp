package com.ebills.message

trait SendMessage extends Serializable

case class RegisterMessage(id:String, memory:Int, cores:Int) extends SendMessage{
  var currentTime:Long = _
}

case class MaskerInfo(path:String) extends SendMessage

case object SendHeartbeat

case class HeartbeatInfo(id:String)

case object CheckHeartbeat