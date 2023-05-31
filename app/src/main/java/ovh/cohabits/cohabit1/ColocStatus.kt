package ovh.cohabits.cohabit1

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class ColocStatus: AppCompatActivity() {

    var queue: RequestQueue? = null
    var httpPort: Int = 8083
    lateinit var  recyclerView: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_statut)
        val app = (application as cohabitsClass)
        val listState : ArrayList<StatusObject> = arrayListOf()
        val serveraddr = "51.38.238.103"
        this.queue = Volley.newRequestQueue(this)
        val stateSwitchBox = findViewById<Switch>(R.id.switchStateId)
        val urlInside = "/student/inside"
        val urlOutside = "/student/outside"
        val dataSessionSwitch = JSONObject()
        dataSessionSwitch.put("session", app?.session)

        fun done(response: JSONObject) {
            //display the response message with a popup on screen
            //todo: change activity is connection was successful
            //todo: display the correct message if connection was refused
            Toast.makeText(applicationContext, response.getString("message"), Toast.LENGTH_SHORT)
                .show()
            //print the response in the android studio trace window (when debugging)
            println(response)

        }


        fun getStatusColoc() {
            this.recyclerView = findViewById(R.id.recyclerViewStatut)
            recyclerView.layoutManager = GridLayoutManager(this, GridLayoutManager.VERTICAL)
            val command = "/flat/info"
            val url = "http://" + serveraddr + ":" + httpPort + command
            val data = JSONObject()
            data.put("session",app?.session )


            val jsonObjectRequest = JsonObjectRequest(Request.Method.DEPRECATED_GET_OR_POST, url, data,
                Response.Listener {
                    Toast.makeText(applicationContext, "SUCCESSFULL", Toast.LENGTH_SHORT).show()
                    for(i in 0 until it.getJSONArray("inside").length()) {
                        val stateJson = it.getJSONArray("inside").getString(i)
                        val stateObject = StatusObject()
                        stateObject.setInside(stateJson)
                        listState.add(stateObject)
                    }

                    recyclerView.adapter = StatusAdapter(listState, applicationContext)

            }, Response.ErrorListener {
                Toast.makeText(applicationContext, "error server", Toast.LENGTH_SHORT).show()
                })
            queue?.add(jsonObjectRequest)
        }

        getStatusColoc()
        app.request(urlOutside, dataSessionSwitch, ::done)

        stateSwitchBox.setOnCheckedChangeListener { _, onCheck ->
            if(onCheck) {
                app.request(urlOutside, dataSessionSwitch, ::done)
                listState.clear()
                getStatusColoc()
            } else {
                app.request(urlInside, dataSessionSwitch, ::done)
                listState.clear()
                getStatusColoc()
            }
        }


    }




}