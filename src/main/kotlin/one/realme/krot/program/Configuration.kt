package one.realme.krot.program

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

/**
 * App Env
 */
class Configuration {
    private val defaultConfFile = "testnet.conf"

    lateinit var genesis: Genesis
    lateinit var net: Net
    lateinit var application: Application

    data class Application(
            val name: String
    )

    data class Net(
            val port: Int,
            val maxPeer: Int
    )

    data class Genesis(
            val version: Int,
            val height: Long,
            val prevBlockHash: String,
            val timestamp: Int
    )

    fun load(confFile: String) {
        // fix me : this is not the finial impl
        val conf = when (confFile) {
            "mainnet.conf", "testnet.conf" -> confFile
            else -> defaultConfFile
        }
        //
        val config = ConfigFactory.load(conf)

        application = Application(
                config.tryGetString("name") ?: "krot"
        )

        net = Net(
                config.tryGetInt("net.port") ?: 50505,
                config.tryGetInt("net.maxPeer") ?: 250
        )

        genesis = Genesis(
                config.tryGetInt("genesis.version") ?: 1,
                config.tryGetLong("genesis.height") ?: 0,
                config.tryGetString("genesis.prevBlockHash") ?: "00000000000000000000000000000000",
                config.tryGetInt("genesis.timestamp") ?: 1540166400
        )
    }

    private fun Config.tryGetInt(path: String): Int? = if (hasPath(path)) getInt(path) else null
    private fun Config.tryGetString(path: String): String? = if (hasPath(path)) getString(path) else null
    private fun Config.tryGetLong(path: String): Long? = if (hasPath(path)) getLong(path) else null
}