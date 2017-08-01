package HttpServerDemo

import io.netty.buffer.Unpooled
import io.netty.channel.*
import io.netty.handler.codec.http.*
import io.netty.util.CharsetUtil
import io.netty.util.internal.SystemPropertyUtil
import java.io.File
import java.io.RandomAccessFile
import java.net.URI
import java.net.URLDecoder
import javax.activation.MimetypesFileTypeMap

/**
 * Created by yupenglei on 17/7/31.
 */
class HttpStaticFileServerHandler : SimpleChannelInboundHandler<FullHttpRequest>() {
    override fun channelRead0(ctx: ChannelHandlerContext, request: FullHttpRequest) {
        println("channelRead0")
        if (!request.decoderResult().isSuccess) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST)
            return
        }
        if (request.method() != HttpMethod.GET) {
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED)
            return
        }
        val uri = URI.create(request.uri())
        //响应query
        if (!uri.query.isNullOrEmpty()) {
            val respon = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                    Unpooled.copiedBuffer("success", CharsetUtil.UTF_8))
            respon.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8")
            ctx.writeAndFlush(respon).addListener(ChannelFutureListener.CLOSE)
            return
        }
        val path = sanitizeUri(request.uri())
        if (path.isNullOrBlank()) {
            sendError(ctx, HttpResponseStatus.FORBIDDEN)
            return
        }
        println(path)
        val file = File(path)
        if (!file.exists() || file.isHidden) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND)
            return
        }
        if (file.isDirectory || !file.isFile) {
            //todo 以后待处理
            sendError(ctx, HttpResponseStatus.FORBIDDEN)
            return
        }

        //响应文件
        val raFile = RandomAccessFile(file, "r")

        val response = DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)
        HttpUtil.setContentLength(response, raFile.length())
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,
                MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file))
        if (HttpUtil.isKeepAlive(request))
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
        ctx.write(response)

        val sendFileFuture: ChannelFuture =
                ctx.write(DefaultFileRegion(raFile.channel, 0, raFile.length()),
                        ctx.newProgressivePromise())

        sendFileFuture.addListener(object : ChannelProgressiveFutureListener {
            override fun operationComplete(p0: ChannelProgressiveFuture?) {
                println("${p0?.channel()} transfer complete")
            }

            override fun operationProgressed(p0: ChannelProgressiveFuture?, progress: Long,
                                             total: Long) {
                println("${p0?.channel()} transfer progress: $progress/$total=${progress * 1f / total}")
            }
        })

        val lastContentFuture: ChannelFuture =
                ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT)
        if (HttpUtil.isKeepAlive(request)) {
            lastContentFuture.addListener(ChannelFutureListener.CLOSE)
        }
    }

    private fun sendError(ctx: ChannelHandlerContext, status: HttpResponseStatus) {
        val response = DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure: $status\r\n", CharsetUtil.UTF_8))
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8")
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
    }

    private fun sanitizeUri(uri: String): String? {
        val newUri = URLDecoder.decode(uri, "utf8")
        if (uri.isEmpty() || !uri.startsWith("/"))
            return null
        return SystemPropertyUtil.get("user.dir") + newUri.replace("/", File.separator)
    }
}


