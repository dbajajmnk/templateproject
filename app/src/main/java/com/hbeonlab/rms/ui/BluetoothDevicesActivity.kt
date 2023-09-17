package com.hbeonlab.rms.ui

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.ColorSpace
import android.graphics.ColorSpace.Rgb
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hbeonlab.rms.R
import com.hbeonlab.rms.bluetooth.BluetoothService
import com.hbeonlab.rms.bluetooth.Contstants
import com.hbeonlab.rms.utils.BluetoothUtills

class BluetoothDevicesActivity : ComponentActivity() {

    private val listItems : MutableLiveData<MutableList<BluetoothDevice>> = MutableLiveData(mutableListOf())
    private var hasPermissions = false
    private var showProgress = false
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            when (message.what) {
                Contstants.STATE_CONNECTING -> {
                    if(message.data.getBoolean(Contstants.EXTRA_IS_CLIENT_DEVICE)) {
                        Toast.makeText(
                            this@BluetoothDevicesActivity,
                            "Connecting...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                Contstants.STATE_CONNECTED -> {
                    showProgress = false
                    Toast.makeText(this@BluetoothDevicesActivity, "Connected", Toast.LENGTH_SHORT)
                        .show()
                    goToChat()
                }
                Contstants.STATE_CONNECTION_FAILED -> {
                    showProgress = false
                    Toast.makeText(this@BluetoothDevicesActivity,"Connection Failed!", Toast.LENGTH_SHORT).show()
                }
                Contstants.STATE_MESSAGE_RECEIVED -> {
                    val readBuff = message.obj as ByteArray
                    val tempMsg = String(readBuff, 0, message.arg1)
                    Toast.makeText(this@BluetoothDevicesActivity,"Message received : $tempMsg", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkBluetoothPermisssions()
        setContent {
            var showProgressBar by remember {
                mutableStateOf(showProgress)
            }
            val deviceList by listItems.observeAsState()
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopAppBar(
                    title = {
                        Text(text = "Devices")
                    },
                    backgroundColor = colorResource(id = R.color.purple_500),
                    contentColor = Color.White,
                    elevation = 5.dp
                )

                deviceList?.let { ShowBluetoothDevices(deviceList = it) }

                if(deviceList?.isEmpty() == true) {
                    Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center) {
                        Text(
                            text = "No Device found",
                            fontSize = 40.sp
                            )
                    }
                }
                ShowProgressBar(showProgressBar)
            }
        }

        connectToBluetooth()
    }

    @Composable
    private fun ShowBluetoothDevices(deviceList : MutableList<BluetoothDevice>){
        LazyColumn() {
            items(deviceList) {
                Text(
                    text = it.name,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable {
                            connectToDevice(it)
                        }
                )
                Divider(color = Color.LightGray, thickness = Dp( 0.5F))
            }
        }
    }
    @Composable
    private fun ShowProgressBar(showProgressBar : Boolean){
        if(showProgressBar) {
            CircularProgressIndicator(
                modifier = Modifier.padding(16.dp),
                color = colorResource(id = R.color.purple_200),
                strokeWidth = Dp(value = 4F)
            )
        }
    }
    private fun checkBluetoothPermisssions() {
        val requestBluetoothPermissionLauncherForRefresh = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted: Boolean? ->
            BluetoothUtills.onPermissionsResult(this, granted!!) {
                hasPermissions = true
                connectToBluetooth()
            }
        }
        hasPermissions = BluetoothUtills.hasPermissions(this,requestBluetoothPermissionLauncherForRefresh)
        if(hasPermissions){
            connectToBluetooth()
        }
    }

    private fun connectToBluetooth() {
        if(hasPermissions){
            BluetoothUtills.initBluetoothAdapter(this, mHandler, resultLauncher)
            val deviceList = BluetoothUtills.queryDevices()
            listItems.value = deviceList
            BluetoothUtills.startServerThread(mHandler)
            if(deviceList.isNotEmpty())
                Toast.makeText(this, "Select bluetooth device to connect.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun connectToDevice(bluetoothDevice: BluetoothDevice) {
        showProgress = true
        BluetoothUtills.startConnection(bluetoothDevice)
    }

    private fun goToChat(){
        showProgress = false
        val intent = Intent(
            this@BluetoothDevicesActivity,
            RmsMessageListActivity::class.java
        )
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        BluetoothUtills.disconnect()
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            connectToBluetooth()
        }
    }

}