package edu.rosehulman.randomoutfitgenerator.objects

class SuperCategory(private val id: String = "", private var itemTags: ArrayList<String> = ArrayList(), private var clothing: ArrayList<Clothing> = ArrayList()) {
    fun getID(): String{
        return id
    }

    fun addTag(tag: String){
        itemTags.add(tag)
    }

    fun removeTag(tag: String){
        itemTags.remove(tag)
    }

    fun addClothing(clothes: Clothing){
        clothing.add(clothes)
    }

    fun removeClothing(clothes: Clothing){
        clothing.remove(clothes)
    }
}