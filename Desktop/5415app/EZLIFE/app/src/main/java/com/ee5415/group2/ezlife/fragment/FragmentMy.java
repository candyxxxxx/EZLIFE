package com.ee5415.group2.ezlife.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ee5415.group2.ezlife.R;


public class FragmentMy extends Fragment {

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_my, null);
        }
        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null)
            parent.removeView(v);
        return v;
    }
}
