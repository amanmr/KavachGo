package com.example.kavachgo

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val loginHelp: Button =findViewById(R.id.buttonLoginHelp)
        loginHelp.setOnClickListener { finishAct() }
        val mobile:TextView= findViewById((R.id.editTextMobile))
        val name:TextView= findViewById((R.id.editTextName))
        val password:TextView= findViewById((R.id.editTextPassword))
        val registerButton:TextView= findViewById((R.id.buttonRegister))
        val userType:Spinner= findViewById((R.id.selectAs))
        registerButton.setOnClickListener{ registerUser(mobile.text.toString(),password.text.toString(),
                        name.text.toString(),userType.selectedItem.toString()) }
    }

    private fun finishAct() {
        finish()
    }

    private fun registerUser(mobile: String, password: String,name: String,userType:String) {
        val p = ProgressDialog(this)
        p.setMessage("Connecting ...")
        p.setCancelable(false)
        p.show()
        var url = "https://sheildkavach.azurewebsites.net/registeruser"
        if(userType=="Volunteer"){
            url = "https://sheildkavach.azurewebsites.net/registervolunteer"
        }

        val queue = Volley.newRequestQueue(this)
        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                p.dismiss()
                try {
                    val respObj = JSONObject(response)
                    val output = respObj.getString("output")

                    Toast.makeText(this, "$output", Toast.LENGTH_SHORT).show()
                    if(output=="User Created") {
                        this.finish()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error ->
                p.dismiss()
                Toast.makeText(
                    this,
                    "Fail to get response = $error",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["mobile"] = "$mobile"
                params["password"] = "$password"
                params["name"] = "$name"
                return params
            }
        }
        queue.add(request)
    }
}