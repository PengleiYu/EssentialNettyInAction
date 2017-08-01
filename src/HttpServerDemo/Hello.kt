package HttpServerDemo

import java.net.URI

/**
 * Created by yupenglei on 17/7/31.
 */
fun main(args: Array<String>) {
    val url = "/log?%7Bhello%7D"
    val uri = URI.create(url)
    println(uri.path)
    println(uri.query)
}
