package com.example.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class CrimePagerActivity extends AppCompatActivity {

    private static final String EXTRA_CRIME_ID =
            "com.example.criminalintent.crime_id";
    private static final String TAG = "CrimePagerActivity";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private Button btJumpToFirst;
    private Button btJumpToLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mCrimes = CrimeLab.get(this).getCrimes();

        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            @NonNull
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getUUID(), true);
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                buttonEnable();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btJumpToFirst = findViewById(R.id.bt_jump_to_first);
        btJumpToFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
                buttonEnable();
            }
        });

        btJumpToLast = findViewById(R.id.bt_jump_to_last);
        btJumpToLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mCrimes.size() - 1);
                buttonEnable();
            }
        });

        findCurrentItem(crimeId);
    }

    /**
     * 当为第一条和最后一条时分别禁用按钮
     */
    private void buttonEnable() {
        int currentItem = mViewPager.getCurrentItem();
        btJumpToFirst.setEnabled(false);
        btJumpToLast.setEnabled(false);

        if (currentItem != 0) {
            btJumpToFirst.setEnabled(true);
        }

        if (currentItem != mCrimes.size() - 1) {
            btJumpToLast.setEnabled(true);
        }
        Log.i(TAG, "count: " + mViewPager.getAdapter().getCount());
    }

    private void findCurrentItem(UUID crimeId) {
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getUUID().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    public static Intent newIntent(Context packgeContext, UUID crimeId) {
        Intent intent = new Intent(packgeContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViewPager.clearOnPageChangeListeners();
    }
}
