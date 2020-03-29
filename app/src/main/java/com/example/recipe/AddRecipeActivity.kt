package com.example.recipe

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe.Adapter.IngredientsRvAdapter
import com.example.recipe.Adapter.StepsRvAdapter
import com.example.recipe.Database.DatabaseHandler
import com.example.recipe.Item.Recipe
import com.example.recipe.Util.ImageUtil
import kotlinx.android.synthetic.main.activity_add_recipe.*
import java.io.IOException
import java.util.*


class AddRecipeActivity : AppCompatActivity() {

    private val aryLstIngredients = ArrayList<String>()
    private val aryLstSteps = ArrayList<String>()

    private lateinit var ivCam: ImageView

    private lateinit var etRecipeName: EditText
    private lateinit var btnOk: Button
    private lateinit var btnCancel: Button

    private lateinit var llIngredients: LinearLayout
    private lateinit var llSteps: LinearLayout

    private lateinit var rvIngredients: RecyclerView
    private lateinit var rvSteps: RecyclerView

    var dbRecipeHandler: DatabaseHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)
        rvIngredients = findViewById(R.id.rv_ingredient)
        rvIngredients.adapter = IngredientsRvAdapter(aryLstIngredients)
        rvIngredients.layoutManager = LinearLayoutManager(this)

        rvSteps = findViewById(R.id.rv_steps)
        rvSteps.adapter = StepsRvAdapter(aryLstSteps)
        rvSteps.layoutManager = LinearLayoutManager(this)

        llIngredients = findViewById(R.id.ll_ingredients_items)
        llSteps = findViewById(R.id.ll_steps_items)
        etRecipeName = findViewById(R.id.et_recipe_name)
        ivCam = findViewById(R.id.iv_add_recipe)
        btnCancel = findViewById(R.id.btn_cancel)
        btnOk = findViewById(R.id.btn_ok)

        ivCam.setOnClickListener { picDialog() }
        btnCancel.setOnClickListener { onBackPressed() }
        btnOk.setOnClickListener { saveRecipe() }

        dbRecipeHandler = DatabaseHandler(this)
    }

    companion object {
        private val GALLERY_CODE = 1000
        private val GALLERY_PERMISSION = 1001
        private val CAMERA_PICK_CODE = 1002
        private val CAMERA_PERMISSION = 1003
    }

    private fun saveRecipe() {
        val recipe = Recipe()
        var success:Boolean
        val sRName = et_recipe_name.text.toString()
        if (sRName.isNotEmpty()) {
            recipe.sRecipeName = sRName
        }
        if (ivCam.drawable != null) {
            val bitmap = (ivCam.getDrawable() as BitmapDrawable).bitmap
            val bImage = ImageUtil().convertBitmapToString(bitmap)
            if (bImage != null) {
                recipe.sImage = bImage
            }
        }
        val itemCount = rvIngredients.adapter!!.itemCount
        var i = 0
        while (i < itemCount) {
            val holder = rvIngredients.findViewHolderForAdapterPosition(i)
            lateinit var etIngredient: EditText
            if (holder != null) {
                etIngredient = (holder as IngredientsRvAdapter.IngredientViewHolder).etField
                recipe.aryLstIngredients.add(etIngredient.text.toString())
            }
            i++
        }
        val iSteps = rvSteps.adapter!!.itemCount
        var j = 0
        while (j < iSteps) {
            val holder = rvSteps.findViewHolderForAdapterPosition(j)
            lateinit var etSteps: EditText
            if (holder != null) {
                etSteps = (holder as StepsRvAdapter.StepsViewHolder).etField
                recipe.aryLstSteps.add(etSteps.text.toString())
            }
            j++
        }
        success = dbRecipeHandler!!.addTask(recipe)
        val CHANNEL_ID = "WARNING"
        if (success) {
            finish()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = getString(R.string.warning)
                val descriptionText = getString(R.string.failed)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }

                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            } else {
                val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_warning)
                    .setContentTitle(getString(R.string.warning))
                    .setContentText(getString(R.string.failed))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                with(NotificationManagerCompat.from(this)) {
                    notify(0, builder.build())
                }
            }
        }
    }

    private fun picDialog() {
        val picDialog = AlertDialog.Builder(this)
        picDialog.setTitle("Select Action")
        val picDialogItems = arrayOf("Gallery", "Camera")
        picDialog.setItems(picDialogItems) { dialog, which ->
            when (which) {
                0 -> checkPermission("0")//Gallery
                1 -> checkPermission("1")//Camera
            }
        }
        picDialog.show()
    }

    private fun checkPermission(iPcode: String) {
        val iCode = iPcode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (iCode == "0") {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, GALLERY_PERMISSION)
                } else {
                    pickImageFromGallery()
                }
            } else if (iCode == "1") {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    val permissions = arrayOf(Manifest.permission.CAMERA)
                    requestPermissions(permissions, CAMERA_PERMISSION)
                } else {
                    captureCamera()
                }
            }
        } else {
            when (iCode) {
                "0" -> pickImageFromGallery()
                "1" -> captureCamera()
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_CODE)
    }

    private fun captureCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            GALLERY_PERMISSION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            CAMERA_PERMISSION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    captureCamera()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_CODE) {
                if (data != null) {
                    val contentURI = data!!.data
                    try {
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                        iv_add_recipe!!.setImageBitmap(bitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (requestCode == CAMERA_PICK_CODE) {
                val thumbnail = data!!.extras!!.get("data") as Bitmap
                iv_add_recipe.setImageBitmap(thumbnail)
            }
        }
    }
}
