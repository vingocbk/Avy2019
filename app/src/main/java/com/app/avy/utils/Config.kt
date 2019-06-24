package com.app.avy.utils

import android.Manifest.permission.VIBRATE
import android.content.Context
import android.media.AudioManager
import com.app.avy.R
import com.app.avy.utils.Constant.CALDAV_SYNC
import com.app.avy.utils.Constant.CALDAV_SYNCED_CALENDAR_IDS
import com.app.avy.utils.Constant.DAILY_VIEW
import com.app.avy.utils.Constant.DAY_MINUTES
import com.app.avy.utils.Constant.DEFAULT_DURATION
import com.app.avy.utils.Constant.DEFAULT_EVENT_TYPE_ID
import com.app.avy.utils.Constant.DEFAULT_REMINDER_1
import com.app.avy.utils.Constant.DEFAULT_REMINDER_2
import com.app.avy.utils.Constant.DEFAULT_REMINDER_3
import com.app.avy.utils.Constant.DEFAULT_START_TIME
import com.app.avy.utils.Constant.DIM_PAST_EVENTS
import com.app.avy.utils.Constant.DISPLAY_EVENT_TYPES
import com.app.avy.utils.Constant.DISPLAY_PAST_EVENTS
import com.app.avy.utils.Constant.END_WEEKLY_AT
import com.app.avy.utils.Constant.FONT_SIZE
import com.app.avy.utils.Constant.LAST_EVENT_REMINDER_MINUTES
import com.app.avy.utils.Constant.LAST_EVENT_REMINDER_MINUTES_2
import com.app.avy.utils.Constant.LAST_EVENT_REMINDER_MINUTES_3
import com.app.avy.utils.Constant.LAST_REMINDER_CHANNEL_ID
import com.app.avy.utils.Constant.LAST_SOUND_URI
import com.app.avy.utils.Constant.LAST_USED_CALDAV_CALENDAR
import com.app.avy.utils.Constant.LAST_USED_LOCAL_EVENT_TYPE_ID
import com.app.avy.utils.Constant.LAST_VIBRATE_ON_REMINDER
import com.app.avy.utils.Constant.LIST_WIDGET_VIEW_TO_OPEN
import com.app.avy.utils.Constant.LOOP_REMINDERS
import com.app.avy.utils.Constant.MONTHLY_VIEW
import com.app.avy.utils.Constant.PULL_TO_REFRESH
import com.app.avy.utils.Constant.REMINDER_AUDIO_STREAM
import com.app.avy.utils.Constant.REMINDER_SOUND_TITLE
import com.app.avy.utils.Constant.REMINDER_SOUND_URI
import com.app.avy.utils.Constant.REPLACE_DESCRIPTION
import com.app.avy.utils.Constant.SHOW_GRID
import com.app.avy.utils.Constant.START_WEEKLY_AT
import com.app.avy.utils.Constant.USE_PREVIOUS_EVENT_REMINDERS
import com.app.avy.utils.Constant.VIEW
import com.app.avy.utils.Constant.WEEK_NUMBERS
import java.util.*

class Config(context: Context) : BaseConfig(context) {
    companion object {
        fun newInstance(context: Context) = Config(context)
    }

    var showWeekNumbers: Boolean
        get() = prefs.getBoolean(WEEK_NUMBERS, false)
        set(showWeekNumbers) = prefs.edit().putBoolean(WEEK_NUMBERS, showWeekNumbers).apply()

    var startWeeklyAt: Int
        get() = prefs.getInt(START_WEEKLY_AT, 7)
        set(startWeeklyAt) = prefs.edit().putInt(START_WEEKLY_AT, startWeeklyAt).apply()

    var endWeeklyAt: Int
        get() = prefs.getInt(END_WEEKLY_AT, 23)
        set(endWeeklyAt) = prefs.edit().putInt(END_WEEKLY_AT, endWeeklyAt).apply()

    var vibrateOnReminder: Boolean
        get() = prefs.getBoolean(VIBRATE, false)
        set(vibrate) = prefs.edit().putBoolean(VIBRATE, vibrate).apply()


    var lastSoundUri: String
        get() = prefs.getString(LAST_SOUND_URI, "")
        set(lastSoundUri) = prefs.edit().putString(LAST_SOUND_URI, lastSoundUri).apply()

    var lastReminderChannel: Long
        get() = prefs.getLong(LAST_REMINDER_CHANNEL_ID, 0L)
        set(lastReminderChannel) = prefs.edit().putLong(LAST_REMINDER_CHANNEL_ID, lastReminderChannel).apply()

    var storedView: Int
        get() = prefs.getInt(VIEW, MONTHLY_VIEW)
        set(view) = prefs.edit().putInt(VIEW, view).apply()

    var lastEventReminderMinutes1: Int
        get() = prefs.getInt(LAST_EVENT_REMINDER_MINUTES, 10)
        set(lastEventReminderMinutes) = prefs.edit().putInt(LAST_EVENT_REMINDER_MINUTES, lastEventReminderMinutes).apply()


    var displayPastEvents: Int
        get() = prefs.getInt(DISPLAY_PAST_EVENTS, DAY_MINUTES)
        set(displayPastEvents) = prefs.edit().putInt(DISPLAY_PAST_EVENTS, displayPastEvents).apply()

    var displayEventTypes: Set<String>
        get() = prefs.getStringSet(DISPLAY_EVENT_TYPES, HashSet<String>())
        set(displayEventTypes) = prefs.edit().remove(DISPLAY_EVENT_TYPES).putStringSet(DISPLAY_EVENT_TYPES, displayEventTypes).apply()



    var listWidgetViewToOpen: Int
        get() = prefs.getInt(LIST_WIDGET_VIEW_TO_OPEN, DAILY_VIEW)
        set(viewToOpenFromListWidget) = prefs.edit().putInt(LIST_WIDGET_VIEW_TO_OPEN, viewToOpenFromListWidget).apply()


    var caldavSyncedCalendarIds: String
        get() = prefs.getString(CALDAV_SYNCED_CALENDAR_IDS, "")
        set(calendarIDs) = prefs.edit().putString(CALDAV_SYNCED_CALENDAR_IDS, calendarIDs).apply()

    var lastUsedCaldavCalendarId: Int
        get() = prefs.getInt(LAST_USED_CALDAV_CALENDAR, getSyncedCalendarIdsAsList().first().toInt())
        set(calendarId) = prefs.edit().putInt(LAST_USED_CALDAV_CALENDAR, calendarId).apply()


    var reminderAudioStream: Int
        get() = prefs.getInt(REMINDER_AUDIO_STREAM, AudioManager.STREAM_ALARM)
        set(reminderAudioStream) = prefs.edit().putInt(REMINDER_AUDIO_STREAM, reminderAudioStream).apply()

    var replaceDescription: Boolean
        get() = prefs.getBoolean(REPLACE_DESCRIPTION, false)
        set(replaceDescription) = prefs.edit().putBoolean(REPLACE_DESCRIPTION, replaceDescription).apply()

    var showGrid: Boolean
        get() = prefs.getBoolean(SHOW_GRID, false)
        set(showGrid) = prefs.edit().putBoolean(SHOW_GRID, showGrid).apply()

    var loopReminders: Boolean
        get() = prefs.getBoolean(LOOP_REMINDERS, false)
        set(loopReminders) = prefs.edit().putBoolean(LOOP_REMINDERS, loopReminders).apply()

    var dimPastEvents: Boolean
        get() = prefs.getBoolean(DIM_PAST_EVENTS, true)
        set(dimPastEvents) = prefs.edit().putBoolean(DIM_PAST_EVENTS, dimPastEvents).apply()

    fun getSyncedCalendarIdsAsList() = caldavSyncedCalendarIds.split(",").filter { it.trim().isNotEmpty() }.map { Integer.parseInt(it) }.toMutableList() as ArrayList<Int>

    fun getDisplayEventTypessAsList() = displayEventTypes.map { it.toLong() }.toMutableList() as ArrayList<Long>

    fun addDisplayEventType(type: String) {
        addDisplayEventTypes(HashSet<String>(Arrays.asList(type)))
    }

    private fun addDisplayEventTypes(types: Set<String>) {
        val currDisplayEventTypes = HashSet<String>(displayEventTypes)
        currDisplayEventTypes.addAll(types)
        displayEventTypes = currDisplayEventTypes
    }

    fun removeDisplayEventTypes(types: Set<String>) {
        val currDisplayEventTypes = HashSet<String>(displayEventTypes)
        currDisplayEventTypes.removeAll(types)
        displayEventTypes = currDisplayEventTypes
    }


    var usePreviousEventReminders: Boolean
        get() = prefs.getBoolean(USE_PREVIOUS_EVENT_REMINDERS, true)
        set(usePreviousEventReminders) = prefs.edit().putBoolean(USE_PREVIOUS_EVENT_REMINDERS, usePreviousEventReminders).apply()

    var defaultReminder1: Int
        get() = prefs.getInt(DEFAULT_REMINDER_1, 10)
        set(defaultReminder1) = prefs.edit().putInt(DEFAULT_REMINDER_1, defaultReminder1).apply()


    var pullToRefresh: Boolean
        get() = prefs.getBoolean(PULL_TO_REFRESH, false)
        set(pullToRefresh) = prefs.edit().putBoolean(PULL_TO_REFRESH, pullToRefresh).apply()


    var defaultStartTime: Int
        get() = prefs.getInt(DEFAULT_START_TIME, -1)
        set(defaultStartTime) = prefs.edit().putInt(DEFAULT_START_TIME, defaultStartTime).apply()

    var defaultDuration: Int
        get() = prefs.getInt(DEFAULT_DURATION, 0)
        set(defaultDuration) = prefs.edit().putInt(DEFAULT_DURATION, defaultDuration).apply()

    var defaultEventTypeId: Long
        get() = prefs.getLong(DEFAULT_EVENT_TYPE_ID, -1L)
        set(defaultEventTypeId) = prefs.edit().putLong(DEFAULT_EVENT_TYPE_ID, defaultEventTypeId).apply()
}
