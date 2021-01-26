package com.sukhralia.gameheist.adapters

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.sukhralia.gameheist.R
import com.sukhralia.gameheist.databinding.GameDealsTileBinding
import com.sukhralia.gameheist.models.DealModel
import com.sukhralia.gameheist.utils.BannerImageView
import com.sukhralia.gameheist.utils.GlideApp

class GameDealsAdapter : RecyclerView.Adapter<GameDealsAdapter.MyViewHolder>() {

    lateinit var mContext: Context

    var myData = listOf<DealModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun submitData(data: List<DealModel>) {
        myData = data
    }

    inner class MyViewHolder(val myItemView: GameDealsTileBinding) :
        RecyclerView.ViewHolder(myItemView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        mContext = parent.context

        return MyViewHolder(
            DataBindingUtil.inflate<GameDealsTileBinding>(
                LayoutInflater.from(parent.context),
                R.layout.game_deals_tile,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = myData[position]

        holder.myItemView.title.text = item.title

        GlideApp.with(mContext)
            .load(item.thumbnail)
            .into(holder.myItemView.image)

        if (item.worth != "N/A")
            holder.myItemView.worth.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                text = item.worth
            }

        holder.myItemView.type.text = item.type

        holder.myItemView.users.text = "${item.users}+ Claimed"

        holder.myItemView.plt.text = item.platforms

        when (item.status) {
            "Active" -> {
                holder.myItemView.container.isEnabled = true
                holder.myItemView.image.mColor =
                    ContextCompat.getColor(mContext, R.color.lime_green)
                holder.myItemView.image.mBannerText = "  FREE"
                holder.myItemView.image.mAlpha = 0
            }
            else -> {
                holder.myItemView.container.isEnabled = false
                holder.myItemView.image.mColor = ContextCompat.getColor(mContext, R.color.dove_gray)
                holder.myItemView.image.mBannerText = "  EXPIRED"
                holder.myItemView.image.mAlpha = 150
            }
        }

        holder.myItemView.image.mColor

        holder.myItemView.root.setOnClickListener {

            val bundle = Bundle()
            bundle.putParcelable("dealModel",item)

            val navController = Navigation.findNavController(holder.myItemView.root)
            navController.navigate(R.id.action_dealListingFragment_to_dealPageFragment,bundle)
        }
    }

    override fun getItemCount(): Int {
        return myData.size
    }

}

