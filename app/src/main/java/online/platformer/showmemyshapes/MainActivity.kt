package online.platformer.showmemyshapes

import android.app.Activity
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle

class MainActivity : Activity() {

    private lateinit var glView: GLSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        glView = MyGLSurfaceView(this)

        setContentView(glView)
    }

    inner class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {
        private val renderer: MyGLRenderer

        init {
            setEGLContextClientVersion(2)

            renderer = MyGLRenderer()

            setRenderer(renderer)

            //renderMode = RENDERMODE_WHEN_DIRTY
        }
    }
}
