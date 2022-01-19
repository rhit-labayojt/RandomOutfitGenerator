package edu.rosehulman.randomoutfitgenerator.models

import androidx.lifecycle.ViewModel
import edu.rosehulman.randomoutfitgenerator.objects.SuperCategory

class ClosetViewModel : ViewModel() {

    var tops = SuperCategory("Tops", propogateArrayList(Array(5){"T-Shirt"; "Long Sleeve"; "Button Down"; "Sweater"; "Sweatshirt"}))
    var bottoms = SuperCategory("Bottoms", propogateArrayList(Array(5){"Jeans"; "Skirt"; "Shorts"; "SweatPants"; "Slacks"}))
    var accessories = SuperCategory("Accessories", propogateArrayList(Array(10){"Watch"; "Hat"; "Necklace"; "Earrings"; "Bracelet"; "Coat"; "Vest"; "Sunglasses"; "Ring"; "Gloves"}))
    var shoes = SuperCategory("Shoes", propogateArrayList(Array(9){"Athletic Shoes"; "Boots"; "Dress Shoes"; "Slides"; "Flip-Flops"; "Crocs"; "Sandals"; "Heels"; "Flats"}))
    var full_body = SuperCategory("Full Body", propogateArrayList(Array(5){"Dress"; "Romper"; "Pant Suit"; "Suit"; "Bathrobe"}))


    private fun propogateArrayList(a: Array<String>): ArrayList<String>{
        var list = ArrayList<String>()

        for(s in a){
            list.add(s)
        }

        return list
    }
}