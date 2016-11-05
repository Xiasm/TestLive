package xsm.org.mlive.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xsm.org.mlive.R;
import xsm.org.mlive.adapters.CommonFragmentAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener {

    private List<BaseFragment> mList;
    @BindView(R.id.homepage_viewpager) ViewPager mViewPager;
    @BindView(R.id.iv_search) ImageView mIvSearch;
    @BindView(R.id.iv_msg) ImageView mIvMsg;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    public HomePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_home_page, container, false);
        ButterKnife.bind(this, ret);
        mList = new ArrayList<>();
        mList.add(new AttentionFragment());
        mList.add(new PopularFragment());
        mList.add(new NearbyFragment());
        CommonFragmentAdapter adapter = new CommonFragmentAdapter(getChildFragmentManager(), mList);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mIvSearch.setOnClickListener(this);
        mIvMsg.setOnClickListener(this);
        return ret;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                toSearch();
                break;
            case R.id.iv_msg:
                toMsg();
                break;
        }
    }

    private void toMsg() {
        Toast.makeText(getContext(), "toMsg", Toast.LENGTH_SHORT).show();
    }

    private void toSearch() {
        Toast.makeText(getContext(), "toSearch", Toast.LENGTH_SHORT).show();
    }
}
