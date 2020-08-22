package kozakiewicz.szymon.dragtimer.viewModels

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import kozakiewicz.szymon.dragtimer.room.Drug

class DrugsViewModel(application: Application):AndroidViewModel(application)
{
    private var dragsRepository=kozakiewicz.szymon.dragtimer.room.DragRepository(application)
    private var allDragsList:Deferred<LiveData<List<Drug>>> =dragsRepository.getAllDrags()
//    var drugsViewModel:DrugsViewModel= ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(DrugsViewModel::class.java)

    fun getAllDragsList()= runBlocking {
        allDragsList.await()

    }
//    fun refreshModel()
//    {
//        allDragsList.await()
//    }

    fun insertNewDrug(newDrug:Drug)
    {
        dragsRepository.insert(newDrug)
    }

    fun updateDrug(drugTuUpdate: Drug) {
        dragsRepository.update(drugTuUpdate)
    }


}