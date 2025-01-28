package com.example.gymbuddychat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Display_Members extends Fragment implements MemberAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private MemberAdapter adapter;
    private List<GymMember> memberList;

    // Firebase
    private DatabaseReference databaseRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display__members, container, false);

        // Initialize Firebase
        databaseRef = FirebaseDatabase.getInstance().getReference("members");

        // Initialize RecyclerView and layout manager
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize member list and adapter
        memberList = new ArrayList<>();
        adapter = new MemberAdapter(memberList, this);

        // Set adapter to RecyclerView
        recyclerView.setAdapter(adapter);

        // Call fetchMembersFromFirebase to fetch and display members from Firebase
        fetchMembersFromFirebase();

        // Set click listener for the "Add Member" button
        Button btnAddMember = view.findViewById(R.id.btnAddMember);
        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddMemberActivity.class));
            }
        });

        // Setup search functionality
        EditText etSearch = view.findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterMembers(s.toString());
            }
        });

        return view;
    }

    private void fetchMembersFromFirebase() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                memberList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GymMember member = snapshot.getValue(GymMember.class);
                    memberList.add(member);
                }
                // Sort memberList alphabetically by member name
                Collections.sort(memberList, new Comparator<GymMember>() {
                    @Override
                    public int compare(GymMember m1, GymMember m2) {
                        return m1.getFullName().compareToIgnoreCase(m2.getFullName());
                    }
                });
                adapter.notifyDataSetChanged(); // Notify adapter after sorting
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to fetch members: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterMembers(String searchText) {
        List<GymMember> filteredList = new ArrayList<>();
        for (GymMember member : memberList) {
            if (member.getFullName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(member);
            }
        }
        adapter.filterList(filteredList);
    }

    @Override
    public void onItemClick(int position) {
        // Handle item click here (e.g., view member details)
        // You can implement this based on your requirements
        GymMember selectedMember = memberList.get(position);
        Intent intent = new Intent(getActivity(), MembersDetailsActivity.class);
        intent.putExtra("memberId", selectedMember.getMemberId());
        startActivity(intent);
    }

    @Override
    public void onRenewClick(int position) {
        // Handle renew button click here
        // You can start the renew activity or perform any other action as needed
        GymMember selectedMember = memberList.get(position);
        Intent intent = new Intent(getActivity(), RenewMemberActivity.class);
        intent.putExtra("memberId", selectedMember.getMemberId());
        startActivity(intent);
    }
}
