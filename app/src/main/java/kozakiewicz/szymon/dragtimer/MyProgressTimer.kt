package kozakiewicz.szymon.dragtimer

import android.app.Activity
import android.graphics.Color
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import kozakiewicz.szymon.dragtimer.adapters.AdapterDrugList
import java.util.*

class MyProgressTimer(var adapter:AdapterDrugList,var activity: DrugsListActivity) : TimerTask() {
    override fun run() {

       activity.runOnUiThread { adapter.notifyDataSetChanged() }

        Timer().schedule(MyProgressTimer(adapter,activity), 2000)
    }


}