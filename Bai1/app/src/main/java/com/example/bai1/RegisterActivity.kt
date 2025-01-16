package com.example.bai1

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bai1.databinding.ActivityRegisterBinding
import java.io.FileOutputStream

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var email = binding.etEmail.text
        var matkhau = binding.etMatkhau.text
        binding.btDangki.setOnClickListener {
             //Kiểm tra xem các ô nhập liệu có trống hay không
             if (email.isEmpty() || matkhau.isEmpty()){
                  // Trường hợp nếu mà trống
                 Toast.makeText(this,"Hãy nhập đầy đủ thông tin!!", Toast.LENGTH_SHORT).show()
             }else{
                 // Mở file để ghi thông tin
                 val fileOutputStream: FileOutputStream = openFileOutput("userInformation.txt", MODE_PRIVATE)
                 // Chuỗi mà lưu vào trong file
                 val data = "Email: $email\nPassword: $matkhau"
                 // Dữ liệu chuỗi (data) được chuyển đổi thành mảng byte và ghi vào tệp thông qua FileOutputStream
                 // Phương thức toByteArray() chuyển đổi chuỗi thành các byte mà hệ thống có thể lưu trữ vào tệp.
                 fileOutputStream.write(data.toByteArray())
                 fileOutputStream.close() // Đóng file sau khi ghi xong

                 // Thông báo ra màn hình và đóng Activity
                 Toast.makeText(this,"Đăng kí thành công",Toast.LENGTH_LONG).show()
                 finish()
             }
        }
    }
}