package one.realme.krot.net.client

/**
 * Peers managed all client's lifecycle
 *
 * When a Peer is made, connect to this Peer, exchange data with this Peer,
 * Peers will score this Peer, every 10 minutes will remove the lowest Peer if
 * it is not the only one.
 *
 * Peers will listen to each Peer in one thread
 */
object Peers {

}