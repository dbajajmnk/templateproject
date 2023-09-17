package com.hbeonlab.rms.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.hbeonlab.rms.R

class DashboardActvity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top) {

                Image(painter = painterResource(id = R.drawable.app_logo), contentDescription = "App logo")

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Button(onClick = {
                        startActivity(Intent(this@DashboardActvity, BluetoothDevicesActivity::class.java))
                    }, Modifier.fillMaxWidth(0.6f)) {
                        Text(text = "Connect with RMS")
                    }

                    Button(onClick = {
                        startActivity(Intent(this@DashboardActvity, RmsDetailsActivity::class.java))
                    }, Modifier.fillMaxWidth(0.6f)) {
                        Text(text = "Fetch data from RMS")
                    }

                    Button(onClick = {

                    }, Modifier.fillMaxWidth(0.6f)) {
                        Text(text = "Download RMS in excel")
                    }

                    Button(onClick = { /*TODO*/ }, Modifier.fillMaxWidth(0.6f)) {
                        Text(text = "Push Data to Cloud Server")
                    }
                }
            }
        }
    }
}