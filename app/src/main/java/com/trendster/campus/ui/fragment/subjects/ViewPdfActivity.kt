package com.trendster.campus.ui.fragment.subjects

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.trendster.campus.databinding.ActivityViewPdfBinding
import com.trendster.campus.utils.COLL_PDF_TITLE
import com.trendster.campus.utils.COLL_PDF_URL
import java.io.File

class ViewPdfActivity : AppCompatActivity() {

    var pdfUrl: String? = "www.trendster.com"
    var pdfTitle: String? = "trendster.pdf"
    private var _binding: ActivityViewPdfBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityViewPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pdfUrl = intent.getStringExtra(COLL_PDF_URL)
        pdfTitle = intent.getStringExtra(COLL_PDF_TITLE)

        if (pdfUrl == null)
            Log.d("MyHH", "pdfUrl!! + pdfTitle")

        binding.progressBar.visibility = View.VISIBLE
        if (pdfUrl != null) {
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
