package online.platformer.showmemyshapes

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

const val COORDS_PER_VERTEX = 3
var triangleCoords = floatArrayOf(
    0.0f, 0.622008459f, 0.0f,
    -0.5f, -0.311004243f, 0.0f,
    0.5f, -0.311004243f, 0.0f
)

class Triangle {
    private val colour = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

    private val vertexShaderCode =
        "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            "   gl_Position = uMVPMatrix * vPosition;" +
            "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "   gl_FragColor = vColor;" +
            "}"

    private var program: Int

    init {
        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)

            GLES20.glAttachShader(it, fragmentShader)

            GLES20.glLinkProgram(it)
        }
    }

    private var vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(triangleCoords.size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(triangleCoords)

                position(0)
            }
        }

    private var positionHandle = 0
    private var colourHandle = 0
    private var modelViewProjectionMatrixHandle = 0

    private val vertexCount = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride = COORDS_PER_VERTEX * 4

    fun draw(modelViewProjectionMatrix: FloatArray) {
        GLES20.glUseProgram(program)

        positionHandle = GLES20.glGetAttribLocation(program, "vPosition").also {
            GLES20.glEnableVertexAttribArray(it)

            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            colourHandle = GLES20.glGetUniformLocation(program, "vColor").also { colourHandle ->
                GLES20.glUniform4fv(colourHandle, 1, colour, 0)
            }

            modelViewProjectionMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix").also {
                GLES20.glUniformMatrix4fv(modelViewProjectionMatrixHandle, 1, false, modelViewProjectionMatrix, 0)
            }

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

            GLES20.glDisableVertexAttribArray(it)
        }
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }
}
