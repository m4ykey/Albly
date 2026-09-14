package com.m4ykey.album.presentation.detail

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.graphics.scale

class CoverViewModel : ViewModel() {

    private val _colors = MutableStateFlow<List<Int>>(emptyList())
    val colors = _colors.asStateFlow()

    fun extractColors(bitmap : Bitmap) {
        viewModelScope.launch {
            val colors = withContext(Dispatchers.Default) {
                val smallBitmap = bitmap.scale(100, 100)

                extractColorsFromImage(smallBitmap)
            }

            _colors.value = colors
        }
    }

    private fun extractColorsFromImage(bitmap: Bitmap) : List<Int> {
        val safeBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false)
        val palette = Palette.from(safeBitmap).generate()
        return palette.swatches.map { it.rgb }
    }

}