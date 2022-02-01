package edu.rosehulman.randomoutfitgenerator.models

data class User(
    var name: String = "",
    var defaultStyle: String = "Casual",
    var hasCompletedSetup: Boolean = false
) {
    companion object{
        const val COLLECTION_PATH = "users"
    }
}