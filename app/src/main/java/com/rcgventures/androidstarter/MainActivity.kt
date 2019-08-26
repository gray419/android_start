package com.rcgventures.androidstarter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.serialization.Mapper
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.io.IOException

class MainActivityUI: AnkoComponent<Context> {
    lateinit var fab: FloatingActionButton

    override fun createView(ui: AnkoContext<Context>): View {
        return with(ui) {
            coordinatorLayout {
                relativeLayout {
                    textView("Hello, World")
                }.lparams {
                    gravity = Gravity.CENTER
                }
                fab = floatingActionButton {
                    imageResource = android.R.drawable.ic_dialog_email
                }.lparams {
                    gravity = Gravity.BOTTOM or Gravity.END
                    margin = dip(16)
                }
            }
        }
    }
}

class MainActivity : AppCompatActivity() {
    private val MOVIE_URL = "https://gist.githubusercontent.com/gray419/a0d58d14a6105c7f423d52210f723f4c/raw/c0ffb5c4ae3d8f417f3fd249f078e2f51ce02d35/movies.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ui = MainActivityUI()
        ui.setContentView(this)

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(MOVIE_URL)
            .build()

        ui.fab.onClick {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.i("MainActivity", e.message ?: "Error making request")
                }

                override fun onResponse(call: Call, response: Response) {
                    val jsonResponse = response.body()?.string() ?: ""
                    val json = Json(JsonConfiguration.Stable)
                    val movieResponse = json.parse(MovieResponse.serializer(), jsonResponse)
                    movieResponse.movies?.forEach {
                        Log.i("MainActivity", "${it.name} - ${it.rating ?: 0.0}")
                    }
                }
            })
        }
    }
}
