package com.eagletech.mydraw

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.eagletech.mydraw.DrawView.Companion.colors
import com.eagletech.mydraw.DrawView.Companion.brush
import com.eagletech.mydraw.DrawView.Companion.paths
import com.eagletech.mydraw.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        var path = Path()
        var paint = Paint()

    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setClicks()
    }

    private fun colorCurrent(color: Int){
        brush = color
        path = Path()
    }

    private fun setClicks() {
        binding.colorRed.setOnClickListener {
            Toast.makeText(this, "red", Toast.LENGTH_LONG).show()
            paint.color = Color.RED
            colorCurrent(paint.color)
        }
        binding.colorYellow.setOnClickListener {
            Toast.makeText(this, "yellow", Toast.LENGTH_LONG).show()
            paint.color = Color.YELLOW
            colorCurrent(paint.color)
        }
        binding.colorBlue.setOnClickListener {
            Toast.makeText(this, "blue", Toast.LENGTH_LONG).show()
            paint.color = Color.BLUE
            colorCurrent(paint.color)
        }
        binding.colorBlack.setOnClickListener {
            Toast.makeText(this, "black", Toast.LENGTH_LONG).show()
            paint.color = Color.BLACK
            colorCurrent(paint.color)
        }
        binding.colorWhite.setOnClickListener {
            paths.clear()
            colors.clear()
            path.reset()
        }

    }

}