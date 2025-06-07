package com.example.cardify.ner

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class NERProcessor(context: Context, modelPath: String) {
    private val interpreter: Interpreter = Interpreter(loadModelFile(context, modelPath))

    private fun loadModelFile(context: Context, path: String): MappedByteBuffer {
        context.assets.openFd(path).use { fd ->
            FileInputStream(fd.fileDescriptor).use { input ->
                val channel = input.channel
                return channel.map(FileChannel.MapMode.READ_ONLY, fd.startOffset, fd.declaredLength)
            }
        }
    }

    fun run(input: Any, output: Any) {
        interpreter.run(input, output)
    }

    fun close() {
        interpreter.close()
    }
}
