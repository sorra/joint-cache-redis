# Joint Cache Redis

### In your busy server cluster, remote caches like Redis are not fast enough.
### You have local caches (maybe Guava) to make hot data faster but less consistent.
### Now connect all local caches together so that they can share cache updates!

This project shows you how to do this via **Redis pub-sub**!

## Run the demo
1. Start your Redis at default port 6379
2. Run shell command `./gradlew shadowJar`
3. Run shell command `java -jar build/libs/joint-cache-redis-1.0-SNAPSHOT-all.jar`
4. Things are printed in the shell console
5. Ctrl+C (or Cmd+C) to shut it down

## Tips:
- You can use the `monitor` command in redis-cli to watch what happened to Redis.
- Throttling may be needed in big applications.
- Kafka is better here because messages can be consumed by all subscribers.
[It requires more work in Redis](http://blog.radiant3.ca/2013/01/03/reliable-delivery-message-queues-with-redis/)
