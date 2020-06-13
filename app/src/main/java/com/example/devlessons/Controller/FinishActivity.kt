package com.example.devlessons.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.devlessons.Model.Player
import com.example.devlessons.R
import com.example.devlessons.Utilities.EXTRA_PLAYER
import kotlinx.android.synthetic.main.activity_finish.*

class FinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        val player = intent.getParcelableExtra<Player>(EXTRA_PLAYER)
        searchLeaguesText.text = "Looking for ${player.league} ${player.skill} league near you..."
    }
}