
#include "view.hpp"
#include "paths.hpp"
#include "model.hpp"


std::string escapeHtml(const std::string& text)
{
    std::string result;
    for (unsigned i = 0; i < text.size(); i++) {
        char c = text[i];
        switch (c) {
            case '<': result += "&lt;"; break;
            case '&': result += "&amp;"; break;
            case '>': result += "&gt;"; break;
            case '\"': result += "&quot;"; break;
            case '\'': result += "&apos;"; break;
            default:  result += c; break;
        }
    }
    return result;
}

std::string layoutPage(const dat_base::Maybe<std::string> & userId, const std::string& title, const std::string& body)
{
//Using the source code from the DAT web page construction of the html page that will be retrieved to the client
    std::string html;
    html.append("\
    <!DOCTYPE html>\n\
    <html><head>\n\
      <title>DAT - Pr&agrave;ctica 2. Login</title>\n\
      <link rel='stylesheet' type='text/css' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css'>\n\
    </head><body><div class='container-fluid'>\n\
    <!-- header -->\n\
      <div class='row page-header'>\n\
        <div class='col-sm-8'>\n\
          <h2>Pràctica 2: Un sistema simple de correu per la WEB</h2>\n\
        </div>\n\
      <div class='col-sm-4'>\n");

    //Log-off button if user have active session
    
    if (dat_base::isJust(userId)) {
        html.append("\
      <form method='post' action='"+APPROOT + LOGOUT_PATH+"'>\n\
        <span class='glyphicon glyphicon-user'></span>Usuari: <b>usuari"+fromJust(userId)+"</b>&nbsp;&nbsp;\n\
        <span class='glyphicon glyphicon-log-out'></span><button type='submit' class='btn btn-link' name='quit'>Tanca sessió</button>\n\
      </form>\n");
    }
    html.append("\
      </div>\n\
    </div>\n");
    
    html.append(body);
    html.append("</body></html>\n");
    return html;
}

std::string inboxBody(const std::string& userId)
{
    std::string html;
    html.append("\
    <!-- body -->\n\
  <ul class='nav nav-tabs'>\n\
    <li class='active'><a href='"+APPROOT + INBOX_PATH+"'><strong>Inbox</strong></a></li>\n\
    <li><a href='"+APPROOT + COMPOSE_PATH+"'><strong>Compose</strong></a></li>\n\
  </ul>\n\
    <h3>Missatges rebuts:</h3>\n");
    
    std::list<Message> msgs = messagesByTo(userId);

    html.append("\
    <form method='POST' action='"+APPROOT + INBOX_PATH+"'>\n\
    <table class='table table-striped table-condensed'>\n\
      <thead><tr><th>&nbsp;</th> <th>De</th> <th>Data</th> <th>Assumpte</th></tr></thead>\n\
      <tbody>\n");
    for (auto mit = msgs.cbegin(); mit != msgs.cend(); ++mit) {
        //time_t date = mit->date;
        //std::string s_date = asctime(localtime(&date));
        html.append("\
    <tr><td><input type=checkbox name='msgId' value='"+ mit->id + "'></td>\n\
        <td><em>"+mit->from+"</em></td><td>"+mit->date+"</td>\n\
        <td><a href='" + APPROOT + MSG_PATH(mit->id) + "'>" + escapeHtml(mit->subject) + "</a></td></tr>\n");
    }

    html.append("\
          </tbody>\n\
        </table>\n\
        <p><input type=submit name='delete' value='Elimina seleccionats'></p>\n\
        </form>\n\
    <!-- footer -->\n\
    <hr size='1' noshade='' width='100%'>\n\
    <p><a HREF='http://soft0.upc.edu/dat/practica2/' title='http://soft0.upc.edu/dat/practica2/'>Enunciat de la pràctica</a></p>\n\
    <center>\n\
      Janda i un tontet.<br>\n\
      Abril, 2018.<br>\n\
    </center>\n");
    return html;
}

std::string errorPage (const std::string& errMsg)
{
      std::string html;
      html.append("\
    <html><head>\n\
      <title>DAT - Pr&agrave;ctica 2. Error </title>\n\
    </head><body>\n\
    <center>\n\
      <h2>ERROR</h2>\n\
      <h3><FONT COLOR='red'>Error"+errMsg+"</FONT></h3>\n\
    </center>\n\
    </body></html>\n");
    return html;
}

std::string loginBody(/**const std::string& errMsg**/)
{
    std::string html;
    html.append("\
    <!-- body -->\n\
    <form method='POST' action='./login'>\n\
        <p>Nom de l'usuari: <input type='text' name='name' size='10'> &nbsp;&nbsp;\n\
        Clau d'acc&eacute;s: <input type='password' name='passwd' maxlength='8' size='8'>\n\
        <input type='submit' value='Entra'>\n\
    </form>\n\
    <hr>\n\
    <p>Hi ha registrats 3 usuaris de prova amb noms <code>usuari1</code>, <code>usuari2</code> i <code>usuari3</code>,");
    html.append("i els 'passwords' <code>clau1</code>, <code>clau2</code> i <code>clau3</code> respectivament.</p>\n\
    <!-- footer -->\n\
    <hr size='1' noshade='' width='100%'>\n\
    <p><a HREF='http://soft0.upc.edu/dat/practica2/' title='http://soft0.upc.edu/dat/practica2/'>Enunciat de la pràctica</a></p>\n\
    <center>\n\
      Janda i un tontet.<br>\n\
      Abril, 2018.<br>\n\
    </center>\n");
    
    return html;
}


std::string composeBody(const std::string& userId)
{
    std::string html;
    html.append("\
    <!-- body -->\n\
      <ul class='nav nav-tabs'>\n\
        <li><a href='"+APPROOT + INBOX_PATH+"'><strong>Inbox</strong></a></li>\n\
        <li class='active'><a href='"+APPROOT + COMPOSE_PATH+"'><strong>Compose</strong></a></li>\n\
      </ul>\n\
    <h3>Missatge a enviar:</h3>\n\
      <form class='form-horizontal' role='form' method='POST' action='#'>\n\
        <div class='form-group'>\n\
          <label class='control-label col-sm-2' for='toName'>Envia a:</label>\n\
          <div class='col-sm-10'>\n\
            <select name='toName'>\n");

            std::list<std::string> users = getUsers();

            for(std::list<std::string>::iterator it=users.begin(); it!=users.end(); ++it)
            {
              html.append("\
                          <option value='"+*it+"'>"+*it+"</option>\n");
            }          
            html.append("\
            </select>\n\
          </div>\n\
        </div>\n\
        <div class='form-group'>\n\
          <label class='control-label col-sm-2' for='subject'>Assumpte:</label>\n\
          <div class='col-sm-10'>\n\
            <input type='text' class='form-control' id='subject' name='subject' placeholder='Introduiu l&apos;assumpte del missatge'>\n\
          </div>\n\
        </div>\n\
        <div class='form-group'>\n\
          <label class='control-label col-sm-2' for='content'>Contingut:</label>\n\
          <div class='col-sm-10'>\n\
            <textarea type='text' class='form-control' id='content' name='content' placeholder='Introduiu el contingut del missatge' rows='15'></textarea>\n\
          </div>\n\
        </div>\n\
        <div class='form-group'>\n\
          <div class='col-sm-offset-2 col-sm-10'>\n\
            <button type='submit' class='btn btn-primary' name='send'>Envia</button>\n\
          </div>\n\
        </div>\n\
      </form>\n\
    <!-- footer -->\n\
    <hr size='1' noshade='' width='100%'>\n\
    <p><a HREF='http://soft0.upc.edu/dat/practica2/' title='http://soft0.upc.edu/dat/practica2/'>Enunciat de la pràctica</a></p>\n\
    <center>\n\
      Janda i un tontet<br>\n\
      Novembre, 2017.<br>\n\
    </center>\n");
    return html;
}

std::string messageBody(const Message msg)
{
    std::string html;
    html.append("\
    <!-- body -->\n\
      <ul class='nav nav-tabs'>\n\
        <li><a href='"+APPROOT + INBOX_PATH+"'><strong>Inbox</strong></a></li>\n\
        <li><a href='"+APPROOT + COMPOSE_PATH+"'><strong>Compose</strong></a></li>\n\
      </ul>\n\
        <h3>Missatge rebut:</h3>\n\
        <div class='jumbotron'><table class='table'>\n\
            <tr><th>De</th>      <td><em>"+msg.from+"</em></td></tr>\n\
            <tr><th>Data</th>    <td>"+msg.date+"\n\
    </td></tr>\n\
            <tr><th>Assumpte</th><td>"+msg.subject+"</td></tr>\n\
        </table></div>\n\
        <pre>"+msg.content+"</pre>\n\
      <form method='POST' action='"+APPROOT + INBOX_PATH+"'>\n\
        <input type='hidden' name='msgId' value='"+msg.id+"'>\n\
        <div class='form-group'>\n\
          <div class='col-sm-offset-2 col-sm-10'>\n\
            <button type='submit' class='btn btn-default' name='delete'>Elimina</button>\n\
          </div>\n\
        </div>\n\
      </form>\n\
    <!-- footer -->\n\
    <hr size='1' noshade='' width='100%'>\n\
    <p><a HREF='http://soft0.upc.edu/dat/practica2/' title='http://soft0.upc.edu/dat/practica2/'>Enunciat de la pràctica</a></p>\n\
    <center>\n\
      Janda i un tontet.<br>\n\
      Abril, 2018.<br>\n\
    </center>\n");
    return html;
}

