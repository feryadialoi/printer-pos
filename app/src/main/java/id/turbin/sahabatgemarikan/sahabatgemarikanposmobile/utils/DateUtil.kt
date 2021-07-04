package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils

import java.util.*

object DateUtil {


    fun serverDateFormat(calendar: Calendar): String {
        val year = calendar.get(Calendar.YEAR)
        val month = (calendar.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        val day = calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')

        return "$year-$month-$day"
    }

    fun serverDateFormat(dayOfMonth: Int, month: Int, year: Int): String {
        val dayString = dayOfMonth.toString().padStart(2, '0')
        val monthString = (month + 1).toString().padStart(2, '0')
        val yearString = year.toString()

        return "$yearString-$monthString-$dayString"
    }

    fun localDateFormat(dayOfMonth: Int, month: Int, year: Int): String {
        val dayString = dayOfMonth.toString().padStart(2, '0')
        val monthString = (month + 1).toString().padStart(2, '0')
        val yearString = year.toString()

        return "$dayString/$monthString/$yearString"
    }
}