package edu.rosehulman.randomoutfitgenerator.models

import edu.rosehulman.randomoutfitgenerator.objects.Clothing
import edu.rosehulman.randomoutfitgenerator.objects.Outfit

class Closet {

    var clothing: ArrayList<Clothing> = ArrayList()
    var savedOutfits: ArrayList<Outfit> = ArrayList()
    var recentOutfits: Array<Outfit?> = Array(10){null; null; null; null; null; null; null; null; null; null}

}