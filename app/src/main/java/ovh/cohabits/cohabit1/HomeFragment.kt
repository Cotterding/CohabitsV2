package ovh.cohabits.cohabit1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class HomeFragment : Fragment() {

    lateinit var app: cohabitsClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = (activity?.application as cohabitsClass)


    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val cardView = view?.findViewById<CardView>(R.id.card_view_box)
        cardView?.setBackgroundResource(R.drawable.box)

        val button_rejoindre = view?.findViewById<Button>(R.id.button_rejoindre_colocation)
        val button_creer = view?.findViewById<Button>(R.id.button_creer_colocation)
        //Verify if student already has a flat
        val titleText = view?.findViewById<TextView>(R.id.textViewColocation)
        val textViewTaColocation = view?.findViewById<TextView>(R.id.textViewTaColocation)
        val recyclerViewStudent = view?.findViewById<RecyclerView>(R.id.recyclerViewStudent)
        recyclerViewStudent?.layoutManager = GridLayoutManager(app, GridLayoutManager.VERTICAL)
        val buttonDeconnexion = view?.findViewById<Button>(R.id.buttonDeconnexion)
        val textNomColoc = view?.findViewById<TextView>(R.id.textViewColocation)

        val json = JSONObject(mapOf("session" to app.session))


        fun done(response: JSONObject) {
            var code = response.getString("code")
            if (code != "0000") {
                //val message = view?.findViewById<TextView>(R.id.textView)
                //message?.setText("Vous êtes déjà dans une colocation")
                button_creer?.visibility = View.INVISIBLE
                button_rejoindre?.visibility = View.INVISIBLE
                titleText?.visibility = View.VISIBLE
                recyclerViewStudent?.visibility = View.VISIBLE
                textViewTaColocation?.text = "Ta colocation : $code "
                titleText?.text = "Les membres de la colocation :"
                val layoutParams = cardView!!.layoutParams
                layoutParams.height = 600 // nouvelle largeur en pixels
                cardView!!.layoutParams = layoutParams


            }
        }
        app.request("/student/info", json, ::done)

        fun colocationStudents(response: JSONObject) {
            val listStudents: ArrayList<String> = arrayListOf()
            for (i in 0 until response.getJSONArray("friends").length()) {
                val students = response.getJSONArray("friends").getString(i)
                listStudents.add(students)
            }
            recyclerViewStudent?.adapter =
                StudentAdapter(listStudents, requireContext().applicationContext)

        }
        app.request("/flat/info", json, ::colocationStudents)

        buttonDeconnexion?.setOnClickListener {
            fun deconnexionColocation(response: JSONObject) {
                val parentActivity = requireActivity() as AppCompatActivity
                val intent = Intent(parentActivity, ConnexionActivity::class.java)
                startActivity(intent)
                parentActivity.startActivity(intent)
            }
            app.request("/student/disconnect", json, ::deconnexionColocation)
        }



        button_creer?.setBackgroundTintList(this.getResources().getColorStateList(R.color.purple_500));
        button_creer?.setOnClickListener() {
            findNavController().navigate(R.id.action_home_to_add_colocation)
        }

        button_rejoindre?.setBackgroundTintList(this.getResources().getColorStateList(R.color.purple_500));
        button_rejoindre?.setOnClickListener() {
            findNavController().navigate(R.id.action_home_to_rejoindre_colocation)
        }
        //Mettre la condition ici
        return view
    }

    class StudentViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class StudentAdapter(private val student: ArrayList<String>, val context: Context) : RecyclerView.Adapter<StudentViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.student_view, parent, false)
            return StudentViewHolder(view)
        }

        override fun getItemCount() = student.size

        override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
            val studentObject = student[position]
            val view = holder.itemView
            val textView = view.findViewById<TextView>(R.id.student_Tview)
            textView.text = studentObject


        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {

            }
    }



}