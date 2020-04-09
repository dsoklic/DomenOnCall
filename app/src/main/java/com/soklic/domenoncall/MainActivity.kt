package com.soklic.domenoncall

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.fixedRateTimer

class MainActivity : AppCompatActivity() {
    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        timer = fixedRateTimer("timer", false, 0, 5000) {
            val context = this@MainActivity

            val queue = Volley.newRequestQueue(context)
            val url = "http://192.168.1.15:5000/"

            val response = StringRequest(Request.Method.GET, url,
                    Response.Listener<String> {
                        val zoomInProgress = it.toBoolean()

                        context.runOnUiThread {
                            if (zoomInProgress) {
                                centerText.rootView.setBackgroundColor(Color.RED)
                                centerText.text = "Do not disturb"
                            } else {
                                centerText.rootView.setBackgroundColor(Color.GREEN)
                                centerText.text = "Available"
                            }
                        }
                    },
                    Response.ErrorListener {
                        Log.e("DomenOnCall", "Error: $it")
                    })

            queue.add(response)
        }
    }

    override fun onPause() {
        super.onPause()

        timer.cancel()
    }
}
