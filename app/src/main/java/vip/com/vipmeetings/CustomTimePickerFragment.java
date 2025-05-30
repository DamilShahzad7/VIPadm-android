package vip.com.vipmeetings;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 24/07/17.
 */

public class CustomTimePickerFragment extends Fragment {


    @BindView(R.id.rvhour)
    RecyclerView rvhour;
    @BindView(R.id.rvminute)
    RecyclerView rvminute;


    String hour[] = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
            "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};


    @BindArray(R.array.minute)
    String[] minute;

    HourAdapter hourAdapter;
    TimeAdapter timeAdapter;

    Calendar c;
    String minuts = "00";
    String hours = "00";


    TimePickerActivity onTimeSelected;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onTimeSelected = (TimePickerActivity) context;
        } catch (Exception e) {


        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timepicker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        rvhour.setLayoutManager(new LinearLayoutManager(getContext()));
        rvminute.setLayoutManager(new LinearLayoutManager(getActivity()));
        hourAdapter = new HourAdapter();
        rvhour.setAdapter(hourAdapter);
        timeAdapter = new TimeAdapter();
        rvminute.setAdapter(timeAdapter);
        if (getArguments() != null) {
            c = Calendar.getInstance();
            //hours = String.format("%02d", c.get(Calendar.HOUR));
            //minuts = String.format("%02d", c.get(Calendar.MINUTE));
//            onTimeSelected.onHour(hours);
//            onTimeSelected.onMinute(minuts);

            setCurrentTime(c.get(Calendar.HOUR), c.get(Calendar.MINUTE));
            Constants.e("time0", hours + "  " + minuts);
        } else {

            //hours = "00";
            //minuts = "00";
//            onTimeSelected.onHour(hours);
//            onTimeSelected.onMinute(minuts);
            setCurrentTime(0, 0);
            Constants.e("time1", hours + "  " + minuts);
        }


    }

    public void setCurrentTime(int currentHour, int currentMinute) {

        hours = String.format("%02d", currentHour);
        minuts = String.format("%02d", currentMinute);

        onTimeSelected.onHour(hours, minuts);
        onTimeSelected.onMinute(hours, minuts);
        hourAdapter.notifyDataSetChanged();
        timeAdapter.notifyDataSetChanged();


        if (hours != null)
            for (int i = 0; i < hour.length; i++) {
                if (hour[i].equalsIgnoreCase(hours)) {
                    rvhour.scrollToPosition(i);
                    break;
                }
            }

        if (minuts != null)
            for (int i = 0; i < minute.length; i++) {
                if (minute[i].equalsIgnoreCase(minuts)) {

                    rvminute.scrollToPosition(i);
                    break;
                }
            }


    }

    public void setEndTime(int endHour, int endMinute) {

        hours = String.format("%02d", endHour);
        minuts = String.format("%02d", endMinute);

        onTimeSelected.onHour(hours, minuts);
        onTimeSelected.onMinute(hours, minuts);
        hourAdapter.notifyDataSetChanged();
        timeAdapter.notifyDataSetChanged();




    }

    public class HourAdapter extends RecyclerView.Adapter<HourAdapter.VH> {


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(getContext()).inflate(R.layout.inflate_hour, null));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {


            holder.tvhour.setText(hour[position]);

            if (hours != null && hours.length() > 0) {

                if (hours.equalsIgnoreCase(minute[position])) {
                    holder.tvhour.setBackgroundResource(R.drawable.recthour);

                    holder.tvhour.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.md_red_500));
                } else {
                    holder.tvhour.setBackgroundResource(0);
                    holder.tvhour.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.md_white_1000));
                }


            } else {

                holder.tvhour.setTextColor(ContextCompat.getColor(
                        getContext(), R.color.md_white_1000));
                holder.tvhour.setBackgroundResource(0);
            }

            holder.tvhour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (onTimeSelected.isEndTime) {
                        if (Integer.parseInt(hour[holder.getAdapterPosition()]) >=
                                onTimeSelected.currentHour + 1) {

                            hours = hour[holder.getAdapterPosition()];
                            if (onTimeSelected != null) {

                                onTimeSelected.onHour(hours, minuts);
                            }
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(onTimeSelected, "End time should be greater than " +
                                    " start time", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        hours = hour[holder.getAdapterPosition()];
                        if (onTimeSelected != null) {

                            onTimeSelected.onHour(hours, minuts);
                        }
                        notifyDataSetChanged();
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return hour.length;
        }

        public class VH extends RecyclerView.ViewHolder {

            @BindView(R.id.tvhour)
            TextView tvhour;

            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }

    public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.VH> {


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(getContext()).inflate(R.layout.inflate_hour, null));
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {


            holder.tvhour.setText(minute[position]);

            if (minuts != null && minuts.length() > 0) {

                if (minuts.equalsIgnoreCase(minute[position])) {
                    holder.tvhour.setBackgroundResource(R.drawable.recthour);
                    holder.tvhour.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.md_red_500));
                } else {
                    holder.tvhour.setBackgroundResource(0);
                    holder.tvhour.setTextColor(ContextCompat.getColor(
                            getContext(), R.color.md_white_1000));
                }


            } else {
                holder.tvhour.setTextColor(ContextCompat.getColor(
                        getContext(), R.color.md_white_1000));
                holder.tvhour.setBackgroundResource(0);
            }

            holder.tvhour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (onTimeSelected.isEndTime) {

                        if (onTimeSelected.endHour ==
                                onTimeSelected.currentHour + 1) {


                            if(onTimeSelected.currentMinute<=
                                    Integer.parseInt(minute[holder.getAdapterPosition()]))
                            {

                                minuts = minute[holder.getAdapterPosition()];
                                if (onTimeSelected != null) {
                                    onTimeSelected.onMinute(hours, minuts);
                                }

                                notifyDataSetChanged();
                            }else {

//                            minuts = minute[holder.getAdapterPosition()];
//                            if (onTimeSelected != null) {
//                                onTimeSelected.onMinute(hours, minuts);
//                            }
//
//                            notifyDataSetChanged();

                                Toast.makeText(getContext(), "Time difference should be greater than or equal to" +
                                        "1 hour", Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            minuts = minute[holder.getAdapterPosition()];
                            if (onTimeSelected != null) {
                                onTimeSelected.onMinute(hours, minuts);
                            }

                            notifyDataSetChanged();
                        }
                    } else if (minuts != null && minuts.length() > 0) {


                        minuts = minute[holder.getAdapterPosition()];
                        if (onTimeSelected != null) {
                            onTimeSelected.onMinute(hours, minuts);
                        }

                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Please select hour", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return minute.length;
        }

        public class VH extends RecyclerView.ViewHolder {

            @BindView(R.id.tvhour)
            TextView tvhour;

            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }


}
