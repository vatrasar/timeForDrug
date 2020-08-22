package kozakiewicz.szymon.dragtimer.room

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*

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
    fun getAllDragsList(): Deferred<List<Drug>>
    {
        return CoroutineScope(Dispatchers.IO).async {
            dragDao.getAllDragsList()
        }
    }
}