package org.ton.adnl.node

import org.ton.tl.ByteString
import org.ton.api.adnl.AdnlProxy as AdnlProxyType

public class AdnlProxy(
    public val type: AdnlProxyType
) {
    init {
        TODO()
    }

    public val id: ByteString get() = type.id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdnlProxy) return false
        if (type != other.type) return false
        return true
    }

    override fun hashCode(): Int = type.hashCode()
}
