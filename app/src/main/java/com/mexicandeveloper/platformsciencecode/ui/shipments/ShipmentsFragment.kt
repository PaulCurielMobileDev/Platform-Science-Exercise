package com.mexicandeveloper.platformsciencecode.ui.shipments

import android.content.ClipData
import android.os.Bundle
import android.view.DragEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.mexicandeveloper.platformsciencecode.placeholder.PlaceholderContent
import com.mexicandeveloper.platformsciencecode.databinding.FragmentShipmentsBinding

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
class ShipmentsFragment : Fragment() {

    private var listText = false

    /**
     * The placeholder content this fragment is presenting.
     */
    private var item: PlaceholderContent.PlaceholderItem? = null

    private var _binding: FragmentShipmentsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val dragListener = View.OnDragListener { v, event ->
        if (event.action == DragEvent.ACTION_DROP) {
            val clipDataItem: ClipData.Item = event.clipData.getItemAt(0)
            val dragData = clipDataItem.text
            item = PlaceholderContent.ITEM_MAP[dragData]
            updateContent()
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the placeholder content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = PlaceholderContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShipmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateContent()
        binding.root.setOnDragListener(dragListener)
        binding.btnDetail?.setOnClickListener {
            listText = !listText
            updateContent()
        }
        binding.toolbarLayout?.title = item?.name
    }

    private fun updateContent() {

        if (listText) binding.btnDetail?.setImageDrawable(
            ContextCompat.getDrawable(requireContext(), android.R.drawable.star_on)
        )
        else binding.btnDetail?.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                android.R.drawable.ic_menu_sort_by_size
            )
        )
        // Show the placeholder content as text in a TextView.
        item?.let {
            binding.itemDetail.text = if (listText) {
                it.details
            } else {
                if (PlaceholderContent.BESTSCORE.isEmpty()) it.details else PlaceholderContent.SHIPMENTS.get(
                    PlaceholderContent.BESTSCORE[it.id.toInt() - 1]
                )
            }
        }
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}