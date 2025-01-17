package com.example.bai1_sharedpreferences

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bai1_sharedpreferences.databinding.ActivityMainBinding
import java.util.logging.LogRecord


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor
    private var username = ""
    private var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        this.editor = sharedPreferences.edit()

        // Kiểm tra xem có dữ liệu đăng nhập đã lưu không khi mở lại ứng dụng
        val sharedPre = getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPre.edit()
        val emailFromSharedPre = sharedPre.getString("saveUsername","")
        val passwordFromSharedPre = sharedPre.getString("savePassword","")


        // Nếu mà dữ liệu không trống tức là người dùng đã lưu thông tin từ trước đó rồi
        if (emailFromSharedPre!!.isNotEmpty() && passwordFromSharedPre!!.isNotEmpty()) {
            binding.etEmail.setText(emailFromSharedPre)
            binding.etPassword.setText(passwordFromSharedPre)
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
                // Xóa dữ liệu của một key cụ thể
                editor.remove("saveUsername")
                editor.remove("savePassword")
                editor.apply()
            }
        }

        binding.btLogin.setOnClickListener {
            if (binding.etEmail.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty()) {
                // Nếu mà trống thì thông báo
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                this.username = sharedPreferences.getString("username","").toString()
                this.password = sharedPreferences.getString("password","").toString()


                // Kiểm tra nếu thông tin nhập vào trùng với thông tin trong file
                if (binding.etEmail.text.toString().trim() == username && binding.etPassword.text.toString().trim() == password) {
                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_LONG).show()

                    // Lưu thông tin nếu checkbox được tích (Chỉ lưu thông tin khi mà thông tin đó là chính xác)
                    if (binding.cbRemember.isChecked) {
                        val sharedPre = getPreferences(Context.MODE_PRIVATE)
                        val editor = sharedPre.edit()
                        editor.putString("saveUsername",username)
                        editor.putString("savePassword",password)
                        editor.apply()
                    }
                } else {
                    Toast.makeText(this, "Thông tin đăng nhập không chính xác", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}