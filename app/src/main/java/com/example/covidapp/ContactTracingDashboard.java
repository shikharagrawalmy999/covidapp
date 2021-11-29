package com.example.covidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    ArrayList<Token> correctTokenList;
    Geocoder geocoder;
    List<Address> addresses;
    long number_children;
    long i=0;

    int j=0;
    long number_children_2;
    ArrayList<RecyclerData> recyclerDataArrayList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_tracing_dashboard);
        recyclerView=findViewById(R.id.idCourseRV);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("Token");
        userReference = FirebaseDatabase.getInstance().getReference().child("users");
        tokenList = new ArrayList<>();
        correctTokenList = new ArrayList<>();
        // created new array list..
        recyclerDataArrayList=new ArrayList<RecyclerData>();
        geocoder = new Geocoder(this, Locale.getDefault());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                number_children= snapshot.getChildrenCount();;

                for (DataSnapshot data : snapshot.getChildren()){

                    DatabaseReference currentTokenReference = reference.child(data.getKey());
                    currentTokenReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot tokenSnapshot) {
                            Token currentToken = new Token();
                            currentToken.setMAC_ADDRESS(tokenSnapshot.child("mac_ADDRESS").getValue(String.class));
                            currentToken.setLATITUDE(tokenSnapshot.child("latitude").getValue(String.class));
                            currentToken.setLONGITUDE(tokenSnapshot.child("longitude").getValue(String.class));
                            currentToken.setDATE(tokenSnapshot.child("date").getValue(String.class));

                            tokenList.add(currentToken);
                            System.out.println("SIZE OF TOKEN LIST IS: "+tokenList.size());

                            i++;
                            if(i==number_children){
                                if(tokenList.isEmpty()){
                                    System.out.println("You're ded!!!!!");
                                }

                                for(int i=tokenList.size()-1;i>=0;i--){
                                    System.out.println(tokenList.get(i).getMAC_ADDRESS());
                                }

                                test();
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

    }
    boolean check = false;
    int nice;
    private void test() {
        nice=tokenList.size()-1;
        for(j=tokenList.size()-1;j>=0;j--){
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                    number_children_2 = userSnapshot.getChildrenCount();
                    for(DataSnapshot data: userSnapshot.getChildren()){
                        if(data.child("mac_address").getValue(String.class)==null){
                            check=false;
                        }
                        else{
                            if(nice>=0 && nice<tokenList.size()){
                                String s1=data.child("mac_address").getValue(String.class);
                                String s2 = tokenList.get(nice).getMAC_ADDRESS();
                                s1=s1.toUpperCase();
                                s2=s2.toUpperCase();
                                check = s1.equals(s2);
                            }
                        }
                        if (check){
                            correctTokenList.add(tokenList.get(nice));
                            check=false;
                            break;
                        } else {

                        }
                    }
                    nice--;
                    if(nice==-1){

                        for(int l=correctTokenList.size()-1;l>=0;l--){
                            System.out.println("Correct MAC ADDRESSES::::"+correctTokenList.get(l).getMAC_ADDRESS());
                            try {
                                addresses = geocoder.getFromLocation(Double.parseDouble(correctTokenList.get(l).getLATITUDE()), Double.parseDouble(correctTokenList.get(l).getLONGITUDE()), 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                            recyclerDataArrayList.add(new RecyclerData(city, state, country, postalCode, correctTokenList.get(l).getDATE().substring(0,10)+" 2021", correctTokenList.get(l).getDATE().substring(11,19), "Healthy"));
                        }

                        // added data from arraylist to adapter class.
                        RecyclerViewAdapter adapter=new RecyclerViewAdapter(recyclerDataArrayList,ContactTracingDashboard.this);

                        // setting grid layout manager to implement grid view.
                        // in this method '2' represents number of columns to be displayed in grid view.
                        GridLayoutManager layoutManager=new GridLayoutManager(ContactTracingDashboard.this,2);

                        // at last set adapter to recycler view.
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}