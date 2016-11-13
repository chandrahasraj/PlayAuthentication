package models;

/**
 * Created by chandra on 10/31/16.
 */
public class Order {

    int no_of_rooms,room_id;

    public Order() {
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getNo_of_rooms() {
        return no_of_rooms;
    }

    public void setNo_of_rooms(int no_of_rooms) {
        this.no_of_rooms = no_of_rooms;
    }
}
