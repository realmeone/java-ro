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
    fun getStringListOrNull(key: String): List<String>? = config.tryGetStringList(key)

    fun load(conf: String) {
        config = ConfigFactory.load(conf)
    }

    private fun Config.tryGetInt(path: String): Int? = if (hasPath(path)) getInt(path) else null
    private fun Config.tryGetString(path: String): String? = if (hasPath(path)) getString(path) else null
    private fun Config.tryGetLong(path: String): Long? = if (hasPath(path)) getLong(path) else null
    private fun Config.tryGetStringList(path: String): List<String>? = if (hasPath(path)) getStringList(path) else null
}