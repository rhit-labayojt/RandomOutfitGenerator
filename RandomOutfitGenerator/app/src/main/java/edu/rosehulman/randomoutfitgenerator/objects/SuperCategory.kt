package edu.rosehulman.randomoutfitgenerator.objects

enum class SuperCategory {
    Top,
    Bottom,
    FullBody,
    Accessory,
    Shoes;

    companion object {
        fun stringArray(): ArrayList<String> {
            var list = ArrayList<String>()
            list.add("Top")
            list.add("Bottom")
            list.add("Full Body")
            list.add("Accessory")
            list.add("Shoes")
            return list
        }

        fun stringToEnum(s: String): SuperCategory{
            val toReturn: SuperCategory

            when(s){
                "Top" -> toReturn = Top
                "Bottom" -> toReturn = Bottom
                "Accessory" -> toReturn = Accessory
                "Shoes" -> toReturn = Shoes
                else -> toReturn = FullBody
            }

            return toReturn
        }

        fun enumToString(cat: SuperCategory): String{
            when(cat){
                Top -> return "Top"
                Bottom -> return "Bottom"
                Shoes -> return "Shoes"
                Accessory -> return "Accessory"
                else -> return "Full Body"
            }
        }
    }

}