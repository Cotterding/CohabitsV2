package ovh.cohabits.cohabit1

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

//we need to subclass the android native Application class
//because we need to store permanent data
//a permanent data should stay alive during the whole life of the application
//we cannot put such data in an Activity object
//because the activity is destroyed when the user switches to another screen

//we have to tell android to instantiate this subclass when launching the app
//this is done in the AndroidManifest.xml file
//see the application tag parameter : android:name

class cohabitsClass() : android.app.Application() {

    //the request queue will manage HTTP request
    //this is a part of the Volley library
    //the Volley library is the high-level library for HTTP requests
    //for example, Volley will retry any aborted HTTP request
    //without notifying the caller code

    //we declare here the queue field for the app object
    //but we cannot initialize it here
    //because when the app is launched, the "context" parameter is not yet allocated
    //and this "context" is the argument of the newRequestQueue method

    //therefore we set the queue to null
    //as the queue type is RequestQueue, null is not allowed for its value
    //so we use the Null safety mechanism of kotlin
    //see https://sebhastian.com/kotlin-question-mark/
    var queue: RequestQueue? = null

    //the app will connect on the default server on port 8080
    //or any development server from 8081 to 8085
    //this value is the active port number
    //it may be changed by clicking on the app logo in the connection Activity
    var httpPort: Int = 8080

    //cohabits backend server IP address
    val serveraddr = "51.38.238.103"

    //session string identifying the app connection on the server
    //session may be empty during onboarding (create account and connect)
    var session = ""

    //firebase token for notifications
    //this token is stored on server in the student table
    //it is received by MyFirebaseMessagingService:onNewToken
    //we need to keep this token locally
    //because it is used to create a new account from this smartphone
    var token = ""

    //flat code when the user has joined a flat
    var code = ""

    //this method is called by kotlin when creating the app object
    //we override its definition for the subclass cohabitsClass
    override fun onCreate() {

        //we first call the superclass method
        //this will execute all the initialisation stuff of native android Application class
        super.onCreate()

        //now the app object is ready to use
        //that means the app object is a valid "context"
        //and we can use it to create the Volley request queue
        //and store the queue in the dedicated field
        this.queue = newRequestQueue(this)
    }

    //this method will return the API url
    //use it by appending the API message and its parameters as URL parameters
    fun apiurl(): String {
        return "http://" + serveraddr + ":" + httpPort
    }

    //send HTTP request
    //callback is 3rd argument
    //error management only ensure logging
    fun request(command: String, data: JSONObject, listener: (response: JSONObject) -> Unit) {
        val url = "http://" + serveraddr + ":" + httpPort + command
        println(url)
        println(data.toString())
        val req = JsonObjectRequest(Request.Method.POST, url, data, listener,
            object : Response.ErrorListener {
                //this is an object used only to add the onErrorResponse function on it
                override fun onErrorResponse(error: VolleyError) {
                    //display the error in android studio trace window (when debugging)
                    if (error is com.android.volley.NoConnectionError) {
                        println("No internet connection")
                    } else if (error is com.android.volley.TimeoutError) {
                        println("TimeoutError")
                    } else if (error is com.android.volley.AuthFailureError) {
                        println("Please check your credentials")
                    } else if (error is com.android.volley.ServerError) {
                        println("Server is not responding. Please try again later")
                        println(String(error.networkResponse.data))
                    } else if (error is com.android.volley.NetworkError) {
                        println("Please check your internet connection")
                    } else if (error is com.android.volley.ParseError) {
                        println("Parsing error! Please try again after some time")
                    }
                }
            }
        )
        queue?.add(req)
    }
}