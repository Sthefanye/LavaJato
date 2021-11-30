package com.example.carwash.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.carwash.R
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.reference
        databaseReference.child("teste").push().setValue("100")
    }
}

