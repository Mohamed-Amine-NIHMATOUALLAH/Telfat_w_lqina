package com.firstproject.telfat_w_lqina.util;

import com.firstproject.telfat_w_lqina.models.Complaint;

public class SessionComplaint {
    private static SessionComplaint sessionComplaint;
    private Complaint curentComplaint;

    public SessionComplaint(){};
    public static SessionComplaint getInstance(){
        if (sessionComplaint==null){
            sessionComplaint= new SessionComplaint();
        }
        return sessionComplaint;
    }
    public void setCurentComplaint(Complaint complaint){
        curentComplaint = complaint;
    }
    public Complaint getCurentComplaint(){
        return curentComplaint;
    }

    public void clearInstance(){
        sessionComplaint=null;
    }

}
