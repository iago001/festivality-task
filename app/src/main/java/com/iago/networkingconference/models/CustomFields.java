package com.iago.networkingconference.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

@DatabaseTable(tableName = "customFields")
public class CustomFields {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String fullName;

    @DatabaseField
    private String firstName;

    @DatabaseField
    private String lastName;

    @DatabaseField
    private String email;

    @DatabaseField
    private String publicEmail;

    @DatabaseField
    private String company;

    @DatabaseField
    private String position;

    @DatabaseField
    private String gender;

    @DatabaseField
    private String countryCode;

    @DatabaseField
    private String phone;

    @DatabaseField
    private String city;

    @DatabaseField
    private int age;

    private ArrayList<String> attendeeProviding;

    private ArrayList<String> attendeeLookingFor;

    private ArrayList<String> positionType;

    private ArrayList<String> attendeeType;

    private ArrayList<String> industryTags;

    private ArrayList<String> industryComplimentaryTags;

    @DatabaseField
    private String companySize;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getAttendeeProviding() {
        return attendeeProviding;
    }

    public void setAttendeeProviding(ArrayList<String> attendeeProviding) {
        this.attendeeProviding = attendeeProviding;
    }

    public ArrayList<String> getAttendeeLookingFor() {
        return attendeeLookingFor;
    }

    public void setAttendeeLookingFor(ArrayList<String> attendeeLookingFor) {
        this.attendeeLookingFor = attendeeLookingFor;
    }

    public ArrayList<String> getPositionType() {
        return positionType;
    }

    public void setPositionType(ArrayList<String> positionType) {
        this.positionType = positionType;
    }

    public ArrayList<String> getAttendeeType() {
        return attendeeType;
    }

    public void setAttendeeType(ArrayList<String> attendeeType) {
        this.attendeeType = attendeeType;
    }

    public ArrayList<String> getIndustryTags() {
        return industryTags;
    }

    public void setIndustryTags(ArrayList<String> industryTags) {
        this.industryTags = industryTags;
    }

    public ArrayList<String> getIndustryComplimentaryTags() {
        return industryComplimentaryTags;
    }

    public void setIndustryComplimentaryTags(ArrayList<String> industryComplimentaryTags) {
        this.industryComplimentaryTags = industryComplimentaryTags;
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPublicEmail() {
        return publicEmail;
    }

    public void setPublicEmail(String publicEmail) {
        this.publicEmail = publicEmail;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
