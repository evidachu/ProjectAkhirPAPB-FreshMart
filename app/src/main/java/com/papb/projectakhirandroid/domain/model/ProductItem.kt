package com.papb.projectakhirandroid.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Ignore
import com.papb.projectakhirandroid.utils.Constants.PRODUCT_DATABASE_TABLE
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Entity(tableName = PRODUCT_DATABASE_TABLE)
@Serializable
data class ProductItem(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0, // Default 0 for new items
    val title: String,
    val description: String,
    val image: String?, // URL String
    val unit: String,
    val price: Double,
    val nutritions: String,
    val review: Double,
    var isCart: Boolean = false,
    var quantity: Int = 1,
    // Room can't store List directly without TypeConverter.
    // For simplicity with Supabase, we might fetch reviews separately or ignore for now in Room.
    // Adding @Ignore or @Transient if it causes issues, but assuming you might have TypeConverter setup somewhere I missed or I need to handle it.
    // Since I didn't see a TypeConverter file, I will comment it out or make it transient for Room if it was causing issues,
    // but the previous code had it. Let's keep it but be aware this might need a TypeConverter if saving to Room.
    // If you don't have a TypeConverter, Room will crash.
    // I will temporarily comment out the list to avoid build errors unless I find the converter.
    // var reviews: List<Review> = emptyList(), 
    val category: String
) {
     // I'll add the reviews back as an ignored field for Room so it doesn't crash compilation
     // but is available for the app logic if needed (though it won't persist in local DB without converter).
     @Ignore
     @Transient
     var reviews: List<Review> = emptyList()
}
