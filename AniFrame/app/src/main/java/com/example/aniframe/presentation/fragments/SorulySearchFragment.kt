package com.example.aniframe.presentation.fragments

import com.example.aniframe.data.network.SorulySearchImageTask
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.aniframe.R
import com.example.aniframe.databinding.FragmentSorulySearchBinding
import com.example.aniframe.data.models.SorulySearchResult
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class SorulySearchFragment : androidx.fragment.app.Fragment(), CoroutineScope {

    private var _binding: FragmentSorulySearchBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var job: Job

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            selectedImageUri?.let {
                launch {
                    try {
                        activity?.contentResolver?.openInputStream(it)?.use { inputStream ->
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            val jpegFile = convertBitmapToJPEG(bitmap)
                            val sorulySearchImageTask = SorulySearchImageTask()
                            val searchResult = sorulySearchImageTask.searchImage(jpegFile)
                            // Переход на SearchResultFragment с передачей результатов поиска
                            val fragment = SorulySearchResultFragment.newInstance(searchResult as List<SorulySearchResult>)
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.frame_layout, fragment)
                                .addToBackStack(null)
                                .commit()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                        activity?.runOnUiThread {
                            Toast.makeText(activity, "Ошибка при конвертации изображения в JPEG", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSorulySearchBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            selectImageButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                selectImageLauncher.launch(intent)
            }
        }

        // Инициализация job
        job = Job()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Отменяем все корутины при уничтожении представления фрагмента
        job.cancel()
    }

    @Throws(IOException::class)
    private fun convertBitmapToJPEG(bitmap: Bitmap): File {
        val jpegFile = File(activity?.cacheDir, "image.jpg")
        val fileOutputStream = FileOutputStream(jpegFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
        return jpegFile
    }

}
