package controllers;

import models.Order;
import models.Rooms;
import models.UserLogin;
import models.UserInfo;
import play.mvc.*;
import play.data.*;

import utils.Authenticaton;
import utils.Authorization;
import views.html.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static play.data.Form.form;

public class Application extends Controller {


    static{
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            // Log or abort here
        }
    }


    public static Result index() {
        String email = session().get("user");
        String s_type=session().get("type");

        System.out.println(email+","+s_type);
        if(email!=null && s_type!=null) {
            int type = Integer.valueOf(s_type);
            return returnPageBasedOnType(type);
        }else {
            return ok(index.render("Index", Authorization.isSessionAvailable(ctx()), Authorization.getUser(ctx()), 0));
        }
    }

    public static Result signup_page(){
        if(session().get("user")==null) {
            Form<UserInfo> form = Form.form(UserInfo.class);
            return ok(register.render("Register", Authorization.isSessionAvailable(ctx()),Authorization.getUser(ctx()),form,0));
        }else{
            int type= Integer.valueOf(session().get("type"));
            return returnPageBasedOnType(type);
        }
    }
    public static Result signup(){
        Form<UserInfo> userInfoForm = Form.form(UserInfo.class).bindFromRequest();
        String res = Authenticaton.insertIntoTabl(userInfoForm.get());
        Form<UserInfo> form = Form.form(UserInfo.class);
        if(!res.equals("SUCCESS"))
            return ok(register.render("Register", Authorization.isSessionAvailable(ctx()),Authorization.getUser(ctx()),form,0));
        else
            return ok(index.render("Index", Authorization.isSessionAvailable(ctx()),Authorization.getUser(ctx()),0));
    }

    public static Result login_page(){
        if(session().get("user")==null) {
            Form<UserLogin> data = Form.form(UserLogin.class);
            return ok(login.render("Login", Authorization.isSessionAvailable(ctx()), Authorization.getUser(ctx()), data,0));
        }else{
            int type= Integer.valueOf(session().get("type"));
            return returnPageBasedOnType(type);
        }
    }

    public static Result login(){
        Form<UserLogin> userInfoForm = Form.form(UserLogin.class).bindFromRequest();
//        System.out.println(userInfoForm.get().getEmail());
        boolean status = Authenticaton.getFromTabl(userInfoForm.get());
        Form<UserLogin> data = Form.form(UserLogin.class);
        if(!status)
            return ok(login.render("Login", Authorization.isSessionAvailable(ctx()),Authorization.getUser(ctx()),data,0));
        else {
            UserInfo user = Authenticaton.getUserByEmail(userInfoForm.get().getEmail());
            session("user",user.getEmail());
            session("type",String.valueOf(user.getType()));
            int type= Integer.valueOf(session().get("type"));
            return returnPageBasedOnType(type);
        }
    }

    public static Result returnPageBasedOnType(int type){
//        UserInfo user = Authenticaton.getUserByEmail(email);
//        int type = user.getType();
        if(type == 1) {
            //admin
            Form<UserInfo> managerForm = Form.form(UserInfo.class);
            return ok(admin.render("Admin", Authorization.isSessionAvailable(ctx()), Authorization.getUser(ctx()), Authenticaton.getUserByType(3),Authenticaton.getUserByType(2), managerForm,type));
        }else if(type == 2){
            Form<Rooms> roomsForm = Form.form(Rooms.class);
            return ok(manager.render("Manager", Authorization.isSessionAvailable(ctx()), Authorization.getUser(ctx()),list_rooms(session().get("user")),roomsForm,type));
        }else{
            Form<Order> roomsForm = Form.form(Order.class);
            return ok(rooms.render("Rooms", Authorization.isSessionAvailable(ctx()), Authorization.getUser(ctx()), list_rooms(session().get("user")), roomsForm,type));
        }
    }

    public static Result logout(){
        session().clear();
        return redirect(routes.Application.index());
    }

    public static List<Rooms> list_rooms(String email){
        List<Rooms> list = Authenticaton.generateRoomsList(null,null,email);
        return list;
    }

    public static Result rooms(){
        Form<Order> roomsForm = Form.form(Order.class);
        int type= Integer.valueOf(session().get("type"));
        String email = session().get("user");
        return ok(rooms.render("Rooms", Authorization.isSessionAvailable(ctx()), Authorization.getUser(ctx()),list_rooms(email),roomsForm,type));
    }

    public static Result booked_rooms(){

        String email = session().get("user");
        UserInfo user = Authenticaton.getUserByEmail(email);
        List<Rooms> rooms = list_rooms(email);
        List<Order> ordersList = new ArrayList<>();
        for(Rooms room:rooms)
         ordersList.addAll(Authenticaton.getOrderByRoomId(room.getId(),user.getId()));
        int type= Integer.valueOf(session().get("type"));

        return ok(booked_rooms.render("Rooms", Authorization.isSessionAvailable(ctx()), Authorization.getUser(ctx()),rooms,ordersList,type));
    }

    public static Result order(){
        final Map<String, String[]> form_values = request().body().asFormUrlEncoded();
        int room_id = Integer.parseInt(form_values.get("room_id")[0]);
        int no_of_rooms = Integer.parseInt(form_values.get("n_rooms")[0]);
        String start_date = form_values.get("start-date")[0];
        String end_date = form_values.get("end-date")[0];
        String email = session().get("user");
//        String card_number = form_values.get("cardNumber")[0];
//        String card_expiry = form_values.get("cardExpiry")[0];
//        String card_cvc = form_values.get("cardCVC")[0];
//        System.out.println(form_values);
        Rooms status = Authenticaton.createOrder(room_id,no_of_rooms,start_date,end_date,email);
        if(status!=null) {
            int type= Integer.valueOf(session().get("type"));
            return ok(order.render("Order", Authorization.isSessionAvailable(ctx()), Authorization.getUser(ctx()), status,type,start_date,end_date));
        }else
            return internalServerError("Oops");
    }

    public static Result addManager(){
        final Map<String, String[]> form_values = request().body().asFormUrlEncoded();
        System.out.println(form_values);
        UserInfo user = new UserInfo();
        user.setEmail(form_values.get("email")[0]);
        user.setFirstName(form_values.get("firstName")[0]);
        user.setLastName(form_values.get("lastName")[0]);
        user.setType(2);
        user.setPassword("password");

        Authenticaton.addManager(user);
        int type= Integer.valueOf(session().get("type"));
        Form<UserInfo> managerForm = Form.form(UserInfo.class);
        return ok(admin.render("Admin", Authorization.isSessionAvailable(ctx()), Authorization.getUser(ctx()), Authenticaton.getUserByType(3),Authenticaton.getUserByType(2), managerForm,type));
    }

    public static Result deleteManager(String email){
        Authenticaton.deleteUserByEmail(email);
        int type= Integer.valueOf(session().get("type"));
        return returnPageBasedOnType(type);
//        Form<UserInfo> managerForm = Form.form(UserInfo.class);
//        return ok(admin.render("Admin", Authorization.isSessionAvailable(ctx()), Authorization.getUser(ctx()), Authenticaton.getUserByType(3),Authenticaton.getUserByType(2), managerForm));
    }

    public static Result deleteRoom(int id){
        Authenticaton.deleteRoomById(id);
        Form<Rooms> roomsForm = Form.form(Rooms.class);
        int type= Integer.valueOf(session().get("type"));
        return ok(manager.render("Manager", Authorization.isSessionAvailable(ctx()), Authorization.getUser(ctx()),list_rooms(session().get("user")),roomsForm,type));
    }

    public static Result addRoom(){
        final Map<String, String[]> form_values = request().body().asFormUrlEncoded();
//        System.out.println(form_values);
        Rooms room = new Rooms();
        room.setAddress(form_values.get("address")[0]);
        room.setCity(form_values.get("city")[0]);
        room.setState(form_values.get("state")[0]);
        room.setPlace_name(form_values.get("place_name")[0]);
        room.setNo_of_beds(Integer.valueOf(form_values.get("no_of_beds")[0]));
        room.setNo_of_rooms(Integer.valueOf(form_values.get("no_of_rooms")[0]));
        if(form_values.get("id_num")!=null)
            room.setId(Integer.valueOf(form_values.get("id_num")[0]));
        Authenticaton.addRoom(room);
        int type= Integer.valueOf(session().get("type"));
        Form<Rooms> roomsForm = Form.form(Rooms.class);
        return ok(manager.render("Manager", Authorization.isSessionAvailable(ctx()), Authorization.getUser(ctx()),list_rooms(session().get("user")),roomsForm,type));
    }

//    public static Result sample(String page){
//        if(page.equals("loginForm"))
//            return ok(main.render("loginForm",login.render()));
//        else
//            return ok(main.render("register data ",register.render()));
//    }
//
//    public static Result register(){
//        DynamicForm requestData = form().bindFromRequest();
//        String username = requestData.get("username");
//        String email = requestData.get("email");
//        String password = requestData.get("password");
//        String type = requestData.get("icode");
//        return ok("Hello " + username + " " + type);
//    }
}

/*
    Returning Pages
    Result ok = ok("Hello world!");
Result notFound = notFound();
Result pageNotFound = notFound("<h1>Page not found</h1>").as("text/html");
Result badRequest = badRequest(views.html.form.render(formWithErrors));
Result oops = internalServerError("Oops");
Result anyStatus = status(488, "Strange response type");
 */