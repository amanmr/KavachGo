package com.example.kavachgo

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.kavachgo.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonRegisterHelp.setOnClickListener { registerActivity() }
        binding.buttonLogin.setOnClickListener { postDataUsingVolley(binding.editTextMobile.text.toString(),
            binding.editTextPassword.text.toString()) }
    }
    private fun registerActivity() {
//        Toast.makeText(this,"Clicked",Toast.LENGTH_LONG)
        val intent= Intent(this,RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun postDataUsingVolley(mobile: String, password: String) {
        // url to post our data
        val p = ProgressDialog(this)
        p.setMessage("Connecting ...")
        p.setCancelable(false)
        p.show()
        val url = "https://sheildkavach.azurewebsites.net/login"


        // creating a new variable for our request queue
        val queue = Volley.newRequestQueue(this@MainActivity)

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty

                p.dismiss()
                // on below line we are displaying a success toast message.

                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.
                    val respObj = JSONObject(response)

                    // below are the strings which we
                    // extract from our json object.

                    val output = respObj.getString("output")
                    val userName = respObj.getString("name")
                    if(output=="LoggedIn"){
                        val intent= Intent(this,MainscreenActivity::class.java).also {
                            it.putExtra("mobile",mobile)
                            it.putExtra("name",userName)
                            startActivity(it)
                        }
                    }
                    else{
                        Toast.makeText(this@MainActivity, "$output", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error -> // method to handle errors.
                p.dismiss()
                Toast.makeText(
                    this@MainActivity,
                    "Fail to get response = $error",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                // below line we are creating a map for
                // storing our values in key and value pair.
                val params: MutableMap<String, String> = HashMap()

                // on below line we are passing our key
                // and value pair to our parameters.
                params["mobile"] = "$mobile"
                params["userPassword"] = "$password"


                return params
            }
        }
//        request.retryPolicy = DefaultRetryPolicy(
//            10000,
//            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        )

        queue.add(request)
    }
}