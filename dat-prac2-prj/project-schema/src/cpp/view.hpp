
#ifndef _VIEW_HPP_
#define _VIEW_HPP_

#include <string>
#include "model.hpp"

std::string layoutPage (const dat_base::Maybe<std::string> &userId, const std::string& title, const std::string& body);

std::string loginBody ();
std::string inboxBody (const std::string& userId);
std::string messageBody (const Message msg);
std::string composeBody (const std::string& userId);

std::string errorPage (const std::string& errMsg);

#endif

