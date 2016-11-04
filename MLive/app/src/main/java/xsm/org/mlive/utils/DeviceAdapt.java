package xsm.org.mlive.utils;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by xsm on 16-11-4.
 */

public class DeviceAdapt {
    public static float dp2px(Context context, int dp) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= 17) {
            DisplayManager displayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
            displayManager.getDisplay(0).getMetrics(outMetrics);
        } else {
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(outMetrics);
        }
        float px = dp * outMetrics.density;
        return px;
    }
}
