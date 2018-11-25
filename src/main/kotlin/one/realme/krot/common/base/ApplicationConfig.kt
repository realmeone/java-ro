package one.realme.krot.common.base

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

/**
 * simple wrap of Hocon config
 */
class ApplicationConfig {
    private lateinit var config: Config

    fun getIntOrNull(key: String): Int? = config.tryGetInt(key)
    fun getLongOrNull(key: String): Long? = config.tryGetLong(key)
    fun getStringOrNull(key: String): String? = config.tryGetString(key)

    fun load(conf: String) {
        config = ConfigFactory.load(conf)
//        application = Application(
//                config.tryGetString("name") ?: "krot"
//        )
//
//        net = Net(
//                config.tryGetInt("net.port") ?: 50505,
//                config.tryGetInt("net.maxPeer") ?: 250
//        )
//
//        genesis = Genesis(
//                config.tryGetInt("genesis.version") ?: 1,
//                config.tryGetLong("genesis.height") ?: 0,
//                config.tryGetString("genesis.prevBlockHash") ?: "00000000000000000000000000000000",
//                config.tryGetInt("genesis.timestamp") ?: 1540166400
//        )
    }

    private fun Config.tryGetInt(path: String): Int? = if (hasPath(path)) getInt(path) else null
    private fun Config.tryGetString(path: String): String? = if (hasPath(path)) getString(path) else null
    private fun Config.tryGetLong(path: String): Long? = if (hasPath(path)) getLong(path) else null
}