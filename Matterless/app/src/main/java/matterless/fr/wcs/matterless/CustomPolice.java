package matterless.fr.wcs.matterless;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LruCache;
import android.widget.Button;


public class CustomPolice extends Button {

    //créé un cache de Typeface, pouvant contenir 12 fonts
    private static LruCache<String, Typeface> TYPEFACE_CACHE = new LruCache<String, Typeface>(12);

    public CustomPolice(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode()) {
            return;
        }

        //accède aux attributs ajoutés à cette CustomTextView
        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.CustomPolice);

        //récupère l'attribut "font"
        String fontName = styledAttrs.getString(R.styleable.CustomPolice_font);

        //permet au garbage collector de récupérer l'espace utilisé par ce TypedArray
        styledAttrs.recycle();

        //puis modifie la font de cet élément
        setTypeFace(fontName);
    }

    public void setTypeFace(String fontName) {
        if(fontName != null){
            try {
                //on regarde dans le cache si cette police est présente
                Typeface typeface = TYPEFACE_CACHE.get(fontName);

                //si non, on la charge à partir des assets
                if (typeface == null) {
                    typeface = Typeface.createFromAsset(getContext().getAssets(),"fonts/" + fontName);

                    //puis on la sauvegarde dans notre cache
                    TYPEFACE_CACHE.put(fontName, typeface);
                }

                //puis on l'utilise sur notre TextView
                setTypeface(typeface);
            } catch (Exception e) {
                Log.e("FONT", fontName + " not found", e);
            }
        }
    }

}