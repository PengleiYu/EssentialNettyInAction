package simpleCodec

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.MessageToByteEncoder

/**
 * Created by yupenglei on 17/7/28.
 */
class ToIntDecoder : ByteToMessageDecoder() {
    override fun decode(p0: ChannelHandlerContext?, p1: ByteBuf?, p2: MutableList<Any>?) {
        if (p1 != null && p1.readableBytes() >= 4) {
            p2?.add(p1.readInt())
        }
    }
}

class FromIntEncoder : MessageToByteEncoder<Int>() {
    override fun encode(p0: ChannelHandlerContext?, p1: Int?, p2: ByteBuf?) {
        if (p1 is Int) {
            p2?.writeInt(p1)
        }
    }
}

class ToCoolDataDecoder : ByteToMessageDecoder() {
    override fun decode(p0: ChannelHandlerContext?, p1: ByteBuf?, p2: MutableList<Any>?) {
        if (p1 != null && p1.readableBytes() >= 7) {
            val first = p1.readByte()
            val second = p1.readShort()
            val third = p1.readInt()
            p2?.add(CoolData(first, second, third))
        }
    }
}

class FromCoolDataEncoder : MessageToByteEncoder<CoolData>() {
    override fun encode(p0: ChannelHandlerContext?, p1: CoolData?, p2: ByteBuf?) {
        if (p1 != null) {
            p2!!.writeByte(p1.start.toInt())
                    .writeShort(p1.second.toInt())
                    .writeInt(p1.third)
        }
    }
}