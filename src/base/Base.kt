package base

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler

/**
 * Created by yupenglei on 17/8/1.
 */
open class BaseServer(val host: String = "localhost", val port: Int = 8080,
                      val channelInitializer: ChannelInitializer<*> = StringHandlerInitializer()) {
    fun run(f: (Channel) -> Unit = {}) {
        val bossGroup = NioEventLoopGroup()
        val workGroup = NioEventLoopGroup()
        try {
            val channel = ServerBootstrap()
                    .group(bossGroup, workGroup)
                    .localAddress(host, port)
                    .channel(NioServerSocketChannel::class.java)
                    .handler(LoggingHandler(LogLevel.INFO))
                    .childHandler(channelInitializer)
                    .bind().sync().channel()
            println("start server at http://$host:$port/")
            f(channel)
            channel.closeFuture().sync()
        } finally {
            workGroup.shutdownGracefully().sync()
            bossGroup.shutdownGracefully().sync()
        }
    }
}

class StringHandlerInitializer : ChannelInitializer<SocketChannel>() {
    override fun initChannel(p0: SocketChannel?) {
        p0?.pipeline()?.addLast(SimpleStringHandler())
    }
}

class SimpleStringHandler : SimpleChannelInboundHandler<String>() {
    override fun channelRead0(p0: ChannelHandlerContext?, p1: String?) {
        println("channelRead0: <== $p1")
    }

    override fun channelWritabilityChanged(ctx: ChannelHandlerContext?) {
        super.channelWritabilityChanged(ctx)
        println("channelWritabilityChanged")
    }

    override fun channelActive(ctx: ChannelHandlerContext?) {
        super.channelActive(ctx)
        println("channelActive")
    }

    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        super.userEventTriggered(ctx, evt)
        println("userEventTriggered")
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext?) {
        super.channelReadComplete(ctx)
        println("channelReadComplete")
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        super.exceptionCaught(ctx, cause)
        println("exceptionCaught")
    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        super.channelInactive(ctx)
        println("channelInactive")
    }

    override fun channelRegistered(ctx: ChannelHandlerContext?) {
        super.channelRegistered(ctx)
        println("channelRegistered")
    }

    override fun channelUnregistered(ctx: ChannelHandlerContext?) {
        super.channelUnregistered(ctx)
        println("channelUnregistered")
    }
}