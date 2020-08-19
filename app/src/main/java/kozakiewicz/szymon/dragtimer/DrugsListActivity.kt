package kozakiewicz.szymon.dragtimer

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kozakiewicz.szymon.dragtimer.adapters.AdapterDrugList
import kozakiewicz.szymon.dragtimer.room.Drug
import kozakiewicz.szymon.dragtimer.viewModels.DrugsViewModel

class DrugsListActivity : AppCompatActivity() {
    lateinit var drugsViewModel:DrugsViewModel
    lateinit var listOfDrugs:LiveData<List<Drug>>
    private lateinit var recyclerView:RecyclerView
    private lateinit var drugListAdapter:AdapterDrugList
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
            if(it.isNotEmpty())
            {
                drugListAdapter= AdapterDrugList(it)
                recyclerView.adapter=drugListAdapter
            }
        })
    }

    fun onInsertNewDrug(view: View) {
        val intent = Intent(this, AddNewDrugActivity::class.java)
        startActivity(intent)

    }
}