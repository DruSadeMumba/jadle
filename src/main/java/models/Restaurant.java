package models;

import java.util.Objects;

public class Restaurant {
    private String name;
    private String address;
    private String website;
    private String email;
    private int id;
    private String phone;
    private String zipcode;


    //getters
    public String getName() {return name;}
    public String getAddress() {return address;}
    public String getWebsite() {return website;}
    public String getEmail() {return email;}
    public int getId() {return id;}
    public String getPhone() {return phone;}
    public String getZipcode() {return zipcode;}

    //setters
    public void setName(String name) {this.name = name;}
    public void setAddress(String address) {this.address = address;}
    public void setWebsite(String website) {this.website = website;}
    public void setEmail(String email) {this.email = email;}
    public void setId(int id) {this.id = id;}
    public void setPhone(String phone) {this.phone = phone;}
    public void setZipcode(String zipcode) {this.zipcode = zipcode;}

    public Restaurant(String name, String address, String zipcode, String phone) {
        this.name = name;
        this.address = address;
        this.zipcode = zipcode;
        this.phone = phone;
        this.website = "no website listed";
        this.email = "no email available";
    }
    public Restaurant(String name, String address, String zipcode, String phone, String website, String email) {
        this.name = name;
        this.address = address;
        this.zipcode = zipcode;
        this.phone = phone;
        this.website = website;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return id == that.id && name.equals(that.name) && address.equals(that.address) && website.equals(that.website) && email.equals(that.email) && phone.equals(that.phone) && zipcode.equals(that.zipcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, website, email, id, phone, zipcode);
    }
}
