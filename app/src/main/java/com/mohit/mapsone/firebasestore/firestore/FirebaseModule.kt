package com.mohit.mapsone.firebasestore.firestore

import com.google.firebase.firestore.FirebaseFirestore
import kotlin.getValue


object FirebaseModule {
    val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
}