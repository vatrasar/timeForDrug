package kozakiewicz.szymon.dragtimer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kozakiewicz.szymon.dragtimer.adapters.AdapterDrugList
import kozakiewicz.szymon.dragtimer.adapters.ClickListener
import kozakiewicz.szymon.dragtimer.room.Drug
import kozakiewicz.szymon.dragtimer.viewModels.DrugsViewModel
import java.util.*

class DrugsListActivity : AppCompatActivity() {
    lateinit var drugsViewModel:DrugsViewModel
    lateinit var listOfDrugs:LiveData<List<Drug>>
    private lateinit var recyclerView:RecyclerView
    private lateinit var drugListAdapter:AdapterDrugList
    lateinit var timerHandler:Handler
    val INTERFACE_UPDATE_DELAY=5000L //milis


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drugs_list)
        drugsViewModel=ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(DrugsViewModel::class.java)
        recyclerView=findViewById(R.id.dragList)
        var manager=LinearLayoutManager(applicationContext)
        manager.orientation=LinearLayoutManager.VERTICAL
        recyclerView.layoutManager=manager
        listOfDrugs=drugsViewModel.getAllDragsList()

        listOfDrugs.observe(this, Observer {
            if (it.isNotEmpty()) {
                val onTakeDrugListener = object : ClickListener {
                    override fun onClick(view: View?, position: Int,activity: AppCompatActivity?) {

                        var drugList: List<Drug>? = drugsViewModel.getAllDragsList().value
                        drugList!![position].dragTime = Calendar.getInstance().timeInMillis
                        drugsViewModel.updateDrug(drugList[position])
                    }
                }

                val showDetailsListener = object : ClickListener {
                    override fun onClick(view: View?, position: Int,activity: AppCompatActivity?) {


                        val intent = Intent(activity, DetailsActivity::class.java)
                        intent.putExtra("position",position)

                        startActivity(intent)
                    }
                }

                drugListAdapter = AdapterDrugList(it, onTakeDrugListener,showDetailsListener,this)
                recyclerView.adapter = drugListAdapter
                Timer().schedule(MyProgressTimer(drugListAdapter, this), INTERFACE_UPDATE_DELAY)
            }
        })



    }

    fun onInsertNewDrug(view: View) {
        val intent = Intent(this, AddNewDrugActivity::class.java)
        startActivity(intent)

    }
}