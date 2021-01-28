package com.sukhralia.gameheist

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.sukhralia.gameheist.R
import com.sukhralia.gameheist.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    var i: Int = 0
    val list = ArrayList<Int>()
    var len = 0

    private lateinit var binding : ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)


        binding.logo.background = VectorDrawableCompat.create(resources, R.drawable.ic_console, null)

        list.add(R.drawable.ic_console)
        list.add(R.drawable.ic_gold)
        list.add(R.drawable.ic_tag)

        len = list.size

        changeImage()

    }

    //Function for shuffling images
    fun changeImage() {
        Handler().postDelayed({
            if (i == len) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            } else {
                binding.logo.background = VectorDrawableCompat.create(resources, list[i], null)
                i++
                changeImage()
            }

        }, 350)
    }


}