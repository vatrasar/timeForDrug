package kozakiewicz.szymon.dragtimer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kozakiewicz.szymon.dragtimer.DrugsListActivity
import kozakiewicz.szymon.dragtimer.R
import kozakiewicz.szymon.dragtimer.Utils
import kozakiewicz.szymon.dragtimer.room.Drug
import java.util.*

class AdapterDrugList(private val listOfDrugs: List<Drug>, private val btnTakeDrugListener: ClickListener, private val btnShowDetailsListener: ClickListener,private val drugsListActivity: DrugsListActivity) :RecyclerView.Adapter<DrugRowViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugRowViewHolder {
        val layoutInflater:LayoutInflater= LayoutInflater.from(parent.context)
        val dragRow:View=layoutInflater.inflate(R.layout.drug_row, parent, false)
        return DrugRowViewHolder(dragRow,btnTakeDrugListener,btnShowDetailsListener,drugsListActivity)
    }

    override fun getItemCount(): Int {
        val test:Int=listOfDrugs.size
        return listOfDrugs.size
    }

    override fun onBindViewHolder(holder: DrugRowViewHolder, position: Int) {
        var labDragName:TextView=holder.view.findViewById<TextView>(R.id.labDragName)
        var prograss:ProgressBar=holder.view.findViewById<ProgressBar>(R.id.dragProgress)
        prograss.max=(Utils.milisecondsToSecounds(listOfDrugs[position].timeInterval)).toInt()
        var progressValue=(Utils.milisecondsToSecounds(Utils.getProgressTime(listOfDrugs[position].timeInterval,Calendar.getInstance().timeInMillis,listOfDrugs[position].dragTime))).toInt()
        prograss.progress=(Utils.milisecondsToSecounds(Utils.getProgressTime(listOfDrugs[position].timeInterval,Calendar.getInstance().timeInMillis,listOfDrugs[position].dragTime))).toInt()
        labDragName.text=listOfDrugs[position].name

        //set btn status
        val button:Button=holder.view.findViewById(R.id.btnTakeDrug)
        if(Utils.isAfterTimeForDrag(listOfDrugs[position].timeInterval,Calendar.getInstance().timeInMillis,listOfDrugs[position].dragTime))
        {

            button.isEnabled=true


        }
        else
        {
            button.isEnabled=false
        }

    }

}


class DrugRowViewHolder(val view: View, private val btnTakeDrugListener: ClickListener, btnShowDetailsListener: ClickListener,private val  drugsListActivity: DrugsListActivity): RecyclerView.ViewHolder(view), View.OnClickListener
{

  init {

      var btnAddDrug:Button=view.findViewById<Button>(R.id.btnTakeDrug)
      var btnDetails:Button=view.findViewById<Button>(R.id.btnDetails)
      btnAddDrug.setOnClickListener(this)
      btnDetails.setOnClickListener { btnShowDetailsListener.onClick(view,adapterPosition,drugsListActivity) }

  }
    override fun onClick(p0: View?) {
        btnTakeDrugListener.onClick(view,adapterPosition,drugsListActivity)
    }

}


interface ClickListener {
    fun onClick(view: View?, position: Int,activity: AppCompatActivity? =null)
}