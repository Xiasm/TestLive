package xsm.org.mlive.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import xsm.org.mlive.R;
import xsm.org.mlive.entitys.NearbyItem;

/**
 * Created by xsm on 16-11-4.
 */

public class NearbyRecyclerAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_BODY = 1;
    private Context mContext;
    private List<NearbyItem> mList;
    private OnChildClickListener listener;
    private RecyclerView mRecyclerView;

    public void setOnChildClickListener(OnChildClickListener listener) {
        this.listener = listener;
    }

    public NearbyRecyclerAdapter(Context context, List<NearbyItem> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder ret = null;
        switch (viewType) {
            case TYPE_HEADER:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_nearby_header, parent, false);
                ret = new HeaderViewHolder(view);
                break;
            case TYPE_BODY:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_nearby, parent, false);
                view.setOnClickListener(this);
                ret = new NearbyViewHolder(view);
                break;
        }
        return ret;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView = null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NearbyViewHolder) {
            NearbyViewHolder nearbyViewHolder = (NearbyViewHolder) holder;
            nearbyViewHolder.mTxtCity.setText(mList.get(position).getCity());
            switch (position % 3) {
                case 0:
                    nearbyViewHolder.mImagePicture.setImageResource(R.mipmap.pic2);
                    break;
                case 1:
                    nearbyViewHolder.mImagePicture.setImageResource(R.mipmap.pic3);
                    break;
                case 2:
                    nearbyViewHolder.mImagePicture.setImageResource(R.mipmap.pic4);
                    break;
            }
        }else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            //TODO: 筛选工作
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_BODY;
    }

    @Override
    public void onClick(View v) {
        if (mRecyclerView != null && listener != null) {
            int position = mRecyclerView.getChildAdapterPosition(v);
            listener.onChildClick(mRecyclerView, v, position);
        }
    }

    public static class NearbyViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImagePicture;
        private ImageView mImageGrade;
        private TextView mTxtCity;
        public NearbyViewHolder(View itemView) {
            super(itemView);
            mImagePicture = ((ImageView) itemView.findViewById(R.id.nearby_item_img));
            mImageGrade = (ImageView) itemView.findViewById(R.id.nearby_item_grade);
            mTxtCity = (TextView) itemView.findViewById(R.id.nearby_item_city);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mHeaderTxt;
        private final TextView mHeaderSee;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mHeaderTxt = (TextView) itemView.findViewById(R.id.nearby_item_header_txt);
            mHeaderSee = (TextView) itemView.findViewById(R.id.nearby_item_header_see);
            mHeaderSee.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //TODO: 点击筛选
            Log.d("mHeadSee", "onClick: 被点击了");
        }
    }
    public interface OnChildClickListener {
        void onChildClick(RecyclerView parent, View view, int position);
    }
}
