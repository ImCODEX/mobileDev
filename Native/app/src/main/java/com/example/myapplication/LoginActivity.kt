package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer


class LoginActivity : AppCompatActivity() {
    private val PERMISSION_CODE = 1000
    private var image_uri: Uri? = null
    private lateinit var capture_btn: Button
    private lateinit var login_btn: Button
    private lateinit var image_view: ImageView
    var serverApi: ServerApi = RetrofitHelper.getInstance().create(ServerApi::class.java)
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
            if (result) {
                image_view.setImageURI(image_uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        var userId = sharedPreferences.getLong("user_id", -1L)
        if (userId != -1L) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            this.finish()
        }
        setContentView(R.layout.activity_login)
        capture_btn = findViewById(R.id.capture_btn)
        image_view = findViewById(R.id.image_view)
        login_btn = findViewById(R.id.login_btn)

        //button click
        capture_btn.setOnClickListener {
            //if system os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    //permission was not enabled
                    val permission = arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    //permission already granted
                    takeImage()
                }
            } else {
                //system os is < marshmallow
                takeImage()
            }
        }
        login_btn.setOnClickListener {
            if (image_uri == null) {
                Toast.makeText(this, "Please capture an image", Toast.LENGTH_SHORT).show()
            } else {
                val baos = ByteArrayOutputStream()
                image_view.drawable.toBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos)
                val img = MultipartBody.Part.createFormData(
                    "image",
                    "image.png",
                    RequestBody.create(MediaType.parse("image/*"), baos.toByteArray())
                )
                RetrofitHelper.baseUrl = "http://localhost:5000/"
                serverApi = RetrofitHelper.getInstance().create(ServerApi::class.java)
                serverApi.login(img)
                    .enqueue(object : retrofit2.Callback<Long> {
                        override fun onResponse(
                            call: retrofit2.Call<Long>,
                            response: retrofit2.Response<Long>
                        ) {
                            if (response.isSuccessful) {
                                RetrofitHelper.baseUrl = "http://localhost:8080/"
                                userId = response.body()!!
                                val editor = sharedPreferences.edit()
                                editor.putLong("user_id", userId)
                                editor.apply()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.putExtra("userId", userId)
                                startActivity(intent)
                                finish()
                            } else {
                                println(response.body())
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Login failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<Long>, t: Throwable) {
                            println(t.message)
                            Toast.makeText(this@LoginActivity, "Login failed2", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
            }
        }
        login_btn.setOnLongClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //called when user presses ALLOW or DENY from Permission Request Popup
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup was granted
                    takeImage()
                } else {
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                image_uri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(
            applicationContext, "${BuildConfig.APPLICATION_ID}.provider", tmpFile
        )
    }

}