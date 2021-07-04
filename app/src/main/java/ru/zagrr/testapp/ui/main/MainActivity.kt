package ru.zagrr.testapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.zagrr.testapp.R
import ru.zagrr.testapp.databinding.ActivityMainBinding
import ru.zagrr.testapp.databinding.ListItemBinding
import ru.zagrr.testapp.model.User

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var listAdapter : ListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userList.layoutManager = LinearLayoutManager(this)
        listAdapter = ListAdapter(emptyList())
        binding.userList.adapter = listAdapter

        viewModel.users.observe(this) {
            listAdapter.submitList(it)
            listAdapter.notifyDataSetChanged()
        }
     }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.action_refresh -> {
                viewModel.refreshUsers()
                true
            }

            R.id.action_delete -> {
                viewModel.deleteAllUsers()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }
}

private class ListAdapter(
    var users :  List<User>
) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    fun submitList(list: List<User>) {
        users = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = users.size

    inner class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {

            val user = users[position]
            binding.id.text = user.id.toString()
            binding.name.text = user.name
            binding.username.text = user.username
            binding.email.text = user.email
            binding.phone.text = user.phone
            binding.website.text = user.website
        }
    }

}




