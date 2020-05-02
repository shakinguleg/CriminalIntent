package com.example.criminalintent;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    private int itemPositionRefresh;                                       //recyclerview刷新项
    private Callbacks mCallbacks;

    public CrimeListFragment() {
        // Required empty public constructor
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
                    mCallbacks.onCrimeSelected(mCrime);
//                    itemPositionRefresh = getBindingAdapterPosition();
            }


        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(DateUtils.allToChinese(getActivity(), mCrime.getDate()));
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

    /**
     * 由于在onAttach中直接强制类型转换,
     * 托管activity必须实现该接口
     */
    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
