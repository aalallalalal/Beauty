package com.dup.changeskin.attr;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dup.changeskin.ResourceManager;
import com.dup.changeskin.SkinManager;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by zhy on 15/9/28.
 */
public enum SkinAttrType {
    BACKGROUND("background") {
        @Override
        public void apply(View view, String resName) {
            Drawable drawable = getResourceManager().getDrawableByName(resName);
            if (drawable != null) {
                view.setBackgroundDrawable(drawable);
            } else {
                try {
                    int color = getResourceManager().getColor(resName);
                    view.setBackgroundColor(color);
                } catch (Resources.NotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }, COLOR("textColor") {
        @Override
        public void apply(View view, String resName) {
            ColorStateList colorList = getResourceManager().getColorStateList(resName);
            if (colorList == null) return;
            ((TextView) view).setTextColor(colorList);
        }
    }, SRC("src") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof ImageView) {
                Drawable drawable = getResourceManager().getDrawableByName(resName);
                if (drawable == null) return;
                ((ImageView) view).setImageDrawable(drawable);
            }

        }
    }, DIVIDER("divider") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof ListView) {
                Drawable divider = getResourceManager().getDrawableByName(resName);
                if (divider == null) return;
                ((ListView) view).setDivider(divider);
            }
        }
    }, CIV_BORDER("civ_border_color") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof CircleImageView) {
                ColorStateList colorList = getResourceManager().getColorStateList(resName);
                if (colorList == null) return;
                ((CircleImageView) view).setBorderColor(colorList.getDefaultColor());
            }
        }
    }, COLLAPSING_TOOLBAR_CONTENT_SCRIM("contentScrim") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof CollapsingToolbarLayout) {
                Drawable drawable = getResourceManager().getDrawableByName(resName);
                if (drawable != null) {
                    ((CollapsingToolbarLayout) view).setContentScrim(drawable);
                } else {
                    try {
                        int color = getResourceManager().getColor(resName);
                        ((CollapsingToolbarLayout) view).setContentScrimColor(color);
                    } catch (Resources.NotFoundException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        }
    }, COLLAPSING_TOOLBAR_STATUSBAR_SCRIM("statusBarScrim") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof CollapsingToolbarLayout) {
                Drawable drawable = getResourceManager().getDrawableByName(resName);
                if (drawable != null) {
                    ((CollapsingToolbarLayout) view).setStatusBarScrim(drawable);
                } else {
                    try {
                        int color = getResourceManager().getColor(resName);
                        ((CollapsingToolbarLayout) view).setStatusBarScrimColor(color);
                    } catch (Resources.NotFoundException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        }
    };

    String attrType;

    SkinAttrType(String attrType) {
        this.attrType = attrType;
    }

    public String getAttrType() {
        return attrType;
    }


    public abstract void apply(View view, String resName);

    public ResourceManager getResourceManager() {
        return SkinManager.getInstance().getResourceManager();
    }

}
