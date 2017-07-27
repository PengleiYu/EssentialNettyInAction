package NettyUserGuide

import java.io.OutputStream
import java.net.Socket

/**
 * 测试用Client
 * Created by yupenglei on 17/7/26.
 */
fun main(args: Array<String>) {
    var socket: Socket? = null
    var out: OutputStream? = null
    try {
        socket = Socket("localhost", 8080)
        out = socket.getOutputStream()
        val text = "床前明月光\r\n疑是地上霜\r\n举头望明月\r\n低头思故乡\r\n"
        out.write(text.toByteArray())
        out.flush()
    } finally {
        socket?.close()
        out?.close()
    }
}
