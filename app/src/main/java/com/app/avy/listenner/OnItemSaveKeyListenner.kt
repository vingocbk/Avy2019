package com.app.avy.listenner

import com.app.avy.module.HotkeyModule

interface OnItemSaveKeyListenner {
    fun onItemSaveKey(isSave: Boolean, listKey: List<HotkeyModule>)
}