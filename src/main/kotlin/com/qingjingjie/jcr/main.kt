package com.qingjingjie.jcr

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import redis.clients.jedis.Jedis

val key = "key"
val localCache: Cache<String, Any> = CacheBuilder.newBuilder().build()

fun main(args: Array<String>) {
  val jedis1 = Utils.newJedis()
  val jedis2 = Utils.newJedis()

  testSync(jedis1, jedis2)
}

fun testSync(subJedis: Jedis, pubJedis: Jedis) {
  localCache.put(key, "A")

  println("Connect the sync channel...")
  val listener = SyncListener(localCache)
  Thread {
    subJedis.subscribe(listener, Utils.CHANNEL)
  }.start()

  println("Wait for sync being connected...")
  Utils.waitPatiently {
    isSyncConnected.get()
  }
  pubJedis.publish(Utils.CHANNEL, key + "=" + "B")
  println("\nPublished the cache change!")

  Utils.waitPatiently {
    localCache.getIfPresent(key) == "B"
  }
  println("\nSynced the cache change!")

  println()
  System.out.flush()
}