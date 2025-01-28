package com.example.gymbuddychat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private List<AttendanceEntry> attendanceList;

    public AttendanceAdapter(List<AttendanceEntry> attendanceList) {
        this.attendanceList = attendanceList;
    }

    public void setAttendanceList(List<AttendanceEntry> attendanceList) {
        this.attendanceList = attendanceList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        AttendanceEntry attendanceEntry = attendanceList.get(position);
        holder.bind(attendanceEntry);
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCheckIn, tvCheckOut;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.text_view_name);
            tvCheckIn = itemView.findViewById(R.id.text_view_check_in);
            tvCheckOut = itemView.findViewById(R.id.text_view_check_out);
        }

        public void bind(AttendanceEntry attendanceEntry) {
            tvName.setText(attendanceEntry.getName());
            tvCheckIn.setText(attendanceEntry.getCheckInTime());
            tvCheckOut.setText(attendanceEntry.getCheckOutTime());
        }
    }
}