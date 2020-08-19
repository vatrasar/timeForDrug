package kozakiewicz.szymon.dragtimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import kozakiewicz.szymon.dragtimer.room.Drug
import kozakiewicz.szymon.dragtimer.viewModels.DrugsViewModel

class AddNewDrugActivity : AppCompatActivity() {
    lateinit var drugsViewModel: DrugsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_drug)
        drugsViewModel= ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(DrugsViewModel::class.java)
    }

    fun onAddNewDrug(view: View) {
        var txtNewDrugName:EditText=findViewById<EditText>(R.id.txtNewDrugName)
        var newDrugName:String=txtNewDrugName.text.toString()
        drugsViewModel.insertNewDrug(Drug(newDrugName,1,1))

    }
}