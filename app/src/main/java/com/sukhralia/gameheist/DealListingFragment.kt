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
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sukhralia.gameheist.adapters.GameDealsAdapter
import com.sukhralia.gameheist.database.DealDatabase
import com.sukhralia.gameheist.databinding.FragmentDealListingBinding
import com.sukhralia.gameheist.models.DealModel
import com.sukhralia.gameheist.utils.GameHeistApp
import com.sukhralia.gameheist.viewmodels.DealViewModel
import com.sukhralia.gameheist.viewmodels.DealViewModelFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

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
    private lateinit var viewModel: DealViewModel
    private lateinit var mContext: MainActivity
    private lateinit var dealAdapter: GameDealsAdapter
    private var myView: View? = null
    private lateinit var dataStore: DataStore<Preferences>

    private var platform: Deferred<String>? = null
    private var type: Deferred<String>? = null
    private var sort: Deferred<String>? = null

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

        if (myView != null)
            return myView

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_deal_listing, container, false)

        dataStore = mContext.createDataStore(name = "settings")

        binding.dealList.let {
            it.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            dealAdapter = GameDealsAdapter()
            it.adapter = dealAdapter
        }

        val application = requireNotNull(this.activity).application

        val dataSource = DealDatabase.getInstance(application).dealDatabaseDao

        val dealViewModelFactory =
            DealViewModelFactory(
                dataSource, application
            )

        viewModel = ViewModelProviders.of(this, dealViewModelFactory).get(DealViewModel::class.java)

        lifecycleScope.launchWhenStarted {

            platform = async { GameHeistApp.instance(mContext).readPreferences("plt")!! }
            type = async { GameHeistApp.instance(mContext).readPreferences("type")!! }
            sort = async { GameHeistApp.instance(mContext).readPreferences("sort")!! }

            setFilter(R.array.platform, binding.filter1)
            setFilter(R.array.type, binding.filter2)
            setFilter(R.array.sort_by, binding.filter3)

            viewModel.getDeals(
                platform?.await() ?: mContext.resources.getStringArray(R.array.platform)[0],
                type?.await() ?: mContext.resources.getStringArray(R.array.type)[0],
                sort?.await() ?: mContext.resources.getStringArray(R.array.sort_by)[0]
            )

            withContext(Dispatchers.Main) {
                viewModel.response.collect() {
                    when (it) {
                        is DealViewModel.ResponseState.Success -> {

//                            withContext(Dispatchers.IO) {
//                                if (viewModel.checkDb) {
//                                    viewModel.checkNewDeals()
//                                }
//                            }

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

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                dealAdapter.filter.filter(newText)
                return false
            }
        })

        myView = binding.root

        return myView
    }


    private fun setFilter(id: Int, spinner: Spinner) {
        val arrayAdapter = ArrayAdapter.createFromResource(
            mContext,
            id,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.setSelection(0, false)

        lifecycleScope.launch {
            when (id) {
                R.array.type -> {
                    type?.await()?.let {
                        spinner.setSelection(arrayAdapter.getPosition(it), false)
                    }
                }
                R.array.platform -> {
                    platform?.await()?.let {
                        spinner.setSelection(arrayAdapter.getPosition(it), false)
                    }
                }
                R.array.sort_by -> {
                    sort?.await()?.let {
                        spinner.setSelection(arrayAdapter.getPosition(it), false)
                    }
                }
            }
        }
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
//        viewModel.getDeals(
//            mContext.resources.getStringArray(R.array.platform)[0],
//            mContext.resources.getStringArray(R.array.type)[0],
//            mContext.resources.getStringArray(R.array.sort_by)[0]
//        )
    }

    override fun onStop() {
        super.onStop()
        lifecycleScope.launch {
            GameHeistApp.instance(mContext).savePreferences("plt", viewModel.mPlt)
            GameHeistApp.instance(mContext).savePreferences("type", viewModel.mType)
            GameHeistApp.instance(mContext).savePreferences("sort", viewModel.mSort)
        }
    }
}
