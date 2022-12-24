package com.example.templateproject.base


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T : BaseViewModel, V : ViewBinding>() : AppCompatActivity() {
    private lateinit var viewModel: T
    private lateinit var viewBinding: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

    }

    private fun init() {
        viewBinding = DataBindingUtil.setContentView(this, getLayoutResourceId())
        viewModel = getViewModel()
        initView()
    }

    abstract fun getViewModel(): T
    abstract fun getLayoutResourceId(): Int

    open fun initView() {

    }



}