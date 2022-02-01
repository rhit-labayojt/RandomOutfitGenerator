package edu.rosehulman.randomoutfitgenerator.models

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.randomoutfitgenerator.objects.Clothing
import edu.rosehulman.randomoutfitgenerator.objects.Outfit
import kotlin.random.Random

class Closet {

    var clothing: ArrayList<Clothing> = ArrayList()
    var savedOutfits: ArrayList<Outfit> = ArrayList()
    var recentOutfits: Array<Outfit?> = Array(10){null; null; null; null; null; null; null; null; null; null}
    var topsTags = arrayListOf("Long Sleeve", "T-Shirt", "Sweater", "Vest", "Tank Top")
    var bottomsTags = arrayListOf("Shorts", "Jeans", "Slacks", "Sweat Pants", "Skirt")
    var accessoriesTags = arrayListOf("Sunglasses", "Hat", "Bracelet", "Necklace", "Ring", "Watch")
    var shoesTags = arrayListOf("Sneakers", "Boots", "Heels", "Flats", "Sandals", "Crocs")
    var fullBodyTags = arrayListOf("Sneakers", "Boots", "Heels", "Flats", "Sandals", "Crocs")
    var styles = arrayListOf("Casual", "Formal", "Relaxation", "Work", "School")

    @get:Exclude
    var defaultWeather = "Moderate"

    private val testImages =
        arrayListOf("https://wallpapersmug.com/large/fb6f49/sunset-horizon-mountains-valley.jpg",
            "https://wallup.net/wp-content/uploads/2015/12/58890-nature-landscape-mountain-Caucasus_Mountains.jpg",
            "https://wallup.net/wp-content/uploads/2015/12/194745-beach-mountain-sea-island-Lofoten-Norway-clouds-waves-snowy_peak-nature-landscape-sand-water.jpg",
            "https://d.newsweek.com/en/full/1768419/iceland-volcano-fagradalsfjall-geldingadalir-reykjanes-peninsula-getty.jpg",
            "https://www.wallpapers-for-phone.com/phone/clouds-mountains-storm-dolomites.jpg",
            "https://cdn.suwalls.com/wallpapers/nature/colorful-mountain-sunrise-22613-1920x1080.jpg",
            "https://www.wallpaperup.com/uploads/wallpapers/2013/07/31/125737/76ae9758c5c0bc2c80d98242cc224898-700.jpg")


    constructor(){
        generateTestImages()
    }

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

    fun generateTestImages(){
        val uid = Firebase.auth.currentUser!!.uid
        var clothingRef = Firebase.firestore.collection(User.COLLECTION_PATH).document(uid).collection(Closet.CLOTHING_COLLECTION_PATH)
        for(i in 0 until 50*5){
            when(i % 5){
                0 -> {
                    var c = Clothing(superCategories[i%5], topsTags.get(
                        Random.nextInt(topsTags.size)), arrayListOf("Casual"), arrayListOf("Moderate"), testImages[Random.nextInt(testImages.size)])
                    clothingRef.add(c)
                }
                1 -> {
                    var c = Clothing(superCategories[i%5], bottomsTags.get(
                        Random.nextInt(bottomsTags.size)), arrayListOf("Casual"), arrayListOf("Moderate"), testImages[Random.nextInt(testImages.size)])
                    clothingRef.add(c)
                }
                2 -> {
                    var c = Clothing(superCategories[i%5], accessoriesTags.get(
                        Random.nextInt(accessoriesTags.size)), arrayListOf("Casual"), arrayListOf("Moderate"), testImages[Random.nextInt(testImages.size)])
                    clothingRef.add(c)
                }
                3 -> {
                    var c = Clothing(superCategories[i%5], shoesTags.get(
                        Random.nextInt(shoesTags.size)), arrayListOf("Casual"), arrayListOf("Moderate"), testImages[Random.nextInt(testImages.size)])
                    clothingRef.add(c)
                }
                else -> {
                    var c = Clothing(superCategories[i%5], fullBodyTags.get(
                        Random.nextInt(fullBodyTags.size)), arrayListOf("Casual"), arrayListOf("Moderate"), testImages[Random.nextInt(testImages.size)])
                    clothingRef.add(c)
                }
            }
        }
    }

}