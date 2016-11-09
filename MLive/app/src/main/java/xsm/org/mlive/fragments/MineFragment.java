package xsm.org.mlive.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xsm.org.mlive.R;
import xsm.org.mlive.tools.GlideCircleTransform;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {

//    @BindView(R.id.mine_head_search)
//    ImageView mSearch;
//    @BindView(R.id.mine_head_msg)
//    ImageView mMsg;
    @BindView(R.id.mine_picture)
    ImageView mImgPicture;
//    @BindView(R.id.mine_zhibo)
//    LinearLayout zhibo;
//    @BindView(R.id.mine_follow)
//    LinearLayout follow;
//    @BindView(R.id.mine_fens)
//    LinearLayout fens;
//    @BindView(R.id.mine_zhibo_number)
//    TextView zhiboNumber;
//    @BindView(R.id.mine_follow_number)
//    TextView followNumber;
//    @BindView(R.id.mine_fens_number)
//    TextView fensNumber;
//    @BindView(R.id.mine_yingpiao)
//    RelativeLayout yingpiao;
//    @BindView(R.id.mine_shouyi)
//    RelativeLayout shouyi;
//    @BindView(R.id.mine_zhanghu)
//    RelativeLayout zhanghu;
//    @BindView(R.id.mine_dengji)
//    RelativeLayout dengji;
//    @BindView(R.id.mine_setting)
//    RelativeLayout setting;
    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, ret);
        Glide.with(getContext()).load(R.mipmap.popular_picture).transform(new GlideCircleTransform(getContext())).into(mImgPicture);
        return ret;
    }

    @OnClick(R.id.mine_head_search)
    public void searchClick() {
        Toast.makeText(getContext(), "搜索", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.mine_head_msg)
    public void msgClick() {
        Toast.makeText(getContext(), "信息", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.mine_picture)
    public void pictureClick() {
        Toast.makeText(getContext(), "头像被点击", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.mine_zhibo)
    public void zhiboClick() {
        Toast.makeText(getContext(), "直播", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.mine_follow)
    public void followClick() {
        Toast.makeText(getContext(), "关注", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.mine_fens)
    public void fensClick() {
        Toast.makeText(getContext(), "粉丝", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.mine_yingpiao)
    public void yingpiaoClick() {
        Toast.makeText(getContext(), "映票贡献榜", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.mine_shouyi)
    public void shouyiClick() {
        Toast.makeText(getContext(), "收益", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.mine_zhanghu)
    public void zhanghuClick() {
        Toast.makeText(getContext(), "账户", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.mine_dengji)
    public void dengjiClick() {
        Toast.makeText(getContext(), "等级", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.mine_setting)
    public void settingClick() {
        Toast.makeText(getContext(), "设置", Toast.LENGTH_SHORT).show();
    }
}
