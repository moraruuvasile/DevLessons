package com.example.devlessons

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.jonnyb.swoosh.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		button.setOnClickListener(){
			val leagueIntent = Intent(this, LeagueActivity::class.java)
			startActivity(leagueIntent)
		}
	}
}