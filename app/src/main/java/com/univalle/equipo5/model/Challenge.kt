package com.univalle.equipo5.model

import com.google.firebase.firestore.DocumentId

data class Challenge(
    @DocumentId
    var id: String? = null,
    val description: String = "",
    var createdAt: Long = System.currentTimeMillis(),
)