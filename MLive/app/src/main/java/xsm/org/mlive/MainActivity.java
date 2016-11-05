package xsm.org.mlive;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import xsm.org.mlive.activitis.RoomActivity;
import xsm.org.mlive.fragments.HomePageFragment;
import xsm.org.mlive.fragments.MineFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rg_bottom_bar)
    RadioGroup mRgBottomBar;
    @BindView(R.id.rb_home)
    RadioButton mRbHome;

    private Fragment[] mFragments;
    private int mCurrentCheckedId = View.NO_ID;
    private int mCurrentIndex;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mFragments = new Fragment[]{new HomePageFragment(), new MineFragment()};
        switchTab (R.id.rb_home, 0);
        mRgBottomBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onCheckTab(group, checkedId);
            }
        });
    }

    private void onCheckTab(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_room:
                toRoom();
                return;
            case R.id.rb_home:
                index = 0;
                switchTab(checkedId, index);
                return;
            case R.id.rb_me:
                index = 1;
                switchTab(checkedId, index);
                mCurrentCheckedId = R.id.rb_me;
                return;
            default:
                break;
        }
    }

    private void toRoom() {
        startActivity(new Intent(this, RoomActivity.class));
    }

    private void switchTab(int checkedId, int index) {
        if (checkedId == mCurrentCheckedId)
            return;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mFragments[mCurrentIndex].isAdded()) {
            ft.hide(mFragments[mCurrentIndex]);
        }
        if (!mFragments[index].isAdded()) {
            ft.add(R.id.fl_fragment_container, mFragments[index]);
        }
        ft.show(mFragments[index]);
        ft.commit();
        mCurrentIndex = index;
        mCurrentCheckedId = checkedId;
    }

}
