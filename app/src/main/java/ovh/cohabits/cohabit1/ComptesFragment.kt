package ovh.cohabits.cohabit1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject


class ComptesFragment : Fragment() {

    lateinit var app : cohabitsClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_comptes, container, false)
        app = (requireActivity().application as cohabitsClass)

        //launch remboursement activity
        val remboursement = view?.findViewById<Button>(R.id.button_remboursement)

        remboursement?.setOnClickListener {
            val intent = Intent(activity, RemboursementActivity::class.java)
            startActivity(intent)
        }

        view?.findViewById<Button>(R.id.depense_button)?.setOnClickListener {
            findNavController().navigate(R.id.action_comptes_to_add_depense)
        }


        //lauch history

        history(view)
        return view
    }

    fun history(v : View) {
        val json = JSONObject(mapOf("session" to app.session))

        var url = "/flat/spending"
        fun done(response: JSONObject) {
            val data = response.getJSONArray("spending")
            var display = ""
            for (i in 0 until data.length()) {
                val record = data.getJSONArray(i)
                display += record.getString(1) + " a payé " +
                        record.getString(2) + " € pour " +
                        record.getString(3) + "\n"
            }
            Log.d("display", data.toString())
            v.findViewById<RecyclerView>(R.id.recycler_view)?.adapter = DepenseAdapter(data, app)
            val balanceView = v.findViewById<TextView>(R.id.historique)
            balanceView?.text = display
        }
        app.request(url, json, ::done)
    }

}