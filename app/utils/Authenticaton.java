package utils;

import models.*;
import play.db.DB;
import play.mvc.*;
import play.data.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Authenticaton {

    static Connection connection = DB.getConnection();

    public static UserInfo getUserByEmail(String email){
        UserInfo user = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM userinfo where email = '"+email+"'");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                user = new UserInfo();
                user.setEmail(resultSet.getString("email"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setType(resultSet.getInt("type"));
                user.setPassword(resultSet.getString("password"));
                user.setId(resultSet.getInt("id"));
               // System.out.println(user.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static List<Report> getReports(){
        List<Report> reports = new ArrayList<>();
        try {
                PreparedStatement st = connection.prepareStatement("select u.*,o.* from userinfo u join orders o on(o.user_id = u.id)");
                ResultSet resultSet = st.executeQuery();
                while(resultSet.next()){

                    Report order = new Report();
                    order.setN_rooms(resultSet.getInt("n_rooms"));
                    order.setRoom_id(resultSet.getInt("room_id"));
                    order.setUser_id(resultSet.getInt("user_id"));
                    order.setOrder_id(resultSet.getInt("id"));
                    order.setStart_date(resultSet.getString("start_date"));
                    order.setEnd_date(resultSet.getString("end_date"));
//                    order.setPlace(resultSet.getString("place_name"));
                    order.setEmail(resultSet.getString("email"));
                    order.setFirst_name(resultSet.getString("firstname"));
                    order.setLast_name(resultSet.getString("lastname"));
                    reports.add(order);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    public static UserInfo getUserById(int id){
        UserInfo user = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM userinfo where id = '"+id+"'");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                user = new UserInfo();
                user.setEmail(resultSet.getString("email"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setType(resultSet.getInt("type"));
                user.setPassword(resultSet.getString("password"));
                user.setId(resultSet.getInt("id"));
                // System.out.println(user.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void addManager(UserInfo user){
        if(getUserByEmail(user.getEmail())==null){
            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO userinfo(email,firstname,lastname,password,type) VALUES('" +
                        user.getEmail()+"','" + user.getFirstName()+"','"+user.getLastName()+"','password','2')");
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addRoom(Rooms room){
        if(!roomExists(room)){
            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO rooms(place_name,city,state,address,no_of_rooms,no_of_beds) VALUES('" +
                        room.getPlace_name()+"','" + room.getCity()+"','"+room.getState()+"','"+room.getAddress()+"','"+room.getNo_of_rooms()+"','"+room.getNo_of_beds()+"')");
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            try {
                String query = "UPDATE rooms SET " +
                        "place_name='"+room.getPlace_name()+"'," +
                        "city='"+room.getCity()+"'," +
                        "state='"+room.getState()+"'," +
                        "address='"+room.getAddress()+"'," +
                        "no_of_rooms='"+room.getNo_of_rooms()+"'," +
                        "no_of_beds='"+room.getNo_of_beds()+"'" +
                        " where id='"+room.getId()+"'";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void changePassword(int id,String pwd){
        try {
            String query = "UPDATE userinfo SET " +
                    "password='"+pwd+"'" +
                    " where id='"+id+"'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changeName(int id,String firstname,String lastname){
        try {
            String query = "UPDATE userinfo SET " +
                    "firstname='"+firstname+"'," +
                    "lastname='"+lastname+"'"+
                    " where id='"+id+"'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUserByEmail(String email){
        try {
            PreparedStatement statement = connection.prepareStatement("delete FROM userinfo where email = '"+email+"'");
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteRoomById(int id){
        try {
            PreparedStatement statement = connection.prepareStatement("delete FROM rooms where id = '"+id+"'");
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteOrderById(int id){
        try {
            PreparedStatement statement = connection.prepareStatement("delete FROM orders where id = '"+id+"'");
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean roomExists(Rooms room){
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM rooms where place_name = '"+room.getPlace_name()+"' " +
                    "and city = '"+room.getCity()+"' and state='"+room.getState()+"' and address = '"+room.getAddress()+"'");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<UserInfo> getUserByType(int type){
        List<UserInfo> users = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM userinfo where type = '"+type+"'");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                UserInfo user = new UserInfo();
                user.setEmail(resultSet.getString("email"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setType(resultSet.getInt("type"));
                user.setPassword(resultSet.getString("password"));
                user.setId(resultSet.getInt("id"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static String insertIntoTabl(UserInfo user){
        if(getUserByEmail(user.getEmail())==null){
            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO userinfo(email,firstname,lastname,password,type) VALUES('" +
                        user.getEmail()+"','" + user.getFirstName()+"','"+user.getLastName()+"','"+user.getPassword()+"','3')");
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "SUCCESS";
        }else
            return "User Exists";
    }

    public static boolean getFromTabl(UserLogin user){
        if(user!=null && user.getEmail()!=null) {
            UserInfo internal_user = getUserByEmail(user.getEmail());
            if(internal_user!=null)
                return internal_user.getPassword().equals(user.getPassword());
            else
                return false;
        }
        return false;
    }

    public static List<Rooms> generateRoomsList(String state,String city,String email){
        String query = "";

        if(state == null && city == null){
            if(email!=null) {
                int user_id = getUserByEmail(email).getId();
//                System.out.println("Current user id:"+user_id);
                query = "select s.* from rooms s where s.id not in (SELECT r.id FROM rooms r join orders o on(r.id=o.room_id) where o.user_id = '" + user_id + "')";
            }else{
                query = "SELECT * FROM rooms r ";
            }
        }else if(state == null && city!=null){
            query = "SELECT * FROM rooms where state like '%"+city+"%'";
        }else if(state!=null && city ==null){
            query = "SELECT * FROM rooms where state like '%"+state+"%'";
        }else{
            query = "SELECT * FROM rooms where state like '%"+state+"%' and city like '%"+city+"%'";
        }
        return getRoomsList(query);
    }

    private static List<Rooms> getRoomsList(String query){
        List<Rooms> list = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Rooms room = new Rooms();
                room.setAddress(resultSet.getString("address"));
                room.setCity(resultSet.getString("city"));
                room.setNo_of_rooms(resultSet.getInt("no_of_rooms"));
                room.setNo_of_beds(resultSet.getInt("no_of_beds"));
                room.setState(resultSet.getString("state"));
                room.setPlace_name(resultSet.getString("place_name"));
                room.setId(resultSet.getInt("id"));
                list.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Rooms createOrder(int room_id,int no_of_rooms,String start_date,String end_date,String email){
        UserInfo user = getUserByEmail(email);
        List<Order> orders = getOrderByRoomId(room_id,user.getId());
        if( orders.isEmpty()) {
            try {
                PreparedStatement statement = connection.prepareStatement("insert into orders(room_id,n_rooms,start_date,end_date,user_id) values('" + room_id + "','" + no_of_rooms
                        + "','" + start_date + "','" + end_date + "','"+user.getId()+"')");
                Rooms room = getRoomById(room_id);
                String query = "UPDATE rooms SET " +
                        "place_name='"+room.getPlace_name()+"'," +
                        "city='"+room.getCity()+"'," +
                        "state='"+room.getState()+"'," +
                        "address='"+room.getAddress()+"'," +
                        "no_of_rooms='"+(room.getNo_of_rooms() - no_of_rooms)+"'," +
                        "no_of_beds='"+room.getNo_of_beds()+"'" +
                        " where id='"+room.getId()+"'";
                PreparedStatement statement_2 = connection.prepareStatement(query);
                statement_2.execute();
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return getRoomById(room_id);
    }

    public static List<Order> getOrderByRoomId(int room_id,int user_id){
        List<Order> orders =  new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select o.*,r.place_name from orders o,rooms r  where o.room_id <> '"+room_id+"' and" +
                    " o.user_id='"+user_id+"' and r.id <> '"+room_id+"'");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Order order = new Order();
                order.setNo_of_rooms(resultSet.getInt("n_rooms"));
                order.setRoom_id(resultSet.getInt("room_id"));
                order.setUser_id(resultSet.getInt("user_id"));
                order.setId(resultSet.getInt("id"));
                order.setStart_date(resultSet.getString("start_date"));
                order.setEnd_date(resultSet.getString("end_date"));
                order.setPlace_name(resultSet.getString("place_name"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        System.out.println("room id = "+room_id+", user id = "+user_id);
        return orders;
    }

    public static Rooms getRoomById(int id){
        Rooms room = new Rooms();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from rooms where id='"+id+"'");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                room.setAddress(resultSet.getString("address"));
                room.setCity(resultSet.getString("city"));
                room.setNo_of_rooms(resultSet.getInt("no_of_rooms"));
                room.setNo_of_beds(resultSet.getInt("no_of_beds"));
                room.setState(resultSet.getString("state"));
                room.setPlace_name(resultSet.getString("place_name"));
                room.setId(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return room;
    }
}
