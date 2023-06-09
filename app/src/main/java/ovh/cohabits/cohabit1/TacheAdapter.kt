package ovh.cohabits.cohabit1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TacheViewHolder(val view: View) : RecyclerView.ViewHolder(view)

class TacheAdapter(private val taches: ArrayList<String>, val context: Context, private val student : ArrayList<String>) : RecyclerView.Adapter<TacheViewHolder>() {
    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(tache: String)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TacheViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.tache_view, parent, false)
        return TacheViewHolder(view)
    }

    override fun getItemCount() = taches.size

    override fun onBindViewHolder(holder: TacheViewHolder, position: Int) {
        val tachesObject = taches[position]
        val studentObject = student[position]
        val view = holder.itemView
        val textView = view.findViewById<TextView>(R.id.tacheView)
        val student_view = view.findViewById<TextView>(R.id.student_view)
        val buttonDeleteTache = view.findViewById<ImageButton>(R.id.buttonDeleteTache)
        textView.text = tachesObject
        student_view.text = studentObject
        buttonDeleteTache.setOnClickListener {
            itemClickListener?.onItemClick(tachesObject)
        }
    }



}