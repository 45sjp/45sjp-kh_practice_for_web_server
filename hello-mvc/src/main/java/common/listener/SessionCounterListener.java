package common.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class SessionCounterListener
 * 
 * @WebListener : Listener를 대신 등록해주는 Annotation(@)
 */
//@WebListener
public class SessionCounterListener implements HttpSessionListener {
	
	// 접속하고 있는 사용자 수
	private static int activeSessions;
	
    /**
     * Default constructor. 
     */
    public SessionCounterListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent se)  { 
    	activeSessions++;
    	System.out.println("> 세션 생성! 접속 사용자 수 : " + activeSessions);
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent se)  { 
    	// 세션을 끄면 폐기가 바로 되어야 하는데 유지되면서 수가 틀어질 수 있음. 이를 방지하기 위함!
    	if(activeSessions > 0)
    		activeSessions--;
    	System.out.println("> 세션 폐기! 접속 사용자 수 : " + activeSessions);
    }
	
}
