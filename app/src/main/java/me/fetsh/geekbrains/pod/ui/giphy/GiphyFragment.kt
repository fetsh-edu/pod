package me.fetsh.geekbrains.pod.ui.giphy

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import me.fetsh.geekbrains.pod.R
import me.fetsh.geekbrains.pod.closeKeyboard
import me.fetsh.geekbrains.pod.databinding.GiphyFragmentBinding


class GiphyFragment : Fragment() {

    private var _binding: GiphyFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GiphyViewModel by lazy {
        ViewModelProvider(this).get(GiphyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = GiphyFragmentBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        when (viewModel.liveData.value) {
            is GiphyData.NotAsked -> {
                viewModel.sendServerRequest()
            }
            else -> {}
        }
        viewModel.liveData.observe(viewLifecycleOwner, { renderData(it) })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar, menu);

        val search = menu.findItem(R.id.action_search)
        val searchText = search.actionView as SearchView

        searchText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.sendServerRequest(tag = query)
                activity?.closeKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
    }

    private fun renderData(data: GiphyData) {
        when (data) {
            is GiphyData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.gif
                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {
                    binding.giphyView.load(url) {
                        lifecycle(this@GiphyFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                    binding.giphyTitle.text = serverResponseData.data?.title
                }
            }
            is GiphyData.Loading -> {
                //showLoading()
            }
            is GiphyData.Error -> {
                //showError(data.error.message)
                toast(data.error.message)
            }
            GiphyData.NotAsked -> {
                //showNotAsked
            }
        }
    }
    private fun Fragment.toast(string: String?) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.BOTTOM, 0, 250)
            show()
        }
    }
}