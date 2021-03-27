package com.zaidoun.karess

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.GpioCallback
import com.google.android.things.pio.PeripheralManager


/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the PeripheralManager
 * For example, the snippet below will open a GPIO pin and set it to HIGH:
 *
 * val manager = PeripheralManager.getInstance()
 * val gpio = manager.openGpio("BCM6").apply {
 *     setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * }
 * gpio.value = true
 *
 * You can find additional examples on GitHub: https://github.com/androidthings
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView: TextView = findViewById(R.id.counter) as TextView

        var cars = 0;


        val manager = PeripheralManager.getInstance()
        val ledEmpty = manager.openGpio("BCM19").apply {
            setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)
        }
        val ledFull = manager.openGpio("BCM26").apply {
            setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
        }

        var increment: GpioCallback = GpioCallback {
            cars += 1;
            textView.text = cars.toString();
            if(cars > 10){
                ledFull.value = true;
                ledEmpty.value = false;
            }

            true
        }

        var decrement: GpioCallback = GpioCallback {
            cars -= 1;
            textView.text = cars.toString();
            if(cars <= 10){
                ledFull.value = false;
                ledEmpty.value = true;
            }
            true
        }

        val inc = manager.openGpio("BCM21").apply {
            setDirection(Gpio.DIRECTION_IN)
            setEdgeTriggerType(Gpio.EDGE_RISING);
            registerGpioCallback(increment)
        }

        val dec = manager.openGpio("BCM20").apply {
            setDirection(Gpio.DIRECTION_IN)
            setEdgeTriggerType(Gpio.EDGE_RISING);
            registerGpioCallback(decrement)
        }

    }
}