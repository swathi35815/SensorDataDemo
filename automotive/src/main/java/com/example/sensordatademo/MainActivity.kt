package com.example.sensordatademo

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sensordatademo.ui.theme.SensordataDemoTheme

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var accelerometer: Sensor? = null
    private val sensorViewModel: SensorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            SensordataDemoTheme {
                SensorScreen(sensorViewModel)
            }
        }
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        /*val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        Log.i("MySensorData", "$deviceSensors")*/
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_LIGHT -> {
                val lux = event.values[0]
                Log.d("MySensorData", "Light sensor value: $lux")
                sensorViewModel.updateLightSensorData(lux)
            }
            Sensor.TYPE_ACCELEROMETER -> {
                val acceleration = event.values[0]
                Log.d("MySensorData", "Accelerometer value: $acceleration")
                if (acceleration > THRESHOLD) {
                    // Trigger ADAS event, e.g., collision warning
                }
                sensorViewModel.updateAccelerometerData(acceleration)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //TO DO
    }

    override fun onResume() {
        super.onResume()
        lightSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    companion object {
        private const val THRESHOLD = 15.0f
    }

}

/*@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}*/

@Composable
fun SensorScreen(viewModel: SensorViewModel) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SensorDataDisplay(viewModel)
            Divider(thickness = 1.dp)
            ADASDataDisplay(viewModel)
        }
    }
}

@Composable
fun SensorDataDisplay(viewModel: SensorViewModel) {
    val sensorValue = viewModel.lightSensorData.collectAsState().value
    Text(text = "Light sensor value: $sensorValue", style = MaterialTheme.typography.bodyLarge)
}

@Composable
fun ADASDataDisplay(viewModel: SensorViewModel) {
    val sensorValue = viewModel.accelerometerData.collectAsState().value
    Text(text = "Accelerometer value: $sensorValue", style = MaterialTheme.typography.bodyLarge)
}

/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SensordataDemoTheme {
        //Greeting("Android")
    }
}*/
