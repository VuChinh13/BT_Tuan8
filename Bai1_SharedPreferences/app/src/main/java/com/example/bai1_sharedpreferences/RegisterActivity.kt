package com.example.bai1_sharedpreferences

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bai1_sharedpreferences.databinding.ActivityRegisterBinding
import java.io.FileOutputStream

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btDangki.setOnClickListener {
            //Kiểm tra xem các ô nhập liệu có trống hay không
            if (binding.etEmail.text.isEmpty() || binding.etMatkhau.text.isEmpty()){
                // Trường hợp nếu mà trống
                Toast.makeText(this,"Hãy nhập đầy đủ thông tin!!", Toast.LENGTH_SHORT).show()
            }else{
                // Khởi tạo giá trị
                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                // Lưu thông tin của người sử dụng vào trong SharePreferences
                editor.putString("username",binding.etEmail.text.toString().trim())
                editor.putString("password",binding.etMatkhau.text.toString().trim())
                // Lưu bất đồng bộ
                editor.apply()
                Toast.makeText(this,"Đăng kí thành công",Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}