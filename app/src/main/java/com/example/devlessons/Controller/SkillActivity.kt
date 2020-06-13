package com.example.devlessons.Controller

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.devlessons.Model.Player
import com.example.devlessons.Utilities.EXTRA_LEAGUE
import com.example.devlessons.R
import com.example.devlessons.Utilities.EXTRA_PLAYER
import com.example.jonnyb.swoosh.BaseActivity
import kotlinx.android.synthetic.main.activity_skill.*

class SkillActivity : BaseActivity() {
    //    var league = ""
    lateinit var player: Player

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(EXTRA_PLAYER, player)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skill)
        //       league = intent.getStringExtra(EXTRA_LEAGUE)
        //       println(league)
        player = intent.getParcelableExtra(EXTRA_PLAYER)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            player = savedInstanceState.getParcelable(EXTRA_PLAYER)!!
        }
    }

    fun onBallerClick(view: View) {
        beginnerSkillBtn.isChecked = false
        player.skill = "baller"
    }

    fun onBeginnerClick(view: View) {
        ballerSkillBtn.isChecked = false
        player.skill = "beginner"
    }

    fun onSkillFinishClicked(view: View) {
        if (player.skill != "") {
            val finishActivity = Intent(this, FinishActivity::class.java)
            finishActivity.putExtra(EXTRA_PLAYER, player)
            startActivity(finishActivity)
        } else {
            Toast.makeText(this, "Please select a skill level", Toast.LENGTH_SHORT).show()
        }
    }
}
