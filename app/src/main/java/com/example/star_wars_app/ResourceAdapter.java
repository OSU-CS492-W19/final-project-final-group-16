package com.example.star_wars_app;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.star_wars_app.utils.SWAPIUtils;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder> {

    private SWAPIUtils.GenericResource[] mResources;
    private OnResourceClickListener mResourceClickListener;

    public interface OnResourceClickListener {
        void onResourceClick(SWAPIUtils.GenericResource resource);
    }

    public ResourceAdapter(OnResourceClickListener clickListener) {
        mResourceClickListener = clickListener;
    }

    public void updateResources(SWAPIUtils.GenericResource[] resources) {
        mResources = resources;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mResources != null) {
            return mResources.length;
        } else {
            return 0;
        }
    }

    @Override
    public ResourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.forecast_list_item, parent, false);
        return new ResourceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResourceViewHolder holder, int position) {
        holder.bind(mResources[position]);
    }

    class ResourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mPersonTempDescriptionTV;

        public ResourceViewHolder(View itemView) {
            super(itemView);
            mPersonTempDescriptionTV = itemView.findViewById(R.id.tv_forecast_temp_description);
            itemView.setOnClickListener(this);
        }

        public void bind(SWAPIUtils.GenericResource resource) {
            String detailString = mPersonTempDescriptionTV.getContext().getString(
                    R.string.forecast_item_details, resource.getInfoString());
            mPersonTempDescriptionTV.setText(detailString);
        }

        @Override
        public void onClick(View v) {
            SWAPIUtils.GenericResource resource = mResources[getAdapterPosition()];
            mResourceClickListener.onResourceClick(resource);
        }
    }
}
