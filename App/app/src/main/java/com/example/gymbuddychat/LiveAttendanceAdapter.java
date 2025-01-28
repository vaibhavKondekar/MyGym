package com.example.gymbuddychat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LiveAttendanceAdapter extends RecyclerView.Adapter<LiveAttendanceAdapter.LiveAttendanceViewHolder> {

    private List<AttendanceEntry> liveAttendanceList;

    public LiveAttendanceAdapter(List<AttendanceEntry> liveAttendanceList) {
        this.liveAttendanceList = liveAttendanceList;
    }

    @NonNull
    @Override
    public LiveAttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_attendance, parent, false);
        return new LiveAttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveAttendanceViewHolder holder, int position) {
        AttendanceEntry attendanceEntry = liveAttendanceList.get(position);
        holder.bind(attendanceEntry);
    }

    @Override
    public int getItemCount() {
        return liveAttendanceList.size();
    }

    static class LiveAttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCheckIn;

        public LiveAttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.text_view_live_name);
            tvCheckIn = itemView.findViewById(R.id.text_view_live_check_in);
        }

        public void bind(AttendanceEntry attendanceEntry) {
            tvName.setText(attendanceEntry.getName());
            tvCheckIn.setText(attendanceEntry.getCheckInTime());
        }
    }
}