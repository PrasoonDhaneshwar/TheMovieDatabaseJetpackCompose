package com.prasoon.themoviedatabasejetpackcompose.common

import android.icu.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    fun getMonthYearFromDate(dateString: String): String {
        // Define the input and output date formats
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("MMMM, yyyy", Locale.ENGLISH)

        // Parse the input date string
        val date = inputFormat.parse(dateString)

        // Format the date to the desired output format
        return outputFormat.format(date)
    }
}