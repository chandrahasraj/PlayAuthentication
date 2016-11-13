package models;

/**
 * Created by chandra on 10/31/16.
 */
public class Rooms {
    int no_of_rooms,no_of_beds,id;
    String address,state,city,place_name;

    public Rooms(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNo_of_beds() {
        return no_of_beds;
    }

    public void setNo_of_beds(int no_of_beds) {
        this.no_of_beds = no_of_beds;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public int getNo_of_rooms() {
        return no_of_rooms;
    }

    public void setNo_of_rooms(int no_of_rooms) {
        this.no_of_rooms = no_of_rooms;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
