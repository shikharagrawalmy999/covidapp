package com.example.covidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactTracingDashboard extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    DatabaseReference userReference;
    ArrayList<Token> tokenList;
    long number_children;
    long i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_tracing_dashboard);
        RecyclerView recyclerView=findViewById(R.id.idCourseRV);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Token");
        userReference = FirebaseDatabase.getInstance().getReference().child("users");
        tokenList = new ArrayList<>();
        // created new array list..
        ArrayList<RecyclerData> recyclerDataArrayList=new ArrayList<RecyclerData>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                number_children= snapshot.getChildrenCount();;

                for (DataSnapshot data : snapshot.getChildren()){

                    DatabaseReference currentTokenReference = reference.child(data.getKey());
                    currentTokenReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot tokenSnapshot) {
//                            System.out.println("MAC ADDRESS:::::: "+tokenSnapshot.child("mac_ADDRESS").getValue(String.class));
//                            System.out.println("MAC ADDRESS:::::: "+tokenSnapshot.child("latitude").getValue(String.class));
//                            System.out.println("MAC ADDRESS:::::: "+tokenSnapshot.child("longitude").getValue(String.class));
//                            System.out.println("MAC ADDRESS:::::: "+tokenSnapshot.child("date").getValue(String.class));
//                            Token currentToken = new Token(tokenSnapshot.child("mac_ADDRESS").getValue(String.class), tokenSnapshot.child("latitude").getValue(String.class), tokenSnapshot.child("longitude").getValue(String.class), tokenSnapshot.child("date").getValue(String.class));
//                            tokenList.add(currentToken);
                            Token currentToken = new Token();
                            currentToken.setMAC_ADDRESS(tokenSnapshot.child("mac_ADDRESS").getValue(String.class));
                            currentToken.setLATITUDE(tokenSnapshot.child("latitude").getValue(String.class));
                            currentToken.setLONGITUDE(tokenSnapshot.child("longitude").getValue(String.class));
                            currentToken.setDATE(tokenSnapshot.child("date").getValue(String.class));
                            if(currentToken == null){
                                System.out.println("YOU'RE DED AGAIN!!!!!");
                            }
                            tokenList.add(currentToken);
                            System.out.println("SIZE OF TOKEN LIST IS: "+tokenList.size());
//                            userReference.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
//                                    for(DataSnapshot data: userSnapshot.getChildren()){
//                                        if (data.child("mac_address").toString().equals(tokenSnapshot.child("mac_ADDRESS").getValue(String.class))) {
//                                            Token currentToken = new Token(tokenSnapshot.child("mac_ADDRESS").getValue(String.class), tokenSnapshot.child("latitude").getValue(String.class), tokenSnapshot.child("longitude").getValue(String.class), tokenSnapshot.child("date").getValue(String.class));
//                                            tokenList.add(currentToken);
//                                            break;
//                                        } else {
//
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });
                            i++;
                            if(i==number_children){
                                if(tokenList.isEmpty()){
                                    System.out.println("You're ded!!!!!");
                                }

                                for(int i=tokenList.size()-1;i>=0;i--){
                                    System.out.println(tokenList.get(i).getMAC_ADDRESS());
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            System.out.println("Error occurred!");
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error occurred!");
            }
        });



        // added data to array list
        recyclerDataArrayList.add(new RecyclerData("Bilaspur", "Chhattisgarh", "India", "495223", "27/11/2021", "02:09 PM", "Healthy"));
        recyclerDataArrayList.add(new RecyclerData("Bilaspur", "Chhattisgarh", "India", "495223", "27/11/2021", "02:09 PM", "Healthy"));
        recyclerDataArrayList.add(new RecyclerData("Bilaspur", "Chhattisgarh", "India", "495223", "27/11/2021", "02:09 PM", "Healthy"));
        recyclerDataArrayList.add(new RecyclerData("Bilaspur", "Chhattisgarh", "India", "495223", "27/11/2021", "02:09 PM", "Healthy"));
        recyclerDataArrayList.add(new RecyclerData("Bilaspur", "Chhattisgarh", "India", "495223", "27/11/2021", "02:09 PM", "Healthy"));
        recyclerDataArrayList.add(new RecyclerData("Bilaspur", "Chhattisgarh", "India", "495223", "27/11/2021", "02:09 PM", "Healthy"));

        // added data from arraylist to adapter class.
        RecyclerViewAdapter adapter=new RecyclerViewAdapter(recyclerDataArrayList,this);

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}