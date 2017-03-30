package com.qingjingjie.jcr

import redis.clients.jedis.Jedis
import java.util.concurrent.atomic.AtomicBoolean


object Utils {
  val CHANNEL = "sync"
  val isSyncConnected = AtomicBoolean(false)

  fun newJedis(): Jedis {
    val jedis = Jedis("localhost")
    jedis.get(key)
    return jedis
  }

  fun waitPatiently(isComplete: Utils.() -> Boolean) {
    var i = 0
    while (!Utils.isComplete()) {
      Thread.sleep(100)
      print(".")
      i++
      if (i > 100) {
        i = 0
        println()
      }
    }
  }
}