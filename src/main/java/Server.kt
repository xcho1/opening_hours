import org.json.JSONArray
import org.json.JSONObject
import spark.*
import spark.Spark.*
import spark.template.thymeleaf.ThymeleafTemplateEngine
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun main() {
    Server()
}

class Server {

    companion object {
        private const val PORT = 8080
        private const val WORKING_HOURS_ROUTE = "/working_hours"
    }

    private val daysOfWeek = arrayOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")

    init {
        port(PORT)
        before(Filter { _, response ->  response.type("application/json")})
        post(WORKING_HOURS_ROUTE, this::parseData, ThymeleafTemplateEngine())
    }

    private fun parseData(request: Request, response: Response): ModelAndView {
        val json = JSONObject(request.body())
        return ModelAndView(HashMap<String, ArrayList<String>>().apply { put("list", convertHours(json)) }, "working_hours")
    }

    private fun getClosingHourFromNextDay(hours: JSONArray) = convertUnixTime((hours[0] as JSONObject)["value"] as Int)

    private fun convertHours(json: JSONObject): ArrayList<String> {

        val dayList = ArrayList<String>()

        for (i in 0 until daysOfWeek.size) {
            val hours = json[daysOfWeek[i]] as JSONArray
            var openingHours = ""

            if (hours.length() == 0 || hours.length() == 1 && (hours[0] as JSONObject)["type"] == "close") {
                System.out.println("${daysOfWeek[i]}: Closed")
                dayList.add("${daysOfWeek[i].capitalize()}: Closed")
                continue
            }

            val startIndex = if ((hours[0] as JSONObject)["type"] == "close") 1 else 0
            for (j in startIndex until hours.length()) {
                openingHours += convertUnixTime((hours[j] as JSONObject)["value"] as Int) +
                        if ((hours[j] as JSONObject)["type"] == "open") " - " else ", "
            }

            openingHours = openingHours.replace(", $".toRegex(), "")

            //closes next day
            if ((hours[hours.length() - 1] as JSONObject)["type"] == "open") {
                val index = if( i + 1 == daysOfWeek.size) 0 else i + 1
                openingHours += getClosingHourFromNextDay(json[daysOfWeek[index]] as JSONArray)
            }
            System.out.println("${daysOfWeek[i]}: $openingHours")
            dayList.add("${daysOfWeek[i].capitalize()}: $openingHours")
        }

        return dayList
    }

    private fun convertUnixTime(unixTime: Int): String {
        val sdf = SimpleDateFormat("hh:mm a")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val date = Date(unixTime * 1000L)
        return sdf.format(date)
    }
}