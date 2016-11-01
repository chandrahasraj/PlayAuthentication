package views;

import play.api.Application;
import play.api.GlobalSettings;
import play.api.http.HttpConfiguration;
import play.mvc.Http;
import scala.Function1;

import static play.mvc.Controller.session;


/**
 * Created by chandra on 10/31/16.
 */
public class Global extends play.GlobalSettings {
    public void onStart(play.Application application){
        session("user","abc@email.com");
    }
}
