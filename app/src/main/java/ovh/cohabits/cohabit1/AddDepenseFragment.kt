package ovh.cohabits.cohabit1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import org.json.JSONObject

class AddDepenseFragment : Fragment() {

    lateinit var app : cohabitsClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_depense, container, false)
        app = (requireActivity().application as cohabitsClass)
        //launch AddExpense
        view?.findViewById<Button>(R.id.add_depense)?.setOnClickListener {
            addExpense(view)
        }
        return view
    }

    fun addExpense(v : View) {

        val amount = v?.findViewById<EditText>(R.id.montant_depense)?.text.toString().toFloat()
        val why = v?.findViewById<EditText>(R.id.name_depense)?.text.toString()

        val json = JSONObject(mapOf("session" to app.session, "amount" to amount, "why" to why))

        var url = "/student/spend"

        fun done(response: JSONObject) {

            Toast.makeText(requireContext().applicationContext, "Dépense prise en compte", Toast.LENGTH_SHORT).show()
        }
        app.request(url, json, ::done)

    }

}