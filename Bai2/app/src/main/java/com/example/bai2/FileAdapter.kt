package com.example.bai2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FileAdapter(private val fileList: MutableList<FileItem>) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    inner class FileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fileName: TextView = view.findViewById(R.id.tv_fileName)
        val filePath: TextView = view.findViewById(R.id.tv_filePath)
        val fileSize: TextView = view.findViewById(R.id.tv_fileSize)
        val fileIcon: ImageView = view.findViewById(R.id.iv_fileIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = fileList[position]
        holder.fileName.text = file.name
        holder.filePath.text = file.path
        holder.fileSize.text = "${file.size} MB"

        // Tạo biểu tượng tùy theo kiểu file (dùng chuỗi thay vì enum)
        when (file.type) {
            "IMAGE" -> holder.fileIcon.setImageResource(R.drawable.image)
            "AUDIO" -> holder.fileIcon.setImageResource(R.drawable.audio)
            "VIDEO" -> holder.fileIcon.setImageResource(R.drawable.video)
            else -> holder.fileIcon.setImageResource(R.drawable.unknown)  // Biểu tượng mặc định cho loại file không xác định
        }
    }

    override fun getItemCount(): Int = fileList.size

    // Hàm để thêm file mới lên đầu danh sách
    fun addNewFiles(files: List<FileItem>) {
        fileList.addAll(0, files)
        notifyItemRangeInserted(0, files.size)
    }
}
