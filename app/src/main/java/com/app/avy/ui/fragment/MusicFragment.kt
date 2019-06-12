package com.app.avy.ui.fragment

import com.app.avy.BaseFragment
import com.app.avy.R
import kotlinx.android.synthetic.main.fragment_music.*
import android.content.Context
import android.content.Intent
import android.graphics.Matrix
import android.view.KeyEvent
import android.media.AudioManager
import android.os.Build
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.RatingCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatTextView
import com.app.avy.mediacontroller.tasks.FindMediaAppsTask
import com.app.avy.mediacontroller.tasks.FindMediaBrowserAppsTask
import com.app.avy.module.MediaAppDetails
import com.app.avy.utils.*
import java.util.*
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatImageView
import kotlinx.android.synthetic.main.fragment_music.seek_bar_volume
import kotlinx.android.synthetic.main.fragment_setting_cabinet.*


class MusicFragment : BaseFragment(), MediaSessionListener.MediaAppDetailListener, SeekBar.OnSeekBarChangeListener,
    View.OnClickListener {

    val TAG = MusicFragment::class.java.simpleName

    lateinit var mMediaAppDetails: MediaAppDetails
    var mBrowser: MediaBrowserCompat? = null
    var mController: MediaControllerCompat? = null

    var mTvtile: AppCompatTextView? = null
    var mTvartist: AppCompatTextView? = null
    var imgAlbumArt: AppCompatImageView? = null
    var mSeekbarVolume: SeekBar? = null
    private var audioManager: AudioManager? = null
    var mImgBack: AppCompatImageView? = null
    var mImgPauseAndPlay: AppCompatImageView? = null
    var mImgNext: AppCompatImageView? = null
    var isPlay: Boolean = false
    lateinit var animRotate: Animation


    private val mBrowserAppsUpdated = object : FindMediaAppsTask.AppListUpdatedCallback {
        override fun onAppListUpdated(
            @NonNull mediaAppEntries: List<MediaAppDetails>
        ) {
            if (mediaAppEntries.isEmpty()) {
                // Show an error if no apps were found.
                return
            }
            for (i in mediaAppEntries.indices) {
                Log.e(
                    "MusicFragment",
                    "sessionToken " + mediaAppEntries[i].appName + "--" + mediaAppEntries[i].sessionToken
                )
            }
        }
    }

    val mCallback = object : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(playbackState: PlaybackStateCompat?) {
            onUpdate()
            if (playbackState != null) {
                //showActions(playbackState.actions)
                // mCustomControlsAdapter.setActions(mController, playbackState.customActions)
            }
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            onUpdate()
        }

        override fun onSessionDestroyed() {

        }

        private fun onUpdate() {
            val mediaInfoStr = fetchMediaInfo()

        }
    }


    override fun getID() = R.layout.fragment_music
    // MediaSessionManager is only supported on API 21+, so all related logic is bundled in a
    // separate inner class that's only instantiated if the device is running L or later.
    var mMediaSessionListener: MediaSessionListener? = null

    override fun onViewReady() {
        mTvtile = tv_tile
        mTvartist = tv_artist
        imgAlbumArt = img_albumArt
        mSeekbarVolume = seek_bar_volume
        mImgBack = img_back
        mImgPauseAndPlay = img_play_pause
        mImgNext = img_next

        animRotate = AnimationUtils.loadAnimation(context, R.anim.anim)

        audioManager = context!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        mSeekbarVolume?.max = audioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        mSeekbarVolume?.progress = audioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC)
        mSeekbarVolume?.setOnSeekBarChangeListener(this)

        if (audioManager!!.isMusicActive) {
            isPlay = true
            imgAlbumArt?.startAnimation(animRotate)
            mImgPauseAndPlay?.setImageResource(R.drawable.ic_play)
        } else {
            imgAlbumArt?.clearAnimation()
            mImgPauseAndPlay?.setImageResource(R.drawable.ic_pause)
        }


        val mAudioManager = context!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val mMediaSessionListener = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            MediaSessionListener(context, this) else null

        Log.e("mMediaSessionListener", "------> $mMediaSessionListener")

        if (mMediaSessionListener != null) {
            mMediaSessionListener.onCreate()
            mMediaSessionListener.onStart(context!!)
        }

        mImgNext?.setOnClickListener(this)
        mImgPauseAndPlay?.setOnClickListener(this)
        mImgBack?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_back -> {
                mController?.transportControls?.skipToPrevious()
            }
            R.id.img_play_pause -> {
                if (isPlay) {
                    mController?.transportControls?.pause()
                    mImgPauseAndPlay?.setImageResource(R.drawable.ic_pause)
                    imgAlbumArt?.clearAnimation()
                } else {
                    mController?.transportControls?.play()
                    mImgPauseAndPlay?.setImageResource(R.drawable.ic_play)
                    imgAlbumArt?.startAnimation(animRotate)
                }
                isPlay = !isPlay
            }
            R.id.img_next -> {
                mController?.transportControls?.skipToNext()
            }
        }
    }

    override fun mediaAppDetail(mediaAppDetails: MutableList<out MediaAppDetails>?) {
        Log.e(TAG, "------->" + mediaAppDetails)
        mediaAppDetails?.let {
            mMediaAppDetails = it[0]
            setupMedia()
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        audioManager!!.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            seekBar!!.progress, 0
        )
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStart() {
        super.onStart()
        FindMediaBrowserAppsTask(context!!, mBrowserAppsUpdated).execute()

    }

    override fun onStop() {
        if (mMediaSessionListener != null) {
            mMediaSessionListener!!.onStop()
        }
        super.onStop()
    }

    override fun onDestroy() {
        mController?.let {
            it.unregisterCallback(mCallback)
            mController = null
        }

        if (mBrowser != null && mBrowser!!.isConnected) {
            mBrowser!!.disconnect()
        }
        mBrowser = null

        super.onDestroy()
    }

    private fun setupMedia() {
        // Should now have a viable details.. connect to browser and service as needed.
        if (mMediaAppDetails!!.componentName != null) {
            mBrowser = MediaBrowserCompat(
                context, mMediaAppDetails!!.componentName,
                object : MediaBrowserCompat.ConnectionCallback() {
                    override fun onConnected() {
                        setupMediaController()
                    }

                    override fun onConnectionSuspended() {
                        //TODO(rasekh): shut down browser.
                    }

                    override fun onConnectionFailed() {
                    }

                }, null
            )
            mBrowser!!.connect()
        } else if (mMediaAppDetails.sessionToken != null) {
            setupMediaController()
        }
    }

    private fun setupMediaController() {
        try {
            var token = mMediaAppDetails!!.sessionToken
            if (token == null) {
                token = mBrowser!!.sessionToken
            }
            mController = MediaControllerCompat(context, token)
            mController!!.registerCallback(mCallback)

            // Force update on connect.
            mCallback.onPlaybackStateChanged(mController?.playbackState)
            mCallback.onMetadataChanged(mController?.metadata)

            Log.d(TAG, "MediaControllerCompat created")
        } catch (remoteException: RemoteException) {
            Log.e(TAG, "Failed to create MediaController from session token", remoteException)
        }
    }

    @NonNull
    private fun fetchMediaInfo() {

        val playbackState = mController?.playbackState
        if (playbackState == null) {
            Log.e(TAG, "Failed to update media info, null PlaybackState.")
            return
        }

        val mediaInfos = HashMap<String, String>()
        mediaInfos[getString(R.string.info_state_string)] = playbackStateToName(playbackState.state)

        val mediaMetadata = mController?.metadata
        if (mediaMetadata != null) {
            addMediaInfo(
                mediaInfos,
                getString(R.string.info_title_string),
                mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE)
            )
            addMediaInfo(
                mediaInfos,
                getString(R.string.info_artist_string),
                mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST)
            )
            addMediaInfo(
                mediaInfos,
                getString(R.string.info_album_string),
                mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ALBUM)
            )

            mTvtile?.isSelected = true
            mTvtile?.text = mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE).plus("    ")
                .plus(mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE)).plus("    ")
            mTvartist?.text = mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST)
            // tv_album.text = mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ALBUM)

            val art = mediaMetadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART)
            if (art != null) {
                imgAlbumArt?.setImageBitmap(art)
            } else {
                imgAlbumArt?.setImageResource(R.drawable.ic_music)
            }
            // Prefer user rating, but fall back to global rating if available.
            var rating: RatingCompat? = mediaMetadata.getRating(MediaMetadataCompat.METADATA_KEY_USER_RATING)
            if (rating == null) {
                rating = mediaMetadata.getRating(MediaMetadataCompat.METADATA_KEY_RATING)
            }
        } else {
            imgAlbumArt?.setImageResource(R.drawable.ic_music)
        }

        val actions = playbackState.actions

        if (actions and PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH != 0L) {
            addMediaInfo(mediaInfos, "ACTION_PREPARE_FROM_SEARCH", "Supported")
        }
        if (actions and PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH != 0L) {
            addMediaInfo(mediaInfos, "ACTION_PLAY_FROM_SEARCH", "Supported")
        }

        if (actions and PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID != 0L) {
            addMediaInfo(mediaInfos, "ACTION_PREPARE_FROM_MEDIA_ID", "Supported")
        }
        if (actions and PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID != 0L) {
            addMediaInfo(mediaInfos, "ACTION_PLAY_FROM_MEDIA_ID", "Supported")
        }

        if (actions and PlaybackStateCompat.ACTION_PREPARE_FROM_URI != 0L) {
            addMediaInfo(mediaInfos, "ACTION_PREPARE_FROM_URI", "Supported")
        }
        if (actions and PlaybackStateCompat.ACTION_PLAY_FROM_URI != 0L) {
            addMediaInfo(mediaInfos, "ACTION_PLAY_FROM_URI", "Supported")
        }

        if (actions and PlaybackStateCompat.ACTION_PREPARE != 0L) {
            addMediaInfo(mediaInfos, "ACTION_PREPARE", "Supported")
        }
        if (actions and PlaybackStateCompat.ACTION_PLAY != 0L) {
            addMediaInfo(mediaInfos, "ACTION_PLAY", "Supported")
        }

        val stringBuilder = StringBuilder()

        val sortedKeys = ArrayList(mediaInfos.keys)
        Collections.sort(sortedKeys, KeyComparator())

        /*  for (key in sortedKeys) {
              stringBuilder.append(key).append(" = ").append(mediaInfos[key]).append('\n')
          }
          return stringBuilder.toString()*/
    }

    private fun addMediaInfo(mediaInfos: MutableMap<String, String>, key: String, value: String) {
        if (!TextUtils.isEmpty(value)) {
            mediaInfos[key] = value
        }
    }

    private class KeyComparator : Comparator<String> {
        private val mCapKeys = HashSet<String>()

        override fun compare(leftSide: String, rightSide: String): Int {
            val leftCaps = isAllCaps(leftSide)
            val rightCaps = isAllCaps(rightSide)

            if (leftCaps && rightCaps) {
                return leftSide.compareTo(rightSide)
            } else if (leftCaps) {
                return 1
            } else if (rightCaps) {
                return -1
            }
            return leftSide.compareTo(rightSide)
        }

        private fun isAllCaps(@NonNull stringToCheck: String): Boolean {
            if (mCapKeys.contains(stringToCheck)) {
                return true
            } else if (stringToCheck == stringToCheck.toUpperCase(Locale.US)) {
                mCapKeys.add(stringToCheck)
                return true
            }
            return false
        }
    }

    private fun playbackStateToName(playbackState: Int): String {
        when (playbackState) {
            PlaybackStateCompat.STATE_NONE -> return "STATE_NONE"
            PlaybackStateCompat.STATE_STOPPED -> return "STATE_STOPPED"
            PlaybackStateCompat.STATE_PAUSED -> return "STATE_PAUSED"
            PlaybackStateCompat.STATE_PLAYING -> return "STATE_PLAYING"
            PlaybackStateCompat.STATE_FAST_FORWARDING -> return "STATE_FAST_FORWARDING"
            PlaybackStateCompat.STATE_REWINDING -> return "STATE_REWINDING"
            PlaybackStateCompat.STATE_BUFFERING -> return "STATE_BUFFERING"
            PlaybackStateCompat.STATE_ERROR -> return "STATE_ERROR"
            PlaybackStateCompat.STATE_CONNECTING -> return "STATE_CONNECTING"
            PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS -> return "STATE_SKIPPING_TO_PREVIOUS"
            PlaybackStateCompat.STATE_SKIPPING_TO_NEXT -> return "STATE_SKIPPING_TO_NEXT"
            PlaybackStateCompat.STATE_SKIPPING_TO_QUEUE_ITEM -> return "STATE_SKIPPING_TO_QUEUE_ITEM"
            else -> return "!Unknown State!"
        }
    }
}