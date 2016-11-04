package xsm.org.mlive.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xsm.org.mlive.R;
import xsm.org.mlive.adapters.NearbyRecyclerAdapter;
import xsm.org.mlive.entitys.NearbyItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyFragment extends BaseFragment implements NearbyRecyclerAdapter.OnChildClickListener {

    @BindView(R.id.nearby_recycler)
    RecyclerView mRecyclerView;
    private List<NearbyItem> mList;
    public NearbyFragment() {
        // Required empty public constructor
    }

    @Override
    public String getFragmentTitle() {
        return "附近";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_nearby, container, false);
        ButterKnife.bind(this, ret);
        mList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mList.add(new NearbyItem("上海" + i, "第" + i + "等级"));
        }
        NearbyRecyclerAdapter adapter = new NearbyRecyclerAdapter(getContext(), mList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 3;
                }
                return 1;
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnChildClickListener(this);
        return ret;
    }

    @Override
    public void onChildClick(RecyclerView parent, View view, int position) {
        Toast.makeText(getContext(), "第" + position + "主播被点击", Toast.LENGTH_SHORT).show();
    }
}
