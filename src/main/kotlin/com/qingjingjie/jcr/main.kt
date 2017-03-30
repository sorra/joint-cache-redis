package com.qingjingjie.jcr

import com.google.common.cache.CacheBuilder
import redis.clients.jedis.Jedis
import java.util.concurrent.atomic.AtomicBoolean

val CHAN_SYNC = "sync"
val key = "key"

val localCache = CacheBuilder.newBuilder().build<String, Any>()
val isSyncConnected = AtomicBoolean(false)

fun main(args: Array<String>) {
  val jedis1 = Jedis("localhost")

  jedis1.set(key, "bar")
  val value = jedis1.get("key")
  println("K=${key}, V=${value}")

  val jedis2 = Jedis("localhost")

  testSync(jedis1, jedis2)
}

fun testSync(subJedis: Jedis, pubJedis: Jedis) {
  localCache.put(key, 1)

  println("Connect the sync channel...")
  val listener = SyncListener(localCache)
  Thread {
    subJedis.subscribe(listener, CHAN_SYNC)
  }.start()

  println("Wait for sync being connected...")
  waitPatiently {
    !isSyncConnected.get()
  }
  pubJedis.publish(CHAN_SYNC, key)
  println("\nPublished the cache change!")

  // Wait for and display the sync
  waitPatiently {
    localCache.getIfPresent(key) != null
  }
  println("\nSynced the cache change!")
}

private fun waitPatiently(needWait: () -> Boolean) {
  var i = 0
  while (needWait()) {
    Thread.sleep(100)
    print(".")
    i++
    if (i > 100) {
      i = 0
      println()
    }
  }
}