package com.app.avy.module

import com.app.avy.database.cabinet.Cabinet

data class HotkeyModule(
    var id: String,
    var key: String,
    var view: String,
    var isSave: Boolean,
    var listCabinet: ArrayList<Cabinet>
)