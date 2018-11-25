package one.realme.krot.common.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

/**
 * TODO simple wrap of hocon config
 */
class Configuration {
    lateinit var genesis: Genesis
    lateinit var net: Net
    lateinit var application: Application
    private val properties = mutableMapOf<String, Any>()

    fun getOrDefault(key: String, default: Any): Any = properties[key] ?: default


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

    fun load(conf: String) {
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