
#ifndef _PATHS_HPP_
#define _PATHS_HPP_

#include <string>

extern std::string APPROOT;

const std::string LOGIN_PATH = "/login";
const std::string LOGOUT_PATH = "/logout";
const std::string INBOX_PATH = "/inbox";
inline const std::string MSG_PATH(std::string mid) { return "/inbox/" + mid; }
const std::string COMPOSE_PATH = "/compose";

#endif

