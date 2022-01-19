package edu.rosehulman.randomoutfitgenerator.objects

class Outfit {

    var top: Clothing? = null
    var bottom: Clothing? = null
    var fullBody: Clothing? = null
    var shoes: Clothing? = null
    var style: String
    var weather: String
    var accessories = ArrayList<Clothing>()
    var isSaved = false

    constructor(clothing: ArrayList<Clothing>, style: String, weather: String){
        this.style = style
        this.weather = weather

        assignClothing(clothing)
    }

    fun assignClothing(clothes: ArrayList<Clothing>){
        for(c in clothes){
            when(c.getSubCat()){
                "Top" -> top = c
                "Bottom" -> bottom = c
                "Shoes" -> shoes = c
                "Full Body" -> fullBody = c
                else -> accessories.add(c)
            }
        }
    }



    fun toggleSaved(){
        isSaved = !isSaved
    }
}