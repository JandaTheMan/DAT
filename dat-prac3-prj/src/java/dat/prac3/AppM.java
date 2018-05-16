
package dat.prac3;

import static dat.prac3.FoundM.*;
import static dat.prac3.handler.MailM.*;
import static dat.prac3.handler.AuthM.*;
import dat.prac3.model.dao.*;

import static dat.web.fw.TypesM.*;
import static dat.web.fw.DispatchM.*;
import static dat.web.fw.AppM.*;
import static dat.web.HttpM.*;
import static dat.web.fw.HandlerM.*;
import static dat.web.fw.ContentM.*;
import static dat.web.http.ServletM.*;

import static dat.base.Types.*;
import static dat.base.Funs.*;
import static dat.base.Lists.*;

import javax.servlet.ServletContext;


/**
 * @author Jordi Forga
 */
public class AppM
{

public static class MsgServlet extends AppServlet {
    @Override
    //Comm1<Request,Response> executable Webapp in compatible server
    public Comm1<Request,Response> getApp() throws Exception {
        ServletContext servletContext = getServletContext();
        DAOFactory daoFact = makeDAOFactory(servletContext);
        Msg site = Msg(daoFact);
        return toApp(dispatchMsg, site);
    }
}

/* Change to true for testing with local memory */
// Creation of the data base. This guunctioncreates a memory data base if testing is set to true.
public static final boolean TESTING = true;

private static DAOFactory makeDAOFactory(ServletContext servletContext) throws Exception {
    if (TESTING) {
        DAOFactory daoFact = new dat.prac3.model.memdao.MemDAOFactory();
        UserDAO users = daoFact.getUserDAO();
        users.insert(new User("usuari1", "clau1"));
        users.insert(new User("usuari2", "clau2"));
        users.insert(new User("usuari3", "clau3"));
        users.insert(new User("usuari4", "clau4"));
        return daoFact;
    } else {
    	//Url, user and pasword modification with ldatus18 parameters and jdbc url
    	//TO-DO: preguntar!
        return new dat.prac3.model.sqldao.SqlDAOFactory(
            servletContext.getInitParameter("message_app.db.url"),//jdbc:postgresql://soft0.upc.edu/ldatusr18
            servletContext.getInitParameter("message_app.db.user"),//ldatusr18
            servletContext.getInitParameter("message_app.db.password")//ldatusr18
        );
    }
}


//------------------- Dispatch's instance ------------------------

public static final Dispatch<Msg> dispatchMsg = new Dispatch<Msg>(webAppMsg){
	//dispatchMsg is the function that acts as an application to handle the requests.
    @Override
    public Response dispatch(DispatchEnv<Msg> dispEnv, Request request) throws Exception
    {
        String method = request.method();
        IList<String> segments = request.pathInfo();
        Maybe<Route<Msg>> mbRoute = parseRouteMsg(segments, request.queryString());
        Comm3<DispatchEnv<Msg>,Maybe<Route<Msg>>,Request,Response> runner;
        if (mbRoute.isJust()) {
        	//if the route function contains direction, depending on the direction there will be a handling of the request maped on this route:
            Route<Msg> route = mbRoute.fromJust();
            Handler<Msg,Void> bm = badMethod();
            if (route == HomeR) {
                if (method.equals("GET")) runner = dispatchHandler(webAppMsg, toTypedContentHtml, getHomeR);
                else runner = dispatchHandler(webAppMsg, toTypedContentVoid, bm);
            } else if (route == InboxR) {
                if (method.equals("GET")) runner = dispatchHandler(webAppMsg, toTypedContentHtml, getInboxR);
                else if (method.equals("POST")) runner = dispatchHandler(webAppMsg, toTypedContentHtml, postInboxR);
                else runner = dispatchHandler(webAppMsg, toTypedContentVoid, bm);
            } else if (route == ComposeR) {
                if (method.equals("GET")) runner = dispatchHandler(webAppMsg, toTypedContentHtml, getComposeR);
                else if (method.equals("POST")) runner = dispatchHandler(webAppMsg, toTypedContentHtml, postComposeR);
                else runner = dispatchHandler(webAppMsg, toTypedContentVoid, bm);
            } else if (route instanceof MessageR) {
                int mid = ((MessageR)route).id;
                if (method.equals("GET")) runner = dispatchHandler(webAppMsg, toTypedContentHtml, getMessageR(mid));
                else if (method.equals("POST")) runner = dispatchHandler(webAppMsg, toTypedContentHtml, postMessageR(mid));
                else runner = dispatchHandler(webAppMsg, toTypedContentVoid, bm);
            } else if (LoginR == route) {
                if (method.equals("GET")) runner = dispatchHandler(webAppMsg, toTypedContentHtml, getLoginR);
                else if (method.equals("POST")) runner = dispatchHandler(webAppMsg, toTypedContentVoid, postLoginR);
                else runner = dispatchHandler(webAppMsg, toTypedContentVoid, bm);
            } else if (LogoutR == route) {
                if (method.equals("POST")) runner = dispatchHandler(webAppMsg, toTypedContentVoid, postLogoutR);
                else runner = dispatchHandler(webAppMsg, toTypedContentVoid, bm);
            } else {
                throw new RuntimeException("Case exception: " + route);
            }
        } else {
        	//not found mehod request direction
            Handler<Msg,Void> nf = notFound();
            runner = dispatchHandler(webAppMsg, toTypedContentVoid, nf);
        }
        //run method in the framework that simulate the functional programation
        return runner.run(dispEnv, mbRoute, request);
    }
};

} // End of module

