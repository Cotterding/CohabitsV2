package ovh.cohabits.cohabit1

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import org.json.JSONObject

class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        //get the app object as an instance of cohabits subclass
        val app = (getApplication() as cohabitsClass)

        //we store the token on the app object
        //because it is needed for registering a new student on server
        app.token = token

        //if the student is already connected
        //i.e. when the session field is set in the app object,
        //then we have to tx the token to the server
        //otherwise the token will be a connection parameter

        if (app.session != "") {
            var url = "/student/token"

            var data = JSONObject()
            data.put("session", app.session)
            data.put("token", token)

            fun done(response: JSONObject) {
                //display the response message with a popup on screen
                Toast.makeText(
                    getApplicationContext(),
                    response.getString("message"),
                    Toast.LENGTH_SHORT
                ).show()
                //print the response in the android studio trace window (when debugging)
                println(response)
            }

            app.request(url, data, ::done)
        }
    }

}