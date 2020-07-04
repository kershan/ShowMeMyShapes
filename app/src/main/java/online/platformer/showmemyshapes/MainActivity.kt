package online.platformer.showmemyshapes

import android.app.Activity
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.MotionEvent

private const val TOUCH_SCALE_FACTOR: Float = 180.0f / 320f

class MainActivity : Activity() {
    private lateinit var glView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        glView = MyGLSurfaceView(this)

        setContentView(glView)
    }

    inner class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {
        private val renderer: MyGLRenderer

        private var previousX = 0f
        private var previousY = 0f

        init {
            setEGLContextClientVersion(2)

            renderer = MyGLRenderer()

            setRenderer(renderer)

            renderMode = RENDERMODE_WHEN_DIRTY
        }

        override fun onTouchEvent(event: MotionEvent?): Boolean {
            val x = event?.x ?: 0f
            val y = event?.y ?: 0f

            when (event?.action) {
                MotionEvent.ACTION_MOVE -> {
                    var dx = x - previousX
                    var dy = x - previousY

                    if (y > height / 2) dx *= -1

                    if (x > width / 2) dy *= -1

                    renderer.angle = (dx + dy) * TOUCH_SCALE_FACTOR
                    requestRender()
                }
            }

            previousX = x
            previousY = y
            return true
        }
    }
}
