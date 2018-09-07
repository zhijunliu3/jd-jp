package com.ebills.akka

import akka.actor.{Actor, ActorSystem, Props}
import com.ebills.message.{CheckHeartbeat, HeartbeatInfo, MaskerInfo, RegisterMessage}
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._
import scala.collection.mutable

class Master(host:String, port:Int) extends Actor{

  val workerMap = new mutable.HashMap[String,RegisterMessage]()

  val workerSet = new mutable.HashSet[RegisterMessage]()

  val CHECK_INTERVAL = 15000

  override def preStart(): Unit = {
    println("preStart invoker")
    import context.dispatcher
    context.system.scheduler.schedule(0 millis,CHECK_INTERVAL millis, self, CheckHeartbeat)
  }

  override def receive: Receive = {
    case RegisterMessage(id, memory, cores) => {
      println("connector invoker")
      if(!workerMap.contains(id)){
        val registerMessage = RegisterMessage(id, memory, cores)
        workerMap(id) = registerMessage
        workerSet += registerMessage
        sender ! MaskerInfo(s"akka.tcp://MasterSystem@$host:$port/user/Master")
      }
    }
    case HeartbeatInfo(id) => {
      if(workerMap.contains(id)){
        val currentTime = System.currentTimeMillis()
        workerMap(id).currentTime = currentTime
      }
    }
    case CheckHeartbeat => {
      val currentTime = System.currentTimeMillis()
      val removeWorker = workerSet.filter(currentTime - _.currentTime >CHECK_INTERVAL)
      for(x <- removeWorker){
        workerSet -= x
        workerMap -= x.id
      }
      println(workerSet.size)
    }
  }
}

object Master{
  def main(args: Array[String]): Unit = {
    val host = args(0)
    val port = args(1).toInt
    val confStr =
      s"""
        |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
        |akka.remote.netty.tcp.hostname = "$host"
        |akka.remote.netty.tcp.port = "$port"
      """.stripMargin
    val config = ConfigFactory.parseString(confStr)
    val actorSystem = ActorSystem.create("MasterSystem",config)
    val master = actorSystem.actorOf(Props{new Master(host, port)},"Master")
    master ! "hello"
  }
}


