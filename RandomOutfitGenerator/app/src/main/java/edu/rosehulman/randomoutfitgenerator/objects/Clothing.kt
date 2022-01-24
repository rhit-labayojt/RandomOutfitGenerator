package edu.rosehulman.randomoutfitgenerator.objects

import android.media.Image

class Clothing(private var superCat: String = "",
               private var subCat: String = "",
               private var styles: MutableMap<String, Boolean>,
               private var weathers: MutableMap<String, Boolean>,
               private var image: String = "" ) {

    var id = ""

    fun addStyle(s: String){
        styles.put(s, true)
    }

    fun addWeather(w: String){
        weathers.put(w, true)
    }

    fun removeStyle(s: String){
        styles.remove(s)
    }

    fun removeWeather(w: String){
        weathers.remove(w)
    }

    fun setImage(i: String){
        image = i
    }

    fun getImage(): String{
        return image
    }

    fun getSubCat(): String{
        return subCat
    }

    fun getSuperCat(): String{
        return superCat
    }

    fun setSuperCat(cat: String){
        superCat = cat
    }

    fun setSubCat(cat: String){
        subCat = cat
    }

    fun getWeather(): MutableSet<String>{
        return weathers.keys
    }

    fun getStyles(): MutableSet<String>{
        return styles.keys
    }

}