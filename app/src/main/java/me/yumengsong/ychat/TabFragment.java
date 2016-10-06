package me.yumengsong.ychat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by yumeng on 9/26/16.
 */

public class TabFragment extends Fragment {
    public static final String TITLE = "title";
    private String mTitle = "Default";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
        }

        TextView textView = new TextView(getActivity());
        textView.setTextSize(30);
        textView.setBackgroundColor(Color.parseColor("#ffffffff"));
        textView.setText(mTitle);
        textView.setGravity(Gravity.CENTER);


        return textView;
    }
}
