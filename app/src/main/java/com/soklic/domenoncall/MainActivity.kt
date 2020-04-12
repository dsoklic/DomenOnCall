package com.soklic.domenoncall

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
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

        configButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        timer = fixedRateTimer("timer", false, 0, 5000) {
            val context = this@MainActivity

            val queue = Volley.newRequestQueue(context)
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
            val url = sharedPreferences.getString("serverUrl", getString(R.string.url_default))

            val response = StringRequest(Request.Method.GET, url,
                    Response.Listener {
                        val zoomInProgress = it.toBoolean()

                        context.runOnUiThread {
                            if (zoomInProgress) {
                                centerText.rootView.setBackgroundColor(Color.RED)
                                centerText.text = getString(R.string.busy)
                            } else {
                                centerText.rootView.setBackgroundColor(Color.GREEN)
                                centerText.text = getString(R.string.available)
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
