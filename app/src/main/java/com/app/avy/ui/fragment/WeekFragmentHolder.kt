package com.app.avy.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.app.avy.BaseFragment
import com.app.avy.R
import com.app.avy.listenner.WeekFragmentListener
import com.app.avy.ui.adapter.MyWeekPagerAdapter
import com.app.avy.ui.view.MyViewPager
import com.app.avy.utils.Constant
import com.app.avy.utils.Constant.WEEK_SECONDS
import com.app.avy.utils.Constant.WEEK_START_DATE_TIME
import com.app.avy.utils.Formatter
import com.app.avy.utils.seconds
import kotlinx.android.synthetic.main.fragmemt_week_holder.*
import org.joda.time.DateTime

class WeekFragmentHolder : BaseFragment(), WeekFragmentListener {

    lateinit var mTvDateTime: AppCompatTextView
    private val PREFILLED_WEEKS = 151
    private var viewPager: MyViewPager? = null
    private var defaultWeeklyPage = 0
    private var thisWeekTS = 0L
    private var currentWeekTS = 0L
    private var isGoToTodayVisible = false
    private var weekScrollY = 0

    companion object {
        fun getInstance(dateTimeString: String): WeekFragmentHolder {
            var df = WeekFragmentHolder()
            var bundle = Bundle()
            bundle.putString(WEEK_START_DATE_TIME, dateTimeString)
            df.arguments = bundle
            return df
        }
    }

    override fun scrollTo(y: Int) {
        weekScrollY = y
    }

    override fun updateHoursTopMargin(margin: Int) {

    }

    override fun getCurrScrollY(): Int {
        return weekScrollY
    }

    override fun getID() = R.layout.fragmemt_week_holder

    override fun onViewReady() {

        mTvDateTime = tv_date_time
        val dateTimeString = arguments?.getString(Constant.WEEK_START_DATE_TIME) ?: return
        currentWeekTS = (DateTime.parse(dateTimeString) ?: DateTime()).seconds()
        thisWeekTS = currentWeekTS
        viewPager = week_view_view_pager
        viewPager!!.id = (System.currentTimeMillis() % 100000).toInt()
        setupFragment()
    }

    private fun setupFragment() {
        val weekTSs = getWeekTimestamps(currentWeekTS)
        val weeklyAdapter = MyWeekPagerAdapter(activity!!.supportFragmentManager, weekTSs, this)

        val hourDateTime = DateTime().withDate(2000, 1, 1).withTime(0, 0, 0, 0)
        for (i in 1..23) {
            val formattedHours = Formatter.getHours(context!!, hourDateTime.withHourOfDay(i))
            (layoutInflater.inflate(R.layout.weekly_view_hour_textview, null, false) as TextView).apply {
                text = formattedHours
                setTextColor(ContextCompat.getColor(context, R.color.color_control))
                //  weekHolder!!.week_view_hours_holder.addView(this)
            }
        }

        defaultWeeklyPage = weekTSs.size / 2
        viewPager!!.apply {
            adapter = weeklyAdapter
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

                override fun onPageSelected(position: Int) {
                    currentWeekTS = weekTSs[position]

                    /*val shouldGoToTodayBeVisible = shouldGoToTodayBeVisible()
                    if (isGoToTodayVisible != shouldGoToTodayBeVisible) {
                        (activity as? MainActivity)?.toggleGoToTodayVisibility(shouldGoToTodayBeVisible)
                        isGoToTodayVisible = shouldGoToTodayBeVisible
                    }*/
                    setupWeeklyActionbarTitle(weekTSs[position])

                    Log.e("ActionbarTitle", "-----> $weekTSs[position]")


                }
            })
            currentItem = defaultWeeklyPage
        }

        setupWeeklyActionbarTitle(currentWeekTS)
    }

    private fun getWeekTimestamps(targetSeconds: Long): List<Long> {
        val weekTSs = ArrayList<Long>(PREFILLED_WEEKS)
        val dateTime = Formatter.getDateTimeFromTS(targetSeconds)
        var currentWeek = dateTime.minusWeeks(PREFILLED_WEEKS / 2)
        for (i in 0 until PREFILLED_WEEKS) {
            weekTSs.add(currentWeek.seconds())
            currentWeek = currentWeek.plusWeeks(1)
        }
        return weekTSs
    }

    @SuppressLint("SetTextI18n")
    private fun setupWeeklyActionbarTitle(timestamp: Long) {
        val startDateTime = Formatter.getDateTimeFromTS(timestamp)
        var curDay = Formatter.getDateTimeFromTS(timestamp)
        val endDateTime = Formatter.getDateTimeFromTS(timestamp + WEEK_SECONDS)
        val startMonthName = Formatter.getMonthName(context!!, startDateTime.dayOfWeek)
        if (startDateTime.monthOfYear == endDateTime.monthOfYear) {
            var newTitle = startMonthName
            if (startDateTime.year != DateTime().year) {
                newTitle += " - ${startDateTime.year}"
            }
            Log.e("updateActionBarTitle", "1-----> $newTitle")

            mTvDateTime.text =
                "$newTitle ${startDateTime.dayOfMonth} - $newTitle  ${startDateTime.dayOfMonth + 6}, ${startDateTime.year} "

        } else {
            val endMonthName = Formatter.getMonthName(context!!, endDateTime.dayOfWeek)

            Log.e("updateActionBarTitle", "2-----> ${curDay.dayOfWeek}  ${curDay.dayOfWeek} $endMonthName")

            mTvDateTime.text =
                "$endMonthName ${startDateTime.dayOfMonth} - $endMonthName  ${startDateTime.dayOfMonth + 6}, ${startDateTime.year} "
        }
        Log.e("updateActionBarTitle", "3-----> ${startDateTime.dayOfMonth} ${startDateTime.dayOfMonth + 6} ")

    }

}