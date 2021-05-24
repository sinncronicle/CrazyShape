package com.o1083007.CrazyShape

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import kotlinx.android.synthetic.main.activity_main.*

@GlideModule
public final class MyAppGlideModule : AppGlideModule()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val shape = intArrayOf(R.drawable.circle, R.drawable.square, R.drawable.triangle, R.drawable.star)

        val i:Int = (0..3).random()
        imgNext.setImageResource(shape[i])

                val img: ImageView = findViewById(R.id.imgTitle)
                GlideApp.with(this)
                    .load(R.drawable.cover)
                    .override(800,600)
                    .into(img)

        Toast.makeText(this, "作者:林施良", Toast.LENGTH_LONG).show()
        imgNext.setOnLongClickListener(object: View.OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                intent = Intent(this@MainActivity, gameactivity::class.java)
                startActivity(intent)
                return true
            }


        })

    }
}