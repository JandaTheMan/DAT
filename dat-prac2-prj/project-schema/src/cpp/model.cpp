
#include "model.hpp"
#include <dat_db/ddb.hpp>
#include <ctime>
#include <string>

using namespace std;
using namespace dat_base;
using namespace dat_db;

const string db_root("mail-db");

Maybe<string> login (const string& name, const string& passwd)
{
    //amb nothing no funciona
    Maybe<string> nothing = Maybe<string>();
    try{ 
	    DBConnection dbc(db_root);   
        dbc.select("users", "name", name);
        if(dbc.next()){
	    //If the name exists in the database, and the associed password is the passed as a parameter one, return the associed id from the database.
            if(dbc.get("password")==passwd) return Just(dbc.get("id"));
        }else{
    	    return nothing;
        }
    }catch(const  DBException& e){
	
    }
    return nothing;
}

//function to return all users from the database

list<string> getUsers ()
{
    list<string> users;
    try{ 
	    DBConnection dbc(db_root);   
        dbc.select("users");
        while(dbc.next()){
            users.push_back(dbc.get("name"));
        }
    }catch(const  DBException& e){
	
    }
    return users;
}


string userName (const string& userId)
{
    //Return of the user name associed with the userId
    try{    
	    DBConnection dbc(db_root);
        dbc.select("users", "id", userId);
        if(dbc.next()){
            if(dbc.get("id")==userId) return dbc.get("name");
        }
    }catch(const  DBException& e){
    }
        return nullptr;
}

list<Message> messagesByTo(const string& toId)
{
    DBConnection dbc(db_root);

    dbc.select("messages", "to", "usuari"+toId);
    list<Message> msgs;
    while (dbc.next()) {
        Message m;
        m.id = dbc.get("id");
        m.from = dbc.get("from");
        m.to = toId;
        //m.date = stol(dbc.get("date"));
        m.date = dbc.get("date");
        m.subject = dbc.get("subject");
        m.content = dbc.get("content");
        msgs.push_back(m);
    }
    return msgs;
}

Message getMessageById(const string& msgId)
{
    Message m;
    try{
        //Connection with the database
        DBConnection dbc(db_root);
        dbc.select("messages", "id", msgId);
        dbc.next();
        m.subject = dbc.get("subject");
        m.id = dbc.get("id");
        m.from = dbc.get("from");
        m.date = dbc.get("date");
        m.subject = dbc.get("subject");
        m.content = dbc.get("content");
    }catch(const  DBException& e){
   
    }
    return m;
}

void deleteMessage (const std::string& toId, const std::string& msgId)
{
    try{
        //Connection with the database
        DBConnection dbc(db_root);
        //Delete of the message form the data base
        dbc.delete_rows("messages","id",msgId);
    }catch(const  DBException& e){
   
    }
}

void deleteMessages (const std::string& toId, const std::list<std::string>& msgIds)
{
    list<string> listt = msgIds;
    //Iteration of the list with message id's
    for(list<string>::iterator it=listt.begin(); it!=listt.end(); ++it)
    {
	//for each id, the rutine invoque deleteMessage that delete this message from the data base
	deleteMessage(toId, *it);
    }
}

bool sendMessage (const std::string& fromId, const std::string& toName, const std::string& subject, const std::string& content)
{
        DBConnection dbc(db_root);
        //Selection of the last id and increment of one unity of it's value to use it as the next msgId
        try{	
        dbc.select("msg-seq");
        dbc.next();
        const string idMsg = dbc.get("value");
        int nextId = (stoi(idMsg) + 1);
        dbc.update("msg-seq","value",to_string(nextId));
        
        //Fillig the parameters to store in the data base as a sent message
        time_t curr_time = time(0);
        string date = asctime(localtime(&curr_time));
        
        list<string> params;
        params.push_back(to_string(nextId));
        params.push_back("usuari"+fromId);
        params.push_back(toName);
        params.push_back(date);
        params.push_back(subject);
        params.push_back(content);
       
	    dbc.insert("messages",params);
        return true;
    }catch(const  DBException& e){
    }
    return false;
}

