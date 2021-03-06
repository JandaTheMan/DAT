
#ifndef _MODEL_HPP_
#define _MODEL_HPP_

#include <dat_base/maybe.hpp>
#include <string>
#include <list>


/****************************************************************
 * User operations
 */

dat_base::Maybe<std::string> login (const std::string& name, const std::string& passwd);
std::string                  userName (const std::string& userId);
std::list<std::string>       otherUserNames (const std::string& userId);


/****************************************************************
 * Message operations
 */

struct Message {
    std::string id;
    std::string from, to;
    //time_t date;
    std::string date;
    std::string subject, content;
};

dat_base::Maybe<Message> messageById (const std::string& msgId);
std::list<Message>       messagesByTo (const std::string& toId);

bool sendMessage (const std::string& fromId, const std::string& toName, const std::string& subject, const std::string& content);
void deleteMessage (const std::string& toId, const std::string& msgId);
void deleteMessages (const std::string& toId, const std::list<std::string>& msgIds);
Message getMessageById(const std::string& msgId);
std::list<std::string> getUsers ();
#endif

