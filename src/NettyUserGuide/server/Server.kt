package NettyUserGuide.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.stream.ChunkedWriteHandler

/**
 * Created by yupenglei on 17/7/26.
 */
private class Server(val host: String = "localhost", val port: Int = 8080) {

    fun stringRun() {
        run(ServerStringInitializer())
    }

    fun fileRun() {
        run(ServerFileInitializer())
    }

    private fun run(initializer: ChannelInitializer<SocketChannel>) {
        val parentGroup = NioEventLoopGroup()
        val childGroup = NioEventLoopGroup()
        try {
            val channelFuture = ServerBootstrap()
                    .group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel::class.java)
                    .localAddress(host, port)
                    .childHandler(initializer)
                    .bind().sync()
            channelFuture.channel().closeFuture().sync()
        } finally {
            parentGroup.shutdownGracefully()
            childGroup.shutdownGracefully()
            println("Server close")
        }
    }

    private class ServerStringInitializer : ChannelInitializer<SocketChannel>() {
        override fun initChannel(p0: SocketChannel?) {
            val pipe = p0?.pipeline()!!
            pipe.addLast(DelimiterBasedFrameDecoder(10000,
                    Unpooled.copiedBuffer(",".toByteArray())))
            pipe.addLast(ServerHandler())
        }
    }

    private class ServerFileInitializer : ChannelInitializer<SocketChannel>() {
        override fun initChannel(p0: SocketChannel?) {
            val pipe = p0?.pipeline()!!
            pipe.addLast(ChunkedWriteHandler())
            pipe.addLast(ServerHandler())
        }
    }
}

fun main(args: Array<String>) {
    Server().fileRun()
//    Server().stringRun()
}
