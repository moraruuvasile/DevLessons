package com.example.devlessons

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		val bimap = BitmapFactory.decodeResource(resources,R.drawable.devslopesprofilelogo)
		val rounded = RoundedBitmapDrawableFactory.create(resources, bimap)
//		rounded.cornerRadius = 15f
		rounded.isCircular = true
		logo.setImageDrawable(rounded)
	}
}