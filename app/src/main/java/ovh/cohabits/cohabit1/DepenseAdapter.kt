package ovh.cohabits.cohabit1

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.util.JsonUtils
import org.json.JSONArray

class DepenseViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    class DepenseAdapter(private val list_depense: JSONArray, val context: Context) : RecyclerView.Adapter<DepenseViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepenseViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.statut_view, parent, false)
            Log.d("Depense", list_depense.toString())
            return DepenseViewHolder(view)
        }

        override fun getItemCount() = list_depense.length()

        override fun onBindViewHolder(holder: DepenseViewHolder, position: Int) {
            val record = list_depense.getJSONArray(position)
            val view = holder.itemView
            view.findViewById<TextView>(R.id.motif_depense).text = record.getString(3)
            view.findViewById<TextView>(R.id.montant_depense).text = record.getString(2)
            view.findViewById<TextView>(R.id.personne_depense).text = record.getString(1)
        }
}