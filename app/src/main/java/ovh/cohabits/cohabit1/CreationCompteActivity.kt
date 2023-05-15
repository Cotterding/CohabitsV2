package ovh.cohabits.cohabit1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class CreationCompteActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation_compte)

        //get the app object as an instance of cohabits subclass
        val app = (getApplication() as cohabitsClass)

        val backButton = findViewById<Button>(R.id.button_retour_connexion)
        backButton.setBackgroundResource(R.drawable.edit_button)
        backButton.setOnClickListener {
            val intent = Intent(this, ConnexionActivity::class.java)
            startActivity(intent)
        }

        val newButton = findViewById<Button>(R.id.button_valide_new_compte)
        newButton.setOnClickListener {

            //get data from editText fields
            var pseudo = findViewById<EditText>(R.id.editText_Speudo).getText().toString()
            var email = findViewById<EditText>(R.id.editText_emailAddress_creation).getText().toString()
            var pwd = findViewById<EditText>(R.id.ditText_Password_creation).getText().toString()

            //todo: check that both passwords are equal and display error if not
            //todo: enforce password security (length and character types)

            //build the url string with server address and arguments
            var url = "/student/create"

            var data = JSONObject()
            data.put("nickname",pseudo)
            data.put("email", email)
            data.put("password", pwd)

            fun done(response: JSONObject) {
                //display the response message with a popup on screen
                //todo: change activity is connection was successful
                //todo: display the correct message if connection was refused
                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show()
                //print the response in the android studio trace window (when debugging)
                println(response)
            }
            app.request(url, data, ::done)
        }

	val cardView = findViewById<CardView>(R.id.card_view)
        cardView.setBackgroundResource(R.drawable.card_view_gauche)
    }
}