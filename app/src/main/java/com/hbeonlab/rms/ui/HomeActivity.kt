package com.hbeonlab.rms.ui

import androidx.activity.viewModels
import com.hbeonlab.rms.R
import com.hbeonlab.rms.base.BaseActivity
import com.hbeonlab.rms.databinding.ActivityHomeBinding
import com.hbeonlab.rms.vm.HomeViewModel

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
        /*viewBinding.progressbar.visibility = View.VISIBLE

        viewModel.data.observe(this ) {
            if(it != "") {
                viewBinding.progressbar.visibility = View.GONE
                Log.v("HomeActivity", "getData = $it")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }*/
    }
}