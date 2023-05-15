package ovh.cohabits.cohabit1

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.cardview.widget.CardView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class ConnexionActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)

        //we need to use the permanent values stored in the app object
        //the getApplication() method is the running app object
        //as this mathod returns a native android.app.Application object,
        //we have to cast it to the subclass that we have defined
        //then we can read the attributes defined by the subclass
        val app = (application as cohabitsClass)

        //port selector
        //we need a way to specify to the app which server it should use
        //the user can click on the app logo on the first screen
        //the port number will change and be displayed briefly
        //the port number will cycle bettween 8080 and 8085
        //8080 is the "production" server
        //8081 to 8085 are the "developpment" servers

        //get the logo object from its ID (set in the layout XML file)
        val logo = findViewById<ImageView>(R.id.imageViewLogoConnexion)

        //add the callback function for a click on the logo
        logo.setOnClickListener {
            //change the port number
            app.httpPort += 1
            if (app.httpPort == 8087) {
                //After 8086, go back to 8080
                app.httpPort = 8080
            }

            //use a "toast" object to display the text of the active port number
            var toast: Toast = Toast.makeText(applicationContext, app.httpPort.toString(), Toast.LENGTH_SHORT)
            //get down into the Toast object
            //because we want big characters displayed
            var layout: RelativeLayout = toast.view as RelativeLayout
            var tv: TextView = layout.getChildAt(0) as TextView
            //now we can set the characters size
            tv.textSize = 30F
            //and set the toast position on screen
            toast.setGravity(Gravity.TOP, 0, 200)
            //ok, time to display the toast now
            toast.show()
        }

        //button "Je n'ai pas de compte"
        val newButton = findViewById<Button>(R.id.button_noCompte)
        newButton.setOnClickListener {
            //launch the new account activity
            val intent = Intent(this, CreationCompteActivity::class.java)
            startActivity(intent)
        }

        //button "continuer"
        val logonButton = findViewById<Button>(R.id.button_connexion)
        logonButton.setOnClickListener {

            //get data from editText fields
            var email = findViewById<EditText>(R.id.editText_emailAddress_identification).text.toString()
            var pwd = findViewById<EditText>(R.id.editText_Password_identification).text.toString()

            //build the url string with server address and arguments
            var url = "/student/connect"

            var data = JSONObject()
            data.put("email", email)
            data.put("password", pwd)
            data.put("token", app.token)

            fun done(response: JSONObject) {
                app.session = response.getString("session")
                //display the response message with a popup on screen
                //todo: change activity is connection was successful
                //todo: display the correct message if connection was refused
                Toast.makeText(applicationContext, response.getString("message"), Toast.LENGTH_SHORT).show()
                //print the response in the android studio trace window (when debugging)
                println(response)

		//launch the home activity
            	val intent = Intent(this, MainActivity::class.java)
            	startActivity(intent)
            }
            app.request(url, data, ::done)




        }

        val cardView = findViewById<CardView>(R.id.card_view)
        cardView.setBackgroundResource(R.drawable.card_view_droit)
        newButton.setBackgroundResource(R.drawable.edit_button_creer_compte)
    }
}
