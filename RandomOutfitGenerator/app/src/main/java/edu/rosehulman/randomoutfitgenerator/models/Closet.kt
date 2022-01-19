package edu.rosehulman.randomoutfitgenerator.models

import edu.rosehulman.randomoutfitgenerator.objects.SuperCategory

class Closet {

    private var tops = SuperCategory("Tops", propogateArrayList(Array(5){"T-Shirt"; "Long Sleeve"; "Button Down"; "Sweater"; "Sweatshirt"}))
    private var bottoms = SuperCategory("Bottoms", propogateArrayList(Array(5){"Jeans"; "Skirt"; "Shorts"; "SweatPants"; "Slacks"}))
    private var accessories = SuperCategory("Accessories", propogateArrayList(Array(10){"Watch"; "Hat"; "Necklace"; "Earrings"; "Bracelet"; "Coat"; "Vest"; "Sunglasses"; "Ring"; "Gloves"}))
    private var shoes = SuperCategory("Shoes", propogateArrayList(Array(9){"Athletic Shoes"; "Boots"; "Dress Shoes"; "Slides"; "Flip-Flops"; "Crocs"; "Sandals"; "Heels"; "Flats"}))
    private var full_body = SuperCategory("Full Body", propogateArrayList(Array(5){"Dress"; "Romper"; "Pant Suit"; "Suit"; "Bathrobe"}))
    var categories = Array(5){tops; bottoms; accessories; shoes; full_body}

    private fun propogateArrayList(a: Array<String>): ArrayList<String>{
        var list = ArrayList<String>()

        for(s in a){
            list.add(s)
        }

        return list
    }
    }