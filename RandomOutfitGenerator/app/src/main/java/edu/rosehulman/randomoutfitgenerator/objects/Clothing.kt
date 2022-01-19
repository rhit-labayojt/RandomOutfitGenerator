package edu.rosehulman.randomoutfitgenerator.objects

import android.media.Image

class Clothing(private var superCat: SuperCategory = SuperCategory.Top, private var subCat: String = "",
               var styles: ArrayList<String> = ArrayList(), var weathers: ArrayList<Weather> = ArrayList(),
               private var image: String = "" ) {

    fun addStyle(s: String){
        styles.add(s)
    }

    fun addWeather(w: Weather){
        weathers.add(w)
    }

    fun removeStyle(s: String){
        styles.remove(s)
    }

    fun removeWeather(w: Weather){
        weathers.remove(w)
    }

    fun editImage(i: String){
        image = i
    }

    fun getImage(): String{
        return image
    }

    fun getSubCat(): String{
        return subCat
    }

    fun getSuperCat(): SuperCategory{
        return superCat
    }

}