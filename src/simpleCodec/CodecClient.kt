package simpleCodec

import io.netty.bootstrap.Bootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel

/**
 * Created by yupenglei on 17/7/28.
 */
class CodecClient(private val host: String = "localhost", private val port: Int = 8080) {
    fun run() {
        val group = NioEventLoopGroup()
        try {
            val channel = Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel::class.java)
                    .remoteAddress(host, port)
                    .handler(ImportantDataCodecInitializer())
                    .connect().sync().channel()
//            (1..100)
//                    .asSequence()
//                    .forEach { channel.writeAndFlush(it).addListener { println(it.isSuccess) } }
            val message = CoolData(1, 2, 3)
            channel.writeAndFlush(message).addListener { println(it.isSuccess) }

            channel.writeAndFlush("hello").addListener { println(it.isSuccess) }

        } finally {
            group.shutdownGracefully().sync()
        }
    }
}

fun main(args: Array<String>) {
    CodecClient().run()
}