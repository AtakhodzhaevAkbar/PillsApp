package com.example.pillsapp.ui.fragment

import MyDialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pillsapp.databinding.FragmentFirstBinding
import com.example.pillsapp.ui.adapter.ItemAdapter
import com.example.pillsapp.data.Item

class MainFragment : Fragment(), ItemAdapter.OnItemClickListener {

    private lateinit var binding: FragmentFirstBinding
    private lateinit var adapter: ItemAdapter
    private val itemList: MutableList<Item> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ItemAdapter(itemList)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        adapter.setOnItemClickListener(this)

        binding.addItem.setOnClickListener {
            MyDialogFragment().show(childFragmentManager, MyDialogFragment.TAG)
        }
    }

    override fun onItemClick(position: Int) {
        val item = itemList[position]
        val dialog = MyDialogFragment()
        val args = Bundle()
        args.putParcelable("item", item)
        dialog.arguments = args
        dialog.show(childFragmentManager, MyDialogFragment.TAG)
    }
}
