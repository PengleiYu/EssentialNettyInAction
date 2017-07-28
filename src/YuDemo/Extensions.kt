package YuDemo

import io.netty.buffer.Unpooled
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.util.CharsetUtil

/**
 * Created by yupenglei on 17/7/27.
 */
fun Channel.writeAndFlushString(message: String): ChannelFuture =
        writeAndFlush(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8))

