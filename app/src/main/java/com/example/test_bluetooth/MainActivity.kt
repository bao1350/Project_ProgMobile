package com.example.test_bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import androidx.core.app.ActivityCompat
import java.util.*

class MainActivity : Activity() {


    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var listView: ListView
    private lateinit var adapter: BluetoothDeviceAdapter
    private val devices = mutableListOf<BluetoothDevice>()

    private var isScanning = false


    @SuppressLint("MissingPermission")
    private fun startScan() {
        devices.clear()
        adapter.notifyDataSetChanged()
        isScanning = bluetoothAdapter!!.startDiscovery()
    }


    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF)
                    if (state == BluetoothAdapter.STATE_ON) {
                        startScan()
                    }
                }

                BluetoothDevice.ACTION_FOUND -> {
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE) ?: return
                    devices.add(device)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onPause() {
        super.onPause()
        if (isScanning) {
            bluetoothAdapter!!.cancelDiscovery()
            isScanning = false
        }
    }


    private val enableBluetoothLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Le Bluetooth est activé
                startScan()
            } else {
                // Le Bluetooth n'a pas pu être activé
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        adapter = BluetoothDeviceAdapter(this, devices)
        listView.adapter = adapter

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (checkPermissions()) {
            if (!bluetoothAdapter!!.isEnabled) {
                enableBluetooth()
            } else {
                startScan()
            }
        } else {
            requestPermissions()
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(bluetoothReceiver, IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(BluetoothAdapter.ACTION_SCAN_FOUND)
            addAction(BluetoothAdapter.ACTION_SCAN_FINISHED)
        })
    }

    private fun checkPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_CODE)
        }
    }

    private fun enableBluetooth() {
        val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        enableBluetoothLauncher.launch(enableIntent)
    }
}

private const val PERMISSION_REQUEST_CODE = 101

class BluetoothDeviceAdapter(context: Context, private val devices: List<BluetoothDevice>) : BaseAdapter() {
    private val inflater = context

