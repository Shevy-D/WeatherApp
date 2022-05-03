package com.shevy.weatherapp

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var user_field: EditText
        lateinit var main_btn: Button
        private lateinit var result_info: TextView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        user_field = findViewById(R.id.user_field)
        main_btn = findViewById(R.id.main_btn)
        result_info = findViewById(R.id.result_info)

        main_btn.setOnClickListener {
            if (user_field.text.toString().trim() == "") {
                Toast.makeText(this, R.string.no_user_input, Toast.LENGTH_LONG).show()
            } else {
                val city = user_field.text.toString()
                val key = "676e4dfee10355670b2905c39bfdcc96"
                val url =
                    "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$key&units=metric"

                GetURLDate().execute(url)
            }
        }
    }

    private class GetURLDate : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            user_field.hint = "Waiting..."
        }

        override fun doInBackground(vararg p0: String?): String? {
            var conection: HttpURLConnection? = null
            var reader: BufferedReader? = null

            try {
                val url: URL = URL(p0[0])
                conection = url.openConnection() as HttpURLConnection?
                conection?.connect()

                val stream: InputStream = conection!!.inputStream
                reader = BufferedReader(InputStreamReader(stream))

                val buffer: StringBuffer = StringBuffer()
                //var line: String = ""

                for (line in reader.readLines()) {
                    //while ((reader.readLine().also { line = it }) != null)
                    buffer.append(line).append("\n")
                }

                return buffer.toString()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                conection?.disconnect()

                try {
                    reader?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return null
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try {
                var jsonObject: JSONObject = JSONObject(result)
                //result_info.text = result.toString()
                result_info.text =
                    "Temperature: " + jsonObject.getJSONObject("main").getDouble("temp")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

    }
}