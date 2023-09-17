package com.hbeonlab.rms.ui

import android.os.*
import android.os.Environment.DIRECTORY_DOCUMENTS
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hbeonlab.rms.R
import com.hbeonlab.rms.bluetooth.Contstants
import com.hbeonlab.rms.data.models.RmsData
import com.hbeonlab.rms.utils.BluetoothUtills
import com.hbeonlab.rms.vm.HomeViewModel
import com.opencsv.CSVWriter
import org.koin.android.ext.android.inject
import java.io.FileWriter
import java.io.IOException


class RmsDetailsActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by inject()
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            when (message.what) {
                Contstants.STATE_CONNECTING -> Toast.makeText(this@RmsDetailsActivity,"Connecting...", Toast.LENGTH_SHORT).show()
                Contstants.STATE_CONNECTED -> {
                    Toast.makeText(this@RmsDetailsActivity, "Connected", Toast.LENGTH_SHORT)
                        .show()
                }
                Contstants.STATE_CONNECTION_FAILED -> Toast.makeText(this@RmsDetailsActivity,"Connection Failed!", Toast.LENGTH_SHORT).show()
                Contstants.STATE_MESSAGE_RECEIVED -> {
                    val readBuff = message.obj as ByteArray
                    val tempMsg = String(readBuff, 0, message.arg1)
                    Toast.makeText(this@RmsDetailsActivity,"Message received : $tempMsg", Toast.LENGTH_SHORT).show()
                    ///viewModel.addMessage(tempMsg)
                    viewModel.addDataToDb(tempMsg)
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BluetoothUtills.changeHandler(mHandler)
        setContent {
            ContentView()
        }
    }

    private fun observeData() {
        viewModel.getAllRmsData().observe(this) {
            it?.let { writeToExcelFile(it) }
        }
    }

    private fun writeToExcelFile(rmsDataList: List<RmsData>) {
        try {
            val writer = CSVWriter(
                FileWriter(
                    Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).absolutePath + "/RMS_Data.csv"
                )
            )
            val data: MutableList<Array<String>> = ArrayList()
            data.add(
                arrayOf(
                    "SNo",
                    "IMEINo",
                    "PowerStatus",
                    "PumpStatus",
                    "PumpError",
                    "Voltage",
                    "Current",
                    "Frequency",
                    "Temperature",
                    "Power",
                    "PhaseCurrentRYB",
                    "Longitude",
                    "Latitude",
                    "GSMSignal",
                    "Digitalinput1",
                    "Digitalinput2",
                    "Analoginput1",
                    "Analoginput2",
                    "LogDateTime",
                    "ManualCommand",
                    "ResetType",
                    "ResetCounter",
                    "ControllerModel",
                    "CreatedOnDate"
                )
            )
            for (rmsData in rmsDataList) {
                data.add(
                    arrayOf(
                        rmsData.sNo.toString(),
                        rmsData.iMEINo,
                        rmsData.powerStatus.toString(),
                        rmsData.pumpStatus.toString(),
                        rmsData.pumpError,
                        rmsData.voltage.toString(),
                        rmsData.current.toString(),
                        rmsData.frequency.toString(),
                        rmsData.temperature.toString(),
                        rmsData.power.toString(),
                        rmsData.phaseCurrentRYB,
                        rmsData.longitude,
                        rmsData.latitude,
                        rmsData.gSMSignal.toString(),
                        rmsData.digitalinput1.toString(),
                        rmsData.digitalinput2.toString(),
                        rmsData.analoginput1.toString(),
                        rmsData.analoginput2.toString(),
                        rmsData.logDateTime,
                        rmsData.manualCommand.toString(),
                        rmsData.resetType.toString(),
                        rmsData.resetCounter.toString(),
                        rmsData.controllerModel.toString(),
                        rmsData.createdOnDate
                    )
                )
            }
            writer.writeAll(data) // data is adding to csv
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun sendData(){
        viewModel.sendDataToApi()
        viewModel.data.observe(this){
            Toast.makeText(
                this@RmsDetailsActivity,
                "Response received $it",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getMessageString(messageList : MutableList<MessageItem>?) : String{
        val stringBuilder = StringBuilder()
        if (messageList != null) {
            for(message in messageList){
                stringBuilder.append(message.message)
            }
        }
        return stringBuilder.toString()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ContentView(){
        var text by remember { mutableStateOf("") }
        val rmsList by viewModel.getAllRmsData().observeAsState()
        val chatMessageList by viewModel.chatMessages.observeAsState()

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            TopAppBar(
                title = { Text(text = "Chat") },
                backgroundColor = colorResource(id = R.color.purple_500),
                contentColor = Color.White,
                elevation = 5.dp
            )
            if(rmsList?.isEmpty() == false) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    rmsList?.let { it ->
                        items(it) {
                            Text(
                                text = "ID: ${it.sNo} IMEI: ${it.iMEINo}",
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )
                        }
                    }
                }
            }

            Text(
                text = getMessageString(chatMessageList),
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(10.dp)
                    .background(Color.Gray)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Enter your message") }
                    )

                Button(onClick = {
                    //viewModel.addMessage(text)
                    BluetoothUtills.sendData(text)
                }) {
                    Text(text = "Send")
                }
            }
        }
    }

}
