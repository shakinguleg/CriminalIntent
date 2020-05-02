package com.example.criminalintent;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks{
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        //手机模式
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePagerActivity
                    .newIntent(this, crime.getUUID());
            startActivity(intent);
        } else {
            //平板模式
            CrimeFragment fragment = CrimeFragment
                    .newInstance(crime.getUUID(), false);  //传给CrimeFragment打开的模式
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, fragment)
                    .commit();
        }
    }
}
