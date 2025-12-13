package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.util.Validator;

public abstract class Person extends MedicalEntity {

    private String name;
    private int age;
    private String email;
    private String phoneNumber;

    public Person(Long id, String name, int age, String email, String phoneNumber) {
        super(id);
        this.setName(name);
        this.setAge(age);
        this.setEmail(email);
        this.setPhoneNumber(phoneNumber);
    }

    public Person() {
        super();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        Validator.validateName(name);
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        Validator.validateAge(age);
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        Validator.validateEmail(email);
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        Validator.validatePhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return String.format("Person{id=%d, name='%s', age=%d, email='%s', phone='%s'}", 
                id, name, age, email, phoneNumber);
    }
}

