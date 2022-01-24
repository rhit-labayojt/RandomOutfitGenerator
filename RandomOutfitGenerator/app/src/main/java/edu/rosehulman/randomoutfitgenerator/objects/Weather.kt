package edu.rosehulman.randomoutfitgenerator.objects

enum class Weather {
    Hot,
    Warm,
    Moderate,
    Cool,
    Cold;

    companion object {
        fun toArrayList(): ArrayList<String>{
            var a = ArrayList<String>()

            a.add("Hot")
            a.add("Warm")
            a.add("Moderate")
            a.add("Cool")
            a.add("Cold")

            return a
        }

        fun enumToString(w: Weather): String{
            when(w) {
                Hot -> return "Hot"
                Warm -> return "Warm"
                Moderate -> return "Moderate"
                Cool -> return "Cool"
                else -> return "Cold"
            }
        }

        fun stringToEnum(s: String): Weather{
            when(s) {
                "Hot" -> return Hot
                "Warm" -> return Warm
                "Moderate" -> return Moderate
                "Cool" -> return Cool
                else -> return Cold
            }
        }
    }
}