package com.app.avy.snowboysupport

import ai.kitt.snowboy.AppResCopy
import ai.kitt.snowboy.MsgEnum
import ai.kitt.snowboy.audio.AudioDataSaver
import ai.kitt.snowboy.audio.PlaybackThread
import ai.kitt.snowboy.audio.RecordingThread
import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.os.Handler
import android.os.Message
import android.speech.SpeechRecognizer
import java.io.*

class SnowboySupport(var context: Context, listenner: OnSpeechListenner) {
    private var recordingThread: RecordingThread? = null
    private var playbackThread: PlaybackThread? = null
    var mContext: Context
    private var preVolume = -1
    private var mListener: OnSpeechListenner? = null
    var message: String = " "

    init {
        @SuppressLint("HandlerLeak")
        var handle = object : Handler() {
            override fun handleMessage(msg: Message) {
                val message = MsgEnum.getMsgEnum(msg.what)
                when (message) {
                    MsgEnum.MSG_ACTIVE -> {
                        //TODO handle speech
                        stopRecording()
                        mListener?.let {
                            it.onActive()
                        }
                    }
                    MsgEnum.MSG_INFO -> {
                    }
                    MsgEnum.MSG_VAD_SPEECH -> {
                    }
                    MsgEnum.MSG_VAD_NOSPEECH -> {
                    }
                    MsgEnum.MSG_ERROR -> {
                    }
                    else -> super.handleMessage(msg)
                }
            }
        }



        mContext = context
        mListener = listenner
        setProperVolume()
        AppResCopy.copyResFromAssetsToSD(mContext)
        recordingThread = RecordingThread(handle, AudioDataSaver())
        playbackThread = PlaybackThread()
        startRecording()
    }

    fun setMaxVolume() {
        val audioManager = mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        preVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }

    fun setProperVolume() {
        val audioManager = mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        preVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val properVolume = (maxVolume.toFloat() * 0.8).toInt()
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, preVolume, 0)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }

    fun restoreVolume() {
        if (preVolume >= 0) {
            val audioManager = mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, preVolume, 0)
            val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        }
    }

    fun startRecording() {
        recordingThread!!.startRecording()
    }

    fun stopRecording() {
        recordingThread!!.stopRecording()
    }

    fun sleep() {
        try {
            Thread.sleep(500)
        } catch (e: Exception) {
        }
    }

    fun getErrorText(errorCode: Int): String {
        when (errorCode) {
            SpeechRecognizer.ERROR_AUDIO -> message = "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> message = "Client side error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> message = "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> message = "Không có kết nối. Xin thử lại"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> message = "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> message = "không tìm thấy kết quả nào. Xin thử lại"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> message = "RecognitionService busy"
            SpeechRecognizer.ERROR_SERVER -> message = "error from server"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Không có giọng nói nào. Xin thử lại"
            else -> message = "Didn't understand, please try again."
        }
        return message
    }

    interface OnSpeechListenner {
        fun onActive()
    }


    fun readData(fileName: String): String {
        try {
            val `in` = mContext.openFileInput(fileName)
            val br = BufferedReader(InputStreamReader(`in`))
            val buffer = StringBuffer()
            var line = ""
            while ((br.readLine()) != null) {
                buffer.append(br.readLine())
            }
            return buffer.toString()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return ""
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }

    }

    fun saveData(fileName: String, data: String) {
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = mContext.openFileOutput(fileName, Context.MODE_PRIVATE)
            fileOutputStream!!.write(data.toByteArray())
            fileOutputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}