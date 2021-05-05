package application.utils;

import application.dto.UserDTO;

import javax.servlet.http.HttpServletRequest;

public class ServerUtils {
    public static UserDTO getUserFromSession(HttpServletRequest request){
        return (UserDTO) request.getSession().getAttribute("user");
    }

}
