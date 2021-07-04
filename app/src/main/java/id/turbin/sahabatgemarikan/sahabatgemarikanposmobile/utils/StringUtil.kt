package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.utils

import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

object StringUtil {

    private const val SERVER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

    private const val LOCAL_DATE_FORMAT = "dd-MM-yyyy HH:mm"

    private const val CURRENCY_FORMAT = "#,###"


    fun dateFormat(dateString: String): String {
//        val date = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(SERVER_DATE_FORMAT))
//        return date.format(DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT))
        return  dateString
    }

    /**
     * example: 50,000
     */
    fun currencyFormat(value: BigDecimal): String {
        return DecimalFormat(CURRENCY_FORMAT).format(value)
    }

    fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }
}