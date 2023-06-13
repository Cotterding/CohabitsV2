package ovh.cohabits.cohabit1

import android.content.Intent
import android.os.Bundle
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

        //launch AddExpense
        view?.findViewById<Button>(R.id.button_add_spent)?.setOnClickListener {
            addExpense(view)
        }

        //lauch history

        history(view)
        return view
    }

    fun addExpense(v : View) {

        val amount = v?.findViewById<EditText>(R.id.amount)?.text.toString().toFloat()
        val why = v?.findViewById<EditText>(R.id.description)?.text.toString()

        val json = JSONObject(mapOf("session" to app.session, "amount" to amount, "why" to why))

        var url = "/student/spend"

        fun done(response: JSONObject) {

            Toast.makeText(requireContext().applicationContext, "Dépense prise en compte", Toast.LENGTH_SHORT).show()
        }
        app.request(url, json, ::done)

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
            val balanceView = v?.findViewById<TextView>(R.id.historique)
            balanceView?.text = display
        }
        app.request(url, json, ::done)
    }

}