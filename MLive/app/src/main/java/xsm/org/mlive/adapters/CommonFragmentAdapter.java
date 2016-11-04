package xsm.org.mlive.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import xsm.org.mlive.fragments.BaseFragment;

/**
 * Created by xsm on 16-11-4.
 */

public class CommonFragmentAdapter extends FragmentPagerAdapter{
    private List<BaseFragment> mList;

    public CommonFragmentAdapter(FragmentManager fm, List<BaseFragment> list) {
        super(fm);
        mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (mList != null) {
            ret = mList.size();
        }
        return ret;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getFragmentTitle();
    }
}
