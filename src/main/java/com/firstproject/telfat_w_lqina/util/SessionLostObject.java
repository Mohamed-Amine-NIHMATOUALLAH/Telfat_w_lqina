package com.firstproject.telfat_w_lqina.util;

import com.firstproject.telfat_w_lqina.models.LostObject;

public class SessionLostObject {
    private static SessionLostObject sessionLostObject;
    private LostObject curentLostObject;

    public SessionLostObject(){};
    public static SessionLostObject getInstance(){
        if (sessionLostObject==null){
            sessionLostObject=new SessionLostObject();
        }
        return sessionLostObject;
    }
    public void setCurentLostObject(LostObject lostObject){
        curentLostObject = lostObject;
    }
    public LostObject getCurentLostObject(){
        return curentLostObject;
    }

    public void clearInstance(){
        sessionLostObject=null;
    }

}
