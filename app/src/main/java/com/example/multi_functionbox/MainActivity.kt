package com.example.multi_functionbox

import BluetoothViewModel
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.multi_functionbox.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null
    private lateinit var bluetoothViewModel: BluetoothViewModel

    private val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // Standard UUID for SPP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bluetoothViewModel = ViewModelProvider(this).get(BluetoothViewModel::class.java)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_voltage, R.id.nav_current, R.id.nav_flame, R.id.nav_temperature_humid, R.id.nav_motion, R.id.nav_distance, R.id.nav_smoke
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        connectToDevice("08:D1:F9:D3:D5:46")
        bluetoothViewModel.outputStream.value = outputStream
        bluetoothViewModel.inputStream.value = inputStream
    }

    // Function to connect to the Bluetooth device
    private fun connectToDevice(deviceAddress: String) {
        val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(deviceAddress)

        // Try to create a BluetoothSocket
        try {
            bluetoothSocket = device?.createRfcommSocketToServiceRecord(uuid)
            bluetoothSocket?.connect()
            outputStream = bluetoothSocket?.outputStream
            inputStream = bluetoothSocket?.inputStream
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Function to send data
    private fun sendData(data: String) {
        try {
            outputStream?.write(data.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Function to receive data (You need to run this in a separate thread)
    private fun receiveData() {
        val buffer = ByteArray(1024)
        var bytes: Int

        while (true) {
            try {
                bytes = inputStream?.read(buffer) ?: 0
                val incomingMessage = String(buffer, 0, bytes)
                // Handle the incoming message as needed

            } catch (e: IOException) {
                e.printStackTrace()
                break
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}