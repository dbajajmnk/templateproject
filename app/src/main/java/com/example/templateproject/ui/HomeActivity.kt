package com.example.templateproject.ui

import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.example.templateproject.R
import com.example.templateproject.base.BaseActivity
import com.example.templateproject.databinding.ActivityHomeBinding
import com.example.templateproject.vm.HomeViewModel

class HomeActivity : BaseActivity<HomeViewModel, ActivityHomeBinding>() {
    override fun initViewModel(): HomeViewModel {
        return viewModels<HomeViewModel>().value
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_home
    }

    override fun initView() {
        super.initView()
        getData()
    }

    private fun getData(){
        viewBinding.progressbar.visibility = View.VISIBLE
        viewModel.getDataFromApi()
        viewModel.data.observe(this ) {
            if(it != "") {
                viewBinding.progressbar.visibility = View.GONE
                Log.v("HomeActivity", "getData = $it")
            }
        }
    }
}