package com.hanibalg.yeneservice.adaptors;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.hanibalg.yeneservice.R;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    //Arrays
    public int[] slider_img = {
            R.drawable.icon_one,
            R.drawable.icon_two,
            R.drawable.icon_three
    };
    public String[] slider_header = {
            "EAT",
            "DRINK",
            "SLEEP"
    };

    public String[] slider_desc = {
            "We have to create three XML layouts for three slide screens. You can make more or less layout screen according to your requirement. In the layout, we " +
                    "add one image and two textview for title and description. Alternatively,",
            "Three slide screens. You can make more or less layout screen according to your requirement. In the layout, we " +
                    "add one image and two textview for title and description. Alternatively,",
            "You can make more or less layout screen according to your requirement. In the layout, we " +
                    "add one image and two textview for title and description. Alternatively,"
    };

    @Override
    public int getCount() {
        return slider_header.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        layoutInflater = context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout,container,false);

        ImageView sliderImageView = view.findViewById(R.id.sliderImg);
        TextView sliderTitleView = view.findViewById(R.id.name);
        TextView sliderDescView = view.findViewById(R.id.description);

        sliderImageView.setImageResource(slider_img[position]);
        sliderTitleView.setText(slider_header[position]);
        sliderDescView.setText(slider_desc[position]);
//        Animation aniFade = AnimationUtils.loadAnimation(context,R.anim.fade_in);
//        container.startAnimation(aniFade);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
