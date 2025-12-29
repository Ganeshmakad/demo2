package com.vbs.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//models kam karta hai table banane ka models ke andar class name jo rahega vo hi name
// ka table my sql me create hoga
//class name first letter capital must class me describe kiya strings etc
//table me vo hisab se changes honge
//to give the power to create table we use anotation entity and annotation data to
@Entity //gives power to create table
@Data //used for lombok to create gettter setter etc
@AllArgsConstructor// constructors are used to actually store the data
@NoArgsConstructor
public class User {
    @Id  //ye hamne id ko primary key set kiya jiske upar likha hai usko primary key
         // banayega chae koi aur chij ko bhi primary key bana ho to use @Id just above
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto generate id and keep in order for new entry
    int id;  //generate kaise karenge ? using below
    @Column(nullable = false,unique = true)
    String username;
    @Column(nullable = false)
    String password;
    @Column(nullable = false,unique = true)
    String email;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String role;
    @Column(nullable = false)
    double balance;

}
