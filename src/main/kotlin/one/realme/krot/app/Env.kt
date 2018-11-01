package one.realme.krot.app

import com.typesafe.config.ConfigFactory

/**
 * App Env
 */
object Env {
    private const val defaultConfFile = "testnet.conf"
    var name = "testnet"
    var netPort = 50505
    var genesisVersion = 1
    var genesisHeight = 0
    var genesisPrevBlockHash = "00000000000000000000000000000000"
    var genesisTimestamp = 1540166400

    fun reload(confFile: String) {
        // fix me : this is not the finial impl
        val conf = when (confFile) {
            "mainnet.conf", "testnet.conf" -> confFile
            else -> defaultConfFile
        }
        //
        val config = ConfigFactory.load(conf)

        name = config.getString("testnet")
        netPort = config.getInt("net.port")
        genesisVersion = config.getInt("genisis.version")
        genesisHeight = config.getInt("genisis.height")
        genesisPrevBlockHash = config.getString("genisis.prevBlockHash")
        genesisTimestamp = config.getInt("genisis.timestamp")
    }
}