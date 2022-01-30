package com.rspackage.kotlindemo.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rspackage.kotlindemo.R
import com.rspackage.kotlindemo.support.data.api.Resource
import com.rspackage.kotlindemo.ui.main.viewmodel.MainViewModel
import com.rspackage.kotlindemo.ui.main.viewmodel.MainviewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    private lateinit var mainViewModel: MainViewModel
    private val mainViewsFactory: MainviewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(this, mainViewsFactory).get(MainViewModel::class.java)
        mainViewModel.getData()
        mainViewModel.getData2()
        initObserver()
    }

    private fun initObserver() {
        mainViewModel.animalData.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    //show progress bar
                    showToast("show loading")

                }

                is Resource.Success -> {
                    //hide progressbar
                    showToast(it.data?.fact ?: "")
                }

                is Resource.Error -> {
                    //hide progressbar
                    showToast(it.message!!)


                }
            }
        })
    }

    private fun showToast(fact: String) {

        Toast.makeText(this@MainActivity, fact, Toast.LENGTH_LONG).show()

    }

    /*  fun showProgressBar(){
          if (showing){

          }else{
              //show
          }
      }

      fun hideProgressbar(){
          if (showing){

          }else{
              //show
          }
      }*/
}