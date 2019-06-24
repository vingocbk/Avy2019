package com.app.avy.listenner

import com.app.avy.database.Event

interface WeeklyCalendar {
    fun updateWeeklyCalendar(events: ArrayList<Event>)
}
