package com.example.imageworld



import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.imageworld.databinding.ActivityMainBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var filepath : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root


        binding.button.setOnClickListener  {
          startFileChooser()
        }


       binding.button2.setOnClickListener {
           uploadFile()
       }

        setContentView(view)
   }


    private fun uploadFile() {
        if(filepath!=null)  {
            var pd = ProgressDialog (this)
            pd.setTitle("uploading")
            pd.show()

            var imageRef : StorageReference  = FirebaseStorage.getInstance().reference.child( "images/pic.jpg")
            imageRef.putFile(filepath)
                    .addOnSuccessListener { p0 ->
                        pd.dismiss()
                        Toast.makeText(applicationContext, "File Uploaded",Toast.LENGTH_LONG).show()

                    }
                    .addOnFailureListener{ p0 ->
                        pd.dismiss()
                        Toast.makeText(applicationContext,p0.message,Toast.LENGTH_LONG).show()
                    }
                    .addOnProgressListener { p0->
                        var progress = (100.0 * p0.bytesTransferred) / p0.totalByteCount
                        pd.setMessage("uploaded ${progress.toInt ()}%")

                    }

        }
    }


    private fun startFileChooser() {
        var i = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(i, "Choose Picture"), 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==111 && resultCode == RESULT_OK && data != null) {
            filepath = data.data!!
            var bitmap : Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filepath)
            binding.imageView.setImageBitmap(bitmap)
        }
    }
}