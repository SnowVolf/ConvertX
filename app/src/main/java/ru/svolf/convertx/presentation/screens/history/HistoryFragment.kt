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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.drag.ItemTouchCallback
import com.mikepenz.fastadapter.drag.SimpleDragCallback
import com.mikepenz.fastadapter.swipe.SimpleSwipeCallback
import com.mikepenz.fastadapter.swipe_drag.SimpleSwipeDragCallback
import com.mikepenz.fastadapter.utils.DragDropUtil
import ru.svolf.convertx.R
import ru.svolf.convertx.data.model.HistoryVH
import ru.svolf.convertx.databinding.FragmentHistoryBinding
import java.util.*
import java.util.stream.Collectors

/**
 * Created by Snow Volf on 21.06.2017, 8:12
 */
class HistoryFragment : Fragment(), ItemTouchCallback, SimpleSwipeCallback.ItemSwipeCallback {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HistoryViewModel
    private val itemAdapter = ItemAdapter<HistoryVH>()
    private val fastItemAdapter by lazy { FastAdapter.with(itemAdapter) }

    //drag & drop
    private lateinit var touchCallback: SimpleDragCallback
    private lateinit var touchHelper: ItemTouchHelper

    private val deleteHandler = Handler(Looper.getMainLooper()) {
        val item = it.obj as HistoryVH
        item.swipedAction = null
        val pos12 = itemAdapter.getAdapterPosition(item)
        if (pos12 != RecyclerView.NO_POSITION) {
            itemAdapter.itemFilter.remove(pos12)
            viewModel.remove(item.id!!)
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.historyList.apply {
            adapter = fastItemAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        viewModel.data.observe(viewLifecycleOwner) { list ->
            binding.textEmpty.visibility = if (list.isNullOrEmpty()) View.VISIBLE else View.GONE
            itemAdapter.set(list.stream()
                .map(::HistoryVH)
                .collect(Collectors.toList()).reversed())
        }

        //add drag and drop for item
        //and add swipe as well
        val leaveBehindDrawableLeft = ContextCompat.getDrawable(requireContext(), R.drawable.ic_remove)
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

    }

    override fun itemSwiped(position: Int, direction: Int) {
        val item = itemAdapter.getAdapterItem(position) ?: return
        item.swipedDirection = direction

        // This can vary depending on direction but remove & archive simulated here both results in
        // removal from list
        val message = Random().nextInt()
        deleteHandler.sendMessageDelayed(Message.obtain().apply { what = message; obj = item }, 400)

        item.swipedAction = Runnable {
            deleteHandler.removeMessages(message)

            item.swipedDirection = 0
            val position1 = itemAdapter.getAdapterPosition(item)
            if (position1 != RecyclerView.NO_POSITION) {
                fastItemAdapter.notifyItemChanged(position1)
            }
        }
        fastItemAdapter.notifyItemChanged(position)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun itemTouchOnMove(oldPosition: Int, newPosition: Int): Boolean {
        DragDropUtil.onMove(itemAdapter, oldPosition, newPosition)  // change position
        return true
    }
}