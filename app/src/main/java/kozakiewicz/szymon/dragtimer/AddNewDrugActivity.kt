package kozakiewicz.szymon.dragtimer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kozakiewicz.szymon.dragtimer.room.DragRepository
import kozakiewicz.szymon.dragtimer.room.Drug
import kozakiewicz.szymon.dragtimer.viewModels.DrugsViewModel
import java.util.*


class AddNewDrugActivity : AppCompatActivity() {
    lateinit var drugsViewModel: DrugsViewModel
    lateinit  var dragsRepository: DragRepository
    var isUpade:Boolean=false
    var position:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var myIntent:Intent = getIntent();
        dragsRepository=DragRepository(application)
        isUpade=myIntent.getBooleanExtra("isUpdate", false)






        setContentView(R.layout.activity_add_new_drug)


        isUpade=myIntent.getBooleanExtra("isUpdate", false)
        if(isUpade)
        {
            position=myIntent.getIntExtra("position", 1)

            var drugList=dragsRepository.getAllDragsList()

            var txtNewDrugName:EditText=findViewById<EditText>(R.id.txtNewDrugNam)
            var btnAddNewDrug:Button=findViewById<Button>(R.id.btnAddNewDrug)
            btnAddNewDrug.setText(R.string.edit)
            var txtSecound:EditText=findViewById<EditText>(R.id.txtSecound)
            txtSecound.setText(Utils.milisecondsToSecounds(drugList[position].timeInterval).toInt().toString())
            var txtHours:EditText=findViewById<EditText>(R.id.txtHour)
            txtHours.setText("0")
            txtSecound.setText(Utils.milisecondsToSecounds(drugList[position].timeInterval).toInt().toString())
            txtNewDrugName.setText(drugList[position].name)


        }

        drugsViewModel= ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(DrugsViewModel::class.java)
    }


    fun onAddNewDrug(view: View) {

        var txtNewDrugName:EditText=findViewById<EditText>(R.id.txtNewDrugNam)
        //time interval
        var txtSecound:EditText=findViewById<EditText>(R.id.txtSecound)
        var txtMinutes:EditText=findViewById<EditText>(R.id.txtMinute)
        var txtHours:EditText=findViewById<EditText>(R.id.txtHour)

        val hoursNumber: Int = txtHours.getText().toString().toInt()
        val minutesNumber: Int = txtMinutes.getText().toString().toInt()
        val secoundsNumber: Int = txtSecound.getText().toString().toInt()
        val timeInterval:Long = Utils.getTimeInterval(minutesNumber, hoursNumber, secoundsNumber)

        var newDrugName:String=txtNewDrugName.text.toString()
        if(isUpade)
        {
            var drugsList=dragsRepository.getAllDragsList()
            var drugToUpdate:Drug=drugsList[position]
            drugToUpdate.timeInterval=timeInterval
            drugToUpdate.name=newDrugName
            dragsRepository.update(drugToUpdate)

        }
        else
            drugsViewModel.insertNewDrug(Drug(newDrugName, timeInterval, Calendar.getInstance().timeInMillis - timeInterval))
        finish()
    }


}