package com.eagletech.mydraw

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.eagletech.mydraw.DrawView.Companion.colorList
import com.eagletech.mydraw.DrawView.Companion.currenrBrush
import com.eagletech.mydraw.DrawView.Companion.pathList
import com.eagletech.mydraw.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        var path = Path()
        var paintBrush = Paint()

    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClicks()
    }

    private fun colorCurrent(color: Int){
        currenrBrush = color
        path = Path()
    }

    private fun setClicks() {
        binding.colorRed.setOnClickListener {
            Toast.makeText(this, "red", Toast.LENGTH_LONG).show()
            paintBrush.color = Color.RED
            colorCurrent(paintBrush.color)
        }
        binding.colorYellow.setOnClickListener {
            Toast.makeText(this, "yellow", Toast.LENGTH_LONG).show()
            paintBrush.color = Color.YELLOW
            colorCurrent(paintBrush.color)
        }
        binding.colorBlue.setOnClickListener {
            Toast.makeText(this, "blue", Toast.LENGTH_LONG).show()
            paintBrush.color = Color.BLUE
            colorCurrent(paintBrush.color)
        }
        binding.colorBlack.setOnClickListener {
            Toast.makeText(this, "black", Toast.LENGTH_LONG).show()
            paintBrush.color = Color.BLACK
            colorCurrent(paintBrush.color)
        }
        binding.colorWhite.setOnClickListener {
            pathList.clear()
            colorList.clear()
            path.reset()
        }

    }

}