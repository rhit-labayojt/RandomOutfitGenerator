package edu.rosehulman.randomoutfitgenerator.models

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.randomoutfitgenerator.objects.Clothing
import edu.rosehulman.randomoutfitgenerator.objects.Outfit
import kotlin.random.Random

class Closet() {

    var clothing: ArrayList<Clothing> = ArrayList()
    var savedOutfits: ArrayList<Outfit> = ArrayList()
    var recentOutfits: Array<Outfit?> = Array(10){null}

    companion object{
        var weathers = arrayOf("Hot", "Warm", "Moderate", "Cool", "Cold")
        var superCategories = arrayOf("Top", "Bottom", "Accessory", "Shoes", "Full Body")
        const val CLOTHING_COLLECTION_PATH = "clothing"
        const val RECENT_OUTFITS_COLLECTION_PATH = "recentOutfits"
        const val SAVED_OUTFITS_COLLECTION_PATH = "savedOutfits"
    }

    fun addClothing(item: Clothing){
        clothing.add(item)
    }

    fun removeClothing(item: Clothing){
        clothing.remove(item)
    }

    fun saveOutfit(fit: Outfit){
        savedOutfits.add(fit)
    }

    fun removeSavedOutfit(fit: Outfit){
        savedOutfits.remove(fit)
    }

    fun toString(list: ArrayList<String>): String{
        var s = ""
        list.forEach{ s+="$it, "}
        return s.removeSuffix(", ")
    }

}