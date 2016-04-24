package com.ee5415.group2.ezlife.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ee5415.group2.ezlife.R;
import com.ee5415.group2.ezlife.UserSettingsActivity;
import com.ee5415.group2.ezlife.functions.currency_converter.HKDConverter;
import com.ee5415.group2.ezlife.functions.translate.TranslateActivity;
import com.ee5415.group2.ezlife.functions.weather.WeatherActivity;

public class FragmentHome extends Fragment {

	private  View v;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		if (v == null) {
			v = inflater.inflate(R.layout.fragment_home, null);
		}
		ViewGroup parent = (ViewGroup) v.getParent();

		if (parent != null)
			parent.removeView(v);

		v.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getContext(), UserSettingsActivity.class);
				startActivityForResult(i, 1);
			}
		});

		v.findViewById(R.id.home_currency).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i_c = new Intent(getContext(), HKDConverter.class);
				startActivity(i_c);
			}
		});

		v.findViewById(R.id.home_translation).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i_t = new Intent(getContext(), TranslateActivity.class);
				startActivity(i_t);
			}
		});

		v.findViewById(R.id.home_weather).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i_w = new Intent(getContext(), WeatherActivity.class);
				startActivity(i_w);
			}
		});

		v.findViewById(R.id.home_isbn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i_i = new Intent(getContext(), HKDConverter.class);
				startActivity(i_i);
			}
		});

		displayButtons();
		return v;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 1:
				displayButtons();
				break;
		}
	}

	public void displayButtons(){
		Button currency = (Button) v.findViewById(R.id.home_currency);
		Button weather = (Button) v.findViewById(R.id.home_weather);
		Button translation = (Button) v.findViewById(R.id.home_translation);
		Button isbn = (Button) v.findViewById(R.id.home_isbn);

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(getContext());

		if(!sharedPrefs.getBoolean("Currency", false))
			currency.setVisibility(View.GONE);
		else
			currency.setVisibility(View.VISIBLE);

		if(!sharedPrefs.getBoolean("Weather", false))
			weather.setVisibility(View.GONE);
		else
			weather.setVisibility(View.VISIBLE);

		if(!sharedPrefs.getBoolean("Translation", false))
			translation.setVisibility(View.GONE);
		else
			translation.setVisibility(View.VISIBLE);

		if(!sharedPrefs.getBoolean("ISBN", false))
			isbn.setVisibility(View.GONE);
		else
			isbn.setVisibility(View.VISIBLE);
	}
}

