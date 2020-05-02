package com.example.criminalintent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class DatePickerActivity extends SingleFragmentActivity {
    private static final String EXTRA_CRIME_ID = "com.example.criminalintent.crime_id";

    public static Intent newIntent(Context context, UUID uuid){
        Intent intent = new Intent(context, DatePickerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, uuid);
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        Date date = CrimeLab.get(this).getCrime(crimeId).getDate();
        return DatePickerFragment.newInstance(date);
    }
}
