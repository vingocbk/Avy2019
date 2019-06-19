package bsoft.hoavt.photoproject.lib_textcollage.helpers.blur;

import android.graphics.Color;

/**
 * Created by vutha on 7/20/2017.
 */

/**
 * Copyright (C) 2017 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class BlurFactor {
    public static final int DEFAULT_RADIUS = 25;
    public static final int DEFAULT_SAMPLING = 1;

    public int width;
    public int height;
    public int radius = DEFAULT_RADIUS;
    public int sampling = DEFAULT_SAMPLING;
    public int color = Color.TRANSPARENT;
}