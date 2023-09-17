package com.hbeonlab.rms.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : BaseViewModel, V : ViewBinding>() : Fragment() {
    private lateinit var viewModel: T
    private lateinit var viewBinding: V

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init(container)
        return viewBinding.root
    }

    private fun init(container: ViewGroup?) {
        viewBinding =
            DataBindingUtil.inflate(layoutInflater, getLayoutResourceId(), container, false)
        viewModel = getViewModel()
        initView()
    }

    abstract fun getViewModel(): T
    abstract fun getLayoutResourceId(): Int

    open fun initView() {

    }

}