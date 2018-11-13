package oupson.apngcreator

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.SeekBar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import oupson.apng.ApngAnimator
import android.widget.Toast
import org.jetbrains.anko.doAsync
import oupson.apng.APNGDisassembler
import oupson.apng.Apng
import oupson.apng.Loader
import oupson.apngcreator.R.id.imageView
import java.io.File
import java.net.URL


class MainActivity : AppCompatActivity() {
    lateinit var animator: ApngAnimator

    val imageUrl = "https://metagif.files.wordpress.com/2015/01/bugbuckbunny.png"
    //val imageUrl = "https://raw.githubusercontent.com/tinify/iMessage-Panda-sticker/master/StickerPackExtension/Stickers.xcstickers/Sticker%20Pack.stickerpack/panda.sticker/panda.png"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        animator = ApngAnimator(this).loadInto(imageView).apply {
            load(imageUrl)
            onLoaded {
                setOnAnimationLoopListener {
                    Log.e("app-test", "onLoop")
                }
            }
        }


        doAsync {
            Loader().load(applicationContext, URL(imageUrl)).apply {
                val a = APNGDisassembler(this).apng
                a.optimise(100, 75)
                File(File(Environment.getExternalStorageDirectory(), "Documents"), "apng.png").writeBytes(a.toByteArray())
            }
        }

        this.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            internal var progress = 0

            // When Progress value changed.
            override fun onProgressChanged(seekBar: SeekBar, progressValue: Int, fromUser: Boolean) {
                progress = progressValue
            }

            // Notification that the user has started a touch gesture.
            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            // Notification that the user has finished a touch gesture
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Log.e("TAG" , (seekBar.progress.toFloat() / 100f).toString())
                animator.speed = (seekBar.progress.toFloat() / 100f)

            }
        })

        Picasso.get().load(imageUrl).into(imageView2)

        play.setOnClickListener {
            animator.play()
        }

        pause.setOnClickListener {
            animator.pause()
        }
    }
}
