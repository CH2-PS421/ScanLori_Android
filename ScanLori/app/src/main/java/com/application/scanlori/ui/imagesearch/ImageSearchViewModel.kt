package com.application.scanlori.ui.imagesearch

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class ImageSearchViewModel: ViewModel() {
    val predictionResult = MutableLiveData<String>()

    fun uploadImage(file: File) {
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val imageBody = MultipartBody.Part.createFormData("file", file.name, requestFile)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.predictImage(imageBody)
                if (response.isSuccessful) {
                    val result = response.body()
                    val prediction = result?.className.toString()
                    val probability = result?.probability ?: 0.0
                    val message = "$prediction"
                    val responseString = response.body()?.toString()

                    if (responseString != null) {
                        Log.d("Response", responseString)
                    }
                    predictionResult.postValue(message)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        JSONObject(errorBody).getString("message")
                    } catch (e: JSONException) {
                        "Error: ${response.code()}"
                    }
                    predictionResult.postValue(errorMessage)
                }
            } catch (e: Exception) {
                predictionResult.postValue("Error: ${e.message}")
            }
        }
    }
}