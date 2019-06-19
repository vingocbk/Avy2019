package com.app.avy.module

import android.graphics.Path

class SVGItem(
    var numImgs: Int,
    var pathData: ArrayList<String>,
    var pathDataList: ArrayList<Path>,
    var viewportWidth: Float,
    var viewportHeight: Float
)