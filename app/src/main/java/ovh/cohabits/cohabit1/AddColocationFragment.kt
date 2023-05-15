package ovh.cohabits.cohabit1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddColocationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddColocationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //we use here a nullable variable (note the question mark after the class name)
    //because we cannot initialize the value when declaring the variable
    //this can be done only after initializing the fragment in onCreate below
    var app: cohabitsClass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //get the app object used for storing persistent values
        //we are in a Fragment so get the activity, then the application
        app = (activity?.application as cohabitsClass)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_colocation, container, false)
        val cardView = view?.findViewById<CardView>(R.id.card_view_box_add_colocation)
        cardView?.setBackgroundResource(R.drawable.box)
        val button_add_colocation = view?.findViewById<Button>(R.id.button_add_colocation)
        button_add_colocation?.setBackgroundTintList(this.getResources().getColorStateList(R.color.purple_500));
        button_add_colocation?.setOnClickListener() {

            //send the request for flat creation
            //use the session stored when the user connects
            //the session identifies the sender for the server and is mandtory for all requests
            //note that the app variable is a nullable so we need to unpack it with "app?" syntax

            var url = "/flat/create"
            var data = JSONObject()
            data.put("session", app?.session)

            fun done(response: JSONObject) {
                app?.code = response.getString("code")
                println(response) //android studio debugging message

                //at this point the new flat is created
                //it will be identified by its code
                //the code must be available to the user
                //the user will then communicate this code to its fellows sharing the flat with him
                //each fellow will then use the code to join the flat
                //note that the user creating th flat automatically joins this flat
                //add here the switch to next activity or fragment

                val textViewNomCodeColoc = view.findViewById<TextView>(R.id.textViewNomCodeColoc)
                textViewNomCodeColoc.visibility = View.VISIBLE
                val textCodeColoc = view.findViewById<TextView>(R.id.textCodeColoc)

                //the flat code was received from the server as response for the flat creation
                //the server garantees the code unicity
                textCodeColoc.setText(app?.code)
                textCodeColoc.visibility = View.VISIBLE
            }
            app?.request(url, data, ::done)



        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddColocationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                AddColocationFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}