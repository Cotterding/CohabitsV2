package ovh.cohabits.cohabit1

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import org.json.JSONObject

class HomeFragment : Fragment() {

    lateinit var app: cohabitsClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = (activity?.application as cohabitsClass)


    }

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

        val json = JSONObject(mapOf("session" to app.session))

        fun done(response: JSONObject) {
            var code = response.getString("code")
            if (code != "0000" ) {
                val message = view?.findViewById<TextView>(R.id.textView)
                message?.setText("Vous êtes déjà dans une colocation")
                button_creer?.setEnabled(false)
                button_rejoindre?.setEnabled(false)

            }
        }
        app.request("/student/info", json, ::done)


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