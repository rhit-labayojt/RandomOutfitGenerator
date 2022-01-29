package edu.rosehulman.randomoutfitgenerator.models

import androidx.lifecycle.ViewModel
import edu.rosehulman.randomoutfitgenerator.objects.Clothing
import edu.rosehulman.randomoutfitgenerator.objects.Outfit

class ClosetViewModel : ViewModel() {

    var closet = Closet()

    var currentItem = closet.clothing.get(0)
    var currentOutfit: Outfit? = null


    fun updateCurrentItem(clothes: Clothing){
        currentItem = clothes
    }

    fun deleteCurrentItem(){
        closet.clothing.remove(currentItem)
        currentItem = closet.clothing.get(0)
    }

    fun updateCurrentOutfit(fit: Outfit){
        currentOutfit = fit
    }

    fun deleteCurrentOutfit(){
        closet.savedOutfits.remove(currentOutfit)
        currentOutfit = null
    }
}