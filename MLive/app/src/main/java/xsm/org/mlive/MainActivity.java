package xsm.org.mlive;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import xsm.org.mlive.fragments.HomePageFragment;
import xsm.org.mlive.fragments.MineFragment;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.main_tab_group)
    RadioGroup mGroup;
    @BindView(R.id.mian_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_tablayout)
    TabLayout mTabLayout;
    private HomePageFragment mHomePageFragment;
    private MineFragment mMineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mHomePageFragment = new HomePageFragment();
        Bundle args = new Bundle();

        mMineFragment =  new MineFragment();
        mGroup.setOnCheckedChangeListener(this);
        mGroup.check(R.id.mian_tab_homepage);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction tx = manager.beginTransaction();
        switch (checkedId) {
            case R.id.mian_tab_homepage:
                tx.replace(R.id.mian_container, mHomePageFragment);
                break;
            case R.id.mian_tab_mine:
                tx.replace(R.id.mian_container, mMineFragment);
                break;
        }
        tx.commit();
    }
}
