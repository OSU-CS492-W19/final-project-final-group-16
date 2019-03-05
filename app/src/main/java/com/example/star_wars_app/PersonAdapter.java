package com.example.star_wars_app;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.star_wars_app.utils.SWAPIUtils;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    private SWAPIUtils.PersonResource[] mPeople;
    private OnForecastItemClickListener mPersonClickListener;

    public interface OnForecastItemClickListener {
        void onForecastItemClick(SWAPIUtils.PersonResource forecastItem);
    }

    public PersonAdapter(OnForecastItemClickListener clickListener) {
        mPersonClickListener = clickListener;
    }

    public void updatePeople(SWAPIUtils.PersonResource[] people) {
        mPeople = people;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mPeople != null) {
            return mPeople.length;
        } else {
            return 0;
        }
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.forecast_list_item, parent, false);
        return new PersonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.bind(mPeople[position]);
    }

    class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mPersonTempDescriptionTV;

        public PersonViewHolder(View itemView) {
            super(itemView);
            mPersonTempDescriptionTV = itemView.findViewById(R.id.tv_forecast_temp_description);
            itemView.setOnClickListener(this);
        }

        public void bind(SWAPIUtils.PersonResource forecastItem) {
            String detailString = mPersonTempDescriptionTV.getContext().getString(
                    R.string.forecast_item_details, forecastItem.name);
            mPersonTempDescriptionTV.setText(detailString);
        }

        @Override
        public void onClick(View v) {
            SWAPIUtils.PersonResource forecastItem = mPeople[getAdapterPosition()];
            mPersonClickListener.onForecastItemClick(forecastItem);
        }
    }
}
