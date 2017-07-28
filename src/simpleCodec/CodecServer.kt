package simpleCodec

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel

/**
 * Created by yupenglei on 17/7/28.
 */
class CodecServer(private val host: String = "localhost", private val port: Int = 8080) {
    fun run() {
        val parent = NioEventLoopGroup()
        val child = NioEventLoopGroup()
        try {
            val channel = ServerBootstrap()
                    .group(parent, child)
                    .localAddress(host, port)
                    .channel(NioServerSocketChannel::class.java)
                    .childHandler(ImportantDataCodecInitializer())
                    .bind().sync().channel()

            channel.closeFuture().sync()
        } finally {
            parent.shutdownGracefully().sync()
            child.shutdownGracefully().sync()
        }
    }
}

fun main(args: Array<String>) {
    CodecServer().run()
}
