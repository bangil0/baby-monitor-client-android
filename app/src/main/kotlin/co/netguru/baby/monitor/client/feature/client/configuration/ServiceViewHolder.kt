package co.netguru.baby.monitor.client.feature.client.configuration

import android.net.nsd.NsdServiceInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import co.netguru.baby.monitor.client.R
import co.netguru.baby.monitor.client.common.view.BaseViewHolder
import kotlinx.android.synthetic.main.item_service.*

class ServiceViewHolder(
        parent: ViewGroup,
        onServiceSelected: (NsdServiceInfo) -> Unit
) : BaseViewHolder<NsdServiceInfo>(
        LayoutInflater.from(parent.context).inflate(R.layout.item_service, parent, false)
) {

    private lateinit var data: NsdServiceInfo

    init {
        itemServiceAddressBtn.setOnClickListener {
            onServiceSelected(data)
        }
    }

    override fun bindView(item: NsdServiceInfo) {
        data = item
        itemServiceAddressBtn.text = item.host.hostAddress
    }
}
