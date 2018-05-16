
package dat.prac3.handler;

import static dat.prac3.FoundM.*;
import dat.prac3.model.dao.*;
import java.util.*;
import static dat.web.fw.TypesM.*;
import static dat.web.fw.HandlerM.*;
import static dat.web.fw.AppM.*;
import static dat.web.fw.ContentM.*;
import static dat.web.fw.HtmlM.*;
import static dat.web.fw.WidgetM.*;

import static dat.base.Types.*;
import static dat.base.Funs.*;
import static dat.base.Lists.*;

import java.util.List;


/**
 * @author Jordi Forga
 */
public class MailM {

    public static final Handler<Msg,Html> getHomeR = webAppMsg.defaultLayout(new Widget<Msg>(){
        @Override
        public Void run(WidgetBuilder<Msg> wbuilder, HandlerData<Msg> henv) throws Exception {
        	//It is not necessary to reidirect to the login if the user is not connected
            final Maybe<Entity<User>> mbUser = maybeUser(henv);
            setTitle(toHtmlString.apply("Home"), wbuilder);
            toWidgetHtmlUrl($( dat.web.fw.html.TemplateM.htmlTemplFile("Route<Msg>", "home.html") ), wbuilder, henv);
            return null;
        }
    });

    public static final Handler<Msg,Html> getInboxR = webAppMsg.defaultLayout(new Widget<Msg>(){
        @Override
        public Void run(WidgetBuilder<Msg> wbuilder, HandlerData<Msg> henv) throws Exception {
            Entity<User> user = requireUser(henv);
            //final Maybe<Html> mbErrMessage = getMessage(henv);
            final List<Entity<Message>> received = getSite(henv).daoFact.getMessageDAO().selectByTo(user.id);
            // TODO: Obtenir els noms dels remitents dels missatges rebuts
            setTitle(toHtmlString.apply("Inbox"), wbuilder);
            toWidgetHtmlUrl($( dat.web.fw.html.TemplateM.htmlTemplFile("Route<Msg>", "inbox.html") ), wbuilder, henv);
            return null;
        }
    });

    
//---------------------TO-DO:---------------------------------------------------------------------------
    //DONE
    public static final Handler<Msg,Html> postInboxR = new Handler<Msg,Html>(){
        @Override
        public Html	 run(HandlerData<Msg> henv) throws Exception {
        	//It is not used in the tempalte because is a post method
        	Entity<User> user = requireUser(henv);
        	final Maybe<Html> mbErrMessage = getMessage(henv);
        	//for each msgId as a post parameter, the rutine will delete it from the data base:----------
        	//list with all message id's to delete
        	IList<String> msgIds = lookupParams("msgId", henv);
        	//for each id, deletion of this message in the data base
        	for(String id:msgIds){
        		//With this, we protectet the deletion of other messages of thee website
        		if( getSite(henv).daoFact.getMessageDAO().get(Integer.parseInt(id)).fromJust().getToId() ==
        				user.id){
        			getSite(henv).daoFact.getMessageDAO().delete(Integer.parseInt(id));
        		}
        	}
        	return redirect(InboxR, henv);
        	//-------------------------------------------------------------------------------------------
        }
    };
    //DONE
    public static final Handler<Msg,Html> getComposeR = webAppMsg.defaultLayout(new Widget<Msg>(){
        @Override
        public Void run(WidgetBuilder<Msg> wbuilder, HandlerData<Msg> henv) throws Exception {
            
        	//final Maybe<Entity<User>> mbUser = maybeUser(henv);
        	final Entity<User> user = requireUser(henv);
            final Maybe<Html> mbErrMessage = getMessage(henv);
            final ArrayList<Entity<User>> users = getSite(henv).daoFact.getUserDAO().getUsers();
            setTitle(toHtmlString.apply("Compose"), wbuilder);
            toWidgetHtmlUrl($( dat.web.fw.html.TemplateM.htmlTemplFile("Route<Msg>", "compose.html") ), wbuilder, henv);
            return null;
        }
    });
    //
    public static final Handler<Msg,Html> postComposeR = new Handler<Msg,Html>(){
        @Override
        public Html	 run(HandlerData<Msg> henv) throws Exception {
        	
        	Maybe<Entity<User>> mbuserFrom = Just(requireUser(henv));
        	Maybe<String> mbuserTo = lookupParam("toName", henv);
        	Maybe<String> mbsubject = lookupParam("subject", henv);
            Maybe<String> mbcontent = lookupParam("content", henv);
            
            //If all the components of the message are filled, then the message is sent
            if(mbuserTo.isJust() && mbsubject.isJust() && mbcontent.isJust()){
            	
            	Entity<User> userTo = getSite(henv).daoFact.getUserDAO().selectByName(mbuserTo.fromJust()).fromJust();
            	Entity<User> userFrom = mbuserFrom.fromJust();
            	final Message msg = new Message(userFrom.id, userTo.id,new Date(),mbsubject.fromJust(),mbcontent.fromJust());
            	getSite(henv).daoFact.getMessageDAO().insert(msg);
	            return redirect(InboxR, henv);
            
            }else{
            	return invalidArgs(mkIList("assumpte","contingut"), henv);
            }

        }
    };

    public static Handler<Msg,Html> getMessageR(final int mid) { 
    	return webAppMsg.defaultLayout(new Widget<Msg>(){
            @Override
            public Void run(WidgetBuilder<Msg> wbuilder, HandlerData<Msg> henv) throws Exception {
            	
                final Entity<User> user = requireUser(henv);
                setTitle(toHtmlString.apply("Message"), wbuilder);
                final Maybe<Message> message= getSite(henv).daoFact.getMessageDAO().get(mid);

                if(message.isJust()){
                    final Message m = message.fromJust();
                    final Maybe<User> mbUserFrom = getSite(henv).daoFact.getUserDAO().get(m.getFromId());
                    
                    final User userFrom =  mbUserFrom.fromJust();
                    toWidgetHtmlUrl($(dat.web.fw.html.TemplateM.htmlTemplFile("Route<Msg>","displayMess.html")),wbuilder,henv);
                    
                  }else{
                    return redirect(InboxR, henv);
                  }
                  return null;
            } 
    	});
    }

    public static Handler<Msg,Html> postMessageR(final int mid) {  
        return new Handler<Msg,Html>(){
            @Override
            public Html run(HandlerData<Msg> henv) throws Exception {
                final Maybe<Entity<User>> mbUser = maybeUser(henv);
                
        		if( getSite(henv).daoFact.getMessageDAO().get(mid).fromJust().getToId() ==
        				mbUser.fromJust().id){
        			getSite(henv).daoFact.getMessageDAO().delete(mid);
        		}
                return redirect(InboxR, henv);

            }
        };
      }

} // End of module

