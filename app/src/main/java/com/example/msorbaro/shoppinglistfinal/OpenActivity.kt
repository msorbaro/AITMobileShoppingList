package com.example.msorbaro.shoppinglistfinal

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.open_page.*


class OpenActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.open_page)

        val anim = AnimationUtils.loadAnimation(this, R.anim.push_anim)

        anim.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                textAnim.startAnimation(anim)
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })

            textAnim.startAnimation(anim)


        Handler().postDelayed({
            var intentStart = Intent()
            intentStart.setClass(this@OpenActivity, MainActivity:: class.java )
            startActivityForResult(intentStart,1001)        }, 3000)

    }
}
