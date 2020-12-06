package com.example.disciplineYourMoney.Activity.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigurationFirebase {


    private static FirebaseAuth auth;
    private static DatabaseReference reference;
    // retorna a instancia  do FirebaseAuth
    // usando estatico nao precisaremos instanciar toda vez para todo objeto
    // assim utilizar  se pode usar sempre o mesmo objeto
    // metodos estaticos nao precisar instanciar classe para usa-los
    // quero usar o atributo sem quer instanciar automaticamente a classe

    //retorna a instancia do firebaseAuth
    public  static FirebaseAuth getFireBaseAuth(){

        if(auth == null){
            auth = FirebaseAuth.getInstance();

        }
        return  auth;

    }

    // retorna a instancia do firebaseDatabase
    public static DatabaseReference getFirebaseDataBase(){

        if(reference == null  ){
            reference = FirebaseDatabase.getInstance().getReference();
        }
        return  reference;
    }


}
