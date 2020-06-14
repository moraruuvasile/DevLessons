package com.example.devlessons.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devlessons.Adapters.MainRecyAdap
import com.example.devlessons.R
import com.example.devlessons.Services.DataService
import com.example.devlessons.Utilities.EXTRA_CATEGORY
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val recyAdap = MainRecyAdap(DataService.categories) { category ->
			val productIntent = Intent(this, ProductsActivity::class.java)
			productIntent.putExtra(EXTRA_CATEGORY, category.title)
			startActivity(productIntent)
		}
		mainListAdapter.apply {
			adapter = recyAdap
			layoutManager = LinearLayoutManager(this@MainActivity)
			setHasFixedSize(true)
		}
	}
}
