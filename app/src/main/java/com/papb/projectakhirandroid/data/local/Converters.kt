package com.papb.projectakhirandroid.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.papb.projectakhirandroid.domain.model.Review

class Converters {
    @TypeConverter
    fun fromReviewList(reviews: List<Review>?): String? {
        if (reviews == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Review>>() {}.type
        return gson.toJson(reviews, type)
    }

    @TypeConverter
    fun toReviewList(reviewsString: String?): List<Review>? {
        if (reviewsString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Review>>() {}.type
        return gson.fromJson(reviewsString, type)
    }
}
