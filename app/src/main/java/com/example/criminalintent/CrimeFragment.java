package com.example.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Date;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrimeFragment extends Fragment implements DatePickerFragment.Callbacks{
    private static final String ARG_CRIME_ID = "crime_id";
    public static final String DIALOG_DATE = "DialogDate";
    public static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final String DIALOG_TIME = "DialogTime";
    private static final String ARG_MODE = "mode";

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private UUID crimeId;
    private Button mTimeButton;
    private FragmentManager fragmentManager;
    private boolean mode;
    private DatePickerFragment datePickerFragment;
    private Handler mHandler;

    public CrimeFragment() {
        // Required empty public constructor
    }


    public static CrimeFragment newInstance(UUID uuid, boolean isPhoneMode) {
        CrimeFragment fragment = new CrimeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, uuid);
        args.putSerializable(ARG_MODE, isPhoneMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
            mode = (boolean) getArguments().getSerializable(ARG_MODE);
        }

        fragmentManager = getFragmentManager();

        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = v.findViewById(R.id.crime_date);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarMode(mCrime);
            }
        });

        mTimeButton = (Button) v.findViewById(R.id.crime_time);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(fragmentManager, DIALOG_TIME);
            }
        });

        updateDayTime();
        mSolvedCheckBox = v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) intent
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDayTime();
        } else if (requestCode == REQUEST_TIME) {
            Date date = (Date) intent.
                    getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setDate(date);
            updateDayTime();
        }
    }

    private void updateDayTime() {
        mDateButton.setText(DateUtils.dateToChinese(getActivity(), mCrime.getDate()));
        mTimeButton.setText(DateUtils.timeToChinese(getActivity(), mCrime.getDate()));
    }

    public void calendarMode(Crime crime){
        if (mode){
            Intent intent = DatePickerActivity
                    .newIntent(getActivity(), crime.getUUID());
            startActivityForResult(intent, REQUEST_DATE);
        }else {
            datePickerFragment = DatePickerFragment.newInstance(crime.getDate());
            datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
            datePickerFragment.show(fragmentManager, DIALOG_DATE);
        }
    }

    @Override
    public void removeCalendar() {
        fragmentManager.beginTransaction().remove(datePickerFragment).commit();
    }
}
