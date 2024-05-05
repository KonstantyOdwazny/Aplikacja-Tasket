package com.example.listazadan.tasks.views

import android.content.res.Resources
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listazadan.MyApp
import com.example.listazadan.R
import com.example.listazadan.data.models.Group
import com.example.listazadan.databinding.FragmentGroupListBinding
import com.example.listazadan.tasks.adapters.GroupAdapter
import com.example.listazadan.tasks.viewmodel.GroupViewModel
import com.example.listazadan.tasks.viewmodel.GroupViewModelFactory

class GroupListFragment : Fragment() {

    private var _binding: FragmentGroupListBinding? = null
    private val binding get() = _binding!!

    private lateinit var groupViewModel: GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            val topPadding = resources.getDimensionPixelSize(R.dimen.toolbar_height) // Definiujesz w dimens.xml
            val bottomPadding = resources.getDimensionPixelSize(R.dimen.bottom_nav_height) // Definiujesz w dimens.xml
            view.setPadding(0, topPadding, 0, bottomPadding)
        }catch (e: Resources.NotFoundException) {
            Log.e("TAG2", "Resource not found: " + e.message)
        }


        val adapter = GroupAdapter(
            onGroupClick = {
                group: Group ->  true
            },
            onGroupDeleteClick = { group: Group ->
                groupViewModel.deleteGroup(group)
            },
            onGroupEditClick = { group: Group ->
                val bundle = Bundle()
                bundle.putInt("groupID", group.groupId)
                findNavController().navigate(R.id.action_groupListFragment_to_addGroupFragment2, bundle)
            }
        )
        binding.recyclerViewGroup.adapter = adapter
        binding.recyclerViewGroup.layoutManager = LinearLayoutManager(context)

        groupViewModel.groups.observe(viewLifecycleOwner, Observer { groups ->
            adapter.updateGroups(groups)
        })

        binding.groupFab.setOnClickListener {
            findNavController().navigate(R.id.action_groupListFragment_to_addGroupFragment2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupListBinding.inflate(inflater, container, false)
        val groupFactory = GroupViewModelFactory((requireActivity().application as MyApp).groupRepository)
        groupViewModel = ViewModelProvider(this, groupFactory)[GroupViewModel::class.java]

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}