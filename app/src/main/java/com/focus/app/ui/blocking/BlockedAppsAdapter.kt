package com.focus.app.ui.blocking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.focus.app.data.database.entity.BlockedApp
import com.focus.app.databinding.ItemBlockedAppBinding

class BlockedAppsAdapter(
    private val onToggle: (BlockedApp) -> Unit,
    private val onDelete: (BlockedApp) -> Unit
) : ListAdapter<BlockedApp, BlockedAppsAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(val binding: ItemBlockedAppBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBlockedAppBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val app = getItem(position)
        with(holder.binding) {
            textAppName.text = app.appName
            textPackageName.text = app.packageName
            textQuota.text = "Opens: ${app.openCount}/${app.dailyQuota}"
            switchEnabled.setOnCheckedChangeListener(null)
            switchEnabled.isChecked = app.isEnabled
            switchEnabled.setOnCheckedChangeListener { _, _ -> onToggle(app) }
            buttonDelete.setOnClickListener { onDelete(app) }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<BlockedApp>() {
            override fun areItemsTheSame(old: BlockedApp, new: BlockedApp) =
                old.packageName == new.packageName

            override fun areContentsTheSame(old: BlockedApp, new: BlockedApp) = old == new
        }
    }
}
