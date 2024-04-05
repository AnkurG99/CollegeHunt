package com.example.collegehunt

        import android.content.Intent
        import androidx.appcompat.app.AppCompatActivity
                import android.os.Bundle
        import androidx.appcompat.app.AppCompatDialogFragment
        import androidx.fragment.app.Fragment
        import com.example.collegehunt.Fragments.*
        import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity() {

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_dashboard)

        val home = home()
                val account = Account()
                val tab3 = otherUsers()
                val test = test()
                makeCurrentFragment(home)
bottomNavigationView.setOnNavigationItemSelectedListener {
    when(it.itemId){
        R.id.nav_home -> makeCurrentFragment(home)
        R.id.nav_account -> makeCurrentFragment(account)
        R.id.nav_test -> makeCurrentFragment(test)
        R.id.nav_users -> makeCurrentFragment(tab3)

    }
    true
}

                floatingActionButton2.setOnClickListener(){
                    val get = Intent(this, getInterest::class.java)
                   
                    startActivity(get)

                }
            }




    private fun makeCurrentFragment(fragment: Fragment)= supportFragmentManager.beginTransaction().apply{
        replace(R.id.frame, fragment)
        commit()

    }
}