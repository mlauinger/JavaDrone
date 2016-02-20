package de.mlauinger.studienarbeit.javadrone;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.text.DecimalFormat;

public class SliderPreference extends Preference implements OnSeekBarChangeListener {

    private final String TAG = getClass().getName();

    private static final int DEFAULT_VALUE = 10;
    private static final int MAX = 100;
    private static final int MIN = 0;
    private static final int INTERVAL = 5;

    public int mCurrentValue;
    private SeekBar mSeekBar;

    private TextView mStatusText;

    public SliderPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPreference(context, attrs);
    }

    public SliderPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPreference(context, attrs);
    }

    private void initPreference(Context context, AttributeSet attrs) {
        mSeekBar = new SeekBar(context, attrs);
        mSeekBar.setMax(MAX);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        RelativeLayout layout = null;
        try {
            LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (RelativeLayout) mInflater.inflate(R.layout.slider_preference, parent, false);
        } catch (Exception e) {
            Log.e(TAG, "Error creating seek bar preference", e);
        }
        return layout;
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        try {
            ViewParent oldContainer = mSeekBar.getParent();
            ViewGroup newContainer = (ViewGroup) view.findViewById(R.id.seekBarPrefBarContainer);
            if (oldContainer != newContainer) {
                if (oldContainer != null) {
                    ((ViewGroup) oldContainer).removeView(mSeekBar);
                }
                newContainer.removeAllViews();
                newContainer.addView(mSeekBar, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error binding view: " + ex.toString());
        }
        updateView(view);
    }

    protected void updateView(View view) {
        try {
            RelativeLayout layout = (RelativeLayout) view;
            mStatusText = (TextView) layout.findViewById(R.id.seekBarPrefValue);
            mStatusText.setText(formatForView(mCurrentValue));
            mStatusText.setMinimumWidth(30);
            mSeekBar.setProgress(mCurrentValue);
        } catch (Exception e) {
            Log.e(TAG, "Error updating seek bar preference", e);
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int newValue = progress;
        int mMaxValue = 100;
        if (newValue > mMaxValue)
            newValue = mMaxValue;
        else if (newValue < MIN)
            newValue = MIN;
        else if (newValue % INTERVAL != 0)
            newValue = Math.round(((float) newValue) / INTERVAL) * INTERVAL;
        if (!callChangeListener(newValue)) {
            seekBar.setProgress(mCurrentValue);
            return;
        }
        mCurrentValue = newValue;
        mStatusText.setText(formatForView(newValue));
        persistInt(newValue);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        notifyChanged();
    }

    @Override
    protected Object onGetDefaultValue(TypedArray ta, int index) {
        return ta.getInt(index, DEFAULT_VALUE);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {

        if (restoreValue) {
            mCurrentValue = getPersistedInt(mCurrentValue);
        } else {
            int temp = 0;
            try {
                temp = (Integer) defaultValue;
            } catch (Exception ex) {
                Log.e(TAG, "Invalid default value: " + defaultValue.toString());
            }

            persistInt(temp);
            mCurrentValue = temp;
        }
    }

    private String formatForView(int input) {
        DecimalFormat formatter = new DecimalFormat("###.##");
        return formatter.format(input * 0.01f);
    }

}