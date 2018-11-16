package one.realme.krot.common.primitive

import one.realme.krot.common.primitive.Address
import one.realme.krot.common.primitive.Coin

class Account(val address: Address) {
    private var balance = Coin.ZERO
}