package com.example.api.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

//#user-case-classes
public final class User {
    public final String name;
    public final int age;
    public final String countryOfResidence;

    @JsonCreator
    public User(@JsonProperty("name") String name, @JsonProperty("age") int age,
                @JsonProperty("countryOfRecidence") String countryOfResidence) {
        this.name = name;
        this.age = age;
        this.countryOfResidence = countryOfResidence;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", countryOfResidence='" + countryOfResidence + '\'' +
                '}';
    }
}
