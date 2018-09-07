package com.ebills.akka

import java.util.UUID

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.ebills.message.{HeartbeatInfo, MaskerInfo, RegisterMessage, SendHeartbeat}
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._

class Worker(host:String, port:Int, memory:Int, cores:Int) extends Actor{

  val id = UUID.randomUUID().toString

  var master:ActorSelection = _

  val HEART_INTERVAL = 10000

  override def preStart(): Unit = {
    master = context.system.actorSelection(s"akka.tcp://MasterSystem@$host:$port/user/Master")
    master ! RegisterMessage(id, memory, cores)
  }

  override def receive: Receive = {
    case MaskerInfo(info) => {
      println(info)
      master = context.system.actorSelection(info)
      import context.dispatcher
      context.system.scheduler.schedule(0 millis,HEART_INTERVAL millis, self, SendHeartbeat)
    }
    case SendHeartbeat => {
      master ! HeartbeatInfo(id)
    }
  }
}

object Worker{
  def main(args: Array[String]): Unit = {
    val host = args(0)
    val port = args(1).toInt
    val serviceHost = args(2)
    val servicePort = args(3).toInt
    val memory = args(4).toInt
    val cores = args(5).toInt
    val confStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
      """.stripMargin
    val config = ConfigFactory.parseString(confStr)
    val actorSystem = ActorSystem.create("WorkerSystem",config)
    actorSystem.actorOf(Props{new Worker(serviceHost, servicePort, memory, cores)},"Worker")
  }
}
