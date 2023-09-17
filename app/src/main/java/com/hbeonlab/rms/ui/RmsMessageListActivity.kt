package com.hbeonlab.rms.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hbeonlab.rms.R
import com.hbeonlab.rms.bluetooth.Contstants
import com.hbeonlab.rms.data.models.RmsData
import com.hbeonlab.rms.utils.BluetoothUtills
import com.hbeonlab.rms.vm.HomeViewModel
import org.koin.android.ext.android.inject
import java.util.*

class RmsMessageListActivity: AppCompatActivity() {
    private lateinit var sendButton: Button
    private val viewModel: HomeViewModel by inject()
    private lateinit var messageInputField : EditText
    private lateinit var messageListRecyclerView : RecyclerView
    private val messageAdapter : MessageListAdapter = MessageListAdapter(mutableListOf())
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            when (message.what) {
                Contstants.STATE_DISCONNECTED -> {
                    Toast.makeText(this@RmsMessageListActivity, "Disconnected!", Toast.LENGTH_SHORT)
                        .show()
                    onConnectionChange(false)
                }
                Contstants.STATE_CONNECTING -> Toast.makeText(this@RmsMessageListActivity,"Connecting...", Toast.LENGTH_SHORT).show()
                Contstants.STATE_CONNECTED -> {
                    Toast.makeText(this@RmsMessageListActivity, "Connected", Toast.LENGTH_SHORT)
                        .show()
                    onConnectionChange(true)
                }
                Contstants.STATE_CONNECTION_FAILED -> Toast.makeText(this@RmsMessageListActivity,"Connection Failed!", Toast.LENGTH_SHORT).show()
                Contstants.STATE_MESSAGE_RECEIVED -> {
                    val readBuff = message.obj as ByteArray
                    val tempMsg = String(readBuff, 0, message.arg1)
                    viewModel.addMessage(MessageItem(1,"Url", tempMsg, Contstants.OTHER, Date()))
                    //viewModel.addDataToDb(tempMsg) TODO open it when start getting real data
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rms_message_list)
        BluetoothUtills.changeHandler(mHandler)

        messageInputField = findViewById(R.id.messageInputField)
        sendButton = findViewById(R.id.sendButton)
        findViewById<Button>(R.id.sendButton).setOnClickListener(object : OnClickListener{
            override fun onClick(v: View?) {
                val message = messageInputField.text.toString()
                viewModel.addMessage(MessageItem(1,"Url", message, Contstants.SELF, Date()))
                BluetoothUtills.sendData(message)
                messageInputField.setText("")
            }
        })

        messageListRecyclerView = findViewById<RecyclerView>(R.id.messageList)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        messageListRecyclerView.layoutManager = layoutManager
        messageListRecyclerView.adapter = messageAdapter

        observeData()
    }

    private fun observeData() {
        viewModel.chatMessages.observe(this) {
            if(it.isNotEmpty()) {
                messageAdapter.update(it.last())
                messageListRecyclerView.scrollToPosition(messageAdapter.itemCount - 1);
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BluetoothUtills.disconnect()
    }

    private fun onConnectionChange(isConnected : Boolean){
        if(isConnected){
            messageInputField.setText("")
            messageInputField.hint = "Type your message here"
            sendButton.isEnabled = true
        }else{
            messageInputField.setText("")
            messageInputField.hint = "Disconnected"
            sendButton.isEnabled = false
        }
    }
}