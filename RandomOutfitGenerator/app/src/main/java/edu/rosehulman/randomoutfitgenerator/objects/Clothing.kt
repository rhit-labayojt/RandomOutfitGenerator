package edu.rosehulman.randomoutfitgenerator.objects

import android.media.Image

class Clothing(private var superCat: SuperCategory = SuperCategory.Top, private var subCat: String = "",
               var styles: ArrayList<String> = ArrayList(), var weathers: ArrayList<Weather> = ArrayList(),
               private var image: String = "" ) {

    var id = ""

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

    fun setImage(i: String){
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

    fun setSuperCat(cat: String){
        superCat = SuperCategory.stringToEnum((cat))
    }

    fun setSubCat(cat: String){
        subCat = cat
    }

    fun getWeather(): ArrayList<String>{
        var list = ArrayList<String>()

        weathers.forEach{ list.add(Weather.enumToString(it))}

        return list
    }

    fun setStyle(s: ArrayList<String>){
        if(!s.isEmpty()){
            styles.addAll(s)
        }
    }

}