package com.example.messengerapp.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messengerapp.AdapterClasses.UserAdapter
import com.example.messengerapp.ModelClasses.Users
import com.example.messengerapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchFragment : Fragment() {
    private var userAdapter:UserAdapter?=null
    private var mUsers:List<Users>?=null
    private var recyclerView:RecyclerView?=null
    private var searchEditText:EditText?=null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View= inflater.inflate(R.layout.fragment_search, container, false)

        searchEditText=view.findViewById(R.id.searchUsersET)
        recyclerView=view.findViewById(R.id.searchList)
        recyclerView!!.hasFixedSize()
        recyclerView!!.layoutManager=LinearLayoutManager(context)

        mUsers=ArrayList()
        retrieveAllUsers()

        searchEditText!!.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                SearchForUser(s.toString().toLowerCase())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        return view

    }

    private fun retrieveAllUsers() {
        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val refUsers= FirebaseDatabase.getInstance().reference.child("Users")

        refUsers.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
                if (searchEditText!!.text.toString()== ""){
                    for (ss in snapshot.children){
                        val user:Users?=ss.getValue(Users::class.java)
                        if(!(user!!.getUID()).equals(firebaseUserID)){
                            (mUsers as ArrayList<Users>).add(user)
                        }
                    }
                }
                userAdapter= UserAdapter(context!!,mUsers!!,isChatCheck = false)
                recyclerView!!.adapter=userAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }


    private fun SearchForUser(str:String){
        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val queryUsers= FirebaseDatabase.getInstance().reference
            .child("Users")
            .orderByChild("search")
            .startAt(str)
            .endAt(str+ "\uf8ff")

        queryUsers.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()
                for (ss in snapshot.children){
                    val user:Users?=ss.getValue(Users::class.java)
                    if(!(user!!.getUID()).equals(firebaseUserID)){
                        (mUsers as ArrayList<Users>).add(user)
                    }
                }
                userAdapter= UserAdapter(context!!,mUsers!!,isChatCheck = false)
                recyclerView!!.adapter=userAdapter

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}