package com.nervos.lightclient

import com.nervos.lightclient.type.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.toHexString
import okio.Buffer
import org.nervos.ckb.service.RpcService
import org.nervos.ckb.type.Script
import org.nervos.ckb.type.ScriptType
import org.nervos.ckb.utils.Numeric
import org.nervos.indexer.model.Order
import org.nervos.indexer.model.SearchKeyBuilder
import java.util.concurrent.atomic.AtomicLong

/// test case write with kotlin test framework kotest and mock tool mockk
class DefaultLightClientApiTestKt : FunSpec({
    val api = DefaultLightClientApi("http://localhost:9000")
    // ckt1qzda0cr08m85hc8jlnfp3zer7xulejywt49kt2rr0vthywaa50xwsq2qf8keemy2p5uu0g0gn8cd4ju23s5269qk8rg4r
    val script = Script(
        Numeric.hexStringToByteArray("0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8"),
        Numeric.hexStringToByteArray("0x4049ed9cec8a0d39c7a1e899f0dacb8a8c28ad14"),
        Script.HashType.TYPE
    )

    val rpcServiceField = DefaultLightClientApi::class.java.getDeclaredField("rpcService")
    val clientField = RpcService::class.java.getDeclaredField("client")
    val bodyField = Response::class.java.getDeclaredField("body")

    lateinit var nextId: AtomicLong

    val slot = slot<Request>()
    var responseStr = ""
    var id = ""

    beforeSpec {
        rpcServiceField.isAccessible = true
        clientField.isAccessible = true
        bodyField.isAccessible = true

        val nextIdField = RpcService::class.java.getDeclaredField("nextId")
        nextIdField.isAccessible = true

        nextId = nextIdField.get(null) as AtomicLong
    }
    /// specify if to mock or use real connection
    val mock = true
    beforeTest {
        if (!mock) {
            val client = spyk<OkHttpClient>()
            val rpcService = rpcServiceField.get(api)
            clientField.set(rpcService, client)
            every {
                client.newCall(capture(slot))
            } answers {
                val call = spyk(callOriginal())
                every {
                    call.execute()
                } answers {
                    val resp = callOriginal()
                    responseStr = resp.body?.string().toString().trim()
                    bodyField.set(resp, responseStr.toResponseBody())
                    resp
                }
                call
            }
        }
        id = "0x" + nextId.get().toHexString()
    }

    afterTest {
        responseStr = ""
        id = ""
    }

    fun mockResponse(response: String) {
        if (mock) {
            responseStr = response
            val client = spyk<OkHttpClient>()
            val rpcService = rpcServiceField.get(api)
            clientField.set(rpcService, client)

            every {
                client.newCall(capture(slot))
            } answers {
                val call = spyk(callOriginal())
                every {
                    call.execute()
                } answers {
                    val resp = mockk<Response>()
                    every { resp.isSuccessful } returns true
                    every { resp.body } returns response.toResponseBody()
                    resp
                }
                call
            }
        }
    }

    fun fetchRequestStr(): String {
        val buffer = Buffer()
        slot.captured.body?.writeTo(buffer)
        return buffer.readUtf8()
    }

    fun getResource(path: String): String {
        val resource = this.javaClass.getResource(path)
        val txt = resource!!.readText()
        val idTail = "0x0\"}"
        txt.substring(txt.length - idTail.length) shouldBe idTail
        return txt.substring(0, txt.length - idTail.length) + "$id\"}"
    }

    test("setScripts") {
        val scriptDetails = ScriptDetail().apply {
            this.script = script
            scriptType = ScriptType.LOCK
            blockNumber = 7033100
        }.let { listOf(it) }

        val expectResponseStr = """{"jsonrpc":"2.0","result":null,"id":"0x0"}"""
        mockResponse(expectResponseStr)

        api.scripts = scriptDetails
        println(responseStr)

        val requestStr = fetchRequestStr()

        requestStr shouldBe """{"jsonrpc":"2.0","method":"set_scripts","params":[[{"script":{"code_hash":"0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8","args":"0x4049ed9cec8a0d39c7a1e899f0dacb8a8c28ad14","hash_type":"type"},"script_type":"lock","block_number":"0x6b510c"}]],"id":"$id"}"""
        responseStr shouldBe expectResponseStr
    }

    test("getScripts") {
        val expectResponseStr =
            """{"jsonrpc":"2.0","result":[{"block_number":"0x6b510c","script":{"args":"0x4049ed9cec8a0d39c7a1e899f0dacb8a8c28ad14","code_hash":"0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8","hash_type":"type"},"script_type":"lock"}],"id":"$id"}"""
        mockResponse(expectResponseStr)
        val scriptDetails = api.scripts
        val requestStr = fetchRequestStr()

        requestStr shouldBe """{"jsonrpc":"2.0","method":"get_scripts","params":[],"id":"$id"}"""
        responseStr shouldBe expectResponseStr
        scriptDetails.size shouldBeGreaterThan 0
        scriptDetails.any { it.script != null } shouldBe true
        scriptDetails.any { it.blockNumber > 0 } shouldBe true
        scriptDetails.any { it.scriptType != null } shouldBe true
    }

    test("getTipHeader") {
        val expectResponse = getResource("/lightclient/getHeader.json")
        mockResponse(expectResponse)
        val header = api.tipHeader
        val requestStr = fetchRequestStr()
        requestStr shouldBe """{"jsonrpc":"2.0","method":"get_tip_header","params":[],"id":"$id"}"""
        responseStr shouldBe expectResponse
        header shouldNotBe null
    }


    test("getGenesisBlock") {
        val expectResponse = getResource("/lightclient/genesis.json")
        mockResponse(expectResponse)
        val block = api.genesisBlock

        val requestStr = fetchRequestStr()

        requestStr shouldBe """{"jsonrpc":"2.0","method":"get_genesis_block","params":[],"id":"$id"}"""

        responseStr shouldBe expectResponse

        block shouldNotBe null
    }

    test("getHeader") {
        val expectResponse = getResource("/lightclient/getHeader.json")
        mockResponse(expectResponse)
        val hex = Numeric.hexStringToByteArray("0xc78c65185c14e1b02d6457a06b4678bab7e15f194f49a840319b57c67d20053c")
        val header = api.getHeader(hex)
        val requestStr = fetchRequestStr()

        requestStr shouldBe """{"jsonrpc":"2.0","method":"get_header","params":["0xc78c65185c14e1b02d6457a06b4678bab7e15f194f49a840319b57c67d20053c"],"id":"$id"}"""
        responseStr shouldBe expectResponse

        header shouldNotBe null
    }

    test("getTransaction") {
        val expectResponse = getResource("/lightclient/getTransaction.json")
        mockResponse(expectResponse)
        val tx =
            api.getTransaction(Numeric.hexStringToByteArray("0x151d4d450c9e3bccf4b47d1ba6942d4e9c8c0eeeb7b9f708df827c164f035aa8"))

        val requestStr = fetchRequestStr()

        requestStr shouldBe """{"jsonrpc":"2.0","method":"get_transaction","params":["0x151d4d450c9e3bccf4b47d1ba6942d4e9c8c0eeeb7b9f708df827c164f035aa8"],"id":"$id"}"""
        responseStr shouldBe expectResponse

        tx.header shouldNotBe null
        tx.transaction shouldNotBe null
    }

    test("fetchHeader") {
        val expectResponse = getResource("/lightclient/fetchHeader.json")
        mockResponse(expectResponse)
        val header =
            api.fetchHeader(Numeric.hexStringToByteArray("0xcb5eae958e3ea24b0486a393133aa33d51224ffaab3c4819350095b3446e4f70"))

        val requestStr = fetchRequestStr()

        requestStr shouldBe """{"jsonrpc":"2.0","method":"fetch_header","params":["0xcb5eae958e3ea24b0486a393133aa33d51224ffaab3c4819350095b3446e4f70"],"id":"$id"}"""
        responseStr shouldBe expectResponse

        with(header) {
            status shouldNotBe null
            data shouldNotBe null
        }
    }

    test("fetchTransaction") {
        val expectResponse = getResource("/lightclient/fetchTransaction.json")
        mockResponse(expectResponse)
        val tx =
            api.fetchTransaction(Numeric.hexStringToByteArray("0x716e211698d3d9499aae7903867c744b67b539beeceddad330e73d1b6b617aef"))

        val requestStr = fetchRequestStr()
        requestStr shouldBe """{"jsonrpc":"2.0","method":"fetch_transaction","params":["0x716e211698d3d9499aae7903867c744b67b539beeceddad330e73d1b6b617aef"],"id":"$id"}"""
        responseStr shouldBe expectResponse

        with(tx) {
            status shouldNotBe null
            data shouldNotBe null
        }
    }

    test("getCells") {
        val key = SearchKeyBuilder()
        key.script(script)
        key.scriptType(ScriptType.LOCK)
        val expectResponse = getResource("/lightclient/getCells.json")
        mockResponse(expectResponse)

        val cells = api.getCells(key.build(), Order.ASC, 10, null)
        val requestStr = fetchRequestStr()

        requestStr shouldBe """{"jsonrpc":"2.0","method":"get_cells","params":[{"script":{"code_hash":"0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8","args":"0x4049ed9cec8a0d39c7a1e899f0dacb8a8c28ad14","hash_type":"type"},"script_type":"lock","group_by_transaction":false},"asc","0xa",null],"id":"$id"}"""

        responseStr shouldBe expectResponse

        with(cells.objects) {
            size shouldBeGreaterThan 0
            any { it.blockNumber != 0 } shouldBe true
            any { it.txIndex != 0 } shouldBe true
            any { it.outPoint != null } shouldBe true
            any { it.output != null } shouldBe true
        }
    }

    test("getTransactions") {
        val key = SearchKeyBuilder().apply {
            script(script)
            scriptType(ScriptType.LOCK)
        }
        val expectResponse = getResource("/lightclient/transactions.json")
        mockResponse(expectResponse)
        val txs = api.getTransactions(key.build(), Order.ASC, 10, null)
        val requestStr = fetchRequestStr()
        requestStr shouldBe """{"jsonrpc":"2.0","method":"get_transactions","params":[{"script":{"code_hash":"0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8","args":"0x4049ed9cec8a0d39c7a1e899f0dacb8a8c28ad14","hash_type":"type"},"script_type":"lock","group_by_transaction":false},"asc","0xa",null],"id":"$id"}"""

        responseStr shouldBe expectResponse

        with(txs.objects) {
            size shouldBeGreaterThan 0
            any { it.transaction != null } shouldBe true
            any { it.blockNumber != 0 } shouldBe true
            any { it.ioIndex != 0 } shouldBe true
            any { it.ioType != null } shouldBe true
            any { it.txIndex != 0 } shouldBe true
        }
    }

    test("getTransactionsGrouped") {
        val key = SearchKeyBuilder()
        key.script(script)
        key.scriptType(ScriptType.LOCK)
        val expectResponse = getResource("/lightclient/TransactionsGrouped.json")
        mockResponse(expectResponse)
        val txs = api.getTransactionsGrouped(key.build(), Order.ASC, 10, null)

        val requestStr = fetchRequestStr()
        requestStr shouldBe """{"jsonrpc":"2.0","method":"get_transactions","params":[{"script":{"code_hash":"0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8","args":"0x4049ed9cec8a0d39c7a1e899f0dacb8a8c28ad14","hash_type":"type"},"script_type":"lock","group_by_transaction":true},"asc","0xa",null],"id":"$id"}"""

        responseStr shouldBe expectResponse

        with(txs.objects) {
            txs.objects.size shouldBeGreaterThan 0
            any { it.blockNumber != 0 } shouldBe true
            any { it.cells.size > 0 } shouldBe true
            any { it.cells[0] != null } shouldBe true
            any { it.cells.size >= 2 && it.cells[1].ioIndex != 0 } shouldBe true
            any { it.txIndex != 0 } shouldBe true
            any { it.transaction != null } shouldBe true
        }
    }

    test("getCellsCapacity") {
        val key = SearchKeyBuilder().apply {
            script(script)
            scriptType(ScriptType.LOCK)
        }
        val expectResponse =
            """{"jsonrpc":"2.0","result":{"block_hash":"0xb4e7ab9b7d9f6bc9d45af4b32a980a525a3ae1ecb8d9d60062d09a850aec8b39","block_number":"0x75d80a","capacity":"0x24914844cfc"},"id":"$id"}"""
        mockResponse(expectResponse)
        val capacity = api.getCellsCapacity(key.build())

        val requestStr = fetchRequestStr()

        requestStr shouldBe """{"jsonrpc":"2.0","method":"get_cells_capacity","params":[{"script":{"code_hash":"0x9bd7e06f3ecf4be0f2fcd2188b23f1b9fcc88e5d4b65a8637b17723bbda3cce8","args":"0x4049ed9cec8a0d39c7a1e899f0dacb8a8c28ad14","hash_type":"type"},"script_type":"lock","group_by_transaction":false}],"id":"$id"}"""
        responseStr shouldBe expectResponse

        with(capacity) {
            this.capacity shouldNotBe 0L
            blockNumber shouldNotBe 0
            blockHash shouldNotBe null
        }
    }
})
