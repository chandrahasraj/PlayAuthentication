package controllers;

import models.Rooms;
import models.UserLogin;
import models.UserInfo;
import play.mvc.*;
import play.data.*;

import utils.Authenticaton;
import utils.Authorization;
import views.html.*;

import java.util.List;

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
        return ok(index.render("Index", Authorization.isSessionAvailable(ctx()),Authorization.getUser(ctx())));
    }

    public static Result signup_page(){
        Form<UserInfo> form = Form.form(UserInfo.class);
        return ok(register.render("Register", Authorization.isSessionAvailable(ctx()),Authorization.getUser(ctx()),form));
    }
    public static Result signup(){
        Form<UserInfo> userInfoForm = Form.form(UserInfo.class).bindFromRequest();
        String res = Authenticaton.insertIntoTabl(userInfoForm.get());
        Form<UserInfo> form = Form.form(UserInfo.class);
        if(!res.equals("SUCCESS"))
            return ok(register.render("Register", Authorization.isSessionAvailable(ctx()),Authorization.getUser(ctx()),form));
        else
            return ok(index.render("Index", Authorization.isSessionAvailable(ctx()),Authorization.getUser(ctx())));
    }

    public static Result login_page(){
        Form<UserLogin> data = Form.form(UserLogin.class);
        return ok(login.render("Login", Authorization.isSessionAvailable(ctx()),Authorization.getUser(ctx()),data));
    }

    public static Result login(){
        Form<UserLogin> userInfoForm = Form.form(UserLogin.class).bindFromRequest();
        System.out.println(userInfoForm.get().getEmail());
        boolean status = Authenticaton.getFromTabl(userInfoForm.get());
        Form<UserLogin> data = Form.form(UserLogin.class);
        if(!status)
            return ok(login.render("Login", Authorization.isSessionAvailable(ctx()),Authorization.getUser(ctx()),data));
        else {
            session("user",userInfoForm.get().getEmail());
            return ok(index.render("Order", Authorization.isSessionAvailable(ctx()), Authorization.getUser(ctx())));
        }
    }

    public static Result logout(){
        session().clear();
        return redirect(routes.Application.index());
    }

    public static Result list_rooms(){
        List<Rooms> list = Authenticaton.generateRoomsList(null,null);
        return ok();
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