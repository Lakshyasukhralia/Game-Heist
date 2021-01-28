package com.sukhralia.gameheist

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sukhralia.gameheist.databinding.FragmentDealPageBinding
import com.sukhralia.gameheist.models.DealModel
import com.sukhralia.gameheist.utils.GlideApp


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DealPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DealPageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var dealModel: DealModel? = null
    private lateinit var binding: FragmentDealPageBinding
    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            dealModel = it.get("dealModel") as DealModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentDealPageBinding>(
            inflater,
            R.layout.fragment_deal_page,
            container,
            false
        )

        dealModel?.let {
            binding.title.text = it.title
            binding.plt.text = it.platforms
            binding.type.text = it.type
            binding.desc.text = it.description
            binding.instruction.text = it.instructions
            binding.users.text = "${it.users}+ Claimed"

            if (it.worth != "N/A")
                binding.worth.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = it.worth
                }

            GlideApp.with(mContext)
                .load(it.thumbnail)
                .into(binding.image)

            when (it.status) {
                "Active" -> {
                    binding.image.mColor =
                        ContextCompat.getColor(mContext, R.color.lime_green)
                    binding.image.mBannerText = "  FREE"
                    binding.image.mAlpha = 0
                    binding.claim.isEnabled = true
                }
                else -> {
                    binding.image.mColor = ContextCompat.getColor(mContext, R.color.dove_gray)
                    binding.image.mBannerText = "  EXPIRED"
                    binding.image.mAlpha = 150
                    binding.claim.isEnabled = false
                }
            }

            val url = it.open_giveaway_url
            binding.claim.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
        }


        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DealPageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DealPageFragment().apply {
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
}