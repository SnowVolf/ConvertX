package ru.svolf.convertx.presentation.screens.history

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.ClickListener
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.drag.ItemTouchCallback
import com.mikepenz.fastadapter.drag.SimpleDragCallback
import com.mikepenz.fastadapter.swipe.SimpleSwipeCallback
import com.mikepenz.fastadapter.swipe_drag.SimpleSwipeDragCallback
import com.mikepenz.fastadapter.utils.DragDropUtil
import ru.svolf.convertx.R
import ru.svolf.convertx.data.model.HistoryVH
import ru.svolf.convertx.databinding.FragmentHistoryBinding
import ru.svolf.convertx.utils.DecoderConst
import java.util.*

/**
 * Created by Snow Volf on 21.06.2017, 8:12
 */
class HistoryFragment : Fragment(), ItemTouchCallback, SimpleSwipeCallback.ItemSwipeCallback {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HistoryViewModel>()
    private val itemHolders = FastItemAdapter<HistoryVH>()
    private val fastItemAdapter by lazy { FastAdapter.with(itemHolders) }

    //drag & drop
    private lateinit var touchCallback: SimpleDragCallback
    private lateinit var touchHelper: ItemTouchHelper

    private val deleteHandler = Handler(Looper.getMainLooper()) {
        val item = it.obj as HistoryVH
        item.swipedAction = null
        val pos12 = itemHolders.getAdapterPosition(item)
        if (pos12 != RecyclerView.NO_POSITION) {
            viewModel.remove(item.identifier)
        }
        true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.historyList.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = fastItemAdapter
        }
        viewModel.data.observe(viewLifecycleOwner) { list ->
            binding.textEmpty.visibility = if (list.isNullOrEmpty()) View.VISIBLE else View.GONE

            FastAdapterDiffUtil[itemHolders.itemAdapter] = list
        }

        //add drag and drop for item
        //and add swipe as well
        val leaveBehindDrawableLeft = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)
        touchCallback = SimpleSwipeDragCallback(
            this,
            this,
            leaveBehindDrawableLeft,
            ItemTouchHelper.LEFT,
            Color.RED
        )
            .withSensitivity(5f)
            .withSurfaceThreshold(0.3f)

        touchHelper =
            ItemTouchHelper(touchCallback) // Create ItemTouchHelper and pass with parameter the SimpleDragCallback
        touchHelper.attachToRecyclerView(binding.historyList) // Attach ItemTouchHelper to RecyclerView

        fastItemAdapter.onClickListener = object : ClickListener<HistoryVH> {
            override fun invoke(v: View?, adapter: IAdapter<HistoryVH>, item: HistoryVH, position: Int): Boolean {
                val sourceItem = viewModel.getItem(item.identifier)
                val bundle = bundleOf(
                    "input_string" to sourceItem.input,
                    "output_string" to sourceItem.output,
                    "decoder_switch" to sourceItem.spinnerPosition
                )
                return when (item.decoder) {
                    DecoderConst.UNICODE -> {
                        findNavController().navigate(R.id.unicodeFragment, bundle)
                        true
                    }
                    DecoderConst.BASE64 -> {
                        findNavController().navigate(R.id.base64Fragment, bundle)
                        true
                    }
                    DecoderConst.HEX -> {
                        findNavController().navigate(R.id.hexFragment, bundle)
                        true
                    }
                    else -> false
                }
            }

        }
    }

    override fun itemSwiped(position: Int, direction: Int) {
        val item = itemHolders.getAdapterItem(position) ?: return
        item.swipedDirection = direction

        // This can vary depending on direction but remove & archive simulated here both results in
        // removal from list
        val message = Random().nextInt()
        deleteHandler.sendMessageDelayed(Message.obtain().apply { what = message; obj = item }, 400)

        item.swipedAction = Runnable {
            deleteHandler.removeMessages(message)

            item.swipedDirection = 0
            val position1 = itemHolders.getAdapterPosition(item)
            if (position1 != RecyclerView.NO_POSITION) {
                viewModel.remove(item.identifier)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun itemTouchOnMove(oldPosition: Int, newPosition: Int): Boolean {
        DragDropUtil.onMove(itemHolders, oldPosition, newPosition)  // change position
        return true
    }
}