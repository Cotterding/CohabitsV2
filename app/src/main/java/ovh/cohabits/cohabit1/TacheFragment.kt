package ovh.cohabits.cohabit1

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TacheFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TacheFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var app : cohabitsClass
    private var param1: String? = null
    private var param2: String? = null
    private var tache : ArrayList<String>? = null
    private var student : ArrayList<String>? = null

    lateinit var recyclerViewTache : RecyclerView



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
        val view = inflater.inflate(R.layout.fragment_tache, container, false)
        app = (requireActivity().application as cohabitsClass)
        val buttonAddTache = view?.findViewById<Button>(R.id.buttonAddTache)
        recyclerViewTache = view.findViewById(R.id.recyclerViewTache)
        val layoutTache = view.findViewById<View>(R.id.layoutTache)
        buttonAddTache?.backgroundTintList = this.getResources().getColorStateList(R.color.gris)
        tache = arguments?.getStringArrayList("task")
        student = arguments?.getStringArrayList("student")
        Log.d("TAG", tache.toString())
        recyclerViewTache.layoutManager = GridLayoutManager(app, GridLayoutManager.VERTICAL)
        if(tache == null) {
            synchro()
        } else {
            val adapter =tache?.let { student?.let { it1 ->
                TacheAdapter(it,requireContext().applicationContext,
                    it1
                )
            } }

            adapter?.setOnItemClickListener(object : TacheAdapter.OnItemClickListener {
                override fun onItemClick(tache: String) {
                    val data = JSONObject()
                    data.put("session", app?.session)
                    data.put("name", tache)
                    Log.d("TAG2",tache)
                    app.request("/task/delete", data, ::doneTache)
                    getToTache()
                }

            })

            recyclerViewTache.adapter = adapter
        }

        buttonAddTache?.setOnClickListener {
            val fragment = newInstance()
            parentFragmentManager.beginTransaction()
                .replace(R.id.containerTache, fragment)
                .commit()
            layoutTache?.visibility = View.INVISIBLE
        }




        // Inflate the layout for this fragment
        return view
    }
    fun synchro() {
        val requestQueue = app.queue ?: Volley.newRequestQueue(app)
        app.queue = requestQueue
        getToTache()
        deleteTache()


    }

    fun getToTache() {
        val json = JSONObject(mapOf("session" to app.session))
        val listTache : ArrayList<String>  = arrayListOf()
        val lisStudent : ArrayList<String> = arrayListOf()
        fun done(response: JSONObject) {
            val tasks = response.getJSONArray("tasks")
            // Récupère les étudiants à l'intérieur dans un array
            for (i in 0 until tasks.length()) {
                val tache = tasks.getJSONArray(i).getString(0)
                val student = tasks.getJSONArray(i).getString(1)
                listTache.add(tache)
                lisStudent.add(student)
            }
            Log.d("TAG1", listTache.toString())
            recyclerViewTache.adapter =
                TacheAdapter(listTache, requireContext().applicationContext, lisStudent)


        }
        app.request("/task/list", json, ::done)

    }

    fun deleteTache(){
        val json = JSONObject(mapOf("session" to app.session))
        val listTache: ArrayList<String> = arrayListOf()
        val lisStudent: ArrayList<String> = arrayListOf()

        fun done(response: JSONObject) {
            val tasks = response.getJSONArray("tasks")
            // Récupère les étudiants à l'intérieur dans un array
            for (i in 0 until tasks.length()) {
                val body = tasks.getJSONArray(i)
                listTache.add(body.getString(0))
                lisStudent.add(body.getString(1))
            }
            val adapter = TacheAdapter(listTache, requireContext().applicationContext, lisStudent)
            adapter.setOnItemClickListener(object : TacheAdapter.OnItemClickListener{
                override fun onItemClick(tache: String) {
                    val data = JSONObject()
                    data.put("session", app?.session)
                    data.put("name", tache)
                    Log.d("TAG2",tache)
                    app.request("/task/delete", data, ::doneTache)
                    getToTache()
                }

            })

            recyclerViewTache.adapter = adapter


        }
        app.request("/task/list", json, ::done)


    }

    fun doneTache(response: JSONObject) {
        findNavController().navigate(R.id.navigation_tache)
        //display the response message with a popup on screen
        //todo: change activity is connection was successful
        //todo: display the correct message if connection was refused
        //print the response in the android studio trace window (when debugging)
        println(response)

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
            TacheFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        fun newInstance(): AddTacheColocation {
            return AddTacheColocation()
        }
    }

}