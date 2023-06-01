package ovh.cohabits.cohabit1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StatutViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class StatutAdapter(private val status: ArrayList<StatusObject>, val context: Context) : RecyclerView.Adapter<StatutViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatutViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.statut_view, parent, false)
            return StatutViewHolder(view)
        }

        override fun getItemCount() = status.size

        override fun onBindViewHolder(holder: StatutViewHolder, position: Int) {
            val statusObject : StatusObject = status[position]
            val view = holder.itemView
            val textView = view.findViewById<TextView>(R.id.textViewStatut2)
            textView.text = statusObject.inside.toString()
        }

        class StatusObject {
            val inside : ArrayList<String> = arrayListOf()

            fun setInside(item : String) {
                inside.add(item)
            }

        }
}