package com.papb.projectakhirandroid.utils

import com.papb.projectakhirandroid.R
import com.papb.projectakhirandroid.domain.model.AboutItem
import com.papb.projectakhirandroid.domain.model.CategoryItem
import com.papb.projectakhirandroid.domain.model.ProductItem
import com.papb.projectakhirandroid.ui.theme.*

object DataDummy {

    // Helper function untuk mengubah Resource ID menjadi URL String yang bisa dibaca Coil
    private fun getResourceUri(resourceId: Int): String {
        return "android.resource://com.papb.projectakhirandroid/$resourceId"
    }

    fun generateDummyProduct(): List<ProductItem> {
        return listOf(
            ProductItem(
                id = 1,
                title = "Makaroni",
                description = "Makaroni adalah pasta kering berbentuk tabung sempit. Dibuat dengan gandum durum, makaroni biasanya dipotong pendek; makaroni melengkung sering disebut makaroni siku.",
                image = getResourceUri(R.drawable.product1),
                unit = "1 pcs",
                price = 15000.0,
                nutritions = "50gr",
                review = 3.5,
                category = "Roti & Snack"
            ),
            ProductItem(
                id = 2,
                title = "Telur",
                description = "Telur memiliki cangkang keras kalsium karbonat yang membungkus putih telur cair dan kuning telur tunggal.",
                image = getResourceUri(R.drawable.product2),
                unit = "10 pcs",
                price = 25000.0,
                nutritions = "150gr",
                review = 5.0,
                category = "Susu & Telur"
            ),
            ProductItem(
                id = 3,
                title = "Mayones",
                description = "Mayones adalah bumbu krim putih yang digunakan untuk salad tuna atau olesan sandwich. Terbuat dari kuning telur, minyak, dan jus lemon.",
                image = getResourceUri(R.drawable.product3),
                unit = "1 pcs",
                price = 35000.0,
                nutritions = "100gr",
                review = 4.0,
                category = "Minyak Goreng & Mentega"
            ),
            ProductItem(
                id = 4,
                title = "Mie Telur",
                description = "Jenis pasta pipih yang berbeda dari pasta tepung biasa karena ditambahkan telur untuk memperkaya adonan. Tersedia dalam berbagai lebar.",
                image = getResourceUri(R.drawable.product4),
                unit = "1 pcs",
                price = 12000.0,
                nutritions = "90gr",
                review = 4.7,
                category = "Roti & Snack"
            ),
            ProductItem(
                id = 5,
                title = "Jahe",
                description = "Jahe adalah salah satu rempah paling populer di dunia yang berasal dari batang bawah tanah tanaman jahe. Rempah aromatik ini menjadi ciri khas masakan Asia.",
                image = getResourceUri(R.drawable.product5),
                unit = "250 gr",
                price = 10000.0,
                nutritions = "50gr",
                review = 4.2,
                category = "Buah & Sayur"
            ),
            ProductItem(
                id = 6,
                title = "Diet Coke",
                description = "Diet Coke® adalah keseimbangan sempurna antara kesegaran dan rasa renyah, tanpa gula dan kalori. Nikmati rasa cola diet yang lezat!",
                image = getResourceUri(R.drawable.product6),
                unit = "1 kaleng",
                price = 9000.0,
                nutritions = "10gr",
                review = 3.8,
                category = "Minuman"
            ),
            ProductItem(
                id = 7,
                title = "Ayam Potong",
                description = "Ayam broiler adalah ayam yang diternakkan khusus untuk produksi daging. Daging segar berkualitas tinggi.",
                image = getResourceUri(R.drawable.product7),
                unit = "1 kg",
                price = 45000.0,
                nutritions = "70gr",
                review = 3.0,
                category = "Daging & Ikan"
            ),
            ProductItem(
                id = 8,
                title = "Jus Apel",
                description = "Cairan jernih dari apel segar tanpa ampas. Jus ini sering digunakan sebagai minuman segar atau bahan campuran saus.",
                image = getResourceUri(R.drawable.product8),
                unit = "1 botol",
                price = 18000.0,
                nutritions = "100gr",
                review = 4.2,
                category = "Minuman"
            ),
            ProductItem(
                id = 9,
                title = "Jus Jeruk",
                description = "Jus buah yang diperoleh dari memeras jeruk segar. Kaya akan vitamin, mineral, dan antioksidan alami.",
                image = getResourceUri(R.drawable.product9),
                unit = "1 botol",
                price = 20000.0,
                nutritions = "100gr",
                review = 4.7,
                category = "Minuman"
            ),
            ProductItem(
                id = 10,
                title = "Pisang",
                description = "Pisang adalah buah memanjang yang dapat dimakan, diproduksi oleh beberapa jenis tumbuhan berbunga herba besar.",
                image = getResourceUri(R.drawable.product10),
                unit = "1 sisir",
                price = 22000.0,
                nutritions = "100gr",
                review = 4.5,
                category = "Buah & Sayur"
            ),
        )
    }

    fun generateDummyCategories(): List<CategoryItem> {
        return listOf(
            CategoryItem(
                title = "Buah & Sayur\n" + "Segar",
                image = R.drawable.category1,
                background = BackgroundCategory1
            ),
            CategoryItem(
                title = "Minyak Goreng\n" + "& Mentega",
                image = R.drawable.category2,
                background = BackgroundCategory2
            ),
            CategoryItem(
                title = "Daging & Ikan",
                image = R.drawable.category3,
                background = BackgroundCategory3
            ),
            CategoryItem(
                title = "Roti & Snack",
                image = R.drawable.category4,
                background = BackgroundCategory4
            ),
            CategoryItem(
                title = "Susu & Telur",
                image = R.drawable.category5,
                background = BackgroundCategory5
            ),
            CategoryItem(
                title = "Minuman",
                image = R.drawable.category6,
                background = BackgroundCategory6
            )
        )
    }

    fun generateDummyAbout(): List<AboutItem> {
        return listOf(
            AboutItem(
                image = R.drawable.ic_orders, // Assuming a collection icon, using orders as placeholder
                title = "Koleksi"
            ),
            AboutItem(
                image = R.drawable.ic_orders,
                title = "Pesanan"
            ),
            AboutItem(
                image = R.drawable.ic_my_details,
                title = "Detail Saya"
            ),
            AboutItem(
                image = R.drawable.ic_address,
                title = "Alamat Pengiriman"
            ),
            AboutItem(
                image = R.drawable.ic_payment,
                title = "Pembayaran"
            ),
            AboutItem(
                image = R.drawable.ic_notification,
                title = "Notifikasi"
            ),
        )
    }

}
