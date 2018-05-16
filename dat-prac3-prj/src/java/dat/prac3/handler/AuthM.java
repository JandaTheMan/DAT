
package dat.prac3.handler;

import static dat.prac3.FoundM.*;
import dat.prac3.model.dao.*;

import static dat.web.fw.TypesM.*;
import static dat.web.fw.HandlerM.*;
import static dat.web.fw.AppM.*;
import static dat.web.fw.ContentM.*;
import static dat.web.fw.HtmlM.*;
import static dat.web.fw.WidgetM.*;

import static dat.base.Types.*;
import static dat.base.Lists.*;

import java.util.*;
import java.text.SimpleDateFormat;


/**
 * @author Jordi Forga
 */
public class AuthM {

    /**
     * Default destination on successful login, if no other destination exists.
     */
    public static final Route<Msg> loginDest = HomeR;

    /**
     * Default destination on successful logout, if no other destination exists.
     */
    public static final Route<Msg> logoutDest = HomeR;


    public static final Handler<Msg,Html> getLoginR = webAppMsg.defaultLayout(new Widget<Msg>(){
        @Override
        public Void run(WidgetBuilder<Msg> wbuilder, HandlerData<Msg> henv) throws Exception {
            setTitle(toHtmlString.apply("Login"), wbuilder);
            final Maybe<Html> mbErrMessage = getMessage(henv);
            toWidgetHtmlUrl($( dat.web.fw.html.TemplateM.htmlTemplFile("Route<Msg>", "login.html") ), wbuilder, henv);
            return null;
        }
    });

    public static final Handler<Msg,Void> postLoginR = new Handler<Msg,Void>(){
        @Override
        public Void run(HandlerData<Msg> henv) throws Exception {
            Maybe<String> mbName = lookupParam("name", henv);
            Maybe<String> mbPasswd = lookupParam("password", henv);
            if (mbName.isJust() && mbPasswd.isJust()) {
                // Autentifica un usuari.
                Maybe<Entity<User>> selected = getSite(henv).daoFact.getUserDAO().selectByName(mbName.fromJust());
                if (selected.isNothing() || !selected.fromJust().value.password.equals(mbPasswd.fromJust())) {
                    setMessage("Error d'autenticació", henv);
                    return redirect(LoginR, henv);
                } else {
                    setSession(userIdKey, Integer.toString(selected.fromJust().id), henv);
                    return redirect(loginDest, henv);
                }
            } else {
                return invalidArgs(mkIList("name", "password"), henv);
            }
        }
    };

    public static final Handler<Msg,Void> postLogoutR = new Handler<Msg,Void>(){
        @Override
        public Void run(HandlerData<Msg> henv) throws Exception {
            deleteSession(userIdKey, henv);
            return redirect(logoutDest, henv);
        }
    };

}

