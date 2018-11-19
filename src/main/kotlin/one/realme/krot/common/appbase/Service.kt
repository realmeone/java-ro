package one.realme.krot.common.appbase

interface Service {
    fun state(): ServiceState
    fun name(): String = this.javaClass.simpleName
    fun initialize(config: Configuration)
    fun startup()
    fun shutdown()
}