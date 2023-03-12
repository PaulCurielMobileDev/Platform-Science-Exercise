package com.mexicandeveloper.platformsciencecode.placeholder

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.google.gson.Gson
import com.mexicandeveloper.platformsciencecode.data.models.DataResponse
import java.util.HashMap
import kotlin.collections.ArrayList

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    val ITEMS: MutableList<PlaceholderItem> = ArrayList()
    var SCORE: MutableList<Array<Double>> = ArrayList()
    var BESTSCORE: MutableList<Int> = ArrayList()
    var SHIPMENTS: MutableList<String> = ArrayList()
    var maxScore: Double = 0.0

    /**
     * A map of sample (placeholder) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, PlaceholderItem> = HashMap()



    fun calculateScore(theList: List<Int>) {
        var score = 0.0
        for (i in 0..theList.lastIndex) {
            score += SCORE[i][theList[i]]
        }
        if (maxScore < score) {
            maxScore = score
            BESTSCORE.clear()
            BESTSCORE = theList.toMutableList()
            printAll(theList.toIntArray())
        }

    }



    fun getScore(theList: IntArray): Double {
        var score = 0.0
        for (i in 0..theList.lastIndex) {
            if (theList[i] >= 0)
                score += SCORE[i][theList[i]]
        }
        return score
    }




    fun findtheBigest(): Double {
        var max = 0.0
        for (i in SCORE) {
            if (max < i.max()) {
                max = i.max()
            }
        }

        return max
    }


    fun myHungarianAlgorithm() {
        val matrix: Array<DoubleArray> = Array(SCORE.size) {
            DoubleArray(SCORE[0].size) {
                0.0
            }
        }
        val max = findtheBigest()

        for (i in SCORE.indices) {
            for (j in SCORE[0].indices) {
                matrix[i][j] = max - SCORE[i][j]
            }
        }
        val drivers = getDrivers()

        matrix.print(drivers)


        BESTSCORE = myHungarian(matrix, max).toMutableList()
        printAll(BESTSCORE.toIntArray())

    }

    fun Array<DoubleArray>.print(drivers: List<String>) {
        Log.d(
            "myMatrix",
            "------------------------------------------------------------------------------------------------------------"
        )
        for (i in 0..this.lastIndex) {
            var str = ""
            for (j in 0..this[i].lastIndex) {
                str += "\t\t${String.format("%.2f", this[i][j])}"
            }
            if (drivers[i].length > 13)
                Log.d("${drivers[i]}\t\t", "\t\t$str")
            else
                Log.d("${drivers[i]}\t\t\t", "\t\t$str")
        }
    }

    fun hungarianStep1(matrix: Array<DoubleArray>) {
        var min: Double
        val matrixSize = matrix.size
        for (i in 0 until matrixSize) {
            min = matrix[i][0]
            for (j in 1 until matrixSize)
                if (matrix[i][j] < min)
                    min = matrix[i][j]
            for (j in 0 until matrixSize)
                matrix[i][j] -= min
        }

    }

    fun hungarianStep2(matrix: Array<DoubleArray>) {
        var min: Double
        val matrixSize = matrix.size
        for (j in 0 until matrixSize) {
            min = matrix[0][j]
            for (i in 1 until matrixSize)
                if (matrix[i][j] < min)
                    min = matrix[i][j]
            for (i in 0 until matrixSize)
                matrix[i][j] -= min
        }
    }

    private fun countZeroesInRow(matrix: Array<DoubleArray>, i: Int) =
        matrix[i].count { it == 0.0 }

    private fun countZeroesInColumn(matrix: Array<DoubleArray>, j: Int): Int {
        var result = 0
        for (i in 0 until matrix.size)
            if (matrix[i][j] == 0.0)
                result++
        return result
    }

    fun checkLines(matrix: Array<DoubleArray>): Int {

        var rowBool = BooleanArray(matrix.size) { false }
        var colBool = BooleanArray(matrix[0].size) { false }
        getCOLROWSBOOLs(matrix, rowBool, colBool)
        var count = 0
        for (i in rowBool) if (i) count++
        for (i in colBool) if (i) count++
        return count
    }

    fun getCOLROWSBOOLs(matrix: Array<DoubleArray>, rowBool: BooleanArray, colBool: BooleanArray) {

        var countRows = 0
        for (i in matrix.indices) {
            if (countZeroesInRow(matrix, i) == 1) {
                countRows++
            }
        }
        var countColumns = 0
        for (j in matrix[0].indices) {
            if (countZeroesInColumn(matrix, j) == 1) {
                countColumns++
            }
        }
        if (countRows > countColumns) {
            for (i in matrix.indices) {
                if (countZeroesInRow(matrix, i) > 1) {
                    rowBool[i] = true
                } else {
                    for (j in matrix[i].indices) {
                        if (matrix[i][j] == 0.0)
                            colBool[j] = true
                    }
                }
            }
        } else {
            for (j in matrix[0].indices) {
                if (countZeroesInColumn(matrix, j) > 1) {
                    colBool[j] = true
                } else {
                    for (i in matrix.indices) {
                        if (matrix[i][j] == 0.0)
                            rowBool[i] = true
                    }
                }
            }
        }

    }

    fun hungarianStep3(matrix: Array<DoubleArray>, maxim: Double) {

        var min = maxim

        var rowBool = BooleanArray(matrix.size) { false }
        var colBool = BooleanArray(matrix[0].size) { false }
        getCOLROWSBOOLs(matrix, rowBool, colBool)
        for (i in matrix.indices) {
            for (j in matrix[0].indices) {
                if (!rowBool[i] && !colBool[j]) {
                    if (min > matrix[i][j]) min = matrix[i][j]
                }
            }
        }


        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (rowBool[i] && colBool[j]) {
                    matrix[i][j] += min
                }
                if (!rowBool[i] && !colBool[j]) {
                    matrix[i][j] -= min
                }
            }
        }


    }

    fun hungarianGetAnswers(matrix: Array<DoubleArray>, answer: IntArray, level: Int) {
        if (level < matrix.size) {
            for (j in matrix[level].indices) {
                if (matrix[level][j] == 0.0) {
                    if (!answer.contains(j)) {
                        answer[level] = j
                        hungarianGetAnswers(matrix, answer, level + 1)
                    }
                }
            }
        }
    }

    fun myHungarian(matrix: Array<DoubleArray>, maxVal: Double): IntArray {
        val drivers = getDrivers()
        hungarianStep1(matrix)
        matrix.print(drivers)
        hungarianStep2(matrix)
        matrix.print(drivers)
        while (checkLines(matrix) < matrix.size) {

            hungarianStep3(matrix, maxVal)
            matrix.print(drivers)
        }
        var answers = IntArray(matrix.size) { -1 }
        hungarianGetAnswers(matrix, answers, 0)
        return answers
    }


    fun callAFor(theList: MutableList<Int>) {

        if (theList.size < SCORE.size) {
            val anotherList = mutableListOf<Int>()
            anotherList.addAll(theList)
            anotherList.add(-1)
            for (i in 0 until SCORE.size) {
                if (!theList.contains(i)) {
                    anotherList[theList.size] = i
                    callAFor(anotherList)
                }
            }
        } else {
            Log.d("TEST", "${theList.toString()}")
            calculateScore(theList)
        }
    }

    fun callWfewOptions(ini: Int, fin: Int) {
        val theList: MutableList<Int> = mutableListOf()
        theList.add(-1)
        for (i in ini..fin) {
            theList[0] = i
            callAFor(theList)

        }

        printAll(BESTSCORE.toIntArray())

    }



    fun SearchBestInRow(row: Int, theTry: Int): Int {
        if (theTry > SCORE.size) return -1
        val theList = SCORE[row]
        var max = theList.max()

        var countTry = -1
        while (countTry < SCORE.size) {
            for ((i, v) in theList.withIndex()) {
                if (v == max) {
                    countTry++
                    if (countTry == theTry) {
                        return i
                    }
                }
            }
            var secondBest = 0.0
            for (v in theList) {
                if (v < max) {
                    if (v > secondBest) {
                        secondBest = v
                    }
                }
            }
            max = secondBest
        }

        return -1
    }


    suspend fun fillBestScore() {
        val theScores = IntArray(ITEMS.size) { -1 }
        search(theScores, 0)
    }

    fun IntArray.toCad(): String {
        var str = java.lang.StringBuilder()

        for (i in this) {
            str.append(" $i")
        }
        return str.toString()
    }

    fun search(intArray: IntArray, level: Int) {
        val newScores = IntArray(ITEMS.size) { -1 }

        for ((i, v) in intArray.withIndex()) {
            newScores[i] = v
        }
        if (level < newScores.size) {
            var theTry = 0
            while (theTry < newScores.size) {
                val bestValue = SearchBestInRow(level, theTry = theTry)
                //               if (bestValue < 0) return

                if (!newScores.contains(bestValue)) {
                    newScores[level] = bestValue
                    Log.i("Advance", "level $level, ${newScores.toCad()}")
                    search(newScores, level + 1)
                }
                theTry++
            }
        } else {
            val newScore = getScore(newScores)
            if (maxScore < newScore) {
                maxScore = newScore
                BESTSCORE = newScores.toMutableList()
            }


        }
    }

    fun getDrivers(): List<String> {
        val drivers: MutableList<String> = ArrayList<String>()
        for (i in ITEMS) {
            drivers.add(i.name)
        }
        return drivers
    }

    fun printAll(newScores: IntArray) {
        val drivers = getDrivers()
        Log.d("MAXScore", "${getScore(newScores)}")

        for (i in 0..SCORE.lastIndex) {
            var str = ""
            for (j in 0..SCORE[i].lastIndex) {
                str += "\t\t${String.format("%.2f", SCORE[i][j])}"
            }
            if (drivers[i].length > 13)
                Log.d("${drivers[i]}\t\t", "\t\t$str")
            else
                Log.d("${drivers[i]}\t\t\t", "\t\t$str")
        }
        for (i in 0..BESTSCORE.lastIndex) {
            if (newScores[i] > -1)
                Log.d(
                    drivers[i],
                    "\t\tBESTSCORE ${BESTSCORE[i]} = ${SCORE[i][newScores[i]]}"
                )
        }
    }

    fun initial(application: Application) = if (ITEMS.isEmpty()) {

        val fileName = "data.json"
        val bufferedReader = application.assets.open(fileName).bufferedReader()
        val data = bufferedReader.use {
            it.readText()
        }
        val gson = Gson()
        val dataResponse = gson.fromJson(data, DataResponse::class.java)
        SHIPMENTS.addAll(dataResponse.shipments)
        for (i in 0..dataResponse.drivers.lastIndex) {
            addItem(createPlaceholderItem(i + 1, dataResponse.drivers[i], dataResponse.shipments))
        }

    } else {
        Toast.makeText(
            application.applicationContext,
            "Trying to add more elements",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun MutableList<Int>.swapValue(ini: Int, fin: Int) {
        val swap = this[ini]
        this[ini] = this[fin]
        this[fin] = swap
    }


    private fun addItem(item: PlaceholderItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createPlaceholderItem(
        position: Int,
        name: String,
        shipments: List<String>
    ): PlaceholderItem {
        return PlaceholderItem(position.toString(), name, makeDetails(position, name, shipments))
    }

    private fun getStreetName(address: String): String {
        val allSplit = address.split(" ")
        if (allSplit[allSplit.lastIndex].isDigitsOnly()) {
            var str = ""
            for (i in 1 until allSplit.size - 2) {
                str += allSplit[i] + " "
            }
            str = str.dropLast(1)
            return str
        } else {
            var str = ""
            for (i in 1 until allSplit.size) {
                str += allSplit[i] + " "
            }
            str = str.dropLast(1)
            return str
        }
    }

    private fun getFactors(num: Int): List<Int> {
        val theFactors = mutableListOf<Int>()
        var length: Int = num / 2
        var factor = 0
        while (length > 1) {
            factor++
            if (num.rem(factor) == 0) {
                theFactors.add(factor)
                theFactors.add(num / factor)
            }
            length -= factor
        }
        return theFactors
    }

    fun List<Int>.containsAnyOf(compareList: List<Int>): Boolean {
        for (i in 1 until compareList.size) {
            if (i > this.lastIndex || i > compareList.lastIndex) return false
            if (this.contains(compareList[i])) return true
        }
        return false
    }

    private fun makeDetails(position: Int, driver: String, shipments: List<String>): String {
        val builder = StringBuilder()
        val scores = Array(shipments.size) { 0.0 }
        val vowel = "AEIOUaeiou"
        val consonant = "bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ"

        for (i in 0..shipments.lastIndex) {
            val streetName = getStreetName(shipments[i])
            var score = 0.0
            if (streetName.length.rem(2) == 0) {
                var vowelNum = 0
                for (j in driver.toCharArray()) {

                    if (vowel.contains(j)) {
                        vowelNum++
                    }

                    score = vowelNum * (1.5)
                }
            } else {
                var consonantNum = 0
                for (j in driver.toCharArray()) {
                    if (consonant.contains(j)) {
                        consonantNum++
                    }
                }

                score = consonantNum.toDouble()
            }
            if (getFactors(streetName.length).containsAnyOf(getFactors(driver.length))) {
                score *= 1.5
            }
            scores[i] = score
            builder.append("\n${shipments[i]} : $score")
        }

        SCORE.add(scores)
        return builder.toString()
    }

    /**
     * A placeholder item representing a piece of content.
     */
    data class PlaceholderItem(val id: String, val name: String, var details: String) {
        override fun toString(): String = name
    }
}