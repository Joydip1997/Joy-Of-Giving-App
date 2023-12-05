package com.csr.donor.data.file



import com.google.firebase.storage.FirebaseStorage
import com.csr.donor.common.Resource
import com.csr.donor.data.file.FileRepository
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FileRepositoryImpl @Inject constructor(private val firebaseStorage: FirebaseStorage) :
    FileRepository {

    override suspend fun storeImageToServer(image: ByteArray,folderPath:String): Resource<String> {

        return suspendCoroutine {continuation->
            val imageRef = firebaseStorage.getReference(folderPath).child(UUID.randomUUID().toString())
            imageRef.putBytes(image).addOnSuccessListener { taskSnapshot->
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    continuation.resume(Resource.Success(imageUrl))
                }
            }.addOnFailureListener {error->
                continuation.resume(Resource.Error(Exception(error.message.toString())))
            }


        }
    }


}