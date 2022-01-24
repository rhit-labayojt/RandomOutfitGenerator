package edu.rosehulman.randomoutfitgenerator.objects

class Outfit {

    var top: String = ""
    var bottom: String = ""
    var fullBody: String = ""
    var shoes: String = ""
    var style: String = ""
    var weather: String = ""
    var accessories = mutableMapOf<String, Boolean>()

    var topCat: String = ""
    var bottomCat: String = ""
    var fullBodyCat: String = ""
    var shoesCat: String = ""
    var accessoriesCats = mutableMapOf<String, Boolean>()

    var isSaved = false
    var isFullBody: Boolean

    var id = ""

    constructor(clothing: ArrayList<Clothing>, style: String, weather: String, fullBody: Boolean){
        this.style = style
        this.weather = weather
        this.isFullBody = fullBody

        assignClothing(clothing)
    }

    fun assignClothing(clothes: ArrayList<Clothing>){

        if(isFullBody){
            for (c in clothes) {
                when (c.getSubCat()) {
                    "Full Body" -> {fullBody = c.getImage(); fullBodyCat = c.getSubCat()}
                    "Shoes" -> {shoes = c.getImage(); shoesCat = c.getSubCat()}
                    else -> {accessories.put(c.getImage(), true); accessoriesCats.put(c.getSubCat(), true)}
                }
            }
        }else {
            for (c in clothes) {
                when (c.getSubCat()) {
                    "Top" -> {top = c.getImage(); topCat = c.getSubCat()}
                    "Bottom" -> {bottom = c.getImage(); bottomCat = c.getSubCat()}
                    "Shoes" -> {shoes = c.getImage(); shoesCat = c.getSubCat()}
                    else -> {accessories.put(c.getImage(), true); accessoriesCats.put(c.getSubCat(), true)}
                }
            }
        }
    }



    fun toggleSaved(){
        isSaved = !isSaved
    }
}