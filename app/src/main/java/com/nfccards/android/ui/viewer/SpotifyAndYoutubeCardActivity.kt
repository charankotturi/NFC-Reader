package com.nfccards.android.ui.viewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.nfccards.android.R
import com.nfccards.android.databinding.ActivitySpotifyAndYoutubeCardBinding

enum class SingleLinkTypes{
    SPOTIFY,
    YOUTUBE,
    API;
}

class SpotifyAndYoutubeCardActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySpotifyAndYoutubeCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_spotify_and_youtube_card)

        intent?.let {
            val isReader = it.getBooleanExtra("isReader", true)

            binding.etLogoUrl.isVisible = !isReader
            binding.btnSubmit.isVisible = !isReader
            binding.textView.isVisible = !isReader
            binding.textView2.isVisible = !isReader

            val type = it.getStringExtra("SingleLinkTypes")

            when (type){
                SingleLinkTypes.SPOTIFY.toString() -> {}
                SingleLinkTypes.YOUTUBE.toString() -> {
                    binding.cardSingleLink.rlCard.setBackgroundColor(Color.Black.toArgb())
                    binding.cardSingleLink.imgCard.setImageResource(R.drawable.youtube_logo)
                }
                SingleLinkTypes.API.toString() -> {
                    binding.cardSingleLink.rlCard.setBackgroundColor(Color.Gray.toArgb())
                    binding.cardSingleLink.imgCard.setImageResource(R.drawable.api_logo)
                }
            }

            binding.btnSubmit.setOnClickListener {
                // TODO: SUBMIT LIST!
            }
        }

    }
}