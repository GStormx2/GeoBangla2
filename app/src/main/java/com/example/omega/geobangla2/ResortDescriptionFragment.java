package com.example.omega.geobangla2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class ResortDescriptionFragment extends Fragment {


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private String Address, Amenities, Description, Email, Image, Map, Name, Pack1, Pack1price, Pack2, Pack2price, Phone,
            Priceperday, Stars, Type;

    ImageView imageView_img2;
     TextView textView_name2;
    TextView textView_hoteltag2;
     RatingBar ratingBar_rating2;
    TextView textView_pack1;
    TextView textView_pack2;
    TextView textView_pack1price;
    TextView textView_pack2price;
    Button button_booknowbtn;
    Button button_call;
    Button button_email;
    TextView textView_desc;
    TextView textView_amenitiestitle;
    TextView textView_amenities ;
    TextView textView_address ;
    TextView textView_email ;
    TextView textView_phone ;
    ImageView imageView_map;
    ImageView resort_desc_fav;

    String user_id;


    private ArrayList<ResortClass> rc = new ArrayList<>();
    private ResortClass resortClass = new ResortClass();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_resort_desc, container,false);
        imageView_img2 = v.findViewById(R.id.resort_desc_img2);
        textView_name2 = v.findViewById(R.id.resort_desc_name2);
        textView_hoteltag2 = v.findViewById(R.id.resort_desc_hoteltag2);
        ratingBar_rating2 = v.findViewById(R.id.resort_desc_rating2);
        textView_pack1 = v.findViewById(R.id.resort_desc_pack1);
        textView_pack2 = v.findViewById(R.id.resort_desc_pack2);
        textView_pack1price = v.findViewById(R.id.resort_desc_pack1price);
        textView_pack2price = v.findViewById(R.id.resort_desc_pack2price);
        button_booknowbtn = v.findViewById(R.id.rest_desc_booknowbtn);
        button_call = v.findViewById(R.id.rest_desc_callbtn);
        button_email = v.findViewById(R.id.rest_desc_emailbtn);
        textView_desc = v.findViewById(R.id.resort_desc_desc);
        textView_amenitiestitle = v.findViewById(R.id.amenities_letter_spacing);
        textView_amenities = v.findViewById(R.id.resort_desc_amenities);
        textView_address = v.findViewById(R.id.resort_desc_address);
        textView_email = v.findViewById(R.id.resort_desc_email);
        textView_phone = v.findViewById(R.id.resort_desc_phone);
        imageView_map = v.findViewById(R.id.resort_desc_map);
        resort_desc_fav = v.findViewById(R.id.resort_desc_fav);


        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user_id = firebaseUser.getUid();

        int temp = StoredResources.getResortPosition() + 1;
        String temp_string = String.valueOf(temp);
        String path = "resort/" + StoredResources.getClickedDivision();

        mRef = FirebaseDatabase.getInstance().getReference(path);
        Query query = mRef.orderByChild("Id").equalTo(temp_string);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        resortClass = snapshot.getValue(ResortClass.class);

                    }
                }
                System.out.println("STORED POSITION: " + StoredResources.getResortPosition());

                StoredResources.setPackOne(Integer.parseInt(resortClass.getPack1price()));
                StoredResources.setPackTwo(Integer.parseInt(resortClass.getPack2price()));
                StoredResources.setPricePerDay(Integer.parseInt(resortClass.getPriceperday()));
                StoredResources.setResortName(resortClass.getName());
                StoredResources.setResortTag(resortClass.getType());
                StoredResources.setResortStars(resortClass.getStars());
                setDetials(resortClass);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        checkFavorite();

        button_booknowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BookingActivity.class);
                startActivity(intent);

            }
        });
        button_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:01749518145"));
                startActivity(intent);
            }
        });
        button_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, "example@gmail.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Booking Query");
                intent.putExtra(Intent.EXTRA_TEXT, "<PUT MESSAGE BODY HERE>");
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Choose an EMAIL client"));
            }
        });
        resort_desc_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) v;
                //assert(R.id.resort_desc_fav == imageView.getId());

                Integer integer = (Integer) imageView.getTag();
                integer = integer == null ? 0 : integer;

                if(integer == R.drawable.ic_favorite_black_24dp_pink){
                    Toast.makeText(getContext(), "Already in Favorites!", Toast.LENGTH_SHORT).show();
                }
                else{
                    String temp = "favorite/" + user_id + "/" + StoredResources.getClickedDivision();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(temp);
                    temp = String.valueOf(StoredResources.getResortPosition() + 1);
                    databaseReference.child(temp).setValue(resortClass);

                    Toast.makeText(getContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                    resort_desc_fav.setImageResource(R.drawable.ic_favorite_black_24dp_pink);
                }
            }
        });

        /*Glide.with(getContext())
                .asBitmap()
                .load(resortClass.getImage())
                .into(imageView_img2);
        textView_name2.setText(resortClass.getName());
        textView_hoteltag2.setText(resortClass.getType());
        //ratingBar_rating2.setNumStars(Integer.parseInt(Stars));
        //ratingBar_rating2.setRating(Integer.parseInt(Stars));
        textView_pack1.setText(resortClass.getPack1());
        textView_pack2.setText(resortClass.getPack2());
        textView_pack1price.setText(resortClass.getPack1price());
        textView_pack2price.setText(resortClass.getPack2price());
        //set BOOK NOW button onCLickListener here
        textView_desc.setText(resortClass.getDescription());
        textView_address.setText(resortClass.getAddress());
        textView_email.setText(resortClass.getEmail());
        textView_phone.setText(resortClass.getPhone());
        Glide.with(getContext())
                .asBitmap()
                .load(resortClass.getMap())
                .into(imageView_map);

    */
        return v;
    }

    private void checkFavorite(){
        String path = "favorite/" + user_id + "/" + StoredResources.getClickedDivision();
        mRef = FirebaseDatabase.getInstance().getReference(path);
        String temp = String.valueOf(StoredResources.getResortPosition() + 1);
        Query query = mRef.orderByChild("id").equalTo(temp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    resort_desc_fav.setImageResource(R.drawable.ic_favorite_black_24dp_pink);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setDetials(ResortClass resortClass){
        textView_address.setText(resortClass.getAddress());
        textView_name2.setText(resortClass.getName());
        Glide.with(getContext())
                .asBitmap()
                .load(resortClass.getImage())
                .into(imageView_img2);
        textView_hoteltag2.setText(resortClass.getType());
        ratingBar_rating2.setNumStars(Integer.parseInt(resortClass.getStars()));
        ratingBar_rating2.setRating(Integer.parseInt(resortClass.getStars()));
        textView_pack1.setText(resortClass.getPack1());
        textView_pack2.setText(resortClass.getPack2());

        String p1price = "TK. " + resortClass.getPack1price();
        textView_pack1price.setText(p1price);

        String p2price = "TK. " + resortClass.getPack2price();
        textView_pack2price.setText(p2price);

        textView_desc.setText(resortClass.getDescription());
        textView_amenities.setText(resortClass.getAmenities());
        textView_email.setText(resortClass.getEmail());
        textView_phone.setText(resortClass.getPhone());
        Glide.with(getContext())
                .asBitmap()
                .load(resortClass.getMap())
                .into(imageView_map);

    }
}
