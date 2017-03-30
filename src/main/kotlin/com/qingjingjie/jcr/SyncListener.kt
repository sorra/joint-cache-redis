package com.qingjingjie.jcr

import com.google.common.cache.Cache
import redis.clients.jedis.JedisPubSub


class SyncListener(val localCache: Cache<String, *>) : JedisPubSub() {

  override fun onMessage(channel: String, message: String) {
    if (channel == CHAN_SYNC) {
      val key = message
      localCache.invalidate(key)
    } else {
      println("onMessage got strange channel: ${channel}")
    }
  }

  override fun onSubscribe(channel: String, subscribedChannels: Int) {
    if (channel == CHAN_SYNC) {
      isSyncConnected.set(true)
    } else {
      println("onSubscribe got strange channel: ${channel}")
    }
  }

  override fun onUnsubscribe(channel: String, subscribedChannels: Int) {}

  override fun onPSubscribe(pattern: String, subscribedChannels: Int) {}

  override fun onPUnsubscribe(pattern: String, subscribedChannels: Int) {}

  override fun onPMessage(pattern: String, channel: String, message: String) {
  }
}
