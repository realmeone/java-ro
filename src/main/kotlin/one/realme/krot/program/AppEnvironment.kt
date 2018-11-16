package one.realme.krot.program

import com.typesafe.config.ConfigFactory

/**
 * App Env
 */
class AppEnvironment {
    private val defaultConfFile = "testnet.conf"
    lateinit var genesis: Genesis
    lateinit var net: Net

    data class Net(
            val name: String,
            val port: Int
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

        net = Net(
                config.getString("name"),
                config.getInt("net.port")
        )

        genesis = Genesis(
                config.getInt("genesis.version"),
                config.getLong("genesis.height"),
                config.getString("genesis.prevBlockHash"),
                config.getInt("genesis.timestamp")
        )
    }
}