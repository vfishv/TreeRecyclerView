package com.baozi.demo.activity

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.baozi.demo.R
import com.baozi.demo.fragment.MineFg
import kotlinx.android.synthetic.main.activity_tb_home.*

class TBActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tb_home)
        val content = findViewById<ViewPager>(R.id.content)
        content.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(i: Int): Fragment {
                return MineFg()
            }

            override fun getCount(): Int {
                return 5
            }

            override fun getPageTitle(position: Int): CharSequence {
                return position.toString() + ""
            }
        }
        tb_title.setupWithViewPager(content)
    }
}
