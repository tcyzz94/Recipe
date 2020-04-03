package com.example.recipe.Item

import java.io.Serializable

class Recipe : Serializable{
     var id :Int=0
     var sRecipeName:String = ""
     var aryLstIngredients =  arrayListOf<String>()
     var aryLstSteps = arrayListOf<String>()
     var sImage:String=""
}