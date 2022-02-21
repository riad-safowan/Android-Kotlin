package com.riadsafowan.to_do.ui.main

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.riadsafowan.to_do.R
import com.riadsafowan.to_do.data.local.pref.UserDataStore
import com.riadsafowan.to_do.databinding.ActivityMainBinding
import com.riadsafowan.to_do.databinding.NavHeaderBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import androidx.activity.result.ActivityResult
import com.github.dhaval2404.imagepicker.ImagePicker


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var hBinding: NavHeaderBinding
    private lateinit var navController: NavController
    private lateinit var drawer: DrawerLayout
    private lateinit var navView: NavigationView

    private lateinit var userDataStore: UserDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawer = binding.drawerLayout
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment)

        setSupportActionBar(binding.toolbar)
//        setupActionBarWithNavController(navController)

        val toggle = ActionBarDrawerToggle(
            this, drawer, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()


        navView.setCheckedItem(R.id.tasks)
        navDrawer()

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.tasks -> {
                    navController.navigate(R.id.tasksFragment)
                }
                R.id.posts -> {
                    navController.navigate(R.id.postsFragment)
                }
                R.id.logout -> {
                    viewModel.logout()
                }
            }
            drawer.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            drawer.closeDrawer(GravityCompat.START)
            when (destination.id) {
                R.id.tasksFragment -> binding.toolbar.title = "Tasks"
                R.id.loginFragment -> binding.toolbar.title = "Login"
                R.id.signupFragment -> binding.toolbar.title = "Signup"
                R.id.addEditTaskFragment -> {
                    arguments?.getString("title").let {
                        binding.toolbar.title = it!!
                    }
                }
                R.id.postsFragment -> binding.toolbar.title = "NewsFeed"
            }
        }
    }

    @DelicateCoroutinesApi
    private fun navDrawer() {
        userDataStore = UserDataStore(this)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val header = navigationView.getHeaderView(0)
        hBinding = NavHeaderBinding.bind(header)

        this.lifecycleScope.launchWhenStarted {
            viewModel.userDataFlow.collect {
                if (it.isLoggedIn) {
                    launch(Dispatchers.Main) {
                        hBinding.name.text = it.name
                        hBinding.textView.text = it.email

                        Glide.with(this@MainActivity)
                            .load(it.imageUrl)
                            .placeholder(R.mipmap.person)
                            .into(hBinding.imageView)

                        hBinding.notLoggedLayout.visibility = View.GONE
                        hBinding.isLoggedLayout.visibility = View.VISIBLE
                    }
                } else {
                    launch(Dispatchers.Main) {
                        hBinding.isLoggedLayout.visibility = View.GONE
                        hBinding.notLoggedLayout.visibility = View.VISIBLE

                        hBinding.login.setOnClickListener {
                            navController.navigate(R.id.loginFragment)
                        }
                        hBinding.signup.setOnClickListener {
                            navController.navigate(R.id.signupFragment)
                        }
                    }
                }
            }
        }

        hBinding.imageView.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .maxResultSize(1000, 1000)
                .galleryOnly()
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }
    }


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data!!
                Glide.with(this)
                    .load(fileUri)
                    .into(hBinding.imageView)

                val imageFile = File(fileUri.path)

                viewModel.uploadImage(
                    MultipartBody.Part.createFormData(
                        "image", imageFile.getName(), imageFile
                            .asRequestBody("image/*".toMediaTypeOrNull())
                    )
                )
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
            }
        }


//    private val resultLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val data: Intent? = result.data
//                Glide.with(this)
//                    .load(data?.data)
//                    .into(hBinding.imageView)
//
//
//                val uri = data?.data
//                val file: File = File(uri?.getPath()) //create path from uri
//
//                val split = file.path.split(":").toTypedArray() //split the path.
//
//                var filePath = split[1] //assign it to a string(your choice).
//
//
//                Log.d("file", "path : " + data?.data?.path)
//                Log.d("file", "path2 : " + filePath)
//
//                var filepath = "/storage/emulated/0/Download/Red-Rose.jpg"
//                val imageFile = File(filePath)
//
//                val multipartBody = MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("image", imageFile.getName(), imageFile.asRequestBody())
//                    .build()
//                val part = MultipartBody.Part.createFormData(
//                    "image",
//                    imageFile.getName(),
//                    imageFile.asRequestBody("image/*".toMediaTypeOrNull())
//                )
//
//                viewModel.uploadImage(
//                    part
////                    multipartBody
//                )
//            }
//        }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}