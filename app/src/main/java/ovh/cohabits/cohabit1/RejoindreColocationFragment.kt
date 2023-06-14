
package ovh.cohabits.cohabit1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RejoindreColocationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RejoindreColocationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var app: cohabitsClass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = (activity?.application as cohabitsClass)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rejoindre_colocation, container, false)
        val cardView = view?.findViewById<CardView>(R.id.card_view_box_rejoindre_colocation)
        cardView?.setBackgroundResource(R.drawable.box)
        val backButton: Button = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        val button_rejoindre_colocation = view?.findViewById<Button>(R.id.button_rejoindre_colocation)
        button_rejoindre_colocation?.setBackgroundTintList(this.getResources().getColorStateList(R.color.purple_500));
        button_rejoindre_colocation?.setOnClickListener() {
            var code = view?.findViewById<EditText>(R.id.editText_CodeColocation)?.getText().toString()

            var url = "/flat/join"
            var data = JSONObject()
            data.put("session", app?.session)
            data.put("code", code)

            fun done(response: JSONObject) {
                Toast.makeText(requireContext(). applicationContext, response.getString("message"), Toast.LENGTH_SHORT).show()
                println(response) //android studio debugging message

                //at this point the user has joined the flat
                //add here the switch to next activity or fragment
            }
            app?.request(url, data, ::done)
            findNavController().navigate(R.id.navigation_home)
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
         * @return A new instance of fragment RejoindreColocataFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RejoindreColocationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}