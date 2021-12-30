package com.example.messengerapp.Activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.messengerapp.Fragments.ChatFragment
import com.example.messengerapp.Fragments.SearchFragment
import com.example.messengerapp.Fragments.SettingFragment
import com.example.messengerapp.ModelClasses.Users
import com.example.messengerapp.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    var refUsers:DatabaseReference?=null
    var firebaseUser:FirebaseUser?=null

    private lateinit var user_name: TextView
    private lateinit var profil_image: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        user_name=findViewById(R.id.user_name)
        profil_image=findViewById(R.id.profile_image)

        firebaseUser=FirebaseAuth.getInstance().currentUser
        refUsers=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val viewPagerAdapter= ViewPagerAdapter(supportFragmentManager)

        viewPagerAdapter.addFragment(ChatFragment(),"Chats")
        viewPagerAdapter.addFragment(SearchFragment(),"Search")
        viewPagerAdapter.addFragment(SettingFragment(),"Setting")

        viewPager.adapter=viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        //Display username and profile pic

        refUsers!!.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val user:Users?=snapshot.getValue(Users::class.java)
                    user_name.text=user!!.getUserName()
                    Picasso.get().load(user.getProfile()).into(profil_image)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       when (item.itemId)
        {
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                return true
            }
       }
        return false
    }
    //////////////////////////////  Creating Adapter  //////////////////////////////

    internal class ViewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentPagerAdapter(fragmentManager) {
        private val fragments: ArrayList<Fragment>
        private val tittles: ArrayList<String>

        init {
            fragments = ArrayList<Fragment>()
            tittles = ArrayList<String>()
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]

        }

        fun addFragment(fragment: Fragment, tittle: String) {
            fragments.add(fragment)
            tittles.add(tittle)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return tittles[position]
        }
    }
}
