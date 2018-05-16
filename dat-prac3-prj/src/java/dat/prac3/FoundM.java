
package dat.prac3;

import static dat.web.fw.TypesM.*;
import static dat.web.fw.AppM.*;
import static dat.web.fw.ContentM.*;
import static dat.web.fw.HandlerM.*;
import static dat.web.fw.WidgetM.*;
import static dat.web.fw.HtmlM.*;

import dat.prac3.model.dao.DAOFactory;
import dat.prac3.model.dao.Entity;
import dat.prac3.model.dao.User;

import static dat.base.Types.*;
import static dat.base.Lists.*;
import static dat.base.Funs.*;


public class FoundM {

    //------------------- Site type definition ------------------------

    public static class Msg
    {
        public final DAOFactory daoFact;

        public Msg(DAOFactory daoFact) {
          this.daoFact = daoFact;
        }
    }
    public static Msg Msg(DAOFactory daoFact) { return new Msg(daoFact); }


    //------------------- WebApp's instance ------------------------

    public static WebApp<Msg> webAppMsg = new WebApp<Msg>(){
        // Redefine methods ...
        @Override
        public Html defaultLayout(Widget<Msg> wdgt, HandlerData<Msg> henv) throws Exception
        {
            final Msg app = getSite(henv);
            final Maybe<Html> mbErrMessage = getMessage(henv);
            final Maybe<Entity<User>> mbUser = maybeUser(henv);
            final PageContent<Route<Msg>> page = widgetToPageContent(wdgt, henv);
            return withUrlRenderer($( dat.web.fw.html.TemplateM.htmlTemplFile("Route<Msg>", "default-layout.html") ), henv);
        }
        @Override
        public AuthzResult isAuthorized(Route<Msg> route, boolean isWrite, HandlerData<Msg> henv) throws Exception {
            Maybe<Entity<User>> mbUser = maybeUser(henv);
            if (route == InboxR) {
                if (mbUser.isNothing()) return AuthzResult.AuthenticationRequired;
                else return AuthzResult.Authorized;
            } else if (route instanceof MessageR) {
                if (mbUser.isNothing()) return AuthzResult.AuthenticationRequired;
                else return AuthzResult.Authorized;
            } else if (route == ComposeR) {
                if (mbUser.isNothing() && isWrite) return AuthzResult.AuthenticationRequired;
                else return AuthzResult.Authorized;
            } else {
                return AuthzResult.Authorized;
            }
        }
        @Override
        public Maybe<Route<Msg>> authRoute(Msg site) {
            return Just(LoginR);
        }
    };

    // Session's key for the user id
    public static final String userIdKey = "userId";

    // Get user from the session
    public static Maybe<Entity<User>> maybeUser(HandlerData<Msg> henv) throws Exception {
        Maybe<String> mbUserId = lookupSession(userIdKey, henv);
        if (mbUserId.isNothing()) {
            return Nothing();
        } else {
            int userId = Integer.parseInt(mbUserId.fromJust());
            Maybe<User> mbUser = getSite(henv).daoFact.getUserDAO().get(userId);
            if (mbUser.isNothing()) {
                return Nothing();
            } else {
                return Just(Entity.Entity(userId, mbUser.fromJust()));
            }
        }
    }

    public static Entity<User> requireUser(HandlerData<Msg> henv) throws Exception {
        Maybe<Entity<User>> mbUser = maybeUser(henv);
        if (mbUser.isNothing()) {
            return redirectLogin(henv);
        } else {
            return mbUser.fromJust();
        }
    }

    public static <A> A redirectLogin(HandlerData<Msg> henv) throws Exception {
        setUltDestCurrent(henv);
        Msg app = getSite(henv);
        Maybe<Route<Msg>> mbar = webAppMsg.authRoute(app);
        if (mbar.isJust()) {
            return redirect(mbar.fromJust(), henv);
        } else {
            return permissionDenied("Please configure authRoute", henv);
        }
    }


    //------------------- Definition of routes ------------------------

    public static final Route<Msg> HomeR = new Route<Msg>() {
        public Pair<IList<String>,IList<Pair<String,String>>> render() { return Pair(Nil(String.class), emptyParams); }
    };

    public static final Route<Msg> LoginR = new Route<Msg>() {
        public Pair<IList<String>,IList<Pair<String,String>>> render() { return Pair(mkIList("login"), emptyParams); }
    };

    public static final Route<Msg> LogoutR = new Route<Msg>() {
        public Pair<IList<String>,IList<Pair<String,String>>> render() { return Pair(mkIList("logout"), emptyParams); }
    };

    public static final Route<Msg> InboxR = new Route<Msg>() {
        public Pair<IList<String>,IList<Pair<String,String>>> render() { return Pair(mkIList("inbox"), emptyParams); }
    };

    public static final Route<Msg> ComposeR = new Route<Msg>() {
        public Pair<IList<String>,IList<Pair<String,String>>> render() { return Pair(mkIList("compose"), emptyParams); }
    };

    public static class MessageR extends Route<Msg> {
        public final int id;
        public MessageR(int id) { this.id = id; }
        public Pair<IList<String>,IList<Pair<String,String>>> render() { return Pair(mkIList("inbox", Integer.toString(id)), emptyParams); }
    }
    public static Route<Msg> MessageR(int id) { return new MessageR(id); }

    public static class StaticR extends Route<Msg> {
        public final IList<String> path;
        public StaticR(IList<String> path) { this.path = path; }
        public Pair<IList<String>,IList<Pair<String,String>>> render() { return Pair(Cons("recursos", path), emptyParams); }
    }
    public static Route<Msg> StaticR(IList<String> path) { return new StaticR(path); }
    public static Route<Msg> StaticR(String... path) { return new StaticR(mkIList(path)); }


    public static Maybe<Route<Msg>> parseRouteMsg(IList<String> segments, IList<Pair<String,String>> params)
    {
        if (segments.isNil()) {
            return Just(HomeR);
        } else {
            String s1 = segments.head();
            IList<String> t1 = segments.tail();
            if (s1.equals("login") && t1.isNil()) {
                return Just(LoginR);
            }
            if (s1.equals("logout") && t1.isNil()) {
                return Just(LogoutR);
            }
            if (s1.equals("recursos") && ! t1.isNil()) {
                return Just(StaticR(t1));
            }
            if (s1.equals("inbox") && t1.isNil()) {
                return Just(InboxR);
            }
            if (s1.equals("compose") && t1.isNil()) {
                return Just(ComposeR);
            }
            if (s1.equals("inbox") && ! t1.isNil()) {
                String s2 = t1.head();
                IList<String> t2 = t1.tail();
                if (t2.isNil()) {
                    return Just(MessageR(Integer.parseInt(s2)));
                }
            }
            return Nothing();
        }
    }

} // End of module

