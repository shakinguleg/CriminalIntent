package com.example.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "com.example.criminalintent.date";
    private static final String ARG_DATE = "date";
    private static final String TAG = "DatePickFragment";
    private DatePicker mDatePicker;
    private Date selectDate;
    private Button btOk;
    private Callbacks mCallback = null;

    public DatePickerFragment() {
        // Required empty public constructor
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
//        Date date = (Date) getArguments().getSerializable(ARG_DATE);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        mDatePicker = (DatePicker) view.findViewById(R.id.dialog_date_picker);
//        mDatePicker.init(year, month, day, null);
//
//        return new AlertDialog.Builder(getActivity())
//                .setView(view)
//                .setTitle(R.string.date_picker_title)
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        int year = mDatePicker.getYear();
//                        int month = mDatePicker.getMonth();
//                        int day = mDatePicker.getDayOfMonth();
//                        Date date = new GregorianCalendar(year, month, day).getTime();
//                        sendResulet(Activity.RESULT_OK,date);
//                    }
//                })
//                .create();
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_date, container, false);
        assert getArguments() != null;
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        selectDate = date;
        mDatePicker = view.findViewById(R.id.dialog_date_picker);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
            }
        });

        btOk = view.findViewById(R.id.bt_ok);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResulet(Activity.RESULT_OK, selectDate);
            }
        });
        return view;
    }

    public void sendResulet(int resultCode, Date date) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        if (getTargetFragment() == null && getActivity() != null) {
            getActivity().setResult(resultCode, intent);
            getActivity().finish();
        } else if (getTargetFragment() != null && getActivity() != null) {
            getTargetFragment()
                    .onActivityResult(getTargetRequestCode(), resultCode, intent);
            if (mCallback != null) {
                mCallback.removeCalendar();
            }
        }
    }

    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public interface Callbacks {
        void removeCalendar();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getTargetFragment() != null) {
            mCallback = (Callbacks) getTargetFragment();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
