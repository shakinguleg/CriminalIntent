package com.example.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrimeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrimeListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    private int itemPositionRefresh;                                       //recyclerview刷新项

    public CrimeListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrimeListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrimeListFragment newInstance(String param1, String param2) {
        CrimeListFragment fragment = new CrimeListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = v.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    /**
     * 装载数据给adapter并设置给recyclerview
     */
    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mCrimeAdapter);
        } else {
            mCrimeAdapter.notifyDataSetChanged();
//            mCrimeAdapter.notifyItemChanged(itemPositionRefresh);             //挑战练习十
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;
        private ImageView mSolvedImageView;

        public CrimeHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    调用RecyclerView自身的动画
//                    mCrimeRecyclerView.getAdapter().notifyItemMoved(0, 5);
//                    return true;
//                }
//            });

            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.crime_title:
                    break;
                default:
                    Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getUUID());
                    startActivity(intent);
//                    itemPositionRefresh = getBindingAdapterPosition();
            }


        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(dateToChain(getActivity(), mCrime.getDate()));
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private final List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            //基于getItemViewType返回的viewType加载布局
//            View itemView = LayoutInflater
//                    .from(getActivity()).inflate(viewType, parent, false);

            View itemView = LayoutInflater
                    .from(getActivity()).inflate(R.layout.list_item_crime, parent, false);

            return new CrimeHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        /**根据相关数据的状态, 返回不同的视图
         * @param position
         * @return
         */
//        @Override
//        public int getItemViewType(int position) {
//            Crime crime = mCrimes.get(position);        //挑战练习八
//            if (crime.getRequiresPolice() == 0) {
//                return R.layout.list_item_crime;
//            }else{
//                return R.layout.list_item_is_crime;
//            }
//
//        }
    }

    /**挑战练习九 自定义时间格式
     * @param activity
     * @param mDate
     * @return 返回String类型的时间
     */
    public static String dateToChain(Context activity, Date mDate) {
        String date;
        //自动根据系统时间格式显示
        if (DateFormat.is24HourFormat(activity)) {
            date = DateFormat.format("yyyy年MM月dd日 EEEE kk:mm", mDate).toString();
        } else {
            date = DateFormat.format("yyyy年MM月dd日 EEEE hh:mm", mDate).toString();
        }
        return date;
    }
}
