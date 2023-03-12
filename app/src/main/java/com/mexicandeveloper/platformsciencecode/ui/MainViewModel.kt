package com.mexicandeveloper.platformsciencecode.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mexicandeveloper.platformsciencecode.placeholder.PlaceholderContent
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {
    fun initializeData(app: Application) {
        PlaceholderContent.initial(app)
        if(PlaceholderContent.BESTSCORE.isEmpty())
        viewModelScope.launch(Dispatchers.IO) {

            PlaceholderContent.myHungarianAlgorithm()
            /*val list = (0 until PlaceholderContent.ITEMS.size).
                flatMap {
                    listOf(
                        async { PlaceholderContent.callWfewOptions(it, it) }
                    )
                }.awaitAll()*/

        }
        /*viewModelScope.launch(Dispatchers.IO) {
            val firstAnswer = async {
                PlaceholderContent.fewOptions(0, 1)
            }
            val secondAnswer = async {
                PlaceholderContent.fewOptions(2, 3)
            }
            val thirdAnswer = async {
                PlaceholderContent.fewOptions(4, 5)
            }
            val fourthAnswer = async {
                PlaceholderContent.fewOptions(6, 7)
            }
            val fifthAnswer = async {
                PlaceholderContent.fewOptions(8, 9)
            }
            PlaceholderContent.printOneAnswer(firstAnswer.await())
            PlaceholderContent.printOneAnswer(secondAnswer.await())
            PlaceholderContent.printOneAnswer(thirdAnswer.await())
            PlaceholderContent.printOneAnswer(fourthAnswer.await())
            PlaceholderContent.printOneAnswer(fifthAnswer.await())

            PlaceholderContent.printAll()

        }*/

    }

    override fun onCleared() {
        super.onCleared()
        PlaceholderContent.ITEMS.clear()
    }
}