/*
 * $Id: BasicSearch.java,v 1.2 2006/04/09 12:13:20 laddi Exp $
 * Created on Jan 18, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.core.search.data;

import java.util.Collection;
import com.idega.core.search.business.Search;
import com.idega.core.search.business.SearchQuery;
import com.idega.core.search.business.SearchResult;
import com.idega.util.ListUtil;


/**
 * 
 *  Last modified: $Date: 2006/04/09 12:13:20 $ by $Author: laddi $
 * A general implementation of Search with simple get and set methods.
 * @author <a href="mailto:eiki@idega.com">Eirikur S. Hrafnsson</a>
 * @version $Revision: 1.2 $
 */
public class BasicSearch implements Search {

	
	private String searchType;
	private String searchName;
	private SearchQuery searchQuery;
	private long numberOfResults = 0;
	private Collection<SearchResult> searchResults;
	
	/**
	 * 
	 */
	public BasicSearch() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.idega.core.search.business.Search#getSearchType()
	 */
	public String getSearchType() {
		return this.searchType;
	}

	/* (non-Javadoc)
	 * @see com.idega.core.search.business.Search#getSearchName()
	 */
	public String getSearchName() {
		return this.searchName;
	}

	/* (non-Javadoc)
	 * @see com.idega.core.search.business.Search#getSearchQuery()
	 */
	public SearchQuery getSearchQuery() {
		return this.searchQuery;
	}

	/* (non-Javadoc)
	 * @see com.idega.core.search.business.Search#getSearchResults()
	 */
	public Collection<SearchResult> getSearchResults() {
		return this.searchResults;
	}

	/* (non-Javadoc)
	 * @see com.idega.core.search.business.Search#getNumberOfResults()
	 */
	public long getNumberOfResults() {
		Collection<SearchResult> results = getSearchResults();
		if (!ListUtil.isEmpty(results)) {
			return results.size();
		}
		return this.numberOfResults;
	}

	/**
	 * @param searchName The searchName to set.
	 */
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	/**
	 * @param searchQuery The searchQuery to set.
	 */
	public void setSearchQuery(SearchQuery searchQuery) {
		this.searchQuery = searchQuery;
	}
	/**
	 * @param searchResults The searchResults to set.
	 */
	public void setSearchResults(Collection<SearchResult> searchResults) {
		this.searchResults = searchResults;
	}
	/**
	 * @param searchType The searchType to set.
	 */
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
}