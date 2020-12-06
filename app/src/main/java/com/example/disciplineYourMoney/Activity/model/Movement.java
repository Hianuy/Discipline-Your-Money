package com.example.disciplineYourMoney.Activity.model;

import com.example.disciplineYourMoney.Activity.config.ConfigurationFirebase;
import com.example.disciplineYourMoney.Activity.helper.Base64Custom;
import com.example.disciplineYourMoney.Activity.helper.DateUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movement {
    private String date;
    private String category;
    private String description;
    private String type;
    private Double value;
    private String key;
    // salary = categoria
    // monthlySalary = descrição


    public Movement() {
    }


    public void save( String dateChoice ){
        FirebaseAuth auth = ConfigurationFirebase.getFireBaseAuth();
        String idUser = Base64Custom.encodeBase64(auth.getCurrentUser().getEmail());

        String monthYear = DateUtil.MonthYearDateChoice( dateChoice );

        DatabaseReference databaseReference = ConfigurationFirebase.getFirebaseDataBase();

        databaseReference.child("movement")
                         .child( idUser )
                         .child( monthYear )
                         .push()
                         .setValue( this );
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }


}
