package cpm.com.gskmtorange.fonts;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by yadavendras on 11-01-2017.
 */

public class FontManager {

    public static final String ROOT = "fonts/",
            FONTAWESOME = ROOT + "fontawesome-webfont.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }
}
