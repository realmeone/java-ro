package one.realme.common

import java.util.*

// Date extensions
fun Date.toUnix(): Long {
    return this.toInstant().epochSecond
}