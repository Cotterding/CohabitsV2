package ovh.cohabits.cohabit1

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import ovh.cohabits.cohabit1.databinding.FragmentAddTachesBinding
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddTacheColocation : Fragment() {
    lateinit var app : cohabitsClass
    private lateinit var spinner : Spinner
    private lateinit var spinnerTache : Spinner
    private lateinit var _binding : FragmentAddTachesBinding
    private val binding get() = _binding!!
    val listColoc : ArrayList<String> = arrayListOf()
    val listTache : ArrayList<String> = arrayListOf()
    var points : Int = 0
    var period : Int = 0
    private lateinit var buttonAddTache : Button
    var coloc : String = ""

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    @SuppressLint("ResourceType", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddTachesBinding.inflate(inflater, container, false)
        app = (requireActivity().application as cohabitsClass)

        val binding = _binding ?: return null
        val root: View = binding.root
        spinner = binding.spinner
        spinnerTache= binding.spinnerTache
        buttonAddTache = binding.buttonAddTache
        synchro()
        return root
    }

       fun synchro() {
           val requestQueue = app.queue ?: Volley.newRequestQueue(app)
           app.queue = requestQueue


           fun getColocTache() {
               val command = "/flat/info"
               val url = "http://" + app.serveraddr + ":" + app.httpPort + command
               val data = JSONObject()
               data.put("session", app?.session)
               val jsonObjectRequest =
                   JsonObjectRequest(Request.Method.DEPRECATED_GET_OR_POST, url, data,
                       Response.Listener {
                           for (i in 0 until it.getJSONArray("friends").length()) {
                               val result = it.getJSONArray("friends").getString(i)
                               listColoc.add(result)
                           }
                           ArrayAdapter(listColoc, requireContext().applicationContext, spinner)

                       }, Response.ErrorListener {
                           Toast.makeText(app, "error server", Toast.LENGTH_SHORT).show()
                       })
               requestQueue.add(jsonObjectRequest)
           }


           fun getTacheColocation() {
               val command = "/task/standard"
               val url = "http://" + app.serveraddr + ":" + app.httpPort + command
               val data = JSONObject()
               data.put("session", app?.session)
               val jsonObjectRequest =
                   JsonObjectRequest(
                       Request.Method.DEPRECATED_GET_OR_POST, url, data,
                       Response.Listener {
                            for(i in 0 until it.getJSONArray("tasks").length()) {
                                val result = it.getJSONArray("tasks").getJSONArray(i).getString(0)
                                listTache.add(result)

                            }

                          ArrayAdapter(listTache, requireContext().applicationContext, spinnerTache).run {
                                spinnerTache.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        p0: AdapterView<*>?,
                                        p1: View?,
                                        p2: Int,
                                        p3: Long
                                    ) {
                                        period = it.getJSONArray("tasks").getJSONArray(p2).getInt(1)
                                        points = it.getJSONArray("tasks").getJSONArray(p2).getInt(2)
                                    }

                                    override fun onNothingSelected(p0: AdapterView<*>?) {
                                        TODO("Not yet implemented")
                                    }

                                }
                          }

                       },
                       Response.ErrorListener {

                       })

               requestQueue.add(jsonObjectRequest)

           }

           fun addTache()  {
               val json = JSONObject(mapOf("session" to app.session))
               val listTache : ArrayList<String>  = arrayListOf()
               val listStudent : ArrayList<String>  = arrayListOf()
               fun done(response: JSONObject) {
                   val tasks = response.getJSONArray("tasks")
                   // Récupère les étudiants à l'intérieur dans un array
                       for(i in 0 until tasks.length()) {
                           val tache = tasks.getJSONArray(i).getString(0)
                           val student = tasks.getJSONArray(i).getString(1)
                           listTache.add(tache)
                           listStudent.add(student)
                       }
                   Log.d("TAG1", listTache.toString())
                   buttonAddTache.setOnClickListener {
                       val data = JSONObject()
                       data.put("session", app?.session)
                       data.put("name", spinnerTache.selectedItem)
                       data.put("points", points)
                       data.put("period", period)
                       data.put("student", spinner.selectedItem.toString())
                       listTache.add(spinnerTache.selectedItem.toString())
                       listStudent.add(spinner.selectedItem.toString())
                       app.request("/task/create", data, ::doneTache)
                       val fragment = newInstance(listTache, listStudent)
                       parentFragmentManager.beginTransaction()
                           .replace(R.id.containerTache, fragment)
                           .commit()


                   }
               }

               app.request("/task/list", json, ::done)


           }

           getColocTache()
           getTacheColocation()
           addTache()

       }


    fun doneTache(response: JSONObject) {
        //display the response message with a popup on screen
        //todo: change activity is connection was successful
        //todo: display the correct message if connection was refused
        //print the response in the android studio trace window (when debugging)
        println(response)

    }



    private fun ArrayAdapter(tache : ArrayList<String>, context : Context, spinner: Spinner) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, tache)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                coloc = spinner.selectedItem as String
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TacheFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddTacheColocation().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        fun newInstance(tache: ArrayList<String>, student : ArrayList<String>): TacheFragment {
            val fragment = TacheFragment()
            val args = Bundle()
            args.putStringArrayList("task",tache)
            args.putStringArrayList("student", student)
            fragment.arguments = args
            return fragment
        }
    }
}