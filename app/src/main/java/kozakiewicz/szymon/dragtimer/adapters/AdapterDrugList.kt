package kozakiewicz.szymon.dragtimer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kozakiewicz.szymon.dragtimer.R
import kozakiewicz.szymon.dragtimer.room.Drug

class AdapterDrugList(private val listOfDrugs:List<Drug>) :RecyclerView.Adapter<DrugRowViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugRowViewHolder {
        val layoutInflater:LayoutInflater= LayoutInflater.from(parent.context)
        val dragRow:View=layoutInflater.inflate(R.layout.drug_row,parent,false)
        return DrugRowViewHolder(dragRow)
    }

    override fun getItemCount(): Int {
        val test:Int=listOfDrugs.size
        return listOfDrugs.size
    }

    override fun onBindViewHolder(holder: DrugRowViewHolder, position: Int) {
        var labDragName:TextView=holder.view.findViewById<TextView>(R.id.labDragName)
        labDragName.text=listOfDrugs[position].name
    }

}


class DrugRowViewHolder(val view:View): RecyclerView.ViewHolder(view)