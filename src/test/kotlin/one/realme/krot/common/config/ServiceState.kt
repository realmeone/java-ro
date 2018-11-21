package one.realme.krot.common.config

enum class ServiceState {
    Registered, // constructed but doesn't do anything
    Initialized, // initialized any state required but is idle
    Started, // actively running
    Stopped // no longer running
}