package com.example.collegehunt.Fragments

import android.os.Bundle
import android.text.TextUtils.replace
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collegehunt.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_tab3.*

class Tab3 : Fragment() {

    private lateinit var homeview: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeview = inflater.inflate(R.layout.fragment_tab3, container, false)
    val all = otherUsers()
        val chats = Chats()
      //  makeCurrentFragment(chats)
       val View2 = view?.findViewById<NavigationView>(R.id.bnv)

      View2?.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_all -> makeCurrentFragment(all)
             //   R.id.nav_chat -> makeCurrentFragment(chats)


            }
            true
        }


        return homeview

    }
   private fun makeCurrentFragment(fragment: Fragment) = childFragmentManager.beginTransaction().apply {
       replace(R.id.fmlayout, fragment)
       commit()
   }

    }
