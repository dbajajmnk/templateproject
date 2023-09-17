package com.hbeonlab.rms.base


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T : BaseViewModel, V : ViewBinding>() : AppCompatActivity() {
    lateinit var viewModel: T
    lateinit var viewBinding: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        viewBinding = DataBindingUtil.setContentView(this, getLayoutResourceId())
        viewModel = initViewModel()
        initView()
    }

    abstract fun initViewModel(): T
    abstract fun getLayoutResourceId(): Int

    open fun initView() {

    }



}