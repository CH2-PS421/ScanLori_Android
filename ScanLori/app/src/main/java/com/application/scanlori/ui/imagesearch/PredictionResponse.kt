package com.application.scanlori.ui.imagesearch

import com.google.gson.annotations.SerializedName

data class PredictionResponse(
    @SerializedName("class")
    val className: String? = null,
    @SerializedName("probability")
    val probability: Double? = null
)
