package com.mexicandeveloper.platformsciencecode.ui.drivers

import android.annotation.SuppressLint
import android.content.ClipData
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mexicandeveloper.platformsciencecode.R
import com.mexicandeveloper.platformsciencecode.databinding.RowDriverBinding
import com.mexicandeveloper.platformsciencecode.placeholder.PlaceholderContent

class DriversRVAdapter(
    private val values: List<PlaceholderContent.PlaceholderItem>,
    private val itemDetailFragmentContainer: View?
) : RecyclerView.Adapter<DriversRVAdapter.DriversViewHolder>() {

    private var selected: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriversViewHolder {

        val binding =
            RowDriverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DriversViewHolder(binding)

    }

    override fun onBindViewHolder(holder: DriversViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)

        with(holder.itemView) {

            tag = item

            setOnClickListener { itemView ->
                val info = tag as PlaceholderContent.PlaceholderItem
                notifyItemChanged(selected)
                selected = itemView.findViewById<TextView>(R.id.tvDriverId).tag as Int
                notifyItemChanged(selected)

                val bundle = Bundle()
                bundle.putString(
                    com.mexicandeveloper.platformsciencecode.ui.shipments.ShipmentsFragment.ARG_ITEM_ID,
                    info.id
                )
                if (itemDetailFragmentContainer != null) {
                    itemDetailFragmentContainer.findNavController()
                        .navigate(
                            com.mexicandeveloper.platformsciencecode.R.id.fragment_item_detail,
                            bundle
                        )
                } else {
                    itemView.findNavController().navigate(
                        com.mexicandeveloper.platformsciencecode.R.id.show_item_detail,
                        bundle
                    )
                }
            }
            /**
             * Context click listener to handle Right click events
             * from mice and trackpad input to provide a more native
             * experience on larger screen devices
             */
            setOnContextClickListener { v ->
                val item = v.tag as PlaceholderContent.PlaceholderItem
                android.widget.Toast.makeText(
                    v.context,
                    "Context click of item " + item.id,
                    android.widget.Toast.LENGTH_LONG
                ).show()
                true
            }


            setOnLongClickListener { v ->
                // Setting the item id as the clip data so that the drop target is able to
                // identify the id of the content
                val clipItem = android.content.ClipData.Item(item.id)
                val dragData = ClipData(
                    v.tag as? CharSequence,
                    arrayOf(android.content.ClipDescription.MIMETYPE_TEXT_PLAIN),
                    clipItem
                )


                v.startDragAndDrop(
                    dragData,
                    android.view.View.DragShadowBuilder(v),
                    null,
                    0
                )

            }
        }
    }

    override fun getItemCount() = values.size

    inner class DriversViewHolder(var binding: RowDriverBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(info: PlaceholderContent.PlaceholderItem) {
            binding.tvDriverId.tag = bindingAdapterPosition
            binding.tvDriverId.text = info.id
            binding.tvDriverName.text = info.name
            if (selected == bindingAdapterPosition) {
                binding.cardDriver.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.cardDriver.context,
                        R.color.dark
                    )
                )
                binding.tvDriverName.setTextColor(
                    ContextCompat.getColor(
                        binding.tvDriverName.context,
                        R.color.white
                    )
                )
                binding.tvDriverId.setTextColor(
                    ContextCompat.getColor(
                        binding.tvDriverId.context,
                        R.color.white
                    )
                )
            } else {
                binding.cardDriver.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.cardDriver.context,
                        R.color.white
                    )
                )
                binding.tvDriverName.setTextColor(
                    ContextCompat.getColor(
                        binding.tvDriverName.context,
                        R.color.primary
                    )
                )
                binding.tvDriverId.setTextColor(
                    ContextCompat.getColor(
                        binding.tvDriverId.context,
                        R.color.primary
                    )
                )
            }
        }
    }

}
