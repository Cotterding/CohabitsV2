package ovh.cohabits.cohabit1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import ovh.cohabits.cohabit1.databinding.FragmentStatutBinding

class StatutFragment : Fragment() {
    private var _binding: FragmentStatutBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StatutAdapter

    val students : ArrayList<StatutAdapter.StatusObject> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatutBinding.inflate(inflater, container, false)
        val binding = _binding ?: return null
        val root: View = binding.root

        recyclerView = binding.recyclerViewStatut
        synchro()
        adapter = StatutAdapter(students, requireActivity().applicationContext)
        return root
    }

    private fun synchro() {

        val app = (requireActivity().application as cohabitsClass)
        val listState : ArrayList<StatutAdapter.StatusObject> = arrayListOf()
        val requestQueue = app.queue ?: Volley.newRequestQueue(app)
        app.queue = requestQueue
        val stateSwitchBox = binding.switchStatut
        val urlInside = "/student/inside"
        val urlOutside = "/student/outside"
        val dataSessionSwitch = JSONObject()
        dataSessionSwitch.put("session", app.session)

        fun done(response: JSONObject) {
            //display the response message with a popup on screen
            //todo: change activity is connection was successful
            //todo: display the correct message if connection was refused
            Toast.makeText(app, response.getString("message"), Toast.LENGTH_SHORT)
                .show()
            //print the response in the android studio trace window (when debugging)
            println(response)

        }


        fun getStatusColoc() {
            recyclerView.layoutManager = GridLayoutManager(app, GridLayoutManager.VERTICAL)
            val command = "/flat/info"
            val url = "http://" + app.serveraddr + ":" + app.httpPort + command
            val data = JSONObject()
            data.put("session",app?.session )


            val jsonObjectRequest = JsonObjectRequest(Request.Method.DEPRECATED_GET_OR_POST, url, data,
                Response.Listener {
                    Toast.makeText(app, "SUCCESSFULL", Toast.LENGTH_SHORT).show()
                    for(i in 0 until it.getJSONArray("inside").length()) {
                        val stateJson = it.getJSONArray("inside").getString(i)
                        val stateObject = StatutAdapter.StatusObject()
                        stateObject.setInside(stateJson)
                        listState.add(stateObject)
                    }

                    recyclerView.adapter = StatutAdapter(listState, app)

                }, Response.ErrorListener {
                    Toast.makeText(app, "error server", Toast.LENGTH_SHORT).show()
                })
            requestQueue.add(jsonObjectRequest)
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

