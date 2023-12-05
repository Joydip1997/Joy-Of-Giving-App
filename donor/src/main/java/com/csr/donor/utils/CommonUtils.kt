package com.csr.donor.utils

object CommonUtils {
    fun extractFileNameFromUrl(urlString: String?): String {
        val parts = urlString?.split("/")
        return parts?.last().toString()
    }


}