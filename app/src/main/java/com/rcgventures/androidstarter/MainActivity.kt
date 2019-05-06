package com.rcgventures.androidstarter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://gist.githubusercontent.com/gray419/a0d58d14a6105c7f423d52210f723f4c/raw/c0ffb5c4ae3d8f417f3fd249f078e2f51ce02d35/movies.json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("MainActivity", e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonResponse = response.body()?.string() ?: ""
                val movieResponse = Gson().fromJson(jsonResponse, MovieResponse::class.java)
                movieResponse?.movies?.forEach {
                    Log.i("MainActivity", "${it.name} - ${it.rating ?: 0.0}")
                }
            }
        })
    }
}
