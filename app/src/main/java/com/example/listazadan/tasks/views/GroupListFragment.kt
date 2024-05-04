package com.example.listazadan.tasks.views

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listazadan.data.models.Group
import com.example.listazadan.databinding.FragmentGroupListBinding
import com.example.listazadan.tasks.adapters.GroupAdapter
import com.example.listazadan.tasks.viewmodel.GroupViewModel

class GroupListFragment : Fragment() {

    private var _binding: FragmentGroupListBinding? = null
    private val binding get() = _binding!!

    private val groupViewModel: GroupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupAdapter(
            onGroupClick = {
                group: Group ->  true
            },
            onGroupDeleteClick = {
                group: Group -> true
            },
            onGroupEditClick = {
                group: Group -> true
            }
        )
        binding.recyclerViewGroup.adapter = adapter
        binding.recyclerViewGroup.layoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}