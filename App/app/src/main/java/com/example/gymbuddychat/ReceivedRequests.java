//package com.example.gymbuddychat;
//
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.gymbuddychat.R;
//import com.example.gymbuddychat.model.ExerciseRequest;
//import com.example.gymbuddychat.utils.FirebaseUtil;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ReceivedRequests extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private DatabaseReference databaseReference;
//    private List<ExerciseRequest> exerciseRequestsList;
//    private ExerciseRequestsAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_received_requests);
//
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("exerciseInfo");
//
//
//        exerciseRequestsList = new ArrayList<>();
//        adapter = new ExerciseRequestsAdapter(exerciseRequestsList);
//
//        recyclerView.setAdapter(adapter);
//
//        fetchExerciseRequests();
//    }
//
//    private void fetchExerciseRequests() {
//        Query query = databaseReference.orderByChild("date");
//
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                exerciseRequestsList.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    ExerciseRequest exerciseRequest = dataSnapshot.getValue(ExerciseRequest.class);
//                    if (exerciseRequest != null) {
//                        exerciseRequestsList.add(exerciseRequest);
//                    }
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error
//            }
//        });
//    }
//
//    static class ExerciseRequestsAdapter extends RecyclerView.Adapter<ExerciseRequestsAdapter.ExerciseRequestViewHolder> {
//
//        private List<ExerciseRequest> exerciseRequestsList;
//
//        ExerciseRequestsAdapter(List<ExerciseRequest> exerciseRequestsList) {
//            this.exerciseRequestsList = exerciseRequestsList;
//        }
//
//        @NonNull
//        @Override
//        public ExerciseRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_request, parent, false);
//            return new ExerciseRequestViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ExerciseRequestViewHolder holder, int position) {
//            ExerciseRequest exerciseRequest = exerciseRequestsList.get(position);
//            holder.bind(exerciseRequest);
//        }
//
//        @Override
//        public int getItemCount() {
//            return exerciseRequestsList.size();
//        }
//
//        static class ExerciseRequestViewHolder extends RecyclerView.ViewHolder {
//            TextView exerciseTypeTextView;
//            TextView timeTextView;
//            TextView dateTextView;
//            TextView userTextView;
//
//            ExerciseRequestViewHolder(@NonNull View itemView) {
//                super(itemView);
//                exerciseTypeTextView = itemView.findViewById(R.id.exerciseTypeTextView);
//                timeTextView = itemView.findViewById(R.id.timeTextView);
//                dateTextView = itemView.findViewById(R.id.dateTextView);
//                userTextView = itemView.findViewById(R.id.userTextView);
//            }
//
//            void bind(ExerciseRequest exerciseRequest) {
//                exerciseTypeTextView.setText(exerciseRequest.getExerciseType());
//                timeTextView.setText(exerciseRequest.getTime());
//                dateTextView.setText(exerciseRequest.getDate());
//                userTextView.setText(exerciseRequest.getUserName());
//            }
//        }
//    }
//}
