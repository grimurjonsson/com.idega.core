package com.idega.core.business;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.idega.business.IBOLookup;
import com.idega.business.IBOService;
import com.idega.business.IBOSession;
import com.idega.core.accesscontrol.business.LoginSession;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWUserContext;
import com.idega.user.data.User;
import com.idega.util.expression.ELUtil;

/**
 * Common methods most often needed in Spring beans
 * 
 * @author <a href="mailto:valdas@idega.com">Valdas Žemaitis</a>
 * @version $Revision: 1.0 $
 *
 * Last modified: $Date: 2009.10.04 19:26:46 $ by: $Author: valdas $
 */
public abstract class DefaultSpringBean {

	private static final Logger LOGGER = Logger.getLogger(DefaultSpringBean.class.getName());
	
	@SuppressWarnings("unchecked")
	protected <T extends IBOSession> T getSessionInstance(IWUserContext iwuc, Class<? extends IBOSession> sessionBeanClass) {
		try {
			return (T) IBOLookup.getSessionInstance(iwuc, sessionBeanClass);	//	Casting is needed to avoid stupid compilation error in Maven 2
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error getting session instance: " + sessionBeanClass);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends IBOService> T getServiceInstance(Class<? extends IBOService> serviceBeanClass) {
		//	Casting is needed to avoid stupid compilation error in Maven 2
		return (T) getServiceInstance(IWMainApplication.getDefaultIWApplicationContext(), serviceBeanClass);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends IBOService> T getServiceInstance(IWApplicationContext iwac, Class<? extends IBOService> serviceBeanClass) {
		try {
			return (T) IBOLookup.getServiceInstance(iwac, serviceBeanClass);	//	Casting is needed to avoid stupid compilation error in Maven 2
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error getting service instance: " + serviceBeanClass);
		}
		return null;
	}
	
	protected Locale getCurrentLocale() {
		try {
			LoginSession loginSession = ELUtil.getInstance().getBean(LoginSession.class);
			return loginSession.getCurrentLocale();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error getting current locale", e);
		}
		return null;
	}
	
	protected User getCurrentUser() {
		try {
			LoginSession loginSession = ELUtil.getInstance().getBean(LoginSession.class);
			return loginSession.getUser();
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error getting current user", e);
		}
		return null;
	}
	
}