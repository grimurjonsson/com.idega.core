package com.idega.presentation.app;

import java.util.Iterator;
import java.util.List;

import com.idega.core.component.data.ICObject;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.repository.data.RefactorClassRegistry;

public class IWControlCenter extends Block {

	private static final int DEFAULT = 1;
	private static final int VERTICAL = 2;
	private static final int HORIZONTAL = 3;
	private int headerHeight = 25;
	private int border = 3;
	private String backgroundColor = "#D4D0C8";
	private String headerColor = this.backgroundColor;
	private String darkerColor = "gray";
	private String bodyColor = "white";
	private boolean showLinesIfNoApplications = true;
	private int layout = 1;

	public void main(IWContext iwc) {
		Table body = new Table();
		// outerWindow.add(body,1,2);
		add(body);
		body.setWidth(Table.HUNDRED_PERCENT);
		// body.setHeight(windowHeight-headerHeight);
		// body.setHeight("100%");
		body.setCellpadding(4);
		// body.setColor(bodyColor);

		List icoList = IWApplication.getApplictionICObjects();
		boolean anyApp = false;
		if (icoList != null) {
			int col = 1;
			int row = 1;
			Iterator iter = icoList.iterator();
			while (iter.hasNext()) {
				ICObject item = (ICObject) iter.next();
				try {
					PresentationObject pObj = (PresentationObject) RefactorClassRegistry.forName(item.getClassName()).newInstance();
					pObj.setICObject(item);
					if (iwc.hasViewPermission(pObj)) {
						anyApp = true;
						Class c = null;
						try {
							c = item.getObjectClass();
						}
						catch (Exception e) {
						}

						PresentationObject icon = IWApplication.getIWApplicationIcon(c, iwc);
						body.setAlignment(col, row, "center");
						body.setVerticalAlignment(col, row, "middle");
						body.add(icon, col, row);
						switch (this.layout) {
							case HORIZONTAL:
								col++;
								break;
							case VERTICAL:
								row++;
								break;
							default:
								if (col == 1) {
									col = 2;
								}
								else {
									col = 1;
									row++;
								}
						}

					}
				}
				catch (ClassCastException e) {
					e.printStackTrace();
				}
				catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				catch (InstantiationException e) {
					e.printStackTrace();
				}
			}
		}

		if (this.showLinesIfNoApplications && !anyApp) {
			body.setAlignment(1, 1, "center");
			body.setVerticalAlignment(1, 1, "middle");
			body.add(new Text("- - -"), 1, 1);
		}

	}

	public void setLayoutVertical(boolean flag) {
		this.layout = flag ? VERTICAL : DEFAULT;
	}

	public void setLayoutHorizontal(boolean flag) {
		this.layout = flag ? HORIZONTAL : DEFAULT;

	}

	/**
	 * @return Returns the backgroundColor.
	 */
	public String getBackgroundColor() {
		return this.backgroundColor;
	}

	/**
	 * @param backgroundColor
	 *          The backgroundColor to set.
	 */
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * @return Returns the bodyColor.
	 */
	public String getBodyColor() {
		return this.bodyColor;
	}

	/**
	 * @param bodyColor
	 *          The bodyColor to set.
	 */
	public void setBodyColor(String bodyColor) {
		this.bodyColor = bodyColor;
	}

	/**
	 * @return Returns the border.
	 */
	public int getBorder() {
		return this.border;
	}

	/**
	 * @param border
	 *          The border to set.
	 */
	public void setBorder(int border) {
		this.border = border;
	}

	/**
	 * @return Returns the darkerColor.
	 */
	public String getDarkerColor() {
		return this.darkerColor;
	}

	/**
	 * @param darkerColor
	 *          The darkerColor to set.
	 */
	public void setDarkerColor(String darkerColor) {
		this.darkerColor = darkerColor;
	}

	/**
	 * @return Returns the headerColor.
	 */
	public String getHeaderColor() {
		return this.headerColor;
	}

	/**
	 * @param headerColor
	 *          The headerColor to set.
	 */
	public void setHeaderColor(String headerColor) {
		this.headerColor = headerColor;
	}

	/**
	 * @return Returns the headerHeight.
	 */
	public int getHeaderHeight() {
		return this.headerHeight;
	}

	/**
	 * @param headerHeight
	 *          The headerHeight to set.
	 */
	public void setHeaderHeight(int headerHeight) {
		this.headerHeight = headerHeight;
	}
}