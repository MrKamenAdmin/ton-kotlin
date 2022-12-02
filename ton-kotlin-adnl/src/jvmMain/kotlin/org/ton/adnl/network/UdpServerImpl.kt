package org.ton.adnl.network

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal actual class UdpServerImpl actual constructor(
    coroutineContext: CoroutineContext,
    actual val port: Int,
    callback: UdpServer.Callback
) : UdpServer {
    override val coroutineContext: CoroutineContext = coroutineContext + CoroutineName(toString())
    private val socket = aSocket(SelectorManager(coroutineContext + CoroutineName("selector-$port")))
        .udp()
        .bind(localAddress = InetSocketAddress("0.0.0.0", port))
    private val job = launch(CoroutineName("listener-$port") + SupervisorJob()) {
        while (true) {
            val datagram = socket.receive()
            val socketAddress = datagram.address as InetSocketAddress
            val address = socketAddress.hostname
            val port = socketAddress.port
            val data = datagram.packet
            try {
                launch {
                    callback.receive(address, port, data)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun send(host: String, port: Int, data: ByteReadPacket) {
        val datagram = Datagram(data, InetSocketAddress(host, port))
        socket.send(datagram)
    }
}
