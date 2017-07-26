package EchoDemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 最简单的NettyServer
 * Created by yupenglei on 17/7/25.
 */
public class SimpleNettyIOServer {
    public void server(int port) throws Exception {
        final ByteBuf buf = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
        EventLoopGroup group = new OioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();        //1
            b
//                    .group(group)                                    //2
//                    .channel(OioServerSocketChannel.class)
                    .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {//3
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                //4
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws
                                        Exception {
                                    ctx.writeAndFlush(buf.duplicate()).addListener
                                            (ChannelFutureListener.CLOSE);//5
                                }
                            });
                        }
                    });
            ChannelFuture f = b.bind().sync();  //6
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();        //7
        }
    }

    public static void main(String[] args) {
        try {
            new SimpleNettyIOServer().server(8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
