package edu.rosehulman.randomoutfitgenerator.objects

import android.media.Image
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import edu.rosehulman.randomoutfitgenerator.Constants

class Clothing() {

    private var superCat = ""
    private var subCat = ""
    private var styles = ArrayList<String>()
    private var weathers = ArrayList<String>()
    private var image = ""

    @get:Exclude
    var id = "" // firestore ID

    constructor(superCat: String = "", subCat: String = "", styles: ArrayList<String>, weathers: ArrayList<String>, image: String = "" ) : this() {
        this.superCat = superCat
        this.subCat = subCat
        this.styles = styles
        this.weathers = weathers
        this.image = image
    }

   companion object{
       fun from(snapshot: DocumentSnapshot): Clothing{
           val c = snapshot.toObject(Clothing::class.java)!!
           c.id = snapshot.id

           return c
       }
   }

    fun addStyle(s: String){
        styles.add(s)
    }

    fun addWeather(w: String){
        weathers.add(w)
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

    fun getWeathers(): ArrayList<String>{
        return weathers
    }

    fun getStyles(): ArrayList<String>{
        return styles
    }

}