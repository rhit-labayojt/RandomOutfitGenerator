package edu.rosehulman.randomoutfitgenerator.models

import androidx.lifecycle.ViewModel
import edu.rosehulman.randomoutfitgenerator.objects.Clothing

class ClosetViewModel : ViewModel() {

    var user = User()
    var closet = Closet()

    var currentItem = closet.clothing.get(0)
    //var currentOutfit = closet.savedOutfits.get(0)


    fun updateCurrentItem(clothes: Clothing){
        currentItem = clothes
    }

    fun deleteCurrentItem(){
        closet.clothing.remove(currentItem)
        currentItem = closet.clothing.get(0)
    }
}