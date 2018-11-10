package one.realme.krot.app

import com.typesafe.config.ConfigFactory

/**
 * App Env
 */
object Env {
    private const val defaultConfFile = "testnet.conf"
    var name = "testnet"
    var netPort = 50505
    lateinit var genesis: Genesis

    init {
        reload(defaultConfFile)
    }

    data class Genesis(
            val version: Int,
            val height: Long,
            val prevBlockHash: String,
            val timestamp: Int
    )

    fun reload(confFile: String) {
        // fix me : this is not the finial impl
        val conf = when (confFile) {
            "mainnet.conf", "testnet.conf" -> confFile
            else -> defaultConfFile
        }
        //
        val config = ConfigFactory.load(conf)

        name = config.getString("name")
        netPort = config.getInt("net.port")
        genesis = Genesis(
                config.getInt("genesis.version"),
                config.getLong("genesis.height"),
                config.getString("genesis.prevBlockHash"),
                config.getInt("genesis.timestamp")
        )
    }
}