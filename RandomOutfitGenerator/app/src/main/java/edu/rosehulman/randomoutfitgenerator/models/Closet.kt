package edu.rosehulman.randomoutfitgenerator.models

import edu.rosehulman.randomoutfitgenerator.objects.Clothing
import edu.rosehulman.randomoutfitgenerator.objects.Outfit
import edu.rosehulman.randomoutfitgenerator.objects.SuperCategory
import edu.rosehulman.randomoutfitgenerator.objects.Weather
import kotlin.random.Random

class Closet {

    var clothing: ArrayList<Clothing> = ArrayList()
    var savedOutfits: ArrayList<Outfit> = ArrayList()
    var recentOutfits: Array<Outfit?> = Array(10){null; null; null; null; null; null; null; null; null; null}
    var topsTags: ArrayList<String> = ArrayList()
    var bottomsTags: ArrayList<String> = ArrayList()
    var accessoriesTags: ArrayList<String> = ArrayList()
    var shoesTags: ArrayList<String> = ArrayList()
    var fullBodyTags: ArrayList<String> = ArrayList()
    var styles: ArrayList<String> = ArrayList()
    private val testImages =
        arrayListOf("https://wallpapersmug.com/large/fb6f49/sunset-horizon-mountains-valley.jpg",
            "https://wallup.net/wp-content/uploads/2015/12/58890-nature-landscape-mountain-Caucasus_Mountains.jpg",
            "https://wallup.net/wp-content/uploads/2015/12/194745-beach-mountain-sea-island-Lofoten-Norway-clouds-waves-snowy_peak-nature-landscape-sand-water.jpg",
            "https://d.newsweek.com/en/full/1768419/iceland-volcano-fagradalsfjall-geldingadalir-reykjanes-peninsula-getty.jpg",
            "https://www.wallpapers-for-phone.com/phone/clouds-mountains-storm-dolomites.jpg",
            "https://cdn.suwalls.com/wallpapers/nature/colorful-mountain-sunrise-22613-1920x1080.jpg",
            "https://www.wallpaperup.com/uploads/wallpapers/2013/07/31/125737/76ae9758c5c0bc2c80d98242cc224898-700.jpg")


    constructor(){
        addTags(topsTags, arrayOf("Long Sleeve", "T-Shirt", "Sweater", "Vest", "Tank Top"))
        addTags(bottomsTags, arrayOf("Shorts", "Jeans", "Slacks", "Sweat Pants", "Skirt"))
        addTags(accessoriesTags, arrayOf("Sunglasses", "Hat", "Bracelet", "Necklace", "Ring", "Watch"))
        addTags(shoesTags, arrayOf("Sneakers", "Boots", "Heels", "Flats", "Sandals", "Crocs"))
        addTags(fullBodyTags, arrayOf("Suit", "Pant Suit", "Dress", "Track Suit"))
        addTags(styles, arrayOf("Casual", "Formal", "Relaxation", "Work", "School"))
        generateTestImages()
    }

    fun addClothing(item: Clothing){
        clothing.add(item)
    }

    fun saveOutfit(fit: Outfit){
        savedOutfits.add(fit)
    }

    fun removeSavedOutfit(fit: Outfit){
        savedOutfits.remove(fit)
    }

    //---------------------------DELETE THESE AFTER APP IS FUNCTIONING----------------------
    private fun addTags(tagList: ArrayList<String>, tagsToAdd: Array<String>){
        for(item in tagsToAdd){
            tagList.add(item)
        }
    }

    private fun generateTestImages(){
        for(i in 0 until 5*7){
            when(i % 5){
                0 -> addClothing(Clothing(SuperCategory.Top, topsTags.get(Random.nextInt(topsTags.size)), arrayListOf(styles.get(i % styles.size), styles.get((i+3)%styles.size), styles.get((i+5)%styles.size)),arrayListOf(
                    Weather.Moderate, Weather.Cool, Weather.Cold), testImages.get(Random.nextInt(testImages.size))))
                1 -> addClothing(Clothing(SuperCategory.Bottom, bottomsTags.get(Random.nextInt(bottomsTags.size)), arrayListOf(styles.get(i % styles.size), styles.get((i+3)%styles.size), styles.get((i+5)%styles.size)),arrayListOf(
                    Weather.Moderate, Weather.Cool, Weather.Cold), testImages.get(Random.nextInt(testImages.size))))
                2 -> addClothing(Clothing(SuperCategory.Accessory, accessoriesTags.get(Random.nextInt(accessoriesTags.size)), arrayListOf(styles.get(i % styles.size), styles.get((i+3)%styles.size), styles.get((i+5)%styles.size)),arrayListOf(
                    Weather.Moderate, Weather.Cool, Weather.Cold), testImages.get(Random.nextInt(testImages.size))))
                3 -> addClothing(Clothing(SuperCategory.Shoes, shoesTags.get(Random.nextInt(shoesTags.size)), arrayListOf(styles.get(i % styles.size), styles.get((i+3)%styles.size), styles.get((i+5)%styles.size)),arrayListOf(
                    Weather.Moderate, Weather.Cool, Weather.Cold), testImages.get(Random.nextInt(testImages.size))))
                else -> addClothing(Clothing(SuperCategory.FullBody, fullBodyTags.get(Random.nextInt(fullBodyTags.size)), arrayListOf(styles.get(i % styles.size), styles.get((i+3)%styles.size), styles.get((i+5)%styles.size)),arrayListOf(
                    Weather.Moderate, Weather.Cool, Weather.Cold), testImages.get(Random.nextInt(testImages.size))))
            }
        }
    }
}