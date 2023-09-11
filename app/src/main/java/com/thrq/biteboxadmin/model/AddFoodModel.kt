package com.thrq.biteboxadmin.model

data class AddFoodModel(
    val foodName : String? = "",
    val foodDesc : String? = "",
    val foodPrice : String? = "",
    val foodCategory : String? = "",
    val foodCoverImg : String? = "",
//    val foodResto : String? = "",
    val foodId : String? = "",
    val foodImages : ArrayList<String>

)
