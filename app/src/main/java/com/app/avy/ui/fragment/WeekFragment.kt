package com.app.avy.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Range
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.collection.LongSparseArray
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.avy.R
import com.app.avy.database.Event
import com.app.avy.listenner.WeekFragmentListener
import com.app.avy.listenner.WeeklyCalendar
import com.app.avy.module.EventWeeklyView
import com.app.avy.utils.Config
import com.app.avy.utils.Constant.WEEK_START_TIMESTAMP
import com.app.avy.utils.Formatter
import kotlinx.android.synthetic.main.fragment_week.view.*
import org.joda.time.DateTime

class WeekFragment : Fragment(), WeeklyCalendar {
    private val CLICK_DURATION_THRESHOLD = 150
    private val PLUS_FADEOUT_DELAY = 5000L

    var mListener: WeekFragmentListener? = null
    private var mWeekTimestamp = 0L
    private var mRowHeight = 0f
    private var minScrollY = -1
    private var maxScrollY = -1
    private var todayColumnIndex = -1
    private var clickStartTime = 0L
    private var primaryColor = 0
    private var lastHash = 0
    private var mWasDestroyed = false
    private var isFragmentVisible = false
    private var wasFragmentInit = false
    private var wasExtraHeightAdded = false
    private var dimPastEvents = true
    private var selectedGrid: View? = null
    private var events = ArrayList<Event>()
    private var allDayHolders = ArrayList<RelativeLayout>()
    private var allDayRows = ArrayList<HashSet<Int>>()
    private var eventTypeColors = LongSparseArray<Int>()
    private var eventTimeRanges = LinkedHashMap<String, ArrayList<EventWeeklyView>>()

    private lateinit var inflater: LayoutInflater
    private lateinit var mView: View
    //private lateinit var mScrollView: MyScrollView
    //private lateinit var mCalendar: WeeklyCalendarImpl
    private lateinit var mRes: Resources
    private lateinit var mConfig: Config

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRes = context!!.resources
        mConfig = Config(context!!)
        mWeekTimestamp = arguments!!.getLong(WEEK_START_TIMESTAMP)
        dimPastEvents = mConfig.dimPastEvents
        allDayRows.add(HashSet())
        // mCalendar = WeeklyCalendarImpl(this, context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.inflater = inflater
        mView = inflater.inflate(R.layout.fragment_week, container, false)

        wasFragmentInit = true
        return mView
    }

    override fun onResume() {
        super.onResume()

        setupDayLabels()
        updateCalendar()

    }

    override fun onPause() {
        super.onPause()
        wasExtraHeightAdded = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mWasDestroyed = true
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        isFragmentVisible = menuVisible
        if (isFragmentVisible && wasFragmentInit) {
            mListener?.updateHoursTopMargin(mView.week_top_holder.height)
        }
    }

    fun updateCalendar() {
        // mCalendar.updateWeeklyCalendar(mWeekTimestamp)
    }

    private fun setupDayLabels() {
        var curDay = Formatter.getDateTimeFromTS(mWeekTimestamp)
        val todayCode = Formatter.getDayCodeFromDateTime(DateTime())
        for (i in 0..6) {
            val dayCode = Formatter.getDayCodeFromDateTime(curDay)
            val dayLetters = mRes.getStringArray(R.array.week_day_letters).toMutableList() as ArrayList<String>
            val dayLetter = dayLetters[curDay.dayOfWeek - 1]

            mView.findViewById<TextView>(mRes.getIdentifier("week_day_label_$i", "id", context!!.packageName)).apply {
                text = "$dayLetter\n${curDay.dayOfMonth}"
                setTextColor(
                    if (todayCode == dayCode) ContextCompat.getColor(
                        context,
                        R.color.color_control
                    ) else ContextCompat.getColor(context, R.color.md_grey_white)
                )
                if (todayCode == dayCode) {
                    todayColumnIndex = i
                }
            }
            curDay = curDay.plusDays(1)
        }
    }


    private fun initGrid() {
        (0..6).map { getColumnWithId(it) }
            .forEachIndexed { _, layout ->
                layout.removeAllViews()
                layout.setOnTouchListener { _, motionEvent ->
                    //checkGridClick(motionEvent, index, layout)
                    true
                }
            }
    }


    override fun updateWeeklyCalendar(events: ArrayList<Event>) {
        val newHash = events.hashCode()
        if (newHash == lastHash || mWasDestroyed || context == null) {
            return
        }

        lastHash = newHash
        this.events = events

        activity!!.runOnUiThread {
            if (context != null && activity != null && isAdded) {
                //addEvents()
            }
        }
    }

    /* private fun addEvents() {
         initGrid()
         allDayHolders.clear()
         allDayRows.clear()
         eventTimeRanges.clear()
         allDayRows.add(HashSet())
         week_all_day_holder?.removeAllViews()

         addNewLine()

         val fullHeight = mRes.getDimension(R.dimen.weekly_view_events_height)
         val minuteHeight = fullHeight / (24 * 60)
         val minimalHeight = mRes.getDimension(R.dimen.weekly_view_minimal_event_height).toInt()
         val density = Math.round(context!!.resources.displayMetrics.density)

         var hadAllDayEvent = false
         val replaceDescription = mConfig.replaceDescription
         val sorted =
             events.sortedWith(compareBy<Event> { it.startTS }.thenBy { it.endTS }.thenBy { it.title }.thenBy { if (replaceDescription) it.location else it.description })
         for (event in sorted) {
             val startDateTime = Formatter.getDateTimeFromTS(event.startTS)
             val endDateTime = Formatter.getDateTimeFromTS(event.endTS)
             if (!event.getIsAllDay() && Formatter.getDayCodeFromDateTime(startDateTime) == Formatter.getDayCodeFromDateTime(
                     endDateTime
                 )
             ) {
                 val startMinutes = startDateTime.minuteOfDay
                 val duration = endDateTime.minuteOfDay - startMinutes
                 val range = Range(startMinutes, startMinutes + duration)
                 val eventWeekly = EventWeeklyView(event.id!!, range)

                 val dayCode = Formatter.getDayCodeFromDateTime(startDateTime)
                 if (!eventTimeRanges.containsKey(dayCode)) {
                     eventTimeRanges[dayCode] = ArrayList()
                 }

                 eventTimeRanges[dayCode]?.add(eventWeekly)
             }
         }

         for (event in sorted) {
             val startDateTime = Formatter.getDateTimeFromTS(event.startTS)
             val endDateTime = Formatter.getDateTimeFromTS(event.endTS)
             if (event.getIsAllDay() || Formatter.getDayCodeFromDateTime(startDateTime) != Formatter.getDayCodeFromDateTime(
                     endDateTime
                 )
             ) {
                 hadAllDayEvent = true
                 addAllDayEvent(event)
             } else {
                 val dayOfWeek = startDateTime.plusDays(if (mConfig.isSundayFirst) 1 else 0).dayOfWeek - 1
                 val layout = getColumnWithId(dayOfWeek)

                 val startMinutes = startDateTime.minuteOfDay
                 val duration = endDateTime.minuteOfDay - startMinutes
                 val range = Range(startMinutes, startMinutes + duration)

                 val dayCode = Formatter.getDayCodeFromDateTime(startDateTime)
                 var overlappingEvents = 0
                 var currentEventOverlapIndex = 0
                 var foundCurrentEvent = false

                 eventTimeRanges[dayCode]!!.forEachIndexed { index, eventWeeklyView ->
                     if (eventWeeklyView.range.touch(range)) {
                         overlappingEvents++

                         if (eventWeeklyView.id == event.id) {
                             foundCurrentEvent = true
                         }

                         if (!foundCurrentEvent) {
                             currentEventOverlapIndex++
                         }
                     }
                 }

                 (inflater.inflate(R.layout.week_event_marker, null, false) as TextView).apply {
                     var backgroundColor = eventTypeColors.get(event.eventType, primaryColor)
                     var textColor = backgroundColor.getContrastColor()
                     if (dimPastEvents && event.isPastEvent) {
                         backgroundColor = backgroundColor.adjustAlpha(LOW_ALPHA)
                         textColor = textColor.adjustAlpha(LOW_ALPHA)
                     }

                     background = ColorDrawable(backgroundColor)
                     setTextColor(textColor)
                     text = event.title
                     contentDescription = text
                     layout.addView(this)
                     y = startMinutes * minuteHeight
                     (layoutParams as RelativeLayout.LayoutParams).apply {
                         width = layout.width - 1
                         width /= Math.max(overlappingEvents, 1)
                         if (overlappingEvents > 1) {
                             x = width * currentEventOverlapIndex.toFloat()
                             if (currentEventOverlapIndex != 0) {
                                 x += density
                             }

                             width -= density
                             if (currentEventOverlapIndex + 1 != overlappingEvents) {
                                 if (currentEventOverlapIndex != 0) {
                                     width -= density
                                 }
                             }
                         }

                         minHeight =
                             if (event.startTS == event.endTS) minimalHeight else (duration * minuteHeight).toInt() - 1
                     }
                     setOnClickListener {
                         Intent(context, EventActivity::class.java).apply {
                             putExtra(EVENT_ID, event.id!!)
                             putExtra(EVENT_OCCURRENCE_TS, event.startTS)
                             startActivity(this)
                         }
                     }
                 }
             }
         }

         if (!hadAllDayEvent) {
             checkTopHolderHeight()
         }

         addCurrentTimeIndicator(minuteHeight)
     }*/

    /* private fun addNewLine() {
         val allDaysLine = inflater.inflate(R.layout.all_day_events_holder_line, null, false) as RelativeLayout
         week_all_day_holder.addView(allDaysLine)
         allDayHolders.add(allDaysLine)
     }*/

    /* private fun addCurrentTimeIndicator(minuteHeight: Float) {
         if (todayColumnIndex != -1) {
             val minutes = DateTime().minuteOfDay
             val todayColumn = getColumnWithId(todayColumnIndex)
             (inflater.inflate(R.layout.week_now_marker, null, false) as ImageView).apply {
                 applyColorFilter(primaryColor)
                 mView.week_events_holder.addView(this, 0)
                 val extraWidth = (todayColumn.width * 0.3).toInt()
                 val markerHeight = mRes.getDimension(R.dimen.weekly_view_now_height).toInt()
                 (layoutParams as RelativeLayout.LayoutParams).apply {
                     width = todayColumn.width + extraWidth
                     height = markerHeight
                 }
                 x = todayColumn.x - extraWidth / 2
                 y = minutes * minuteHeight - markerHeight / 2
             }
         }
     }*/

    /*private fun checkTopHolderHeight() {
        mView.week_top_holder.onGlobalLayout {
            if (isFragmentVisible && activity != null && !mWasDestroyed) {
                mListener?.updateHoursTopMargin(mView.week_top_holder.height)
            }
        }
    }*/

    /* private fun addAllDayEvent(event: Event) {
         (inflater.inflate(R.layout.week_all_day_event_marker, null, false) as TextView).apply {
             var backgroundColor = eventTypeColors.get(event.eventType, primaryColor)
             var textColor = backgroundColor.getContrastColor()
             if (dimPastEvents && event.isPastEvent) {
                 backgroundColor = backgroundColor.adjustAlpha(LOW_ALPHA)
                 textColor = textColor.adjustAlpha(LOW_ALPHA)
             }
             background = ColorDrawable(backgroundColor)

             setTextColor(textColor)
             text = event.title
             contentDescription = text

             val startDateTime = Formatter.getDateTimeFromTS(event.startTS)
             val endDateTime = Formatter.getDateTimeFromTS(event.endTS)

             val minTS = Math.max(startDateTime.seconds(), mWeekTimestamp)
             val maxTS = Math.min(endDateTime.seconds(), mWeekTimestamp + WEEK_SECONDS)

             // fix a visual glitch with all-day events or events lasting multiple days starting at midnight on monday, being shown the previous week too
             if (minTS == maxTS && (minTS - mWeekTimestamp == WEEK_SECONDS.toLong())) {
                 return
             }

             val daysCnt = Days.daysBetween(
                 Formatter.getDateTimeFromTS(minTS).toLocalDate(),
                 Formatter.getDateTimeFromTS(maxTS).toLocalDate()
             ).days
             val startDateTimeInWeek = Formatter.getDateTimeFromTS(minTS)
             val firstDayIndex = (startDateTimeInWeek.dayOfWeek - if (mConfig.isSundayFirst) 0 else 1) % 7

             var doesEventFit: Boolean
             val cnt = allDayRows.size - 1
             var wasEventHandled = false
             var drawAtLine = 0
             for (index in 0..cnt) {
                 doesEventFit = true
                 drawAtLine = index
                 val row = allDayRows[index]
                 for (i in firstDayIndex..firstDayIndex + daysCnt) {
                     if (row.contains(i)) {
                         doesEventFit = false
                     }
                 }

                 for (dayIndex in firstDayIndex..firstDayIndex + daysCnt) {
                     if (doesEventFit) {
                         row.add(dayIndex)
                         wasEventHandled = true
                     } else if (index == cnt) {
                         if (allDayRows.size == index + 1) {
                             allDayRows.add(HashSet())
                           //  addNewLine()
                             drawAtLine++
                             wasEventHandled = true
                         }
                         allDayRows.last().add(dayIndex)
                     }
                 }
                 if (wasEventHandled) {
                     break
                 }
             }

             allDayHolders[drawAtLine].addView(this)
             (layoutParams as RelativeLayout.LayoutParams).apply {
                 leftMargin = getColumnWithId(firstDayIndex).x.toInt()
                 bottomMargin = 1
                 width = getColumnWithId(Math.min(firstDayIndex + daysCnt, 6)).right - leftMargin - 1
             }

             calculateExtraHeight()

             setOnClickListener {
                 Intent(context, EventActivity::class.java).apply {
                     putExtra(EVENT_ID, event.id)
                     putExtra(EVENT_OCCURRENCE_TS, event.startTS)
                     startActivity(this)
                 }
             }
         }
     }*/

    /* private fun calculateExtraHeight() {
         mView.week_top_holder.onGlobalLayout {
             if (activity != null && !mWasDestroyed) {
                 if (isFragmentVisible) {
                     mListener?.updateHoursTopMargin(mView.week_top_holder.height)
                 }

                 if (!wasExtraHeightAdded) {
                     maxScrollY += mView.week_all_day_holder.height
                     wasExtraHeightAdded = true
                 }
             }
         }
     }*/

    private fun getColumnWithId(id: Int) =
        mView.findViewById<ViewGroup>(mRes.getIdentifier("week_column_$id", "id", context!!.packageName))

}
