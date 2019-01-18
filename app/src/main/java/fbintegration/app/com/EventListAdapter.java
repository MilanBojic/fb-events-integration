package fbintegration.app.com;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.DataObjectHolder> {

    private List<EventItem> mEventList;
    private fbintegration.app.com.OnItemClick mOnItemClick;

    EventListAdapter(List<EventItem> mEventDataSet, fbintegration.app.com.OnItemClick onItemClick) {
        mEventList = mEventDataSet;
        this.mOnItemClick = onItemClick;
    }

    EventListAdapter(List<EventItem> mEventDataSet) {
        mEventList = mEventDataSet;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item_layout, parent, false);

        return new DataObjectHolder(view);

    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        holder.name.setText(mEventList.get(position).getName());
    }

    class DataObjectHolder extends RecyclerView.ViewHolder {

        TextView name;

        DataObjectHolder(final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.event_item_id);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    {
                        if (mOnItemClick != null) {
                            mOnItemClick.onItemClick(itemView, getAdapterPosition());
                        }
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }
}
