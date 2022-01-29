package edu.rosehulman.randomoutfitgenerator.models

import edu.rosehulman.randomoutfitgenerator.objects.Clothing
import edu.rosehulman.randomoutfitgenerator.objects.Outfit
import kotlin.random.Random

class Closet {

    var clothing: ArrayList<Clothing> = ArrayList()
    var savedOutfits: ArrayList<Outfit> = ArrayList()
    var recentOutfits: Array<Outfit?> = Array(10){null; null; null; null; null; null; null; null; null; null}
    var topsTags = mutableMapOf("Long Sleeve" to true, "T-Shirt" to true, "Sweater" to true, "Vest" to true, "Tank Top" to true)
    var bottomsTags = mutableMapOf("Shorts" to true, "Jeans" to true, "Slacks" to true, "Sweat Pants" to true, "Skirt" to true)
    var accessoriesTags = mutableMapOf("Sunglasses" to true, "Hat" to true, "Bracelet" to true, "Necklace" to true, "Ring" to true, "Watch" to true)
    var shoesTags = mutableMapOf("Sneakers" to true, "Boots" to true, "Heels" to true, "Flats" to true, "Sandals" to true, "Crocs" to true)
    var fullBodyTags = mutableMapOf("Sneakers" to true, "Boots" to true, "Heels" to true, "Flats" to true, "Sandals" to true, "Crocs" to true)
    var styles = mutableMapOf("Casual" to true, "Formal" to true, "Relaxation" to true, "Work" to true, "School" to true)

    var id = ""

    var defaultStyle = "Casual"
    var defaultWeather = "Moderate"

    companion object{
        var weathers = mutableMapOf("Hot" to true, "Warm" to true, "Moderate" to true, "Cool" to true, "Cold" to true)
        var superCategories = arrayOf("Top", "Bottom", "Accessory", "Shoes", "Full Body")
    }


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

    fun toString(list: MutableSet<String>): String{
        var s = ""
        list.forEach{ s+="$it, "}
        return s.removeSuffix(", ")
    }

    private fun generateTestImages(){
        for(i in 0 until 5*5){
            when(i % 5){
                0 -> addClothing(Clothing(superCategories[i%5], topsTags.keys.toList().get(Random.nextInt(topsTags.keys.size)), mutableMapOf("Casual" to true), mutableMapOf("Hot" to true), testImages[Random.nextInt(testImages.size)]))
                1 -> addClothing(Clothing(superCategories[i%5], bottomsTags.keys.toList().get(Random.nextInt(bottomsTags.keys.size)), mutableMapOf("Casual" to true), mutableMapOf("Warm" to true), testImages[Random.nextInt(testImages.size)]))
                2 -> addClothing(Clothing(superCategories[i%5], accessoriesTags.keys.toList().get(Random.nextInt(accessoriesTags.keys.size)), mutableMapOf("Casual" to true), mutableMapOf("Moderate" to true), testImages[Random.nextInt(testImages.size)]))
                3 -> addClothing(Clothing(superCategories[i%5], shoesTags.keys.toList().get(Random.nextInt(shoesTags.keys.size)), mutableMapOf("Casual" to true), mutableMapOf("Cool" to true), testImages[Random.nextInt(testImages.size)]))
                else -> addClothing(Clothing(superCategories[i%5], fullBodyTags.keys.toList().get(Random.nextInt(fullBodyTags.keys.size)), mutableMapOf("Casual" to true), mutableMapOf("Cold" to true), testImages[Random.nextInt(testImages.size)]))
            }
        }
    }
}