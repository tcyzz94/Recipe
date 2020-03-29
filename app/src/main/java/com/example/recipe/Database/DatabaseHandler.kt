package com.example.recipe.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.recipe.Item.Recipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $NAME TEXT,$INGREDIENTS TEXT,$STEPS TEXT,$IMAGE TEXT);"
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun addTask(recipe: Recipe): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        val gson = Gson()
        val aryIngredient = gson.toJson(recipe.aryLstIngredients).toString()
        val arySteps = gson.toJson(recipe.aryLstSteps).toString()

        values.put(NAME, recipe.sRecipeName)
        values.put(INGREDIENTS, aryIngredient)
        values.put(STEPS, arySteps)
        values.put(IMAGE, recipe.sImage)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        Log.v("InsertedId", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }

    fun getTask(_id: Int): Recipe {
        val recipe = Recipe()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()
        recipe.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        recipe.sRecipeName = cursor.getString(cursor.getColumnIndex(NAME))
        recipe.sImage = cursor.getString(cursor.getColumnIndex(IMAGE))
        val aryIngredient = cursor.getString(cursor.getColumnIndex(INGREDIENTS))
        val arSteps = cursor.getString(cursor.getColumnIndex(STEPS))
        Log.i("Ingredients without :!:", recipe.aryLstIngredients.toString())
        Log.i("Steps without :!:", recipe.aryLstSteps.toString())
        cursor.close()
        return recipe
    }

    fun task(): ArrayList<Recipe> {
        val recipeList = ArrayList<Recipe>()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val recipe = Recipe()
                    recipe.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    recipe.sRecipeName = cursor.getString(cursor.getColumnIndex(NAME))
                    recipe.sImage = cursor.getString(cursor.getColumnIndex(IMAGE))
                    val sJsIngredient = cursor.getString(cursor.getColumnIndex(INGREDIENTS))
                    val sJsSteps = cursor.getString(cursor.getColumnIndex(STEPS))
                    val gson = Gson()
                    val ttIngredient = object : TypeToken<ArrayList<String>>() {}.type
                    val aryLstIngredient: ArrayList<String> =
                        gson.fromJson(sJsIngredient, ttIngredient)
                    val ttSteps = object : TypeToken<ArrayList<String>>() {}.type
                    val aryLstStep: ArrayList<String> = gson.fromJson(sJsSteps, ttSteps)
                    Log.i("Ing", sJsIngredient)
                    Log.i("Steps", sJsSteps)
                    recipe.aryLstIngredients = aryLstIngredient
                    recipe.aryLstSteps = aryLstStep
                    recipeList.add(recipe)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return recipeList
    }

    fun updateTask(recipe: Recipe): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, recipe.sRecipeName)
        values.put(IMAGE, recipe.sImage)
        val aryIngredient = recipe.aryLstIngredients.joinToString(":!:")
        val arySteps = recipe.aryLstSteps.joinToString(":!:")
        values.put(INGREDIENTS, aryIngredient)
        values.put(STEPS, arySteps)
        val _success =
            db.update(TABLE_NAME, values, ID + "=?", arrayOf(recipe.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteTask(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteAllTasks(): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, null, null).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }


    companion object {
        private val DB_VERSION = 1
        private val DB_NAME = "MyRecipe"
        private val TABLE_NAME = "Recipe"
        private val ID = "id"
        private val NAME = "name"
        private val INGREDIENTS = "ingredients"
        private val STEPS = "steps"
        private val IMAGE = "image"
    }
}