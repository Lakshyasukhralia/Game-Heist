package com.sukhralia.gameheist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sukhralia.gameheist.adapters.GameDealsAdapter
import com.sukhralia.gameheist.databinding.FragmentDealListingBinding
import com.sukhralia.gameheist.models.DealModel
import com.sukhralia.gameheist.viewmodels.DealViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DealFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@ExperimentalCoroutinesApi
class DealFragment : Fragment(), AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentDealListingBinding
    private val viewModel: DealViewModel by viewModels()
    private lateinit var mContext: MainActivity
    private lateinit var dealAdapter: GameDealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_deal_listing, container, false)

        binding.dealList.let {
            it.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            dealAdapter = GameDealsAdapter()
            it.adapter = dealAdapter
        }

        if(!viewModel.viewModelInitialized) {
            viewModel.getDeals(
                mContext.resources.getStringArray(R.array.platform)[0],
                mContext.resources.getStringArray(R.array.type)[0],
                mContext.resources.getStringArray(R.array.sort_by)[0]
            )
            viewModel.viewModelInitialized = true
        }

        lifecycleScope.launchWhenStarted {
            withContext(Dispatchers.Main) {
                viewModel.response.collect() {
                    when (it) {
                        is DealViewModel.ResponseState.Success -> {
                            Snackbar.make(binding.root, "Success", Snackbar.LENGTH_LONG).show()
                            loadDealAdapter(it.data)
                        }
                        is DealViewModel.ResponseState.Error -> {
                            Snackbar.make(binding.root, "Error", Snackbar.LENGTH_LONG).show()
                            loadDealAdapter(listOf<DealModel>())
                        }
                        is DealViewModel.ResponseState.Loading -> {
                            Snackbar.make(binding.root, "Loading", Snackbar.LENGTH_LONG).show()
                        }
                        else -> {
                        }
                    }
                }
            }
        }

        setFilter(R.array.platform, binding.filter1)
        setFilter(R.array.type, binding.filter2)
        setFilter(R.array.sort_by, binding.filter3)

        return binding.root
    }

    private fun setFilter(id: Int, spinner: Spinner) {
        ArrayAdapter.createFromResource(
            mContext,
            id,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.setSelection(0, false)
        spinner.onItemSelectedListener = this
    }

    private fun loadDealAdapter(modelList: List<DealModel>) {

        if (modelList.isNotEmpty()) {
            binding.dealList.visibility = View.VISIBLE
            binding.noData.visibility = View.GONE
        } else {
            binding.dealList.visibility = View.GONE
            binding.noData.visibility = View.VISIBLE
        }

        dealAdapter.submitData(modelList)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DealFrMyAdapteragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DealFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as MainActivity
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when (parent) {
            binding.filter1 -> {
                viewModel.getDeals(
                    plt = mContext.resources.getStringArray(R.array.platform)[position]
                )
            }
            binding.filter2 -> {
                viewModel.getDeals(
                    type = mContext.resources.getStringArray(R.array.type)[position]
                )
            }
            binding.filter3 -> {
                viewModel.getDeals(
                    sort = mContext.resources.getStringArray(R.array.sort_by)[position]
                )
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        viewModel.getDeals(
            mContext.resources.getStringArray(R.array.platform)[0],
            mContext.resources.getStringArray(R.array.type)[0],
            mContext.resources.getStringArray(R.array.sort_by)[0]
        )
    }
}