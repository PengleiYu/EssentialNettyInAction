package simpleCodec

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.util.CharsetUtil

/**
 * Created by yupenglei on 17/7/28.
 */
class SimpleHandler : ChannelInboundHandlerAdapter() {
    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        val text = when (msg) {
            is ByteBuf -> msg.toString(CharsetUtil.UTF_8)
            is Int -> msg.toString()
            is CoolData -> msg.toString()
            else -> "nothing"
        }
        println("channelRead: <== $text(${msg?.javaClass?.name})")
        super.channelRead(ctx, msg)
    }

    override fun channelRegistered(ctx: ChannelHandlerContext?) {
        println("channelRegistered")
        super.channelRegistered(ctx)
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext?) {
        println("channelReadComplete")
        super.channelReadComplete(ctx)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        println("exceptionCaught")
        super.exceptionCaught(ctx, cause)
    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        println("channelInactive")
        super.channelInactive(ctx)
    }

    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        println("userEventTriggered")
        super.userEventTriggered(ctx, evt)
    }

    override fun channelWritabilityChanged(ctx: ChannelHandlerContext?) {
        println("channelWritabilityChanged")
        super.channelWritabilityChanged(ctx)
    }

    override fun channelUnregistered(ctx: ChannelHandlerContext?) {
        println("channelUnregistered")
        super.channelUnregistered(ctx)
    }

    override fun channelActive(ctx: ChannelHandlerContext?) {
        println("channelActive")
        super.channelActive(ctx)
    }
}

class SimpleInitializer : ChannelInitializer<SocketChannel>() {
    override fun initChannel(p0: SocketChannel?) {
        p0?.pipeline()?.addLast(SimpleHandler())
    }
}

class IntCodecInitializer : ChannelInitializer<SocketChannel>() {
    override fun initChannel(p0: SocketChannel?) {
        val pipe = p0?.pipeline()!!
        pipe.addLast("decoder", ToIntDecoder())
        pipe.addLast("encoder", FromIntEncoder())
        pipe.addLast(SimpleHandler())
    }
}

class ImportantDataCodecInitializer : ChannelInitializer<SocketChannel>() {
    override fun initChannel(p0: SocketChannel?) {
        val pipe = p0?.pipeline()!!
        pipe.addLast("decoder", ToCoolDataDecoder())
        pipe.addLast("encoder", FromCoolDataEncoder())
        pipe.addLast(SimpleHandler())
    }
}