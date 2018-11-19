package one.realme.krot.common.appbase

class ChainService : AbstractService() {
    override fun name(): String = "chainService"

    override fun doInitialize(config: Configuration) {
        println("init chain service")
    }

    override fun doStartup() {
        println("start chain service")
    }

    override fun doShutdown() {
        println("shutdown chain service")
    }

}

class NetService : AbstractService() {
//    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    override fun doInitialize(config: Configuration) {
        println("init net service")
    }

    override fun doStartup() {
//        executor.execute {
            println("start net service")
//        }
    }

    override fun doShutdown() {
//        executor.shutdown()
        println("shutdown net service")
    }
}

fun main(args: Array<String>) {
    val config = Configuration("testnet.conf")
    val manager = ServiceManager()
    manager.registerService(ChainService())
    manager.registerService(NetService())

    manager.initialize(config)
    manager.startup()

    Runtime.getRuntime().addShutdownHook(Thread {
        manager.shutdown()
    })
}