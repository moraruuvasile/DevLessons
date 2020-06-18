package com.example.devlessons.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devlessons.Adapters.MessageAdapter
import com.example.devlessons.Model.Channel
import com.example.devlessons.Model.Message
import com.example.devlessons.R
import com.example.devlessons.Services.AuthService
import com.example.devlessons.Services.MessageService
import com.example.devlessons.Services.UserDataService
import com.example.devlessons.Util.BROADCAST_USER_DATA_CHANGE
import com.example.devlessons.Util.SOCKET_URL
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_channel_dialog.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    val socket = IO.socket(SOCKET_URL)
    lateinit var channelAdapter: ArrayAdapter<Channel>
    lateinit var messageAdapter: MessageAdapter
    var selectedChannel : Channel? = null

    private fun setupAdapters() {
        channelAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, MessageService.channels)
        channel_list.adapter = channelAdapter

        messageAdapter = MessageAdapter(this, MessageService.messages)

        messageListView.apply {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        socket.connect()
        socket.on("channelCreated", onNewChannel)
        socket.on("messageCreated", onNewMessage)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        setupAdapters()
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver,
                IntentFilter(BROADCAST_USER_DATA_CHANGE))

        channel_list.setOnItemClickListener { parent, view, position, id ->
            selectedChannel = MessageService.channels[position]
            drawer_layout.closeDrawer(GravityCompat.START)
            updateWithChannel()
        }

        if (App.prefs.isLoggedIn) {
            AuthService.findUserByEmail(this){}
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        socket.disconnect()
    }

    private val userDataChangeReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (App.prefs.isLoggedIn) {
                navHeaderName.text = UserDataService.name
                navHeaderEmail.text = UserDataService.email
                val resourceId = resources.getIdentifier(UserDataService.avatarName, "drawable",
                    packageName)
                navHeaderProf_Image.setImageResource(resourceId)
                navHeaderProf_Image.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
                navHeaderBtn_Login.text = "Logout"

                MessageService.getChannels { complete ->
                    if (complete) {
                        if (MessageService.channels.count() > 0) {
                            selectedChannel = MessageService.channels[0]
                            channelAdapter.notifyDataSetChanged()
                            updateWithChannel()
                        }
                    }
                }
            }
        }
    }

    fun updateWithChannel() {
        mainChannalName.text = "#${selectedChannel?.name}"
        // download messages for channel
        if (selectedChannel != null) {
            MessageService.getMessages(selectedChannel!!.id) { complete ->
                if (complete) {
                    messageAdapter.notifyDataSetChanged()
                    if (messageAdapter.itemCount > 0) {
                        messageListView.smoothScrollToPosition(messageAdapter.itemCount - 1)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun navHeaderBtn_Login(v: View) {

        if (App.prefs.isLoggedIn) {
            // log out
            UserDataService.logout()
            navHeaderName.text = ""
            navHeaderEmail.text = ""
            navHeaderProf_Image.setImageResource(R.drawable.profiledefault)
            navHeaderProf_Image.setBackgroundColor(Color.TRANSPARENT)
            navHeaderBtn_Login.text = "Login"
            mainChannalName.text = "Please log in"

        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

	fun navHeaderBtn_AddCha(view: View) {
        if (App.prefs.isLoggedIn) {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)

            builder.setView(dialogView)
                .setPositiveButton("Add") { dialogInterface, i ->
                    // perform some logic when clicked
//                   val nameTextField = dialogView.findViewById<EditText>(R.id.addChannelNameTxt)
//                   val descTextField = dialogView.findViewById<EditText>(R.id.addChannelDescTxt)
//                    val channelName = nameTextField.text.toString()
//                    val channelDesc = descTextField.text.toString()
                    val channelName = dialogView.addChannelNameTxt.text.toString()
                    val channelDesc = dialogView.addChannelDescTxt.text.toString()

                    // Create channel with the channel name and description
//                   hideKeyboard()// le mutam in manifest
                    socket.emit("newChannel", channelName, channelDesc)
                }
                .setNegativeButton("Cancel") { dialogInterface, i ->
                    // Cancel and close the dialog
 //                   hideKeyboard()// le mutam in manifest
                }
                .show()
        }
    }

    private val onNewChannel = Emitter.Listener { args ->
        if (App.prefs.isLoggedIn) {
            runOnUiThread {
                val channelName = args[0] as String
                val channelDescription = args[1] as String
                val channelId = args[2] as String

                val newChannel = Channel(channelName, channelDescription, channelId)
                MessageService.channels.add(newChannel)
                println(newChannel.name)
                println(newChannel.description)
                println(newChannel.id)
                channelAdapter.notifyDataSetChanged()
            }
        }
    }

    private val onNewMessage = Emitter.Listener { args ->
        if (App.prefs.isLoggedIn) {
            runOnUiThread {
                val channelId = args[2] as String
                if (channelId == selectedChannel?.id) {
                    val msgBody = args[0] as String

                    val userName = args[3] as String
                    val userAvatar = args[4] as String
                    val userAvatarColor = args[5] as String
                    val id = args[6] as String
                    val timeStamp = args[7] as String

                    val newMessage = Message(msgBody, userName, channelId, userAvatar, userAvatarColor, id, timeStamp)
                    MessageService.messages.add(newMessage)
                    messageAdapter.notifyDataSetChanged()
                    messageListView.smoothScrollToPosition(messageAdapter.itemCount - 1)
                }
            }
        }
    }


    fun sendMsgBtnClicked(view: View) {
        if (App.prefs.isLoggedIn && messageTextField.text.isNotEmpty() && selectedChannel != null) {
            val userId = UserDataService.id
            val channelId = selectedChannel!!.id
            socket.emit("newMessage", messageTextField.text.toString(), userId, channelId,
                UserDataService.name, UserDataService.avatarName, UserDataService.avatarColor)
            messageTextField.text.clear()
            hideKeyboard()
        }
    }

    private fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
}