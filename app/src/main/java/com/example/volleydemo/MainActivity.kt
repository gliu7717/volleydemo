package com.example.volleydemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {
    private var server_url = "http://150.136.175.102:3000"
    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val register = findViewById<Button>(R.id.button)
        val textView = findViewById<TextView>(R.id.tvGreeting)
        val login = findViewById<Button>(R.id.btnLogin)
        val logout = findViewById<Button>(R.id.btnLogout)
        val volleyGetTest = findViewById<Button>(R.id.btnGetTest)

        volleyGetTest.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            val url  = server_url + "/api/json?name=ben&phone=1234567&password=123"
            val jsonOBJ = JSONObject()
            jsonOBJ.put("dd", 2)
            val jsonRequest =  JsonObjectRequest(Request.Method.GET, url, jsonOBJ, {
                    response ->
                run {
                    val jsonArray = response.getJSONArray("students")
                    for( i in 1..jsonArray.length())
                    {
                        val json = jsonArray.getJSONObject(i-1)
                        Log.i("MYTAG", json.getString("name"))
                    }
                    textView.text = response.toString()
                    queue.stop()
                }

            },{
                textView.text = "Wrong $it"
                queue.stop()

            })
            queue.add(jsonRequest)
        }

        register.setOnClickListener{
            val queue = Volley.newRequestQueue(this)
            val requestBody = "name=ben&phone=1234567&password=123"
            val registry_url = server_url + "/registration"
            val stringRequest =
                object: StringRequest(Request.Method.POST, registry_url,
                {
                    response ->
                    run {
                        textView.text = response
                        queue.stop()
                    }
                },
                {
                    textView.text = "Wrong $it"
                    queue.stop()
                }
            ) {
                override fun getBody():ByteArray
                {
                    return requestBody.toByteArray(Charset.defaultCharset())
                }
            }
            queue.add(stringRequest)
        }

        login.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            val requestBody = "phone=1234567&password=123"
            val login_url = server_url + "/login"
            val getAllusers_url = server_url + "/getAllUsers"
            val stringRequest =
                object: StringRequest(Request.Method.POST, getAllusers_url,
                    { response ->
                        run {
                            textView.text = response
                            queue.stop()
                        }
                    },
                    {
                        textView.text = "Wrong $it"
                        queue.stop()
                    }
                ) {
                    override fun getBody():ByteArray
                    {
                        return requestBody.toByteArray(Charset.defaultCharset())
                    }
                }
            queue.add(stringRequest)
        }

        logout.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            val url = server_url + "/logout"

            val requestBody = ""
            val stringReq: StringRequest =
                object : StringRequest(
                    Method.POST, url,
                    Response.Listener { response ->
                        textView.text = response
                        queue.stop()
                    },
                    Response.ErrorListener { error ->
                        Log.i("myLog", "error = " + error)
                        textView.text = "Wrong $it"
                        queue.stop()
                    }
                ) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Authorization"] =
                            "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2NGE0NzEzMjA3ZmQzMGRlMTdmMTUzYTEiLCJpYXQiOjE2ODg1MDI3NzN9.jQF4kKeFVHMh9yX7fuz-NHLMPE45anxXlTTyVJBLJBg"
                        return headers
                    }

                    override fun getBody(): ByteArray {
                        return requestBody.toByteArray(Charset.defaultCharset())
                    }
                }
            queue.add(stringReq)
        }
    }
}