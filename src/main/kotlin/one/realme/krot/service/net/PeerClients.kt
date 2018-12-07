package one.realme.krot.service.net

/**
 * PeerClients managed all client's lifecycle
 *
 * When a PeerClient was made, connect to this Peer, exchange data with this PeerClient,
 * Peers will score this Peer, every 10 minutes will remove the lowest Peer if
 * it is not the only one.
 *
 * Peers will listen to each Peer in one thread
 */
object PeerClients {

}