package ovh.cohabits.cohabit1

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class ColocViewHolder(val view: View) : RecyclerView.ViewHolder(view)

//class ClientViewHolder extends ViewHolder {
//
//    private View view;
//    public ClientViewHolder(View view){
//        super(view);
//        this.view = view;
//    }
//}


    class ColocAdapter(val colocs: List<Coloc>, val context: Context) : RecyclerView.Adapter<ColocViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColocViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.coloc_view, parent, false)
            return ColocViewHolder(view)
        }

        override fun getItemCount() = colocs.size

        override fun onBindViewHolder(holder: ColocViewHolder, position: Int) {
            val coloc : Coloc = colocs[position]

            val view = holder.itemView


        }
}