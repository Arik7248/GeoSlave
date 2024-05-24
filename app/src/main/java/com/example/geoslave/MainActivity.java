package com.example.geoslave;

import static com.example.geoslave.Logic.NetworkUtil.CheckNetwork;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.geoslave.Logic.NetworkUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.Toast;

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
    public static  List<List<Formula>> arrFormulas = new ArrayList<>();
    private Activity activity = this;
    public static FirebaseAuth mAuth;
    private ImageButton signOutButton;
    public FirebaseFirestore fStore;
    public static DocumentReference documentReference;
    public static Map<String,Object> Liked = new HashMap<>();
    private ProgressDialog progressDialog;
    private Dialog CustomProgressDialog;
    ArrayList<CardAdapter> adapters = new ArrayList<>();
    LikeAdapter LikedAdapter;
    ArrayList<RecyclerView> recyclerViews = new ArrayList<>();
    ArrayList<TextView> TextViews = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));


        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerViews.add(findViewById(R.id.recyclerTriangleS));
        recyclerViews.add(findViewById(R.id.recyclerTriangleOthers));
        recyclerViews.add(findViewById(R.id.recyclerQuadrilateral));
        recyclerViews.add(findViewById(R.id.recyclerCircle));
        recyclerViews.add(findViewById(R.id.recyclerLikes));
        TextViews.add(findViewById(R.id.textView6));
        TextViews.add(findViewById(R.id.textview61));
        TextViews.add(findViewById(R.id.textview62));
        TextViews.add(findViewById(R.id.textview63));
        TextViews.add(findViewById(R.id.textview64));
        TextViews.add(findViewById(R.id.textview65));
        TextViews.add(findViewById(R.id.textview66));
        for (int i = 0; i < TextViews.size(); i++){
            TextViews.get(i).setVisibility(View.GONE);
        }
        //progressDialog = new ProgressDialog(this);
        //progressDialog.setMessage("Loading...");
        //progressDialog.setCancelable(false);
        //progressDialog.setBa
        //progressDialog.show();
        showCustomProgressDialog();
        if (!LikedFormulas.isEmpty()) {
            LikedFormulas.clear();
        }
        documentReference = fStore.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        CheckNetwork(getApplicationContext(), activity);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                CheckNetwork(getApplicationContext(), activity);
                if (documentSnapshot.exists()) {
                    Map<String, Object> data = documentSnapshot.getData();
                    if (data != null && data.containsKey("LikedFormulas")) {
                        // Get the LikedFormulas from the document data
                        LikedFormulas = ((List<Map>) data.get("LikedFormulas")).stream().map(el ->
                                        new Formula((String) el.get("name"),
                                                Math.toIntExact((Long) el.get("image")),
                                                Math.toIntExact((Long) el.get("textSize")),
                                                (String) el.get("type"),
                                                (String) el.get("URL")))
                                .collect(Collectors.toList());

                        // Now LikedFormulas has the data from Firestore
                        Log.d("TAG", "LikedFormulas: " + LikedFormulas);
                    } else {
                        Log.d("TAG", "LikedFormulas not found in document");
                    }
                } else {
                    Log.d("TAG", "Document does not exist");
                }

                LikedAdapter = new LikeAdapter(activity, getApplicationContext(), MainActivity.LikedFormulas, recyclerViews,
                        (Vibrator)getSystemService(Context.VIBRATOR_SERVICE),adapters,LikedAdapter);
                setupRecyclerView();
                //progressDialog.dismiss();
                dismissCustomProgressDialog();
                for (int i = 0; i < TextViews.size(); i++){
                    TextViews.get(i).setVisibility(View.VISIBLE);
                }
                signOutButton = findViewById(R.id.LogOut);
                View.OnClickListener SignOutlistener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CheckNetwork(getApplicationContext(), activity);
                        showSignOutConfirmationDialog();
                    }
                };
                signOutButton.setOnClickListener(SignOutlistener);
                TextViews.get(0).setOnClickListener(SignOutlistener);

                setupOtherRecyclers();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "Error fetching document", e);
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
            }
        });
//        new Handler().postDelayed(() -> {
//
//        },1400);
    }
    private void setupRecyclerView() {
        CheckNetwork(getApplicationContext(), activity);
        if (LikedFormulas.isEmpty()) {
            recyclerViews.get(4).setVisibility(View.GONE);
        } else {
            recyclerViews.get(4).setVisibility(View.VISIBLE);
            recyclerViews.get(4).setAdapter(LikedAdapter);
            recyclerViews.get(4).setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }
    }
    private void setupOtherRecyclers(){
        CreateFormulas();
        Formulas.addAll(CircleFormulas);
        arrFormulas.add(TrianglesFormulasS);
        arrFormulas.add(TrianglesFormulasOther);
        arrFormulas.add(QuadrilateralsFormulas);
        arrFormulas.add(CircleFormulas);
        for (int i = 0; i < 4; i++){
            adapters.add(new CardAdapter(activity, getApplicationContext(), arrFormulas.get(i),
                    (Vibrator)getSystemService(Context.VIBRATOR_SERVICE), recyclerViews, LikedAdapter,adapters));
            recyclerViews.get(i).setAdapter(adapters.get(i));
            recyclerViews.get(i).setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }
    }
    private void CreateFormulas(){
        TrianglesFormulasS.add(new Formula("Heron's Formula",R.drawable.heron,15,"Heron","https://en.wikipedia.org/wiki/Heron%27s_formula"));
        TrianglesFormulasS.add(new Formula("Area By height and side",R.drawable.bardzrutyuntomakeres,12,"AreaByHeightAndSide","https://en.wikipedia.org/wiki/Area_of_a_triangle#"));
        TrianglesFormulasS.add(new Formula("Area By Two Sides One Angle",R.drawable.areaby2sides1angle,11,"AreaByTwoSidesOneAngle","https://en.wikipedia.org/wiki/Area_of_a_triangle#Using_trigonometry"));
        //TrianglesFormulasS.add(new Formula("Area By One Side Two Angles",R.drawable.areaby1side2angles,11,"AreaByOneSideTwoAngles","https://en.wikipedia.org/wiki/Area_of_a_triangle#Using_trigonometry"));
        TrianglesFormulasS.add(new Formula("Area By Incircle",R.drawable.areabyincircle,13,"AreaByIncircle","https://en.wikipedia.org/wiki/Incircle_and_excircles#Incircle_and_its_radius_properties"));
        TrianglesFormulasS.add(new Formula("Area By Excircle",R.drawable.areabyexcircle,13,"AreaByExcirle","https://en.wikipedia.org/wiki/Incircle_and_excircles#Exradii"));
        Formulas.addAll(TrianglesFormulasS);
        TrianglesFormulasOther.add(new Formula("Formula of cosines",R.drawable.cosinusneri,14,"Cosines","https://en.wikipedia.org/wiki/Law_of_cosines"));
        TrianglesFormulasOther.add(new Formula("Formula of sines",R.drawable.sinusneri,14,"Sines","https://en.wikipedia.org/wiki/Law_of_sines"));
        TrianglesFormulasOther.add(new Formula("Height of a triangle",R.drawable.erankyunbardzrutyun,14,"Height","https://en.wikipedia.org/wiki/Triangle#Area"));
        TrianglesFormulasOther.add(new Formula("Bisector of a triangle",R.drawable.erankyunkisord,14,"Bisector","https://en.wikipedia.org/wiki/Angle_bisector_theorem#Length_of_the_angle_bisector"));
        TrianglesFormulasOther.add(new Formula("Median of a triangle",R.drawable.erankyunmijnagic,14,"Median","https://en.wikipedia.org/wiki/Median_(geometry)#Formulas_involving_the_medians'_lengths"));
        Formulas.addAll(TrianglesFormulasOther);
        QuadrilateralsFormulas.add(new Formula("Brahmagupta's Formula",R.drawable.qarankyunbrah,13,"Brahmagupt","https://en.wikipedia.org/wiki/Brahmagupta%27s_formula"));
        QuadrilateralsFormulas.add(new Formula("Area Of A Trapezoid By Sides",R.drawable.sexanmakeres,11,"AreaOfATrapezoidBySides","https://en.wikipedia.org/wiki/Trapezoid#Area"));
        QuadrilateralsFormulas.add(new Formula("Area Of A Parallelogram",R.drawable.zugankqarakusinerigumar,12,"AreaOfAParallelogram","https://en.wikipedia.org/wiki/Parallelogram#Area_formula"));
        Formulas.addAll(QuadrilateralsFormulas);
//        PolygonFormulas.add(new Formula("Area Of A Polygon",R.drawable.mnogougolnikarea,14,"AreaOfAPolygon","https://en.wikipedia.org/wiki/Regular_polygon#Area"));
//        PolygonFormulas.add(new Formula("Radius Of Excircle",R.drawable.mnogougolnikexcirle,13,"PolygonRadiusOfExcircle","https://www.mathopenref.com/polygoncircumcircle.html"));
//        PolygonFormulas.add(new Formula("Radius Of Incircle",R.drawable.mnogougolnikincircle,14,"PolygonRadiusOfIncircle","https://www.mathopenref.com/polygonincircle.html"));
//        Formulas.addAll(PolygonFormulas);
        CircleFormulas.add(new Formula("Area Of A Circle Sector",R.drawable.shrjanisektor,13,"AreaSector","https://en.wikipedia.org/wiki/Circular_sector#Area"));
        //CircleFormulas.add(new Formula("Area Of A Circle Segment",R.drawable.shrjanisegment,12,"AreaSegment","https://en.wikipedia.org/wiki/Circular_segment#Arc_length_and_area"));
        CircleFormulas.add(new Formula("Height Of A Circle Segment",R.drawable.shrjanisegmentibardz,11,"HeightSegment","https://en.wikipedia.org/wiki/Circular_segment#Chord_length_and_height"));
    }
    private void showSignOutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_sign_out, null);
        builder.setView(dialogView);

        TextView title = dialogView.findViewById(R.id.dialog_title);
        TextView message = dialogView.findViewById(R.id.dialog_message);
        Button positiveButton = dialogView.findViewById(R.id.dialog_positive_button);
        Button negativeButton = dialogView.findViewById(R.id.dialog_negative_button);

        AlertDialog dialog = builder.create();

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckNetwork(getApplicationContext(), activity);
                // Sign out the user
                LoginActivity.auth.signOut();
                mAuth.signOut();
                clearAllRecyclerViews();
                FirebaseAuth.getInstance().signOut();
                LoginActivity.mGoogleSignInClient.signOut();
                for (int i = 0; i < TextViews.size(); i++){
                    TextViews.get(i).setVisibility(View.GONE);
                }
                recyclerViews.get(4).setVisibility(View.GONE);
                // Start LoginActivity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                // Finish MainActivity
                finish();
                dialog.dismiss();
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckNetwork(getApplicationContext(), activity);
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void showCustomProgressDialog() {
        // Inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View customView = inflater.inflate(R.layout.custom_progress_dialog, null);

        // Create a Dialog with the custom view and custom style
        CustomProgressDialog = new Dialog(this, R.style.CustomDialog);
        CustomProgressDialog.setContentView(customView);
        CustomProgressDialog.setCancelable(false);
        if (CustomProgressDialog.getWindow() != null) {
            CustomProgressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            CustomProgressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            CustomProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }// Optional: make it non-cancelable
        CustomProgressDialog.show();
    }


    private void dismissCustomProgressDialog() {
        if (CustomProgressDialog != null && CustomProgressDialog.isShowing()) {
            CustomProgressDialog.dismiss();
        }
    }
    private void clearAllRecyclerViews() {
        for (int i = 0; i < 4; i++){
            arrFormulas.get(i).clear();
            adapters.get(i).updateDataList(arrFormulas.get(i));
        }
    }
}
