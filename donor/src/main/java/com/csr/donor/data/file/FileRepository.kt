package com.csr.donor.data.file

import com.csr.donor.common.Resource


interface FileRepository {
    suspend fun storeImageToServer(image: ByteArray,folderPath:String) : Resource<String>
}