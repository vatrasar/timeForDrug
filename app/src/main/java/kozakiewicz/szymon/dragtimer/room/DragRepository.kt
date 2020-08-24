package kozakiewicz.szymon.dragtimer.room

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import java.util.*

class DragRepository(aplication:Application){
    private var dragDao:DragDao
    init {
        val dataBase:DragDataBase?= DragDataBase.getInstance(aplication.applicationContext)
        dragDao=dataBase!!.DragDao();
    }
    fun update(drug:Drug): Job = CoroutineScope(Dispatchers.IO).launch {
        dragDao.update(drug)


    }
    fun insert(drug:Drug): Job = CoroutineScope(Dispatchers.IO).launch {
        dragDao.insert(drug)


    }

    fun getAllDrags(): Deferred<LiveData<List<Drug>>>
    {
        return CoroutineScope(Dispatchers.IO).async {
            dragDao.getAllDrags()
        }
    }
    fun getAllDragsList(): List<Drug>
    {
        var result:List<Drug>
        return  runBlocking {
            result=CoroutineScope(Dispatchers.IO).async {
            dragDao.getAllDragsList()
        }.await()
            return@runBlocking result
        }

    }

    fun deleteDrug(drug:Drug)=CoroutineScope(Dispatchers.IO).launch {
        dragDao.delete(drug)


    }

    fun insertTakeDrugStac(drug:Drug)
            : Job = CoroutineScope(Dispatchers.IO).launch {
        var takeDrugEvent:TakeDrugEvent= TakeDrugEvent(Calendar.getInstance().timeInMillis,drug.id)
        dragDao.insertTakeDrugStac(takeDrugEvent)


    }
    fun getStacForDrugAndDay(drug:Drug,startOfDay:Long,endOfDay:Long):List<TakeDrugEvent> {
        var result: List<TakeDrugEvent>
        return runBlocking {
            result = CoroutineScope(Dispatchers.IO).async {
                dragDao.getStacForDrugAndDay(drug.id,startOfDay,endOfDay)
            }.await()
            return@runBlocking result
        }
    }
}