package com.example.bai2

import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bai2.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var fileAdapter: FileAdapter
    private val fileList = mutableListOf<FileItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cấu hình RecyclerView
        recyclerView = binding.rvMain
        fileAdapter = FileAdapter(fileList)
        recyclerView.adapter = fileAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Thực hiện quét MediaStore ngay lập tức
        scanFiles()
    }

    private fun scanFiles() {
        lifecycleScope.launch(Dispatchers.IO) {
            // Chỉ định các cột bạn muốn truy vấn từ MediaStore
            val projection = arrayOf(
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.SIZE,
                MediaStore.MediaColumns.MIME_TYPE
            )

            val sortOrder = "${MediaStore.MediaColumns.DATE_ADDED} DESC" // Sắp xếp theo thời gian thêm vào

            // Quét hình ảnh
            val cursorImages = applicationContext.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null, // Quét tất cả hình ảnh
                null, // Không cần điều kiện
                sortOrder
            )

            // Quét video
            val cursorVideos = applicationContext.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null, // Quét tất cả video
                null, // Không cần điều kiện
                sortOrder
            )

            // Quét âm thanh
            val cursorAudio = applicationContext.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null, // Quét tất cả âm thanh
                null, // Không cần điều kiện
                sortOrder
            )

            val newFileItems = mutableListOf<FileItem>()

            cursorImages?.use { cursor ->
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
                    val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)) / (1024 * 1024) // MB
                    val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE))

                    val type = "IMAGE"
                    val fileItem = FileItem(name, "content://media/external/images/media/$id", size, type)
                    newFileItems.add(fileItem)
                }
            }

            cursorVideos?.use { cursor ->
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
                    val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)) / (1024 * 1024) // MB
                    val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE))

                    val type = "VIDEO"
                    val fileItem = FileItem(name, "content://media/external/video/media/$id", size, type)
                    newFileItems.add(fileItem)
                }
            }

            cursorAudio?.use { cursor ->
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
                    val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)) / (1024 * 1024) // MB
                    val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE))

                    val type = "AUDIO"
                    val fileItem = FileItem(name, "content://media/external/audio/media/$id", size, type)
                    newFileItems.add(fileItem)
                }
            }

            // Cập nhật UI (RecyclerView) trên thread chính
            withContext(Dispatchers.Main) {
                fileAdapter.addNewFiles(newFileItems) // Thêm các file quét được vào đầu danh sách
            }
        }
    }



}
