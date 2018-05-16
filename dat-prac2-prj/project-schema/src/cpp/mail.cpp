
#include "paths.hpp"
#include "model.hpp"
#include "view.hpp"
#include <dat_http/cgi.hpp>
#include <dat_http/params.hpp>
#include <dat_http/session.hpp>
#include <dat_base/except.hpp>

using namespace std;
using namespace dat_base;
using namespace dat_http;

/****************************************************************
 * CGI process
 */

string APPROOT;

Response mailApp(const Request& request);

int main(int argc, char **argv, char **env)
{
    if (getenv("SERVER_NAME") != nullptr && getenv("SCRIPT_NAME") != nullptr) {
        APPROOT = string("http://") + getenv("SERVER_NAME") + getenv("SCRIPT_NAME");
    }
    cgiApp(env, mailApp);
    return 0;
}


/****************************************************************
 * GET/POST processing (CONTROLLER)
 */

Response postLoginR(const Request& request);
Response getLoginR(const Request& request);
Response handleLogoutR(const Request& request);
Response getInboxR (const Request& request);
Response postInboxR(const Request& request);
Response getComposeR(const Request& request);
Response postComposeR(const Request& request);
Response postMessageR(const Request& request);
Response getMessageR(const Request& request, const string& msgId);

Response errorResp(const Status & status, const string &msg);

Response mailApp(const Request& request)
{
    //Request handling application
    string pathInfo = request.rawPathInfo();

    if (pathInfo == LOGIN_PATH) {
        if(request.method() == "POST") {
            return postLoginR(request);
        }else if(request.method() == "GET"){ 
	        return getLoginR(request);
	    }else {
            return errorResp(methodNotAllowed405, "Invalid request method");
        }
    } else if (pathInfo == LOGOUT_PATH) {
        return handleLogoutR(request);
    } else if (pathInfo == INBOX_PATH) {
        if (request.method() == "GET") {
            return getInboxR(request);
        } else if(request.method() == "POST") {
            return postInboxR(request);
        } else {
            return errorResp(methodNotAllowed405, "Invalid request method");
        }
    }else if(pathInfo == COMPOSE_PATH){ 
        if(request.method() == "GET"){
          return getComposeR(request);
        }else if(request.method() == "POST"){
          return postComposeR(request);
        }else{
          return errorResp(methodNotAllowed405,"Invalid request method");
        }
    }else if(pathInfo.find(INBOX_PATH+"/") != string::npos){
        if(request.method() == "POST") {
            return postMessageR(request);
        }else if(request.method() == "GET"){ 
            //Getting the message numer to pass as a parameter to the function
            string msgId =pathInfo.substr(7);
	        return getMessageR(request,msgId);
	    }else {
            return errorResp(methodNotAllowed405, "Invalid request method");
        }        
    }else {
        return errorResp(notFound404, pathInfo);
    }
}

/****************************************************************/
Response getLoginR(const Request& request)
{
    pair<SessionMap,SaveSession> session = clientLoadSession(request);
    SessionMap &sessionMap = session.first;
    string userId;
    //Two different loggin pages;
    //If the usser have active session it will see it, otherwise, there will be nothing
    if(sessionMap.count("userId") == 0){
	    ResponseHeaders hs = session.second(sessionMap);
        hs.push_front(Header("Content-Type", MIME_HTML));
        return Response(ok200, hs, layoutPage(Maybe<string>(), "Login", loginBody()));
    }else{
        userId = sessionMap.at("userId");
    }
    ResponseHeaders hs = session.second(sessionMap);
    hs.push_front(Header("Content-Type", MIME_HTML));
    return Response(ok200, hs, layoutPage(Just(userId), "Login", loginBody()));
}
Response postLoginR(const Request& request)
{
    Query params = postParams(request);
    pair<SessionMap,SaveSession> session = clientLoadSession(request);
    SessionMap &sessionMap = session.first;
    Maybe<string> mb_name = lookupParam("name", params);
    Maybe<string> mb_passwd = lookupParam("passwd", params);
    if (isNothing(mb_name) || isNothing(mb_passwd)) {
        return errorResp(badRequest400, "Invalid parameters");
    } else {
        Maybe<string> mb_userId = login(fromJust(mb_name), fromJust(mb_passwd));
        if (isNothing(mb_userId)) {
            return errorResp(badRequest400, "Error d'autentificacio");
        } else {
            sessionMap.insert(pair<string,string>("userId", fromJust(mb_userId)));
            ResponseHeaders hs = session.second(sessionMap);
            hs.push_front(Header("Location", APPROOT + INBOX_PATH));
            return Response(seeOther303, hs, [](ostream &out){});
        }
    }
}

Response handleLogoutR(const Request& request)
{
    pair<SessionMap,SaveSession> session = clientLoadSession(request);
    SessionMap &sessionMap = session.first;
    Query params = postParams(request);
    sessionMap.erase("userId");
    ResponseHeaders hs = session.second(sessionMap);
    hs.push_front(Header("Location", APPROOT + LOGIN_PATH));
    return Response(seeOther303, hs, "");
}

Response getInboxR(const Request& request)
{
    pair<SessionMap,SaveSession> session = clientLoadSession(request);
    SessionMap &sessionMap = session.first;
    if (sessionMap.count("userId") == 0) {
        return errorResp(badRequest400, "Invalid session");
    }
    string userId = sessionMap.at("userId");
    ResponseHeaders hs = session.second(sessionMap);
    hs.push_front(Header("Content-Type", MIME_HTML));
    return Response(ok200, hs, layoutPage(Just(userId), "Inbox", inboxBody(userId)));
}

Response postInboxR(const Request& request)
{
    //preguntar per session i que te a veure amb els headers	
    Query params = postParams(request);
    pair<SessionMap,SaveSession> session = clientLoadSession(request);
    SessionMap &sessionMap = session.first;
    //If the session is closed, it will return error page
    if (sessionMap.count("userId") == 0) {
      return errorResp(badRequest400, "Invalid session");
    }
    string userId = sessionMap.at("userId");
    
    //Looking for the messages to delete. These messages are included in the post method as variables msgId with assosied value. value
    list<string> msg_Ids = lookupParams("msgId", params);
    //mb_Ids is a list with the messages to delete
    deleteMessages(userId,msg_Ids);
    //delete message, in the model.cpp is a function that deletes the messags passed as a parameter

    ResponseHeaders hs = session.second(sessionMap);
    hs.push_front(Header("Content-Type", MIME_HTML));
    return Response(ok200, hs, layoutPage(Just(userId), "Inbox", inboxBody(userId)));
}

Response errorResp(const Status & status, const string &msg)
{
    ResponseHeaders hs;
    hs.push_front(Header("Content-Type", MIME_HTML));
    return Response(status, hs, errorPage(msg));
}

Response getComposeR(const Request& request)
{
    pair<SessionMap,SaveSession> session = clientLoadSession(request);
    SessionMap &sessionMap = session.first;
    if (sessionMap.count("userId") == 0) {
        return errorResp(badRequest400, "Invalid session");
    }
    string userId = sessionMap.at("userId");

    ResponseHeaders hs = session.second(sessionMap);
    hs.push_front(Header("Content-Type", MIME_HTML));
    return Response(ok200, hs, layoutPage(Just(userId), "Compose", composeBody(userId)));

}

Response postComposeR(const Request& request)
{
    Query params = postParams(request);
    pair<SessionMap,SaveSession> session = clientLoadSession(request);
    SessionMap &sessionMap = session.first;
    //If the session is closed, it will return error page
    if (sessionMap.count("userId") == 0) {
      return errorResp(badRequest400, "Invalid session");
    }
    string userId = sessionMap.at("userId");
    if(
    sendMessage(userId, fromJust(lookupParam("toName", params)),fromJust(lookupParam("subject", params)),fromJust(lookupParam("content", params))) ){
    ResponseHeaders hs = session.second(sessionMap);
    hs.push_front(Header("Content-Type", MIME_HTML));
    return Response(ok200, hs, layoutPage(Just(userId), "Inbox", inboxBody(userId)));    
    }
    return errorResp(badRequest400, "Not sent");
}

Response postMessageR(const Request& request)
{
    Query params = postParams(request);
    pair<SessionMap,SaveSession> session = clientLoadSession(request);
    SessionMap &sessionMap = session.first;
    //If the session is closed, it will return error page
    if (sessionMap.count("userId") == 0) {
      return errorResp(badRequest400, "Invalid session");
    }
    string userId = sessionMap.at("userId");

    Maybe<string> msg_ID = lookupParam("msgId", params);
    deleteMessage(userId,fromJust(msg_ID));

    ResponseHeaders hs = session.second(sessionMap);
    hs.push_front(Header("Content-Type", MIME_HTML));
    return Response(ok200, hs, layoutPage(Just(userId), "Inbox", inboxBody(userId)));
}
Response getMessageR(const Request& request, const string& msgId)
{
    pair<SessionMap,SaveSession> session = clientLoadSession(request);
    SessionMap &sessionMap = session.first;
    //If the session is closed, it will return error page
    if (sessionMap.count("userId") == 0) {
      return errorResp(badRequest400, "Invalid session");
    }
    string userId = sessionMap.at("userId");
    Message msg =getMessageById(msgId);
    ResponseHeaders hs = session.second(sessionMap);
    hs.push_front(Header("Content-Type", MIME_HTML));
    return Response(ok200, hs, layoutPage(Just(userId), "msg", messageBody(msg)));


}
