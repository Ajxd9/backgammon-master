//
//  @ Project: Project Gammon
//  @ File: Master.java
//  @ Date: 12/12/2012
//  @ Authors: DONG Chuan, BONNETTO Benjamin, FRANCON Adrien, POTHELUNE Jean-Michel
//
//

package fr.ujm.tse.info4.pgammon.models;

import java.util.ArrayList;
import java.util.Calendar;
import fr.ujm.tse.info4.pgammon.controller.MainController;

public class Master {
    @SuppressWarnings("unused")
    private static Master master;
    private int sessionId;
    private ArrayList<Session> sessionList;
    private MainController mainController;
    
    public Master() {
        Calendar date = Calendar.getInstance();
        sessionId = 10000 * date.get(Calendar.MONTH)
                + 1000 * date.get(Calendar.DATE)
                + 100 * date.get(Calendar.HOUR)
                + 10 * date.get(Calendar.MINUTE)
                + date.get(Calendar.SECOND);
        mainController = new MainController(this);
        sessionList = new ArrayList<Session>();
    }
    
    public static void main(String[] args) {
        master = new Master();
    }
    
    public void launchSession(GameParameters gameParameters) {
        if (isSessionLaunchable()) {
            sessionList.add(new Session(sessionId, gameParameters));
        }
        try {
            SessionManager manager = SessionManager.getSessionManager();
            manager.setSessionList(sessionList);
        } catch (Exception e) {
            
        }
    }
    
    public void addSession(Session session) {
        if (isSessionLaunchable()) {
            sessionList.add(session);
        }   
    }
    
    public void stopSession(int id) {
        if(sessionList.size() != 0) {
            for (Session session : sessionList) {
                if(session.getSessionId() == id) {
                    sessionList.remove(session);
                    break;
                }
            }   
        }   
    }

    public boolean isSessionLaunchable() {    
        return sessionList.size() != 1;
    }
    
    public Session getSession() {
        // To be modified for multi-threading
        return sessionList.get(sessionList.size()-1);
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
