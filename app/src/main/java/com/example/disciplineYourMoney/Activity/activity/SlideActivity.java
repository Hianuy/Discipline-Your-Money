package com.example.disciplineYourMoney.Activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.disciplineYourMoney.Activity.config.ConfigurationFirebase;
import com.example.disciplineYourMoney.R;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class SlideActivity extends IntroActivity {
    private  Button buttonCadastro;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_slide);


        verifyLoggedInUser();
        buttonCadastro = findViewById(R.id.buttonCadastro);


        setButtonNextVisible(false);
        setButtonBackVisible(false);
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .build()


        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build()


        );

//        addSlide(new FragmentSlide.Builder()
//                .background(android.R.color.white)
//                .fragment(R.layout.intro_3)
//                .build()
//
//
//        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                .build()


        );
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)

                .build()


        );

    }

    public void loginActivity(View view){
        Intent intentLogin = new Intent( SlideActivity.this,LoginActivity.class );
        startActivity( intentLogin );

    }


    public void registerActivity(View view){
//
        startActivity(new Intent(this,RegisterActivity.class));

    }

    public void verifyLoggedInUser(){
        firebaseAuth = ConfigurationFirebase.getFireBaseAuth();

        // se existe um usuario logado
//        firebaseAuth.signOut();
        if(firebaseAuth.getCurrentUser()!= null){
            goingMainActivity();


        }

    }

    public void goingMainActivity(){
        Intent intent_main = new Intent(SlideActivity.this,MainActivity.class);
        startActivity( intent_main );
    }

    @Override
    protected void onStart() {
        super.onStart();
        verifyLoggedInUser();
    }
}

