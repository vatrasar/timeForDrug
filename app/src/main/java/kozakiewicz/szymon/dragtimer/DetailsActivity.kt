package kozakiewicz.szymon.dragtimer

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kozakiewicz.szymon.dragtimer.room.DragRepository
import kozakiewicz.szymon.dragtimer.viewModels.DrugsViewModel
import java.text.SimpleDateFormat
import java.util.*

class DetailsActivity : AppCompatActivity() {
    var counter = 0

    //runs without a timer by reposting this handler at the end of the runnable
    var timerHandler = Handler()
    var position = 0
    var timerRunnable: MyTime? = null
    var drugsViewModel: DrugsViewModel? = null
    lateinit  var drugsRepository:DragRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myIntent = intent
        position = myIntent.getIntExtra("position", 1)
        drugsRepository=DragRepository(application)
        drugsViewModel=ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(DrugsViewModel::class.java)

        counter = 0
        setContentView(R.layout.activity_main)
        val progressBar = findViewById<View>(R.id.timeProgressbar) as ProgressBar
        progressBar.visibility = View.GONE

        if (isDragTime) {
            setDragTimeLook()
        } else {
            //get data
            var drugList=drugsRepository.getAllDragsList()
            val drug = drugList[position]

            var timeInterval: Long = drug.timeInterval

            var lastDragTime: Long = drug.dragTime


            //set Time in timeView
            (findViewById<View>(R.id.txtRemainigTime) as TextView).setText(R.string.remaining_time)

            //set color:
            var labInfo=(findViewById<View>(R.id.labTime) as TextView)
            labInfo.setTextColor(Color.BLUE)
            labInfo.setText(Utils.milisecondsToMinutes(Utils.getRemaingTime(timeInterval,Calendar.getInstance().timeInMillis,lastDragTime)).toString())



            //set progrss
            setProgrss(lastDragTime, timeInterval)
        }
    }

    private fun setProgrss(lastDragTime: Long, timeInterval: Long) {
        val progressBar = findViewById<View>(R.id.timeProgressbar) as ProgressBar
        progressBar.max = Utils.milisecondsToSecounds(timeInterval).toInt()
        val progress = Utils.milisecondsToSecounds(timeInterval).toInt() - Utils.milisecondsToSecounds(Utils.getRemaingTime(timeInterval, Calendar.getInstance().timeInMillis, lastDragTime)).toInt()
        progressBar.progress = progress
        progressBar.visibility = View.VISIBLE
    }

    private fun setDragTimeLook() {
        val labTime = findViewById<View>(R.id.labTime) as TextView
        labTime.setText(R.string.timeForDrag)
        labTime.setTextColor(Color.MAGENTA)
        (findViewById<View>(R.id.txtRemainigTime) as TextView).text = ""
    }

    private val isDragTime: Boolean
        private get() {

            var drugList=drugsRepository.getAllDragsList()
            val drug = drugList[position]

            var timeInterval: Long = drug.timeInterval

            var lastDragTime: Long = drug.dragTime

            val currentTime = Calendar.getInstance().timeInMillis
            return Utils.isAfterTimeForDrag(timeInterval, currentTime, lastDragTime)
        }

    fun onSettings(view: View?) {
        val intent = Intent(this, AddNewDrugActivity::class.java)
        intent.putExtra("position",position)
        intent.putExtra("isUpdate",true)
        startActivity(intent)
    }

    fun onTakeDrag(view: View?) {


        //get Shared Preferences instance
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPref.edit()

        //get data
        val lastTime = Calendar.getInstance().timeInMillis
        var timeInterval: Long = 1
        timeInterval = sharedPref.getLong("timeInterval", timeInterval)

        //new lastDragTime
        val lastDragTime = Calendar.getInstance().timeInMillis
        editor.putLong("lastDragTime", lastDragTime)
        editor.commit()

        //set Time in timeView
        (findViewById<View>(R.id.txtRemainigTime) as TextView).setText(R.string.remaining_time)

        //set labRemaningtime:
        val labRemainingTime = findViewById<View>(R.id.labTime) as TextView
        labRemainingTime.setTextColor(Color.BLUE)
        labRemainingTime.text = Utils.milisecondsToMinutes(timeInterval).toString() + ""

        //run thread
        timerHandler.removeCallbacks(timerRunnable!!)
        timerRunnable = MyTime(findViewById<View>(R.id.labTime) as TextView, findViewById<View>(R.id.txtRemainigTime) as TextView, timerHandler, lastTime, timeInterval, findViewById<View>(R.id.timeProgressbar) as ProgressBar)
        timerHandler.postDelayed(timerRunnable!!, 5000)

        //set notify service
        val intent = Intent(this, AlarmService::class.java)
        startService(intent)


        //set progress
        setProgrss(lastDragTime, timeInterval)


        //update stac
        //get day of week
        val sdf = SimpleDateFormat("EEEE", Locale.US)
        val currentDayOfTheWeek = sdf.format(Calendar.getInstance().time)
        //checking should memmory  be reset
        //get current day form memory
        val currentDayFromMemmory = sharedPref.getString("currentDay", "")
        if (currentDayFromMemmory != currentDayOfTheWeek) {
            editor.putString("currentDay", currentDayOfTheWeek)
            editor.putInt(currentDayOfTheWeek, 0)
            editor.commit()
        }
        //update drugs number
        val numberOfDrugsToday = sharedPref.getInt(currentDayOfTheWeek, 0)
        editor.putInt(currentDayOfTheWeek, numberOfDrugsToday + 1)
        editor.commit()
    }

    fun onReset(view: View?) {

        var drugList=drugsRepository.getAllDragsList()





        val drugToUpadte = drugList[position]
        drugToUpadte.dragTime = Calendar.getInstance().timeInMillis - drugToUpadte.timeInterval
        drugsRepository.update(drugToUpadte)



    }

    fun onStac(view: View?) {
        val intent = Intent(this, StacActivity::class.java)
        intent.putExtra("position",position)
        startActivity(intent)
    }

    fun onDeleteDrug(view: View) {
        var drugsList=drugsRepository.getAllDragsList()
        drugsRepository.deleteDrug(drugsList[position])
        finish()

    }
}

class MyTime(var labTime: TextView, var labInfoLab: TextView, var timerHandler: Handler, var lastTime: Long, var timeInterval: Long, var progressBar: ProgressBar) : Thread() {
    var takeDragButton: Button? = null
    override fun run() {
        super.run()
        if (Utils.isAfterTimeForDrag(timeInterval, Calendar.getInstance().timeInMillis, lastTime)) {
            labTime.setText(R.string.timeForDrag)
            labTime.setTextColor(Color.GREEN)
            labInfoLab.text = ""
            takeDragButton!!.isEnabled = true
            progressBar.visibility = View.GONE
        } else {
            labTime.text = Utils.milisecondsToMinutes(Utils.getRemaingTime(timeInterval, Calendar.getInstance().timeInMillis, lastTime)).toString() + ""
            timerHandler.postDelayed(this, 5000)
            val progress = Utils.milisecondsToSecounds(timeInterval).toInt() - Utils.milisecondsToSecounds(Utils.getRemaingTime(timeInterval, Calendar.getInstance().timeInMillis, lastTime)).toInt()
            progressBar.progress = progress
        }
    }
}