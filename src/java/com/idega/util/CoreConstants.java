package com.idega.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CoreConstants {
	
	public static final String CORE_IW_BUNDLE_IDENTIFIER = "com.idega.core";
	
	private static final String[] _HEXIDECIMAL_LETTERS = new String[] {"a", "b", "c", "d", "e", "f", "A", "B", "C", "D", "E", "F"};
	public static final List <String> HEXIDECIMAL_LETTERS = Collections.unmodifiableList(Arrays.asList(_HEXIDECIMAL_LETTERS));
	
	public static final String SLASH = "/";
	public static final String NUMBER_SIGN = "#";
	public static final String SEMICOLON = ";";
	
	public static final String PROP_SYSTEM_SMTP_MAILSERVER = "messagebox_smtp_mailserver";
	public static final String PROP_SYSTEM_MAIL_FROM_ADDRESS = "messagebox_from_mailaddress";

	public static final String HANDLER_PARAMETER = "handler_parameter";

	public static final String CONTENT_PATH = "/files/cms";
	public static final String PAGES_PATH = "/files/cms/pages";

	public static final String ARTICLE_CONTENT_PATH = "/article";
	public final static String ARTICLE_FILENAME_SCOPE = "article";

	public static final String IW_USER_BUNDLE_IDENTIFIER = "com.idega.user";

	public static final String GROUP_SERVICE_DWR_INTERFACE_SCRIPT = "/dwr/interface/GroupService.js";

}