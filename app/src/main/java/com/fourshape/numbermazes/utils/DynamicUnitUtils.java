package com.fourshape.numbermazes.utils;

import android.content.res.Resources;
import android.util.TypedValue;

public class DynamicUnitUtils {

    public static int convertDpToPixels(float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, Resources.getSystem().getDisplayMetrics()));
    }

    public static int convertSpToPixels(float sp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, Resources.getSystem().getDisplayMetrics()));
    }

    public static int convertDpToSp(float dp) {
        return Math.round(convertDpToPixels(dp) / (float) convertSpToPixels(dp));
    }


}
