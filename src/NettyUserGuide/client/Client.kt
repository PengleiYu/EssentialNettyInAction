package NettyUserGuide.client

import NettyUserGuide.writeAndFlushString
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.stream.ChunkedStream
import io.netty.handler.stream.ChunkedWriteHandler
import java.io.File
import java.io.FileInputStream
import java.util.*

/**
 * Created by yupenglei on 17/7/26.
 */
private class Client(private val host: String = "localhost", private val port: Int = 8080) {
    fun StringRun() {
        /**
         * 写字符串
         */
        fun writeString(channel: Channel) {
            listOf("Hello", "world", "Hello", "world", "Hello", "world", "Hello", "world")
                    .asSequence()
                    .map { it + "," }
                    .forEach { channel.writeAndFlushString(it).addListener { println("send done") } }

            val scanner = Scanner(System.`in`)
            while (scanner.hasNextLine()) {
                channel.writeAndFlushString(scanner.nextLine()).addListener { println("send done") }
            }
        }
        run(StringInitializer(), ::writeString)
    }

    fun fileRun() {
        /**
         * 写文件
         */
        fun writeFile(channel: Channel) {
            val file = File(".gitignore")
            channel.writeAndFlush(ChunkedStream(FileInputStream(file))).addListener { println(it.isSuccess) }
        }

        run(ChunkedInitializer(), ::writeFile)
    }

    private fun run(channelInitializer: ChannelInitializer<SocketChannel>, f: (Channel) -> Unit) {
        val group = NioEventLoopGroup()
        try {
            val channel = Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel::class.java)
                    .remoteAddress(host, port)
                    .handler(channelInitializer)
                    .connect().sync().channel()
            f(channel)
            channel.close().sync()
        } finally {
            group.shutdownGracefully().sync()
        }
    }

    private class StringInitializer : ChannelInitializer<SocketChannel>() {
        override fun initChannel(p0: SocketChannel?) {
            p0?.pipeline()?.addLast(ClientHandler())
        }
    }

    private class ChunkedInitializer : ChannelInitializer<SocketChannel>() {
        override fun initChannel(p0: SocketChannel?) {
            val pipe = p0?.pipeline()!!
            pipe.addLast(ChunkedWriteHandler())//处理ChunkedStream
            pipe.addLast(ClientHandler())
        }
    }
}


fun main(args: Array<String>) {
//    Client().StringRun()
    Client().fileRun()
}
