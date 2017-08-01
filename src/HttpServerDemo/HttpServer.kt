package HttpServerDemo

import base.BaseServer
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.stream.ChunkedWriteHandler

/**
 * Created by yupenglei on 17/8/1.
 */
class HttpServer : BaseServer(channelInitializer = HttpInitializer())

class HttpInitializer : ChannelInitializer<SocketChannel>() {
    override fun initChannel(p0: SocketChannel?) {
        p0?.pipeline()!!
                .addLast(HttpServerCodec())
                .addLast(HttpObjectAggregator(Int.MAX_VALUE))
                .addLast(ChunkedWriteHandler())
                .addLast(HttpStaticFileServerHandler())
    }
}

fun main(args: Array<String>) {
    HttpServer().run()
}

