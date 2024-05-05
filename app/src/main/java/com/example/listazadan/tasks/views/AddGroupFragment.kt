package com.example.listazadan.tasks.views

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.listazadan.MyApp
import com.example.listazadan.R
import com.example.listazadan.data.models.Group
import com.example.listazadan.databinding.FragmentAddGroupBinding
import com.example.listazadan.tasks.viewmodel.GroupViewModel
import com.example.listazadan.tasks.viewmodel.GroupViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class AddGroupFragment : Fragment() {

    private var _binding: FragmentAddGroupBinding? = null
    private val binding get() = _binding!!

    private lateinit var groupViewModel: GroupViewModel

    private var groupID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGroupBinding.inflate(inflater, container, false)

        val groupFactory = GroupViewModelFactory((requireActivity().application as MyApp).groupRepository)
        groupViewModel = ViewModelProvider(this, groupFactory)[GroupViewModel::class.java]
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Padding na bottom navigation
        val bottomPadding = resources.getDimensionPixelSize(R.dimen.bottom_nav_height) // Definiujesz w dimens.xml
        this.view?.setPadding(0, 0, 0, bottomPadding)

        // Ukrywanie toolbar i bottom navigation
        (activity as MainActivity).supportActionBar?.hide()
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.GONE

        groupID = arguments?.getInt("groupID") ?: -1

        observeViewModel()
        configureListeners()

    }

    override fun onDestroyView() {
        super.onDestroyView()

        (activity as MainActivity).supportActionBar?.show()
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.VISIBLE

        _binding = null
    }

    private fun observeViewModel(){
        if (groupID != -1){
            groupViewModel.getGroupById(groupID).observe(viewLifecycleOwner){ group ->
                binding.edGroupName.setText(group.name)
            }
        }
    }

    private fun showAlertDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Brak Nazwy!")
            .setMessage("Niemożliwe utworzenie grupy bez nazwy. Proszę uzupełnij pole.")
            .setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
            .show()
    }

    private fun configureListeners(){
        binding.grBtnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.grBtnSave.setOnClickListener {
            val grName: String = binding.edGroupName.text.toString()

            if (grName.isBlank()){
                showAlertDialog()
            }
            else{
                if (groupID != -1){
                    val updatesGroup = Group(
                        groupId = groupID,
                        name = grName
                    )
                    groupViewModel.updateGroup(updatesGroup)
                }
                else{
                    val newGroup = Group(
                        name = grName
                    )
                    groupViewModel.addGroup(newGroup)
                }
                findNavController().popBackStack()
            }
        }


    }

}