package com.example.disciplineYourMoney.Activity.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.disciplineYourMoney.Activity.config.ConfigurationFirebase;
import com.example.disciplineYourMoney.Activity.model.User;
import com.example.disciplineYourMoney.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.pd.chocobar.ChocoBar;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btLogin;
    private User user;
    private FirebaseAuth auth;
    private ProgressBar pbMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail     = findViewById( R.id.edtEmail );
        edtPassword  = findViewById( R.id.edtPassword );
        btLogin      = findViewById( R.id.btLogin );

        pbMain = findViewById( R.id.pbMain );

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail     = edtEmail    .getText().toString();
                String textPassword  = edtPassword . getText().toString();
                // validação


                if (textEmail.isEmpty() && textPassword.isEmpty()) {
                    ChocoBar.builder().setView(view)
                            .setText("Fill in all fields")
                            .setDuration(ChocoBar.LENGTH_SHORT)
                            .red()
                            .show();

                } else if (textEmail.isEmpty() && !(textEmail.isEmpty())) {

                    ChocoBar.builder().setView(view)
                            .setText("Fill in the name field")
                            .setDuration(ChocoBar.LENGTH_SHORT)
                            .red()   // in built red ChocoBar
                            .show();
                } else if (textPassword.isEmpty() && !(textEmail.isEmpty())) {

                    ChocoBar.builder().setView(view)
                            .setText("Fill in the password field")
                            .setDuration(ChocoBar.LENGTH_SHORT)
                            .red()
                            .show();
                }


                if (!textEmail.isEmpty()) {
                    if (!textPassword.isEmpty()) {
                        pbMain.setVisibility(View.VISIBLE);

                        user = new User();
                        user.setEmail(textEmail);
                        user.setPassword(textPassword);
                        validLogin();
                    }
                }
            }
        });
    }

    public void validLogin() {

        auth = ConfigurationFirebase.getFireBaseAuth();
        auth.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()

        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    goingMainActivity();

//
//                    ChocoBar.builder().setActivity(LoginActivity.this)
//                            .setText("Sucesso ao logar")
//                            .setDuration(ChocoBar.LENGTH_SHORT)
//                            .green()  // in built green ChocoBar
//                            .show();

                } else {
                    String exception = "";
                    pbMain.setVisibility(View.GONE);

                    try {
                        throw task.getException();

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        exception = "Email and password do not match a registered user";
                        customBar( exception );

                    } catch (FirebaseAuthInvalidUserException e) {

                        exception = "User is not registered";

                        customBar( exception );

                    }catch (Exception e ){
                        exception = "Error: Check Your Connection";
                        e.printStackTrace();
                        customBarInternalError( exception );
                    }

                }
            }
        });

    }


    public void customBar ( String exception ) {
        ChocoBar.builder().setBackgroundColor(Color.parseColor("#FFB32D26"))
                .setTextSize(12)
                .setTextColor(Color.parseColor("#FFFFFF"))
                .setTextTypefaceStyle(Typeface.BOLD)
                .setText(exception)
                .centerText()
                .setIcon(R.drawable.ic_exclamation_triangle_solid)
                .setActivity(LoginActivity.this)
                .setDuration(ChocoBar.LENGTH_LONG)
                .build()
                .show();

    }

    public void customBarInternalError( String exception ) {
        ChocoBar.builder().setBackgroundColor( Color.parseColor("#FFB32D26") )
                .setTextSize( 13 )
                .setTextColor(Color.parseColor("#FFFFFF"))
                .setTextTypefaceStyle(Typeface.BOLD)
                .setText( exception )
                .centerText()
                .setIcon( R.drawable.ic_wifi_solid )
                .setDuration( ChocoBar.LENGTH_LONG )
                .setActivity( LoginActivity.this )
                .build()
                .show();

    }
    public void goingMainActivity(){
        pbMain.setVisibility(View.GONE);
        Intent intent_main = new Intent(LoginActivity.this,MainActivity.class);
        startActivity( intent_main );
        finish();

    }
}

