package org.ton.experimental

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.ton.adnl.ipv4
import org.ton.api.adnl.AdnlAddress
import org.ton.api.adnl.AdnlAddressUdp
import org.ton.api.adnl.AdnlIdShort
import org.ton.api.adnl.AdnlPacketContents
import org.ton.api.adnl.message.AdnlMessageCreateChannel
import org.ton.api.dht.DhtNode
import org.ton.api.dht.DhtNodes
import org.ton.api.dht.functions.DhtPing
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKeyEd25519
import org.ton.block.DnsAdnlAddress
import org.ton.crypto.base64
import org.ton.crypto.encodeHex
import org.ton.crypto.hex
import org.ton.lite.client.LiteClient
import org.ton.proxy.adnl.Adnl
import org.ton.proxy.adnl.engine.CIOAdnlNetworkEngine
import org.ton.proxy.adnl.resolver.AdnlAddressResolver
import org.ton.proxy.adnl.resolver.MapAdnlAddressResolver
import org.ton.proxy.dht.Dht
import org.ton.proxy.dht.DhtPeer
import org.ton.proxy.dns.DnsCategory
import org.ton.proxy.dns.DnsResolver
import org.ton.proxy.rldp.Rldp
import java.io.File
import kotlin.test.Test

class Test {
    @Test
    fun dhtTest() = runBlocking {
        val dht = Dht.lite(CONFIG_GLOBAL.dht.static_nodes)
        val liteClient = LiteClient(CONFIG_GLOBAL)
        val dns = DnsResolver(liteClient)

        val site = dns.resolve("foundation.ton", DnsCategory.SITE) as? DnsAdnlAddress ?: return@runBlocking
        println(site)

        val res = dht.resolve(AdnlIdShort(site.adnl_addr))
        println(res)
    }

    @Test
    fun testPing() = runBlocking {
        val privateKey = PrivateKeyEd25519(File("/Users/andreypfau/CLionProjects/ton/cmake-build-debug/dht-server/db/keyring/D6B538ADA6729B917DA714E3C1DE6A3F0E3807169A43B980849205FDF663EAD7").readBytes())
        val publicKey = privateKey.publicKey()
        println(publicKey.toAdnlIdShort().id.encodeHex())
        val resolver = MapAdnlAddressResolver(
            mapOf(
                publicKey.toAdnlIdShort() to (publicKey to listOf(
                    AdnlAddressUdp(2130706433, 3278)
                ))
            )
        )
        val adnl = Adnl(
            CIOAdnlNetworkEngine(),
            resolver
        )
//        val transfer = Rldp(
//            adnl
//        ).transfer(
//            publicKey.toAdnlIdShort(),
//            ByteArray(32) { 0xFF.toByte() }
//        )
//        transfer.job.start()
//        delay(Long.MAX_VALUE)
    }

    private fun loadNodes() = Json.decodeFromString<DhtNodes>(File("dht.json").readText())

    @Test
    fun collectInfo() {
        var dhtNodes: List<DhtNode> = loadNodes()
        println("DHT Nodes: ${dhtNodes.size}")
        val addresses = HashSet<AdnlAddress>()
        dhtNodes.forEach { node ->
            node.addr_list.addrs.forEach { addr ->
                if (!addresses.add(addr)) {
//                    println("Duplicate address: $addr - $node")
                }
            }
        }
        println("Addresses: ${addresses.size}")
        addresses.sortedBy { (it as? AdnlAddressUdp)?.ip?.toUInt()?.toLong() ?: 0 }.forEach {
            if (it is AdnlAddressUdp) {
                println("${ipv4(it.ip)}:${it.port} - $it")
            } else {
                println("exotic: $it")
            }
        }
    }

    @Test
    fun testKeys() {
        val alicePrivate = PrivateKeyEd25519(ByteArray(32) { 0xFF.toByte() })
        val alicePublic = alicePrivate.publicKey()

        println("alice secret "+alicePrivate.key.encodeHex())
        println("alice public "+alicePublic.key.encodeHex())

        val bobPrivate = PrivateKeyEd25519(ByteArray(32) { 0x00.toByte() })
        val bobPublic = bobPrivate.publicKey()

        println("bob secret "+bobPrivate.key.encodeHex())
        println("bob public "+bobPublic.key.encodeHex())

        val original = "Hello, world!".toByteArray()
        val encrypted = alicePublic.encrypt(original)
        val decrypted = alicePrivate.decrypt(encrypted)

        println("Original: ${original.encodeHex()}")
        println("Decrypted: ${decrypted.encodeHex()}")
    }


    @Test
    fun packetContents() {
        val message =
            AdnlPacketContents.decodeBoxed(hex("89cd42d1073c306ea2a96d35d90c0000c6b413486ef4b8019c944ca848ace2f88a4f67dc08a8487d0ca11c9dd27a5659a185a2c6010000007af98bb400000000000000000000000000000000000000000000000000000000000000000c183febcb390500000000000000000001000000e7a60d670100007fce0c0000cde25f63cde25f6300000000b5e65f6300000000000000000000000000000000cde25f63ff1c74c840f45759eefb9c438532c6fe635f869290aee2b9324fbb46c569d05d5ee0f256df639030893be5977f01a2781d3c14bf9587d8129d8be5ad569cedf908b48b200100000007373585b0e7bb1b"))
    }
}