package edu.rosehulman.randomoutfitgenerator.models

import edu.rosehulman.randomoutfitgenerator.R

data class User(
    var name: String = "",
    var defaultStyle: String = "Casual",
    var hasCompletedSetup: Boolean = false,
    var topsTags: ArrayList<String> = arrayListOf(),
    var bottomsTags: ArrayList<String> = arrayListOf(),
    var shoesTags: ArrayList<String> = arrayListOf(),
    var accessoriesTags: ArrayList<String> = arrayListOf(),
    var fullBodyTags: ArrayList<String> = arrayListOf(),
    var styles: ArrayList<String> = arrayListOf(),
    var theme: Int = R.style.Theme_RandomOutfitGenerator
) {
    companion object{
        const val COLLECTION_PATH = "users"
    }
}