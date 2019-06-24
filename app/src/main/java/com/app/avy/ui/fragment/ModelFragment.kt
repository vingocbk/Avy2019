package com.app.avy.ui.fragment

import android.annotation.TargetApi
import android.graphics.Color
import android.graphics.PixelFormat
import android.net.Uri
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper.getMainLooper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.avy.ui.view.ExampleSceneLoader
import com.app.avy.ui.view.ModelSurfaceView
import com.app.avy.ui.view.SceneLoader
import org.andresoviedo.util.android.ContentUtils
import android.opengl.GLSurfaceView.RENDERMODE_WHEN_DIRTY
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class ModelFragment : Fragment() {

    private val REQUEST_CODE_LOAD_TEXTURE = 1000
    private val FULLSCREEN_DELAY = 10000

    /**
     * Type of model if file name has no extension (provided though content provider)
     */
    private var paramType: Int = -1
    /**
     * The file to load. Passed as input parameter
     */
    private var paramUri: Uri? = null
    /**
     * Enter into Android Immersive mode so the renderer is full screen or not
     */
    private var immersiveMode = true
    /**
     * Background GL clear color. Default is light gray
     */
    private val backgroundColor = floatArrayOf(0.07f, 0.07f, 0.07f, 1.0f)

    private var gLView: ModelSurfaceView? = null

    private var scene: SceneLoader? = null

    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gLView = ModelSurfaceView(this)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return gLView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ContentUtils.provideAssets(activity)

        paramUri = Uri.parse("assets://" + context!!.packageName + "/" + "models/ToyPlane.obj")


        handler = Handler(getMainLooper())

        // Create our 3D sceneario
        if (paramUri == null) {
            scene = ExampleSceneLoader(this)
        } else {
            scene = SceneLoader(this)
        }
        scene!!.toggleLighting()

        scene!!.init()

        // TODO: Alert user when there is no multitouch support (2 fingers). He won't be able to rotate or zoom
        ContentUtils.printTouchCapabilities(context!!.packageManager)

        setupOnSystemVisibilityChangeListener()
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun setupOnSystemVisibilityChangeListener() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return
        }
        activity!!.window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            // Note that system bars will only be "visible" if none of the
            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                // The system bars are visible. Make any desired
                hideSystemUIDelayed()
            }
        }
    }

    private fun hideSystemUIDelayed() {
        if (!this.immersiveMode) {
            return
        }
        handler!!.removeCallbacksAndMessages(null)
        handler!!.postDelayed(Runnable { this.hideSystemUI() }, FULLSCREEN_DELAY.toLong())

    }

    private fun hideSystemUI() {
        if (!this.immersiveMode) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //   hideSystemUIKitKat()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            hideSystemUIJellyBean()
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun hideSystemUIKitKat() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        val decorView = activity!!.window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar

                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar

                or View.SYSTEM_UI_FLAG_IMMERSIVE)
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun hideSystemUIJellyBean() {
        val decorView = activity!!.window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LOW_PROFILE)
    }

    fun getParamUri(): Uri {
        return paramUri!!
    }

    fun getParamType(): Int {
        return paramType
    }

    fun getBackgroundColor(): FloatArray {
        return backgroundColor
    }

    fun getScene(): SceneLoader {
        return scene!!
    }

    fun getGLView(): ModelSurfaceView {
        return gLView!!
    }

}