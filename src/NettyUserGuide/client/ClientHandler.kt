package NettyUserGuide.client

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.util.CharsetUtil

/**
 * Created by yupenglei on 17/7/27.
 */
class ClientHandler : ChannelInboundHandlerAdapter() {
    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        if (msg is ByteBuf)
            println("channelRead: <== ${msg.toString(CharsetUtil.UTF_8)}")
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext?) {
        super.channelReadComplete(ctx)
        println("channelReadComplete")
    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        super.channelInactive(ctx)
        println("channelInactive")
    }

    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        super.userEventTriggered(ctx, evt)
        println("userEventTriggered")
    }

    override fun channelWritabilityChanged(ctx: ChannelHandlerContext?) {
        super.channelWritabilityChanged(ctx)
        println("channelWritabilityChanged")
    }

    override fun channelUnregistered(ctx: ChannelHandlerContext?) {
        super.channelUnregistered(ctx)
        println("channelUnregistered")
    }

    override fun channelActive(ctx: ChannelHandlerContext?) {
        super.channelActive(ctx)
        println("channelActive")
    }

    override fun channelRegistered(ctx: ChannelHandlerContext?) {
        super.channelRegistered(ctx)
        println("channelRegistered")
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        super.exceptionCaught(ctx, cause)
        println("exceptionCaught")
    }
}