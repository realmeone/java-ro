package one.realme.krot.service.net

/**
 *
 */
data class PeerStatus(
        val peer: String,
        val connecting: Boolean = false,
        val syncing: Boolean = false
)