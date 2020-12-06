package com.example.disciplineYourMoney.Activity.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.disciplineYourMoney.Activity.adapter.AdapterMovement;
import com.example.disciplineYourMoney.Activity.config.ConfigurationFirebase;
import com.example.disciplineYourMoney.Activity.helper.Base64Custom;
import com.example.disciplineYourMoney.Activity.model.Movement;
import com.example.disciplineYourMoney.Activity.model.User;
import com.github.clans.fab.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.disciplineYourMoney.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabRecipe;
    private FloatingActionButton fabExpense;

    private MaterialCalendarView mCalendarView;
    private TextView tvSalutation,tvBalance;
    private FirebaseAuth firebaseAuth = ConfigurationFirebase.getFireBaseAuth();
    private DatabaseReference firebaseRef = ConfigurationFirebase.getFirebaseDataBase();
    private DatabaseReference userRef;
    private ValueEventListener valueEventListenerUser;
    private ValueEventListener valueEventListenerMovement;
    private Double expenseTotal = 0.0;
    private Double recipeTotal = 0.0;
    private Double resumeUser = 0.0;
    private RecyclerView recyclerMovements;
    private AdapterMovement adapterMovement;
    private List< Movement >  movements = new ArrayList<>();
    private Movement movement;
    private DatabaseReference movimentRef ;
    private String monthYearSelect;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Discipline your money");


        setSupportActionBar( toolbar );

        mCalendarView = findViewById(R.id.calendar_view);
        tvSalutation = findViewById(R.id.tv_salutation);
        tvBalance = findViewById(R.id.tv_balance);
        recyclerMovements = findViewById(R.id.recycle_movement);
        fabRecipe = (FloatingActionButton) findViewById(R.id.menu_recipe);
        fabExpense = (FloatingActionButton) findViewById(R.id.menu_expenses);

        // configurar adapter
        // configurar recycleView
        adapterMovement = new AdapterMovement( movements,this );

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerMovements.setLayoutManager( layoutManager );
        recyclerMovements.setHasFixedSize( true );
        recyclerMovements.setAdapter( adapterMovement  );




        fabRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecipe();

            }
        });

        fabExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExpense();

            }
        });

        settingCalendar();
        swipe();
        recoverResume();
        recoverMovements();
    }
    public void swipe(){
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragsFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags( dragsFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                Log.i("swipe","Item foi arrastado");
                excludeMovement( viewHolder );

            }
        };
        new ItemTouchHelper( itemTouch ).attachToRecyclerView( recyclerMovements );

    }
    public void excludeMovement(RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("");
        alertDialog.setMessage("Are you sure you really want to delete this move from your account?");
        alertDialog.setCancelable( false );

        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                movement = movements.get( position );

                String emailUser = firebaseAuth.getCurrentUser().getEmail();
                String idUser = Base64Custom.encodeBase64( emailUser );

                movimentRef =firebaseRef.child("movement")
                        .child( idUser )
                        .child( monthYearSelect );
                movimentRef.child( movement.getKey() ).removeValue();
                adapterMovement.notifyItemRemoved( position );
                adapterMovement.notifyDataSetChanged();
                updateBalance();


            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recoverResume();
                adapterMovement.notifyDataSetChanged();


            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();

    }
    public void updateBalance(){

        String emailUser = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64( emailUser );
        userRef = firebaseRef.child("users").child(idUser);


        if(movement.getType().equals("r")){
            recipeTotal = recipeTotal - movement.getValue();
            userRef.child("recipeTotal").setValue( recipeTotal );

        }
        if(movement.getType().equals("d")){
            expenseTotal = expenseTotal - movement.getValue();
            userRef.child("expenseTotal").setValue( expenseTotal );

        }


    }

    public void recoverMovements(){
        String emailUser = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64( emailUser );

        movimentRef = firebaseRef.child("movement")
                                .child( idUser )
                                .child( monthYearSelect );
//        Log.i("dados","dadosRetorno" + " " + monthYearSelect);


        valueEventListenerMovement = movimentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                movements.clear();

                for(DataSnapshot  data:dataSnapshot.getChildren() ){
//                    Log.i("dados","retorno" + data.toString());

                    Movement movement = data.getValue( Movement.class );
                    movement.setKey( data.getKey() );
//                    Log.i("dados","dadosRetorno" + " " + movement.getCategory());
                    movements.add( movement );


                }
                adapterMovement.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void recoverResume(){

        String emailUser = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64( emailUser );

        userRef = firebaseRef.child("users").child(idUser);
//        Log.i("evento","Evento foi adicionado");

        valueEventListenerUser = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user  = dataSnapshot.getValue( User.class );


                expenseTotal = user.getExpenseTotal();
                recipeTotal = user.getRecipeTotal();
                resumeUser = recipeTotal - expenseTotal;

                DecimalFormat decimalFormat  = new DecimalFormat("0.##");
                String resultFormated = decimalFormat.format( resumeUser );

                tvSalutation.setText("Hello, " + user.getName() );
                tvBalance.setText( "US$  " + resultFormated );





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_exit:
                firebaseAuth.signOut();
                startActivity(  new Intent(this,SlideActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addRecipe() {
        startActivity(new Intent(this, RecipeActivity.class));


    }

    public void addExpense() {
        startActivity(new Intent(this, ExpenseActivity.class));


    }
    private void settingCalendar(){

        CharSequence months [] = {"JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER"};
        mCalendarView.setTitleMonths(  months );

        CalendarDay dateCurrent = mCalendarView.getCurrentDate();

        String monthSelectFormated =  String.format("%02d",(dateCurrent.getMonth()+ 1 ));


        monthYearSelect = String.valueOf( monthSelectFormated + dateCurrent.getYear());

        mCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                String monthSelectFormated =  String.format("%02d",(date.getMonth()+ 1 ));


//                Toast.makeText(MainActivity.this,"" + (date.getMonth() + 1) + "/" + date.getYear(),Toast.LENGTH_LONG).show();
                monthYearSelect = String.valueOf( monthSelectFormated + "" + date.getYear());
                movimentRef.removeEventListener( valueEventListenerMovement );
                recoverMovements();
//                Log.i("tt","tt" + " " + monthYearSelect );

            }


        });
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        recoverResume();
//        recoverMovements();
//
//
//    }


    @Override
    protected void onRestart() {
        super.onRestart();
        recoverResume();
        recoverMovements();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.i("onStop"," Evento foi removido! ");
//        userRef.removeEventListener( valueEventListenerUser );
//        movimentRef.removeEventListener( valueEventListenerMovement );
//    }
}
