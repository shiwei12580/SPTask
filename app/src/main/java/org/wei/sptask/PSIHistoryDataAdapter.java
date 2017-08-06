package org.wei.sptask;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wei.sptask.data.ItemsData;
import org.wei.sptask.data.PSIData;
import org.wei.sptask.utils.DateUtils;

import java.util.List;

/**
 * Created by admin on 6/8/2017.
 */
public class PSIHistoryDataAdapter extends RecyclerView.Adapter<PSIHistoryDataAdapter.ViewHolder> {

    Context mContext;
    List<ItemsData> mData;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private boolean hasHeader;


    public PSIHistoryDataAdapter(Context context, List<ItemsData> data) {
        mContext = context;
        mData = data;
        hasHeader = true;
    }


    public void addHeader() {
        hasHeader = true;
    }

    public void removeHeader() {
        hasHeader = false;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader && position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.layout_data_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        } else if (viewType == TYPE_ITEM) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.layout_data_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_ITEM){
            int pos = getItemPosition(position);
            ItemsData data = mData.get(pos);
            String time = DateUtils.getTimeStringFromDateTimeZone(data.getUpdateTimeStamp());
            holder.mTime.setText(time);
            if(pos%2==0){
                holder.mContainer.setBackgroundColor(ContextCompat.getColor(mContext,R.color.light_grey));
            } else {

                TypedValue a = new TypedValue();
                mContext.getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);
                if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                    // windowBackground is a color
                    int color = a.data;
                    holder.mContainer.setBackgroundColor(color);
                } else {
                    // windowBackground is not a color, probably a drawable
                    Drawable d = mContext.getResources().getDrawable(a.resourceId);
                    holder.mContainer.setBackground(d);
                }
            }
            data.getDataReading().getPSI24HourlyData();
            setupData(holder, data.getDataReading().getPSI24HourlyData());
        }
    }

    private void setupData(ViewHolder holder, PSIData data) {
        holder.mNorth.setText(String.valueOf(((int) data.getNorthReading())));
        holder.mSouth.setText(String.valueOf(((int) data.getSouthReading())));
        holder.mWest.setText(String.valueOf(((int) data.getWestReading())));
        holder.mEast.setText(String.valueOf(((int) data.getEastReading())));
        holder.mCentral.setText(String.valueOf(((int) data.getCentralReading())));
    }

    private int getItemPosition(int position) {
        if(hasHeader) return position - 1;
        else return position;
    }

    @Override
    public int getItemCount() {
        if(hasHeader) {
            return mData.size() + 1;
        }
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTime;
        public TextView mNorth;
        public TextView mSouth;
        public TextView mWest;
        public TextView mEast;
        public TextView mCentral;
        public LinearLayout mContainer;
        public ViewHolder(View v) {
            super(v);
            mContainer = (LinearLayout) v.findViewById(R.id.container);
            mTime = (TextView) v.findViewById(R.id.time);
            mNorth = (TextView) v.findViewById(R.id.north);
            mSouth = (TextView) v.findViewById(R.id.south);
            mWest = (TextView) v.findViewById(R.id.west);
            mEast = (TextView) v.findViewById(R.id.east);
            mCentral = (TextView) v.findViewById(R.id.central);
        }
    }
}
