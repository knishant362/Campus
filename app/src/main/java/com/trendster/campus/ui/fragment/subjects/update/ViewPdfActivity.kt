package com.trendster.campus.ui.fragment.subjects.update

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.trendster.campus.R
import com.trendster.campus.databinding.ActivityViewPdfBinding
import com.trendster.campus.utils.COLL_PDF_TITLE
import com.trendster.campus.utils.COLL_PDF_URL
import retrofit2.http.Url
import java.io.File
import java.io.InputStream
import java.net.URL

class ViewPdfActivity : AppCompatActivity() {

    var pdfUrl : String? = "www.trendster.com"
    var pdfTitle : String? = "trendster.pdf"
    private var _binding : ActivityViewPdfBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityViewPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)


        pdfUrl = intent.getStringExtra(COLL_PDF_URL)
        pdfTitle = intent.getStringExtra(COLL_PDF_TITLE)

        binding.progressBar.visibility = View.VISIBLE
        if (pdfUrl != null){
            val fileName = "$pdfTitle.pdf"
            downloadPdfFromInternet(
                pdfUrl!!,
                getRootDirPath(this),
                fileName
                )
        }
    }

    private fun downloadPdfFromInternet(url: String, dirPath: String, fileName: String) {
        PRDownloader.download(
            url,
            dirPath,
            fileName
        ).build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    Toast.makeText(this@ViewPdfActivity, "downloadComplete", Toast.LENGTH_LONG)
                        .show()
                    val downloadedFile = File(dirPath, fileName)
                    binding.progressBar.visibility = View.GONE
                    showPdfFromFile(downloadedFile)
                }

                override fun onError(error: Error?) {
                    Toast.makeText(
                        this@ViewPdfActivity,
                        "Error in downloading file : $error",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            })
    }


    private fun showPdfFromFile(file: File) {
        binding.pdfView.fromFile(file)
            .password(null)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .onPageError { page, _ ->
                Toast.makeText(
                    this@ViewPdfActivity,
                    "Error at page: $page", Toast.LENGTH_LONG
                ).show()
            }
            .load()
    }

    private fun getRootDirPath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }

}