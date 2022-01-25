package edu.rosehulman.randomoutfitgenerator.models

import edu.rosehulman.randomoutfitgenerator.objects.Clothing
import edu.rosehulman.randomoutfitgenerator.objects.Outfit

class User() {
    private var closet = Closet()
    var currentItem = closet.clothing.get(0)

    fun updateCurrentItem(clothes: Clothing){
        currentItem = clothes
    }

    fun deleteCurrentItem(){
        closet.clothing.remove(currentItem)
        currentItem = closet.clothing.get(0)
    }

    fun addClothing(clothes: Clothing){
        closet.addClothing(clothes)
    }

    fun removeClothing(clothes: Clothing){
        closet.removeClothing(clothes)
    }

    fun saveOutfit(fit: Outfit){
        closet.saveOutfit(fit)
    }

    fun removeSavedOutfit(fit: Outfit){
        closet.removeSavedOutfit(fit)
    }

    fun getRecentOutfits(): Array<Outfit?>{
        return closet.recentOutfits
    }

    fun toString(list: MutableSet<String>): String{
        return closet.toString(list)
    }
}