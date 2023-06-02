package ovh.cohabits.cohabit1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import org.json.JSONObject

lateinit var app : cohabitsClass

class RemboursementActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remboursement)
        app = (application as cohabitsClass)
        balance()

        val rembourser = findViewById<Button>(R.id.rembourser)
        rembourser?.setOnClickListener {
            rembourser()
        }

    }

    fun balance() {
        val json = JSONObject(mapOf("session" to app.session))

        var url = "/flat/balance"
        fun done(response: JSONObject) {
            val data = response.getJSONArray("balance")
            var display = ""
            for (i in 0 until data.length()) {
                val record = data.getJSONArray(i)
                display += record.getString(2) + "€ de " +
                        record.getString(0) + " vers " +
                        record.getString(1) + "\n"
            }
            val balanceView = findViewById<TextView>(R.id.balance)
            balanceView?.text = display
        }
        app.request(url, json, ::done)
    }

    fun rembourser() {
        val amount = findViewById<EditText>(R.id.amount)?.text.toString().toFloat()
        val friend = findViewById<EditText>(R.id.destinataire)?.text.toString()

        val json = JSONObject(mapOf("session" to app.session, "amount" to amount, "friend" to friend))

        var url = "/student/pay"

        fun done(response: JSONObject) {

            Toast.makeText(applicationContext, "Remboursement effectué", Toast.LENGTH_SHORT).show()
            balance()
        }
        app.request(url, json, ::done)

    }

}