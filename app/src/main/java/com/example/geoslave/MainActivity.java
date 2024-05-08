package com.example.geoslave;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geoslave.Adapters.CardAdapter;
import com.example.geoslave.Adapters.LikeAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    public static List<Formula> LikedFormulas = new ArrayList<>();
    public static List<Formula> Formulas = new ArrayList<>();
    public static  List<Formula> TrianglesFormulasS = new ArrayList<>();
    public static  List<Formula> TrianglesFormulasOther = new ArrayList<>();
    public static  List<Formula> QuadrilateralsFormulas = new ArrayList<>();
    public static  List<Formula> PolygonFormulas = new ArrayList<>();
    public static  List<Formula> CircleFormulas = new ArrayList<>();

    private FirebaseAuth mAuth;
    private ImageButton signOutButton;
    public FirebaseFirestore fStore;
    public static String userID;
    public static DocumentReference documentReference;
    public static Map<String,Object> Liked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        //

        RecyclerView recyclerLiked = findViewById(R.id.recyclerLikes);
        ConstraintLayout emptyLikes = findViewById(R.id.emptyLikes);
        userID = LoginActivity.auth.getCurrentUser().getUid();
        fStore = FirebaseFirestore.getInstance();
        documentReference = fStore.collection("users")
                .document(LoginActivity.auth.getCurrentUser().getEmail());
        Liked = new HashMap<>();
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> data = documentSnapshot.getData();
                    if (data != null && data.containsKey("LikedFormulas")) {
                        // Get the LikedFormulas from the document data
                        LikedFormulas = ((List<Map>) data.get("LikedFormulas")).stream().map(el->
                                new Formula((String) el.get("name"),Math.toIntExact((Long) el.get("image")),
                                        Math.toIntExact((Long) el.get("textSize")))).collect(Collectors.toList());

                        // Now LikedFormulas has the data from Firestore
                        Log.d("TAG", "LikedFormulas: " + LikedFormulas);
                    } else {
                        Log.d("TAG", "LikedFormulas not found in document");
                    }
                } else {
                    Log.d("TAG", "Document does not exist");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "Error fetching document", e);
            }
        });
        if(LikedFormulas.isEmpty()) {
            recyclerLiked.setVisibility(View.GONE);
            emptyLikes.setVisibility(View.VISIBLE);
            emptyLikes.setVisibility(View.GONE);
        }
        else {
            recyclerLiked.setVisibility(View.VISIBLE);
            emptyLikes.setVisibility(View.GONE);
        }
        //
        mAuth = FirebaseAuth.getInstance();
        signOutButton = findViewById(R.id.LogOut);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FirebaseAuth.getInstance().signOut();
                showSignOutConfirmationDialog();
            }
        });
        //liked
        recyclerLiked.setAdapter(new LikeAdapter(getApplicationContext(), MainActivity.LikedFormulas, recyclerLiked,emptyLikes));
        recyclerLiked.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //liked
        //TriangleS
        RecyclerView recyclerTriangleS = findViewById(R.id.recyclerTriangleS);
        TrianglesFormulasS.add(new Formula("Heron's Formula",R.drawable.heron,15));
        TrianglesFormulasS.add(new Formula("Area By height and side",R.drawable.bardzrutyuntomakeres,12));
        TrianglesFormulasS.add(new Formula("Area By Two Sides One Angle",R.drawable.areaby2sides1angle,11));
        TrianglesFormulasS.add(new Formula("Area By One Side Two Angles",R.drawable.areaby1side2angles,11));
        TrianglesFormulasS.add(new Formula("Area By Incircle",R.drawable.areabyincircle,13));
        TrianglesFormulasS.add(new Formula("Area By Excircle",R.drawable.areabyexcircle,13));
        Formulas.addAll(TrianglesFormulasS);
        recyclerTriangleS.setAdapter(new CardAdapter(getApplicationContext(), TrianglesFormulasS,(Vibrator)getSystemService(Context.VIBRATOR_SERVICE), recyclerLiked, emptyLikes));
        recyclerTriangleS.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //TriangleS
        //TriangleOthers
        RecyclerView recyclerTriangleOthers = findViewById(R.id.recyclerTriangleOthers);
        TrianglesFormulasOther.add(new Formula("Formula of cosines",R.drawable.cosinusneri,14));
        TrianglesFormulasOther.add(new Formula("Formula of sines",R.drawable.sinusneri,14));
        TrianglesFormulasOther.add(new Formula("Height of a triangle",R.drawable.erankyunbardzrutyun,14));
        TrianglesFormulasOther.add(new Formula("Bisector of a triangle",R.drawable.erankyunkisord,14));
        TrianglesFormulasOther.add(new Formula("Median of a triangle",R.drawable.erankyunmijnagic,14));
        Formulas.addAll(TrianglesFormulasOther);
        recyclerTriangleOthers.setAdapter(new CardAdapter(getApplicationContext(), TrianglesFormulasOther,(Vibrator)getSystemService(Context.VIBRATOR_SERVICE), recyclerLiked, emptyLikes));
        recyclerTriangleOthers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //TriangleOthers
        //QuadrilateralsFormulas
        RecyclerView recyclerQuadrilateral = findViewById(R.id.recyclerQuadrilateral);
        QuadrilateralsFormulas.add(new Formula("Brahmagupta's Formula",R.drawable.qarankyunbrah,13));
        QuadrilateralsFormulas.add(new Formula("Area Of A Trapezoid By Sides",R.drawable.sexanmakeres,11));
        QuadrilateralsFormulas.add(new Formula("Parallelogram Formula",R.drawable.zugankqarakusinerigumar,13));
        Formulas.addAll(QuadrilateralsFormulas);
        recyclerQuadrilateral.setAdapter(new CardAdapter(getApplicationContext(), QuadrilateralsFormulas,(Vibrator)getSystemService(Context.VIBRATOR_SERVICE), recyclerLiked, emptyLikes));
        recyclerQuadrilateral.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //QuadrilateralsFormulas
        //polygons
        RecyclerView recyclerPolygon = findViewById(R.id.recyclerPolygon);
        PolygonFormulas.add(new Formula("Area Of A Polygon",R.drawable.mnogougolnikarea,14));
        PolygonFormulas.add(new Formula("Radius Of Excircle",R.drawable.mnogougolnikexcirle,13));
        PolygonFormulas.add(new Formula("Radius Of Incircle",R.drawable.mnogougolnikincircle,14));
        Formulas.addAll(PolygonFormulas);
        recyclerPolygon.setAdapter(new CardAdapter(getApplicationContext(), PolygonFormulas,(Vibrator)getSystemService(Context.VIBRATOR_SERVICE), recyclerLiked, emptyLikes));
        recyclerPolygon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //polygons
        //circle
        RecyclerView recyclerCircle = findViewById(R.id.recyclerCircle);
        CircleFormulas.add(new Formula("Area Of A Circle Sector",R.drawable.shrjanisektor,13));
        CircleFormulas.add(new Formula("Area Of A Circle Segment",R.drawable.shrjanisegment,12));
        CircleFormulas.add(new Formula("Height Of A Circle Segment",R.drawable.shrjanisegmentibardz,11));
        Formulas.addAll(CircleFormulas);
        recyclerCircle.setAdapter(new CardAdapter(getApplicationContext(), CircleFormulas,(Vibrator)getSystemService(Context.VIBRATOR_SERVICE), recyclerLiked, emptyLikes));
        recyclerCircle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //circle
//        Button imageButton = findViewById(R.id.temp);
//        imageButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, FormulaActivity.class)));



//        Liked.put("LikedFormulas",LikedFormulas);
//        documentReference.set(Liked).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Log.d("TAG", "onSuccess: for user"+userID);
//
//            }
//        });


    }
    private void showSignOutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign Out");
        builder.setMessage("Are you sure you want to sign out?");
        builder.setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Sign out the user
                LoginActivity.auth.signOut();
                LoginActivity.mGoogleSignInClient.signOut();
                // Start LoginActivity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                // Finish MainActivity
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
