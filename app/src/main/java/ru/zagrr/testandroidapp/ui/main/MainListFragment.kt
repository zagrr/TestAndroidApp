package ru.zagrr.testandroidapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import ru.zagrr.testandroidapp.R
import ru.zagrr.testandroidapp.databinding.FragmentMainListBinding
import ru.zagrr.testandroidapp.databinding.FragmentMainListItemBinding
import ru.zagrr.testandroidapp.model.User
import ru.zagrr.testandroidapp.utils.ui.ListEventsListener
import ru.zagrr.testandroidapp.utils.ui.OnActionItemClickListener
import ru.zagrr.testandroidapp.utils.ui.PrimaryActionModeCallback
import ru.zagrr.testandroidapp.utils.ui.onQueryTextChanged
import java.util.ArrayList

class MainListFragment : Fragment(R.layout.fragment_main_list), ListEventsListener<User>,
    OnActionItemClickListener {

    private lateinit var binding : FragmentMainListBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val listAdapter = UsersListAdapter(this)

    private var selectedItems  = ArrayList<User>()

    private val primaryActionModeCallback : PrimaryActionModeCallback by lazy {

        val it = PrimaryActionModeCallback()
        it.actionItemClickListener = this@MainListFragment
        it
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainListBinding.bind(view).apply {

            userList.apply {
                adapter = listAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        viewModel.users.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }

        setHasOptionsMenu(true)

        selectedItems = (savedInstanceState?.getParcelableArrayList("SelectedItems") ?: ArrayList<User>())

        if (savedInstanceState?.getBoolean("ActionMode", false) == true) {

            primaryActionModeCallback.startActionMode(binding.userList, R.menu.main_actions)
            primaryActionModeCallback.setTitle(selectedItems.size.toString())
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            viewModel.usersEvent.collectLatest { event ->

                when (event) {

                    is MainViewModel.UsersEvents.UsersDeleted -> {
                        Snackbar.make(requireView(), "Users deleted", Snackbar.LENGTH_LONG)
                            .setAction("Cancel") {
                                viewModel.saveUsers(event.users)
                            }.show()
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putBoolean("ActionMode", primaryActionModeCallback.isStarted);
        outState.putParcelableArrayList("SelectedItems", selectedItems)
        super.onSaveInstanceState(outState)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged{
            viewModel.getUsers(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.action_refresh -> {
                viewModel.refreshUsers()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }


    override fun onActionItemClick(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.action_delete -> {
                viewModel.deleteUsers(
                    ArrayList(selectedItems)
                )
                true
            }
            else ->
                true
        }
    }

    override fun onDestroyActionMode() {
        selectedItems.clear()
        listAdapter.notifyDataSetChanged()
    }

    override fun onStartActionMode() = Unit

    private fun handleItemSelection(item: User, position: Int) {

        val isAlreadySelected = selectedItems.contains(item)

        if (isAlreadySelected)
            selectedItems.remove(item)
        else
            selectedItems.add(item)

        listAdapter.notifyItemChanged(position)

        if (selectedItems.isEmpty())
            primaryActionModeCallback.finishActionMode()
        else
            primaryActionModeCallback.setTitle(selectedItems.size.toString())
    }



    override fun onItemLongClick(item: User, position: Int) {

        if (!primaryActionModeCallback.isStarted) {
            primaryActionModeCallback.startActionMode(binding.userList, R.menu.main_actions)

            handleItemSelection(item, position)
        }
    }

    override fun onItemClick(item: User, position: Int) {

        if (primaryActionModeCallback.isStarted)
            handleItemSelection(item, position)
    }


    override fun isItemSelected(item: User) = selectedItems.contains(item)


    private class UsersListAdapter(private val listEventsListener : ListEventsListener<User>?) : ListAdapter<User, UsersListAdapter.ViewHolder>(DiffCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

             val binding = FragmentMainListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val currentItem = getItem(position)
            holder.bind(currentItem)
        }

        inner class ViewHolder(private val binding: FragmentMainListItemBinding) : RecyclerView.ViewHolder(binding.root) {

            init {
                listEventsListener?.let {
                    binding.apply {

                        root.setOnLongClickListener {

                            val position = bindingAdapterPosition
                            if (position != RecyclerView.NO_POSITION) {

                                val item = getItem(position)
                                listEventsListener.onItemLongClick(item, position)
                            }
                            true
                        }
                        root.setOnClickListener {

                            val position = bindingAdapterPosition
                            if (position != RecyclerView.NO_POSITION) {

                                val item = getItem(position)
                                listEventsListener.onItemClick(item, position)
                            }
                        }
                    }
                }
            }

            fun bind(user: User) {

                binding.apply {

                    binding.tvName.text = user.name
                    binding.tvUsername.text = user.username
                    binding.tvEmail.text = user.email
                    binding.tvPhone.text = user.phone
                    binding.tvWebsite.text = user.website

                    val firstLetter = if (user.username.isEmpty()) "-" else user.username[0]

                    binding.tvImgName.text = firstLetter.toString()

                    val imgBackground = when (firstLetter) {

                        in 'A'..'K', in 'a'..'k' ->  R.color.red
                        in 'L'..'R', in 'l'..'r' ->  R.color.blue
                        in 'S'..'Z', in 's'..'z' ->  R.color.green
                        else -> R.color.black
                    }

                    binding.tvImgName.setBackgroundResource(imgBackground)

                    binding.imgSelected.visibility  = if (listEventsListener?.isItemSelected(user) == true) View.VISIBLE else View.GONE
                }
            }
        }

        class DiffCallback : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(oldItem: User, newItem: User) =
                (oldItem.id == newItem.id)

            override fun areContentsTheSame(oldItem: User, newItem: User) =
                (oldItem == newItem)
        }
    }
}