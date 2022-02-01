package edu.rosehulman.randomoutfitgenerator.objects

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude

class Outfit {

    var top: String = ""
    var bottom: String = ""
    var fullBody: String = ""
    var shoes: String = ""
    var style: String = ""
    var weather: String = ""
    var accessories = arrayListOf<String>()

    var topCat: String = ""
    var bottomCat: String = ""
    var fullBodyCat: String = ""
    var shoesCat: String = ""
    var accessoriesCats = arrayListOf<String>()

    var isSaved = false
    var isFullBody: Boolean

    @get:Exclude
    var id = "" // Firestore ID

    constructor(clothing: ArrayList<Clothing>, style: String, weather: String, fullBody: Boolean){
        this.style = style
        this.weather = weather
        this.isFullBody = fullBody

        assignClothing(clothing)
    }

    companion object{
        fun from(snapshot: DocumentSnapshot): Outfit{
            val fit = snapshot.toObject(Outfit::class.java)!!

            return fit
        }
    }

    fun assignClothing(clothes: ArrayList<Clothing>){

        if(isFullBody){
            for (c in clothes) {
                when (c.getSubCat()) {
                    "Full Body" -> {fullBody = c.getImage(); fullBodyCat = c.getSubCat()}
                    "Shoes" -> {shoes = c.getImage(); shoesCat = c.getSubCat()}
                    else -> {accessories.add(c.getImage()); accessoriesCats.add(c.getSubCat())}
                }
            }
        }else {
            for (c in clothes) {
                when (c.getSubCat()) {
                    "Top" -> {top = c.getImage(); topCat = c.getSubCat()}
                    "Bottom" -> {bottom = c.getImage(); bottomCat = c.getSubCat()}
                    "Shoes" -> {shoes = c.getImage(); shoesCat = c.getSubCat()}
                    else -> {accessories.add(c.getImage()); accessoriesCats.add(c.getSubCat())}
                }
            }
        }
    }

    fun toggleSaved(){
        isSaved = !isSaved
    }
}