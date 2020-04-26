package com.example.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private final List<Crime> mCrimes;
    private final HashMap<UUID, Crime> map;


    public CrimeLab(Context context) {
        map = new HashMap<>();
        mCrimes = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
//            map.put(crime.getUUID(), crime);
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);
            crime.setRequiresPolice(i % 2);
            mCrimes.add(crime);
        }
    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID uuid) {
        for (Crime crime : mCrimes) {
            if (crime.getUUID().equals(uuid)) {
                return crime;
            }
        }
        return null;
    }

    //挑战练习10.2
//    public Crime getCrime(UUID uuid) {
//        return map.get(uuid);
//    }

}
