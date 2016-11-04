package xsm.org.mlive.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xsm.org.mlive.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttentionFragment extends BaseFragment {


    public AttentionFragment() {
        // Required empty public constructor
    }

    @Override
    public String getFragmentTitle() {
        return "关注";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attention, container, false);
    }

}
