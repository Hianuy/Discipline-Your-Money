package com.example.disciplineYourMoney.Activity.model;

import com.example.disciplineYourMoney.Activity.config.ConfigurationFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

public class User {


    private String idUser;
    private String name;
    private String email;
    private String password;
    private Double recipeTotal  =  0.00;
    private Double expenseTotal =  0.00;

    public User() {
        // se eu fizer um construtor vazio eu passo meus atributos direto no objeto
    }

    public void save(){
        DatabaseReference databaseReference = ConfigurationFirebase.getFirebaseDataBase();
        databaseReference.child("users")
                .child( this.idUser )
//                 set value e o proprio objeto usuario
                .setValue( this );
//        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(this);
    }

    public Double getRecipeTotal() {
        return recipeTotal;
    }

    public void setRecipeTotal(Double recipeTotal) {
        this.recipeTotal = recipeTotal;
    }

    public Double getExpenseTotal() {
        return expenseTotal;
    }

    public void setExpenseTotal(Double expenseTotal) {
        this.expenseTotal = expenseTotal;
    }

    @Exclude
    // remove esses dados na hora de salvar o objeto
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
