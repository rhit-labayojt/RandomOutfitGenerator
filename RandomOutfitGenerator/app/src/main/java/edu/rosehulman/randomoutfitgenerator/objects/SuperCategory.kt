package edu.rosehulman.randomoutfitgenerator.objects

enum class SuperCategory {
    Top,
    Bottom,
    FullBody,
    Accessory,
    Shoes;

    companion object {
        fun stringArray(): Array<String> {
            return Array(5) {
                "Top";
                "Bottom";
                "Full Body";
                "Accessory";
                "Shoes"
            }
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
    }

}