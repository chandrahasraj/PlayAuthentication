package utils;

import models.Rooms;
import models.UserInfo;
import models.UserLogin;
import play.db.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chandra on 10/31/16.
 */
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
               // System.out.println(user.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static String insertIntoTabl(UserInfo user){
        if(getUserByEmail(user.getEmail())==null){
            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO userinfo(email,firstname,lastname,password,type) VALUES('" +
                        user.getEmail()+"','" + user.getFirstName()+"','"+user.getLastName()+"','"+user.getPassword()+"','"+user.getType()+
                        "')");
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "SUCCESS";
        }else
            return "User Exists";
    }

    public static boolean getFromTabl(UserLogin user){
        UserInfo internal_user = getUserByEmail(user.getEmail());
        return internal_user.getPassword().equals(user.getPassword());
    }

    public static List<Rooms> generateRoomsList(String state,String city){
        String query = "";
        if(state == null && city == null){
            query = "SELECT * FROM rooms";
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
                room.setNo_of_beds(resultSet.getInt("no_of_beds"));
                room.setNo_of_rooms(resultSet.getInt("no_of_rooms"));
                room.setState(resultSet.getString("state"));
                list.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
