package com.app.avy.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSessionManager;
import android.util.Log;
import com.app.avy.mediacontroller.tasks.FindMediaAppsTask;
import com.app.avy.mediacontroller.tasks.FindMediaSessionAppsTask;
import com.app.avy.module.MediaAppDetails;

import java.util.List;

public class MediaSessionListener {

    private MediaAppDetailListener mListener;

    public MediaSessionListener(Context context, MediaAppDetailListener listener) {
        this.context = context;
        mListener = listener;
    }


    private Context context;
    private final FindMediaAppsTask.AppListUpdatedCallback mSessionAppsUpdated =
            mediaAppDetails -> {
                if (mediaAppDetails.isEmpty()) {
                    // Show an error if no apps were found.
                    return;
                }

                if (mListener != null) {
                    mListener.mediaAppDetail(mediaAppDetails);
                }

                for (int i = 0; i < mediaAppDetails.size(); i++) {
                    Log.e("mBrowserAppsUpdated", "-----" + mediaAppDetails.get(i).appName + "-----" + mediaAppDetails.get(i).sessionToken);
                }
            };

    final MediaSessionManager.OnActiveSessionsChangedListener mSessionsChangedListener = list -> {
        assert list != null;
        mSessionAppsUpdated.onAppListUpdated(MediaAppControllerUtils.getMediaAppsFromControllers(list, context.getPackageManager(), context.getResources()));
    };

    private MediaSessionManager mMediaSessionManager;

    public void onCreate() {
        mMediaSessionManager = (MediaSessionManager) context.getSystemService(Context.MEDIA_SESSION_SERVICE);
    }

    public void onStart(Context context) {
        if (!NotificationListener.isEnabled(context)) {
            context.startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            return;
        }

        Log.e("MediaSessionListener", "------>" + !NotificationListener.isEnabled(context) + "-----" + mMediaSessionManager);

        if (mMediaSessionManager == null) {
            return;
        }
        ComponentName listenerComponent =
                new ComponentName(context, NotificationListener.class);
        mMediaSessionManager.addOnActiveSessionsChangedListener(
                mSessionsChangedListener, listenerComponent);
        new FindMediaSessionAppsTask(mMediaSessionManager, listenerComponent,
                context.getPackageManager(), context.getResources(), mSessionAppsUpdated).execute();
    }

    public void onStop() {
        if (mMediaSessionManager == null) {
            return;
        }
        mMediaSessionManager.removeOnActiveSessionsChangedListener(mSessionsChangedListener);
    }


    public interface MediaAppDetailListener {
        void mediaAppDetail(List<? extends MediaAppDetails> mediaAppDetails);
    }
}
