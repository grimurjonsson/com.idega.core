/*
 * $Id: ComponentProperty.java,v 1.2 2006/05/09 14:47:18 tryggvil Exp $
 * Created on 20.9.2005 in project com.idega.core
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.core.component.business;

import java.util.Locale;


/**
 * <p>
 * This class holds info about properties available for each ComponentInfo.
 * </p>
 *  Last modified: $Date: 2006/05/09 14:47:18 $ by $Author: tryggvil $
 * 
 * @author <a href="mailto:tryggvil@idega.com">tryggvil</a>
 * @version $Revision: 1.2 $
 */
public interface ComponentProperty {
	
	public String getName();
	
	public String getName(Locale locale);
	
	public String getDescription();
	
	public String getDescription(Locale locale);
	
	public String getDisplayName();
	
	public String getDisplayName(Locale locale);
	
	public String getClassName();
	
	public String getIcon();
	
}