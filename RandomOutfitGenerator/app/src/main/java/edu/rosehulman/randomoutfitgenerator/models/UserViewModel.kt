package edu.rosehulman.randomoutfitgenerator.models

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserViewModel: ViewModel(){
    var ref = Firebase.firestore.collection(User.COLLECTION_PATH).document(Firebase.auth.uid!!)

    var user: User? = null

    fun getOrMakeUser(observer: () -> Unit){
        ref = Firebase.firestore.collection(User.COLLECTION_PATH).document(Firebase.auth.uid!!)

        if(user != null){
            //get
            observer()
        }else{
            //make
            ref.get().addOnSuccessListener {
                if(it.exists()){
                    user = it.toObject(User::class.java)
                }else{
                    user = User(name= Firebase.auth.currentUser!!.displayName!!)
                    ref.set(user!!)
                }

                observer()
            }
        }
    }

    fun hasCompletedSetup(): Boolean = user?.hasCompletedSetup ?: false

    fun update(newName: String, newAge: Int, newMajor: String, newHasCompletedSetup: Boolean){
        ref = Firebase.firestore.collection(User.COLLECTION_PATH).document(Firebase.auth.uid!!)

        if(user!=null){
            with(user!!){
                name = newName
                defaultStyle = "Casual"
                hasCompletedSetup = newHasCompletedSetup
                ref.set(this)
            }
        }
    }
}