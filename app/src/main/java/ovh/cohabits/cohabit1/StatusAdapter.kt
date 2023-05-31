package ovh.cohabits.cohabit1

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StatusAdapter(private val status: ArrayList<StatusObject>, val context: Context) : RecyclerView.Adapter<StatusViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        return StatusViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun getItemCount(): Int {
        return status.size
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val statusObject : StatusObject = status[position]
        val view = holder.itemView
        val textView = view.findViewById<TextView>(R.id.statusItem)
        textView.text = statusObject.inside.toString()
    }

}
