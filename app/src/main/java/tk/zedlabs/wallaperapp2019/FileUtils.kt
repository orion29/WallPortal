package tk.zedlabs.wallaperapp2019

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.widget.ProgressBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class FileUtils(private val scope: CoroutineScope, private val appContext : Context) {


          fun setWallpaper1(image: Bitmap, id: String){
            saveImage(image, id)

          }
          fun saveImage(image: Bitmap,id : String): String? {
            var savedImagePath: String? = null
            val imageFileName = "$id.jpg"
            val storageDir =
                File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}/WallPortal")
            var success = true
            if (!storageDir.exists()) {
                success = storageDir.mkdirs()
            }
            if (success) {
                val imageFile = File(storageDir, imageFileName)
                savedImagePath = imageFile.absolutePath

                //scope.launch (Dispatchers.IO) {
                    try {
                        val fOut = FileOutputStream(imageFile)
                        image.compress(Bitmap.CompressFormat.JPEG, 80, fOut)
                        fOut.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    galleryAddPic(savedImagePath)
                }
            //}
            return savedImagePath
        }

         private fun galleryAddPic(imagePath: String) {
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(imagePath)
            val contentUri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            appContext.sendBroadcast(mediaScanIntent)

        }
}