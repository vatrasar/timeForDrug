package kozakiewicz.szymon.dragtimer

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kozakiewicz.szymon.dragtimer.room.Drug
import kozakiewicz.szymon.dragtimer.viewModels.DrugsViewModel
import java.util.*

class AddNewDrugActivity : AppCompatActivity() {
    lateinit var drugsViewModel: DrugsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_drug)
        drugsViewModel= ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(DrugsViewModel::class.java)
    }

    fun onAddNewDrug(view: View) {
        var txtNewDrugName:EditText=findViewById<EditText>(R.id.txtNewDrugName)
        //time interval
        var txtSecound:EditText=findViewById<EditText>(R.id.txtSecound)
        var txtMinutes:EditText=findViewById<EditText>(R.id.txtMinute)
        var txtHours:EditText=findViewById<EditText>(R.id.txtHour)

        val hoursNumber: Int = txtHours.getText().toString().toInt()
        val minutesNumber: Int = txtMinutes.getText().toString().toInt()
        val secoundsNumber: Int = txtSecound.getText().toString().toInt()
        val timeInterval:Long = (1000 * (minutesNumber * 60 + hoursNumber * 60 * 60 + secoundsNumber)).toLong()

        var newDrugName:String=txtNewDrugName.text.toString()

        drugsViewModel.insertNewDrug(Drug(newDrugName, timeInterval, Calendar.getInstance().timeInMillis - timeInterval))
        finish()
    }
}