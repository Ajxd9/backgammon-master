// 
//
//  @ Project: Project Gammon
//  @ File: SessionManager.java
//  @ Date: 19/12/2012
//  @ Authors: DONG Chuan, BONNETTO Benjamin, FRANCON Adrien, POTHELUNE Jean-Michel
//
//

package com.HyenaBgammon.models;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class SessionManager {
    public List<Session> sessionList = new ArrayList<Session>();
    private static SessionManager sessionManager;

    private SessionManager() {
    }
    
    /**
     * Constructor: Singleton
     * @return sessionManager the list of Sessions
     * @throws IOException
     * @throws JDOMException
     */
    public static SessionManager getSessionManager() throws IOException, JDOMException {
        if(sessionManager == null) {
            sessionManager = new SessionManager();  
            sessionManager.load();
        } 
        return sessionManager; 
    }

    /**
     * Save all sessions in the "savedSessions" folder as XML
     */
    public void save() {
        try {
            for(int i = 0; i < sessionList.size(); i++) {
                String tmpName = "Session" + String.valueOf(sessionList.get(i).getSessionId());
                Element root = new Element(tmpName);
                Document document = new Document(root);
                sessionList.get(i).save(root);
                XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
                
                File path = new File("savedSessions");
                if(!path.exists()) 
                    path.mkdirs();
                
                String tmpPath = "savedSessions/" + tmpName + ".xml"; 
                output.output(document, new FileOutputStream(tmpPath));
            }
        } catch(Exception e) {
            //System.out.println("Save error");
        }
    }

    /**
     * Load XML files from the "savedSessions" folder
     * @throws JDOMException
     * @throws IOException
     */
    public void load() throws JDOMException, IOException {
        File files[];         
        
        File path = new File("savedSessions");
        
        files = path.listFiles();
        if(files == null)
            return;
        Arrays.sort(files);
        
        SAXBuilder builder = new SAXBuilder();
        
        for (int i = 0; i < files.length; i++) {
            String tmpPath = files[i].toString();
            Document document = builder.build(tmpPath);
            Element root = document.getRootElement();
            sessionList.add(new Session());
            // If loading failed, remove the session
            if (!sessionList.get(i).load(root))
                deleteSession(sessionList.get(i).getSessionId());
        }
    }     

    public List<Session> getSessionList() {
        return sessionList;
    }

    public void setSessionList(List<Session> sessionList) {
        this.sessionList = sessionList;
    }

    /**
     * Delete a session from the list by its ID
     * @param sessionId ID for each session
     */
    public void deleteSession(int sessionId) {
        deleteSessionFile(sessionId);
        for(int i = 0; i < sessionList.size(); i++) {
            if(sessionList.get(i).getSessionId() == sessionId) {
                sessionList.remove(i);
            }
            else
                return;
        }
    }

    /**
     * Delete the session XML file by its ID
     * @param sessionId ID for each session
     */
    public void deleteSessionFile(int sessionId) {
        File files[]; 
        File path = new File("savedSessions");
        files = path.listFiles();
        
        if(files == null)
            return;

        for (int i = 0; i < files.length; i++) {
            String tmpPath = files[i].toString();
            if(tmpPath.equals("savedSessions\\Session" + String.valueOf(sessionId) + ".xml")) {
                files[i].delete();
            }
        }
    }
}
