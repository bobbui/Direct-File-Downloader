/*
 * Created on May 6, 2008
 * Created by Paul Gardner
 * 
 * Copyright 2008 Vuze, Inc.  All rights reserved.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 2 of the License only.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */

package com.aelitis.azureus.core.metasearch.impl.web;

import com.aelitis.azureus.core.metasearch.Engine;
import com.aelitis.azureus.core.metasearch.Result;
import com.aelitis.azureus.core.metasearch.impl.DateParser;
import org.apache.commons.lang.Entities;
import org.gudy.azureus2.core3.util.Base32;
import org.gudy.azureus2.core3.util.ByteFormatter;
import org.gudy.azureus2.core3.util.Debug;
import org.gudy.azureus2.core3.util.UrlUtils;

import java.util.Date;
import java.util.StringTokenizer;

public class WebResult extends Result {
	
	
	
	String searchQuery;
	
	String rootPageURL;
	String basePageURL;
	DateParser dateParser;
	
	
	String contentType = "";
	String name;
	String category = "";
	
	String drmKey = null;
	
	Date publishedDate;
	
	long size = -1;
	int nbPeers = -1;
	int nbSeeds = -1;
	int nbSuperSeeds = -1;
	int	comments	= -1;
	int votes = -1;
	int votesDown = -1;
	float rank = -1;
	
	boolean privateTorrent;
	
	String cdpLink;
	String torrentLink;
	String downloadButtonLink;
	String playLink;
	
	String uid;
	String hash;
	
	public WebResult(Engine engine, String rootPageURL,String basePageURL,DateParser dateParser,String searchQuery) {
		super( engine );
		this.rootPageURL = rootPageURL;
		this.basePageURL = basePageURL;
		this.dateParser = dateParser;
		this.searchQuery = searchQuery;
	}
		
	public void setName(String name) {
		if(name != null) {
			this.name = name;
		}
	}
	
	public void setNameFromHTML(String name) {
		if(name != null) {
			name = removeHTMLTags(name);
			this.name = Entities.HTML40.unescape(name);
		}
	}
	
	public void setCommentsFromHTML(String comments) {
		if(comments != null) {
			comments = removeHTMLTags(comments);
			comments = Entities.HTML40.unescape(comments);
			comments = comments.replaceAll(",", "");
			comments = comments.replaceAll(" ", "");
			try{
				this.comments = Integer.parseInt(comments);
			}catch( Throwable e ){
				//e.printStackTrace();
			}
		}
	}
	public void setCategoryFromHTML(String category) {
		if(category != null) {
			category = removeHTMLTags(category);
			this.category = Entities.HTML40.unescape(category).trim();
			/*int separator = this.category.indexOf(">");
			
			if(separator != -1) {
				this.category = this.category.substring(separator+1).trim();
			}*/
		}
	}
	
	public void
	setUID(
		String	_uid )
	{
		uid	= _uid;
	}
	
	public String
	getUID()
	{
		return( uid );
	}
	
	public void setNbPeersFromHTML(String nbPeers) {
		if(nbPeers != null) {
			nbPeers = removeHTMLTags(nbPeers);
			String nbPeersS = Entities.HTML40.unescape(nbPeers);
			nbPeersS = nbPeersS.replaceAll(",", "");
			nbPeersS = nbPeersS.replaceAll(" ", "");
			try {
				this.nbPeers = Integer.parseInt(nbPeersS);
			} catch(Throwable e) {
				//this.nbPeers = 0;
				//e.printStackTrace();
			}
		}
	}	
	
	public void setNbSeedsFromHTML(String nbSeeds) {
		if(nbSeeds != null) {
			nbSeeds = removeHTMLTags(nbSeeds);
			String nbSeedsS = Entities.HTML40.unescape(nbSeeds);
			nbSeedsS = nbSeedsS.replaceAll(",", "");
			nbSeedsS = nbSeedsS.replaceAll(" ", "");
			try {
				this.nbSeeds = Integer.parseInt(nbSeedsS);
			} catch(Throwable e) {
				//this.nbSeeds = 0;
				//e.printStackTrace();
			}
		}
	}
	
	public void setNbSuperSeedsFromHTML(String nbSuperSeeds) {
		if(nbSuperSeeds != null) {
			nbSuperSeeds = removeHTMLTags(nbSuperSeeds);
			String nbSuperSeedsS = Entities.HTML40.unescape(nbSuperSeeds);
			nbSuperSeedsS = nbSuperSeedsS.replaceAll(",", "");
			nbSuperSeedsS = nbSuperSeedsS.replaceAll(" ", "");
			try {
				this.nbSuperSeeds = Integer.parseInt(nbSuperSeedsS);
			} catch(Throwable e) {
				//this.nbSeeds = 0;
				//e.printStackTrace();
			}
		}
	}

	public void setRankFromHTML( String rank_str, float divisor ){
		if (rank_str == null) {
			return;
		}
		try{
			float f = Float.parseFloat( rank_str.trim() );
			
			rank = f / divisor;
		}catch( Throwable e ){
		}
	}

	public void setRankFromHTML( String rank_str ){
		if ( rank_str != null ){
			try{
					// either a float 0->1 or integer 0->100
				
				float f = Float.parseFloat( rank_str.trim() );
				
				if ( rank_str.indexOf( "." ) == -1 ){
					
					if ( f >= 0 &&  f <= 100 ){
						
						rank = f/100;
					}
				}else{
					
					if ( f >= 0 &&  f <= 1 ){
						
						rank = f;
					}
				}
			}catch( Throwable e ){
			}
		}
	}
	
	public float
	getRank()
	{
		if ( rank != -1 ){
			
			return( applyRankBias( rank ));
		}
		
		return( super.getRank());
	}
	
	public void setPublishedDate(Date date) {
		this.publishedDate = date;
	}
	
	public void setPublishedDateFromHTML(String publishedDate) {
		if(publishedDate != null) {
			publishedDate = removeHTMLTags(publishedDate);
			String publishedDateS = Entities.HTML40.unescape(publishedDate).replace((char)160,(char)32);
			this.publishedDate = dateParser.parseDate(publishedDateS);
		}
	}
	

	public void setSizeFromHTML(String size) {
		if(size != null) {
			size = removeHTMLTags(size);
			String sizeS = Entities.HTML40.unescape(size).replace((char)160,(char)32);
			sizeS = sizeS.replaceAll("<[^>]+>", " ");
			//Add a space between the digits and unit if there is none
			sizeS = sizeS.replaceFirst("(\\d)([a-zA-Z])", "$1 $2");
			try {
				StringTokenizer st = new StringTokenizer(sizeS," ");
				double base = Double.parseDouble(st.nextToken());
				String unit = "b";
				try {
					unit = st.nextToken().toLowerCase();
				} catch(Throwable e) {
					//No unit
				}
				long multiplier = 1;
				long KB_UNIT = 1024;
				long KIB_UNIT = 1024;
				if("mb".equals(unit)) {
					multiplier = KB_UNIT*KB_UNIT;
				} else if("mib".equals(unit)) {
					multiplier = KIB_UNIT*KIB_UNIT;
				} else if("m".equals(unit)) {
					multiplier = KIB_UNIT*KIB_UNIT;
				} else if("gb".equals(unit)) {
					multiplier = KB_UNIT*KB_UNIT*KB_UNIT;
				} else if("gib".equals(unit)) {
					multiplier = KIB_UNIT*KIB_UNIT*KIB_UNIT;
				} else if("g".equals(unit)) {
					multiplier = KIB_UNIT*KIB_UNIT*KIB_UNIT;
				} else if("kb".equals(unit)) {
					multiplier = KB_UNIT;
				} else if("kib".equals(unit)) {
					multiplier = KIB_UNIT;
				} else if("k".equals(unit)) {
					multiplier = KIB_UNIT;
				}
				
				this.size = (long) (base * multiplier);
			} catch(Throwable e) {
				//e.printStackTrace();
			}
		}
	}
	
	public void setVotesFromHTML(String votes_str) {
		if(votes_str != null) {
			votes_str = removeHTMLTags(votes_str);
			votes_str = Entities.HTML40.unescape(votes_str);
			votes_str = votes_str.replaceAll(",", "");
			votes_str = votes_str.replaceAll(" ", "");
			try {
				this.votes = Integer.parseInt(votes_str);
			} catch(Throwable e) {
				//e.printStackTrace();
			}
		}
	}	
	
	public void setVotesDownFromHTML(String votes_str) {
		if(votes_str != null) {
			votes_str = removeHTMLTags(votes_str);
			votes_str = Entities.HTML40.unescape(votes_str);
			votes_str = votes_str.replaceAll(",", "");
			votes_str = votes_str.replaceAll(" ", "");
			try {
				this.votesDown = Integer.parseInt(votes_str);
			} catch(Throwable e) {
				//e.printStackTrace();
			}
		}
	}
	
	public void setPrivateFromHTML(String privateTorrent) {
		if(privateTorrent != null && ! "".equals(privateTorrent)) {
			this.privateTorrent = true;
		}
	}
	
	public int
	getVotes()
	{
		return( votes );
	}
	
	public int
	getVotesDown()
	{
		return( votesDown );
	}
	
	public void setCDPLink(String cdpLink) {
		this.cdpLink = UrlUtils.unescapeXML(cdpLink);
	}
	
	public void setDownloadButtonLink(String downloadButtonLink) {
		this.downloadButtonLink = UrlUtils.unescapeXML(downloadButtonLink);
	}
	
	public void setTorrentLink(String torrentLink) {
		this.torrentLink = UrlUtils.unescapeXML(torrentLink);
	}
	
	public void setPlayLink(String playLink) {
		this.playLink = playLink;
	}
	
	public String getContentType() {
		return this.contentType;
	}
	
	public String getPlayLink() {
		return( reConstructLink(  playLink ));
	}
	
	public void setCategory(String category) {
		this.category = category;
		
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
		
	}
	
	public void setDrmKey(String drmKey) {
		this.drmKey = drmKey;
	}
	
	public void
	setHash(
		String	_hash )
	{
		try{
			hash = _hash.trim();
			
			if ( hash.length() == 32 ){
				
					// base 32 hash
				
			}else if ( hash.length() == 40 ){
				
					// base 16
				
				hash = Base32.encode( ByteFormatter.decodeString( hash ));
				
			}else{
				
				hash = null;
			}
		}catch( Throwable e ){
			
			Debug.printStackTrace(e);
			
			hash = null;
		}
		
		if(hash != null && downloadButtonLink == null) {
			setDownloadButtonLink(UrlUtils.normaliseMagnetURI(hash));			
		}
		if(hash != null && torrentLink == null) {
			setTorrentLink(UrlUtils.normaliseMagnetURI(hash));			
		}
	}
	
	public String
	getHash()
	{
		return( hash );
	}

	public String getCDPLink() {
		
		return reConstructLink(cdpLink);
	}

	public String getCategory() {
		return category;
	}

	public String getDownloadLink() {

		return reConstructLink(torrentLink);
		
	}
	
	public String getDownloadButtonLink() {

		//If we don't have a download button link, but we do have a direct download link,
		//then we should use the direct download link...
		if(downloadButtonLink != null) {
			return reConstructLink(downloadButtonLink);
		} else {
			return getDownloadLink();
		}
		
	}
	
	private String 
	reConstructLink(
		String link) 
	{
		if ( link != null ){
			
			String lc_link = link.toLowerCase();
			
			if ( 	lc_link.startsWith("http://") || 
					lc_link.startsWith("https://") ||
					lc_link.startsWith("azplug:") ||
					lc_link.startsWith("magnet:") ||
					lc_link.startsWith("bc:") ||
					lc_link.startsWith("bctp:") ||
					lc_link.startsWith("dht:" )){
				
				return( link );
			}
			
			if ( link.startsWith("/")){
				
				return((rootPageURL==null?"":rootPageURL) + link );
			}
			
			return((basePageURL==null?"":basePageURL) + link );
		}
		
		return( "" );
	}

	public String getName() {
		return name;
	}

	public int getNbPeers() {
		return nbPeers;
	}

	public int getNbSeeds() {
		return nbSeeds;
	}
	
	public int getNbSuperSeeds() {
		return nbSuperSeeds;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public long getSize() {
		return size;
	}
	
	public int
	getComments()
	{
		return( comments );
	}
	
	public String getSearchQuery() {
		return searchQuery;
	}
	
	public boolean isPrivate() {
		return privateTorrent;
	}
	
	public String getDRMKey() {
		return drmKey;
	}
	
	public float getAccuracy() {
		return -1;
	}
}
