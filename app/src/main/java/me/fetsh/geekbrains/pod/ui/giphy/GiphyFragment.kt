package me.fetsh.geekbrains.pod.ui.giphy

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import coil.load
import me.fetsh.geekbrains.pod.R
import me.fetsh.geekbrains.pod.databinding.GiphyFragmentBinding

class GiphyFragment : Fragment() {

    companion object {
        fun newInstance() = GiphyFragment()
    }

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
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner, { renderData(it) })
    }

    private fun renderData(data: GiphyData) {
        when (data) {
            is GiphyData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.gif
                Log.d("GIF", serverResponseData.gif!!)
                if (url.isNullOrEmpty()) {
                    toast("Link is empty")
                } else {
                    binding.giphyView.load(url) {
                        lifecycle(this@GiphyFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                }
            }
            is GiphyData.Loading -> {
                //showLoading()
            }
            is GiphyData.Error -> {
                //showError(data.error.message)
                toast(data.error.message)
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