package com.idega.presentation.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.idega.block.web2.business.Web2Business;
import com.idega.presentation.IWContext;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.util.expression.ELUtil;

/**
 * @author <a href="mailto:valdas@idega.com">Valdas Žemaitis</a>
 * @version $Revision: 1.1 $
 *
 * Date (range) picker
 *
 * Last modified: $Date: 2008/06/26 08:35:33 $ by $Author: valdas $
 */
public class IWDatePicker extends TextInput {
	
	private Date date = null;
	
	private boolean dateRange = false;
	private boolean useCurrentDateIfNotSet = true;
	private boolean showCalendarImage = true;
	
	private String onSelectAction = null;
	private String inputName = null;

	@Override
	public void main(IWContext iwc) {
		if (inputName == null) {
			inputName = this.getId();
		}
		setName(inputName);
		
		Locale locale = iwc.getCurrentLocale();
		if (locale == null) {
			locale = Locale.ENGLISH;
		}
		
		String language = locale.getLanguage();
		addRequiredLibraries(iwc, language);
		
		IWTimestamp iwDate = null;
		if (date == null && useCurrentDateIfNotSet) {
			date = new Date(System.currentTimeMillis());
		}
		if (date != null) {
			iwDate = new IWTimestamp(date);
		}
		if (iwDate != null) {
			setValue(iwDate.getLocaleDate(locale, IWTimestamp.SHORT));
		}
		
		String pickerVar = new StringBuffer("jQuery(function($){ $('#").append(this.getId()).append("')").toString();
		String pickerCallEnd = " });";
		StringBuffer initAction = new StringBuffer(pickerVar).append(".datepicker({");
		
			initAction.append("rangeSelect: ").append(isDateRange()).append(", ");
			
			if (iwDate != null) {
				initAction.append("defaultDate: new Date(").append(iwDate.getYear()).append(", ").append(iwDate.getMonth() - 1).append(", ").append(iwDate.getDay())
							.append("), ");
			}
			if (isShowCalendarImage()) {
				initAction.append("showOn: 'both', buttonImage: '").append(getBundle(iwc).getVirtualPathWithFileNameString("calendar.gif"))
							.append("', buttonImageOnly: true, ");
			}
			if (onSelectAction != null) {
				initAction.append("onSelect: function() {").append(onSelectAction).append("}, ");
			}
			if (language != null && !CoreConstants.EMPTY.equals(language)) {
				initAction.append("regional: ['").append(language).append("']");
			}
	
		initAction.append("});").append(pickerCallEnd);
		
		PresentationUtil.addJavaScriptActionToBody(iwc, initAction.toString());
	}
	
	private void addRequiredLibraries(IWContext iwc, String language) {
		List<String> scripts = new ArrayList<String>();
		
		Web2Business web2 = ELUtil.getInstance().getBean(Web2Business.SPRING_BEAN_IDENTIFIER, iwc);
		scripts.add(web2.getBundleURIToJQueryLib());
		scripts.add(web2.getBundleURIToJQueryUILib("1.5b/datepicker/core", "ui.datepicker.js"));
		if (language != null  && !CoreConstants.EMPTY.equals(language)) {
			scripts.add(web2.getBundleURIToJQueryUILib("1.5b/datepicker/i18n", "ui.datepicker-" + language + ".js"));
		}
		PresentationUtil.addJavaScriptSourcesLinesToHeader(iwc, scripts);
		
		PresentationUtil.addStyleSheetToHeader(iwc, web2.getBundleURIToJQueryUILib("1.5b/datepicker/core", "ui.datepicker.css"));
	}

	@Override
	public String getBundleIdentifier() {
		return CoreConstants.CORE_IW_BUNDLE_IDENTIFIER;
	}

	public boolean isDateRange() {
		return dateRange;
	}

	public void setDateRange(boolean dateRange) {
		this.dateRange = dateRange;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isUseCurrentDateIfNotSet() {
		return useCurrentDateIfNotSet;
	}

	public void setUseCurrentDateIfNotSet(boolean useCurrentDateIfNotSet) {
		this.useCurrentDateIfNotSet = useCurrentDateIfNotSet;
	}

	public boolean isShowCalendarImage() {
		return showCalendarImage;
	}

	public void setShowCalendarImage(boolean showCalendarImage) {
		this.showCalendarImage = showCalendarImage;
	}

	public String getOnSelectAction() {
		return onSelectAction;
	}

	public void setOnSelectAction(String onSelectAction) {
		this.onSelectAction = onSelectAction;
	}

	public String getInputName() {
		return inputName;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}
	
}