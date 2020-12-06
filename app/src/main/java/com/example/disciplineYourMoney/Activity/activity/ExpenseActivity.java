package com.example.disciplineYourMoney.Activity.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import com.example.disciplineYourMoney.Activity.config.ConfigurationFirebase;
import com.example.disciplineYourMoney.Activity.helper.Base64Custom;
import com.example.disciplineYourMoney.Activity.helper.DateUtil;
import com.example.disciplineYourMoney.Activity.model.Movement;
import com.example.disciplineYourMoney.Activity.model.User;
import com.example.disciplineYourMoney.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pd.chocobar.ChocoBar;

public class ExpenseActivity extends AppCompatActivity {

    private ConstraintLayout clCancel;
    private ConstraintLayout clSave;
    private TextInputEditText edtDate, edtCategory, edtDescription;
    private EditText edtValue;
    private Movement movement;
    private DatabaseReference firebaseRef = ConfigurationFirebase.getFirebaseDataBase();
    private FirebaseAuth firebaseAuth = ConfigurationFirebase.getFireBaseAuth();
    private Double expenseTotal;
//    private Double expenseUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        edtValue = findViewById(R.id.edt_value);
        clCancel = findViewById(R.id.cc_cancel);
        clSave = findViewById(R.id.cc_save);
        edtDate = findViewById(R.id.edt_date);
        edtCategory = findViewById(R.id.edt_category);
        edtDescription = findViewById(R.id.edt_description);


        edtDate.setText(DateUtil.dateAtual());


        clCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        clSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExpense();
            }
        });

        recoverExpenseTotal();

    }

    public void saveExpense() {

        if ( isValidFieldExpense() ) {

            String dateChoice = edtDate.getText().toString();

            movement = new Movement();
            Double valueRecover = Double.parseDouble(edtValue.getText().toString());

            movement.setValue( valueRecover );
            movement.setCategory(edtCategory.getText().toString());
            movement.setDescription(edtDescription.getText().toString());
            movement.setDate( dateChoice );
            movement.setType("d");
            Double expenseUpdated = expenseTotal + valueRecover;
            updateExpense( expenseUpdated );
            movement.save( dateChoice );
            barGreen();


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    finishMyActivity();

                }
            }, 1700);
        }


    }

    public Boolean isValidFieldExpense() {

        String textValue            = edtValue.getText().toString();
        String textDate             = edtDate.getText().toString();
        String textCategory         = edtCategory.getText().toString();
        String textDescription      = edtDescription.getText().toString();


        if (textValue.isEmpty() && textDate.isEmpty() && textCategory.isEmpty() && textDescription.isEmpty()) {
            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in all fields")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;


        } else if (textValue.isEmpty() && textDate.isEmpty() && textCategory.isEmpty()) {
            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the value, date and category fields.")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;

        } else if (textValue.isEmpty() && textDate.isEmpty() && textDescription.isEmpty()) {
            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the value, date and description fields.")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;
        } else if (textValue.isEmpty() && textCategory.isEmpty() && textDescription.isEmpty()) {
            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the value, category and description fields.")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;

        } else if (textValue.isEmpty() && textDate.isEmpty() && !((textCategory.isEmpty()) && textDescription.isEmpty())) {

            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the value and date fields")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;
        } else if (textValue.isEmpty() && textCategory.isEmpty() && !((textDate.isEmpty()) && textDescription.isEmpty())) {

            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the value and category fields")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;
        } else if (textValue.isEmpty() && textDescription.isEmpty() && !((textDate.isEmpty()) && textCategory.isEmpty())) {

            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the value and description fields")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;
        } else if (textDate.isEmpty() && textCategory.isEmpty() && !((textValue.isEmpty()) && textDescription.isEmpty())) {

            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the date and category fields")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;
        } else if (textDate.isEmpty() && textDescription.isEmpty() && !((textValue.isEmpty()) && textCategory.isEmpty())) {

            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the date and description fields")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;
        } else if (textCategory.isEmpty() && textDescription.isEmpty() && !((textValue.isEmpty()) && textValue.isEmpty())) {

            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the category and description fields\n")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;
        } else if (textValue.isEmpty() && !((textDate.isEmpty() && textCategory.isEmpty()) && textDescription.isEmpty())) {

            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the value field")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;

        } else if (textDate.isEmpty() && !((textValue.isEmpty() && textCategory.isEmpty()) && textDescription.isEmpty())) {
            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the date field")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;


        } else if (textCategory.isEmpty() && !((textValue.isEmpty()) && textDate.isEmpty() && textDescription.isEmpty())) {
            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the category field")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;


        } else if (textDescription.isEmpty() && !((textValue.isEmpty() && textDate.isEmpty() && textCategory.isEmpty()))) {
            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the description field")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;


        } else if (textValue.isEmpty() && textDate.isEmpty()) {
            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the value and date fields")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;


        } else if (textValue.isEmpty() && textCategory.isEmpty()) {
            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the value and category fields")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;


        } else if (textValue.isEmpty() && textDescription.isEmpty()) {
            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the value and description fields")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;


        } else if (textDate.isEmpty() && textCategory.isEmpty()) {
            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the date and category fields")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;


        } else if (textDate.isEmpty() && textDescription.isEmpty()) {
            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the date and description fields")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;


        } else if (textDescription.isEmpty() && textCategory.isEmpty()) {
            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the description and category fields")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;


        } else if (textDescription.isEmpty() && textCategory.isEmpty()) {
            ChocoBar.builder().setActivity(ExpenseActivity.this)
                    .setText("Fill in the description and category fields")
                    .setDuration(ChocoBar.LENGTH_SHORT)
                    .orange()
                    .show();
            return false;


        }


        if (!textValue.isEmpty()) {
            if (!textDate.isEmpty()) {
                if (!textCategory.isEmpty()) {
                    if (!textDescription.isEmpty()) {
                        return true;
                    }
                }
            }
        }


        return true;
    }

    public void finishMyActivity() {
        finish();
    }



    public void recoverExpenseTotal() {

        String emailUser = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64( emailUser );

        DatabaseReference userRef = firebaseRef.child("users").child(idUser);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                expenseTotal = user.getExpenseTotal();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void updateExpense(Double expense) {
        String emailUser = firebaseAuth.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64(emailUser);

        DatabaseReference userRef = firebaseRef.child("users").child(idUser);

        userRef.child("expenseTotal").setValue( expense );


    }
    public void barGreen() {

        ChocoBar.builder().setActivity(ExpenseActivity.this)
                .setText("Expense Recorded")
                .setDuration(ChocoBar.LENGTH_SHORT)
                .green()
                .show();

    }
}
