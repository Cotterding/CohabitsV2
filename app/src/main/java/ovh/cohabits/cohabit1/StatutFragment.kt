package ovh.cohabits.cohabit1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class StatutFragment : Fragment() {

    /*private var _binding: FragmentStatutBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StatutAdapter

    val students : ArrayList<StatutAdapter.StatusObject> = arrayListOf()*/

    lateinit var app : cohabitsClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statut, container, false)
        view?.findViewById<Button>(R.id.test)?.setOnClickListener {
            displayStatus(view)
        }
        app = (requireActivity().application as cohabitsClass)
        val switch = view?.findViewById<Switch>(R.id.switchStatut)

        //Position de départ du switch
        switch?.setOnCheckedChangeListener { _, onCheck ->
            val url = if (onCheck) "/student/inside" else "/student/outside"
            val json = JSONObject(mapOf("session" to app.session))
            app.request(url, json, null)
            displayStatus(view)
        }
        displayStatus(view)
        return view



        /*_binding = FragmentStatutBinding.inflate(inflater, container, false)
        val binding = _binding ?: return null
        val root: View = binding.root

        recyclerView = binding.recyclerViewStatut
        synchro()
        adapter = StatutAdapter(students, requireActivity().applicationContext)*/


    }

    fun displayStatus(v : View) {
        val recycler_statut = v?.findViewById<RecyclerView>(R.id.recyclerViewStatut)

        val json = JSONObject(mapOf("session" to app.session))

        fun done(response: JSONObject) {
            val inside = response.getJSONArray("inside")
            // Récupère les étudiants à l'intérieur dans un array
            val arrayinside = Array(inside.length()) { inside.getString(it) }
            // Affiche les étudiants à l'intérieur
            recycler_statut?.adapter = StatutAdapter(arrayinside, app)

        }
        app.request("/flat/info", json, ::done)
    }









    /*private fun synchro() {

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
    }*/


}

