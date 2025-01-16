package com.example.bai1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bai1.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Kiểm tra xem có dữ liệu đăng nhập đã lưu không khi mở lại ứng dụng
        val data = getData("saveInformation.txt")
        val emailFromFile =
            data.lines().find { it.startsWith("Email: ") }?.substringAfter("Email: ")?.trim()
        val passwordFromFile =
            data.lines().find { it.startsWith("Password: ") }?.substringAfter("Password: ")?.trim()


        // Nếu mà dữ liệu không trống tức là người dùng đã lưu thông tin từ trước đó rồi
        if (emailFromFile != null && passwordFromFile != null) {
            binding.etEmail.setText(emailFromFile)
            binding.etPassword.setText(passwordFromFile)
            binding.cbRemember.isChecked = true
        }

        // Xử lý khi nhấn vào đăng ký
        binding.tvDangki.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Xử lý sự kiện khi checkbox "Remember me" thay đổi
        // Khi mà bỏ tích thì không cần lưu thông tin đó nữa
        binding.cbRemember.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                // nếu mà bỏ tích thì xóa thông tin lưu trữ cho lần sau
                val file = File(filesDir, "saveInformation.txt")
                if (file.exists()) {
                    file.delete()  // Xóa file để không lưu lại thông tin cũ
                }
            }
        }

        binding.btLogin.setOnClickListener {
            if (binding.etEmail.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty()) {
                // Nếu mà trống thì thông báo
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                val data = getData("userInformation.txt")
                val emailFromFile = data.lines().find { it.startsWith("Email: ") }?.substringAfter("Email: ")?.trim()
                val passwordFromFile = data.lines().find { it.startsWith("Password: ") }?.substringAfter("Password: ")?.trim()


                // Kiểm tra nếu thông tin nhập vào trùng với thông tin trong file
                if (binding.etEmail.text.toString().trim() == emailFromFile && binding.etPassword.text.toString().trim() == passwordFromFile) {
                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_LONG).show()

                    // Lưu thông tin nếu checkbox được tích (Chỉ lưu thông tin khi mà thông tin đó là chính xác
                    if (binding.cbRemember.isChecked) {
                        saveUserInfo(binding.etEmail.text.toString(), binding.etPassword.text.toString())
                    }
                } else {
                    Toast.makeText(this, "Thông tin đăng nhập không chính xác", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    // Lưu vào file saveInformation file này dùng để lưu thông tin người dùng khi mà nhấn vào checkbox
    private fun saveUserInfo(email: String, password: String) {
        val fileOutputStream: FileOutputStream = openFileOutput("saveInformation.txt", MODE_PRIVATE)
        val userInfo = "Email: $email\nPassword: $password\n"
        fileOutputStream.write(userInfo.toByteArray())
        fileOutputStream.close()
    }


    private fun getData(fileName: String): String {
        try {
            val result = openFileInput(fileName)
                .bufferedReader() // Đọc file theo dòng
                .useLines { lines -> // Sử dụng 'useLines' để tự động quản lý việc đóng file
                    lines.fold("") { some, text -> // text là 1 dòng trong danh sách lines
                        "$some\n$text" // Kết hợp các dòng thành một chuỗi
                    }
                }
            return result

        } catch (e: FileNotFoundException) {
            // Nếu file không tồn tại, trả về chuỗi rỗng hoặc thông báo nào đó
            return ""
        }
    }
}