/*
 * File    : CategoryImpl.java
 * Created : 09 feb. 2004
 * By      : TuxPaper
 *
 * Azureus - a Java Bittorrent client
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details ( see the LICENSE file ).
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.gudy.azureus2.core3.category.impl;

import com.aelitis.azureus.core.AzureusCoreFactory;
import com.aelitis.azureus.core.networkmanager.LimitedRateGroup;
import com.aelitis.azureus.core.tag.TagDownload;
import com.aelitis.azureus.core.tag.Taggable;
import com.aelitis.azureus.core.tag.impl.TagBase;
import org.gudy.azureus2.core3.category.Category;
import org.gudy.azureus2.core3.category.CategoryListener;
import org.gudy.azureus2.core3.download.DownloadManager;
import org.gudy.azureus2.core3.download.DownloadManagerState;
import org.gudy.azureus2.core3.internat.MessageText;
import org.gudy.azureus2.core3.util.IndentWriter;
import org.gudy.azureus2.core3.util.ListenerManager;
import org.gudy.azureus2.core3.util.ListenerManagerDispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class 
CategoryImpl 
	extends TagBase 
	implements Category, Comparable, TagDownload 
{
  private String sName;
  private int type;
  private List<DownloadManager> managers = new ArrayList<DownloadManager>();

  private int upload_speed;
  private int download_speed;

  private final Map<String,String>	attributes;
  
  private static AtomicInteger	tag_ids = new AtomicInteger();
  
  private LimitedRateGroup upload_limiter = 
	  new LimitedRateGroup()
	  {
		  public String 
		  getName() 
		  {
			  return( "cat_up: " + sName);
		  }
		  public int 
		  getRateLimitBytesPerSecond()
		  {
			  return( upload_speed );
		  }
		  
		  public void
		  updateBytesUsed(
				int	used )
		  {
			  
		  }
	  };
   
  private LimitedRateGroup download_limiter = 
	  new LimitedRateGroup()
  {
	  public String 
	  getName() 
	  {
		  return( "cat_down: " + sName);
	  }
	  public int 
	  getRateLimitBytesPerSecond()
	  {
		  return( download_speed );
	  }
	  
	  public void
	  updateBytesUsed(
			int	used )
	  {
		  
	  }
  };  
  
  private static final int LDT_CATEGORY_DMADDED     = 1;
  private static final int LDT_CATEGORY_DMREMOVED   = 2;
	private ListenerManager<CategoryListener>	category_listeners = ListenerManager.createManager(
		"CatListenDispatcher",
		new ListenerManagerDispatcher<CategoryListener>()
		{
			public void
			dispatch(
				CategoryListener		target,
				int						type,
				Object					value )
			{
				if ( type == LDT_CATEGORY_DMADDED )
					target.downloadManagerAdded((Category) CategoryImpl.this, (DownloadManager)value);
				else if ( type == LDT_CATEGORY_DMREMOVED )
					target.downloadManagerRemoved(CategoryImpl.this, (DownloadManager)value);
			}
		});

  public CategoryImpl(CategoryManagerImpl manager, String sName, int maxup, int maxdown, Map<String,String> _attributes ) {
	super( manager, tag_ids.incrementAndGet(), sName, true );
	
    this.sName = sName;
    this.type = Category.TYPE_USER;
    upload_speed	= maxup;
    download_speed	= maxdown;
    attributes = _attributes;
  }

  public CategoryImpl(CategoryManagerImpl manager, String sName, int type, Map<String,String> _attributes) {
	super( manager, tag_ids.incrementAndGet(), sName, true );
    this.sName = sName;
    this.type = type;
    attributes = _attributes;
  }

  public void addCategoryListener(CategoryListener l) {
	  category_listeners.addListener( l );
  }

  public void removeCategoryListener(CategoryListener l) {
	  category_listeners.removeListener( l );
  }

  public boolean
  hasCategoryListener(
	 CategoryListener	l )
  {
	  return( category_listeners.hasListener( l ));
  }
  
  public String getName() {
    return sName;
  }
  
  public int getType() {
    return type;
  }
  
  public List<DownloadManager> getDownloadManagers(List<DownloadManager> all_dms) {
	  if ( type == Category.TYPE_USER ){
		  return managers;
	  }else if ( type == Category.TYPE_ALL || all_dms == null ){
		  return all_dms;
	  }else{
		  List<DownloadManager> result = new ArrayList<DownloadManager>();
		  for (int i=0;i<all_dms.size();i++){
			  DownloadManager dm = all_dms.get(i);
			  Category cat = dm.getDownloadState().getCategory();
			  if ( cat == null || cat.getType() == Category.TYPE_UNCATEGORIZED){
				  result.add( dm );
			  }
		  }
		  
		  return( result );
	  }
  }
  
  public void addManager(DownloadManagerState manager_state) {
  	Category manager_cat = manager_state.getCategory();
		if ((type != Category.TYPE_UNCATEGORIZED && manager_cat != this)
				|| (type == Category.TYPE_UNCATEGORIZED && manager_cat != null)) {
    	manager_state.setCategory(this);
      // we will be called again by CategoryManager.categoryChange
      return;
    }
    
    DownloadManager	manager = manager_state.getDownloadManager();
    
    	// can be null if called during downloadmanagerstate construction
    if ( manager == null ){
    	return;
    }
    
    addTaggable( manager );
    
    if (!managers.contains(manager)) {
    	if (type == Category.TYPE_USER) {
    		managers.add(manager);
    	}
      
      manager.addRateLimiter( upload_limiter, true );
      manager.addRateLimiter( download_limiter, false );
      
      int pri = getIntAttribute( AT_UPLOAD_PRIORITY, -1 );
      
      if ( pri >= 0 ){
    	  
    	  	// another call-during-construction issue to avoid here
    	  
    	  if ( manager.getDownloadState() != null ){
    	  
    		  manager.setUploadPriority( pri );
    	  }
      }
      
      category_listeners.dispatch(LDT_CATEGORY_DMADDED, manager);
    }
  }

  public void removeManager(DownloadManagerState manager_state) {
    if (manager_state.getCategory() == this) {
    	manager_state.setCategory(null);
      // we will be called again by CategoryManager.categoryChange
      return;
    }
    DownloadManager	manager = manager_state.getDownloadManager();

   	// can be null if called during downloadmanagerstate construction
    if ( manager == null ){
    	return;
    }
    
    removeTaggable( manager );
    
    if (type != Category.TYPE_USER || managers.contains(manager)) {
      managers.remove(manager);
      
      manager.removeRateLimiter( upload_limiter, true );
      manager.removeRateLimiter( download_limiter, false );
 
      int pri = getIntAttribute( AT_UPLOAD_PRIORITY, -1 );
      
      if ( pri >= 0 ){
    	  
    	  	// another call-during-construction issue to avoid here
    	  
    	  if ( manager.getDownloadState() != null ){
    	  
    		  manager.setUploadPriority( 0 );
    	  }
      }
      
      category_listeners.dispatch( LDT_CATEGORY_DMREMOVED, manager );
    }
  }

  public void
  setDownloadSpeed(
	int		speed )
  {
	  if ( download_speed != speed ){
		  
		  download_speed = speed;
		  
		  CategoryManagerImpl.getInstance().saveCategories(this);
	  }
  }
  
  public int
  getDownloadSpeed()
  {
	  return( download_speed );
  }
  
  public void
  setUploadSpeed(
	int		speed )
  {
	  if ( upload_speed != speed ){
		  
		  upload_speed	= speed;
	  
		  CategoryManagerImpl.getInstance().saveCategories(this);
	  }
  }
  
  public int
  getUploadSpeed()
  {
	  return( upload_speed );
  }
  
  protected void
  setAttributes(
	Map<String,String> a )
  {
	  attributes.clear();
	  attributes.putAll( a );
  }
  
  protected Map<String,String>
  getAttributes()
  {
	  return( attributes );
  }
  
  public String
  getStringAttribute(
	String		name )
  {
	  return( attributes.get(name));
  }
  
  public void
  setStringAttribute(
	String		name,
	String		value )
  {
	  String old = attributes.put( name, value );
	  
	  if ( old == null || !old.equals( value )){
	  
		  CategoryManagerImpl.getInstance().saveCategories(this);
	  }

  }
  
  public int
  getIntAttribute(
	String		name )
  {
	  return( getIntAttribute( name, 0 ));
  }
  
  private int
  getIntAttribute(
	String		name,
	int			def )
  {
	 String str = getStringAttribute( name );
	 
	 if ( str == null ){
		 return( def );
	 }
	 return( Integer.parseInt( str ));
  }
  
  public void
  setIntAttribute(
	String		name,
	int			value )
  {
	  String	str_val = String.valueOf( value );
	  
	  String old = attributes.put( name, str_val );
	  
	  if ( old == null || !old.equals( value )){
	  
		  if ( name.equals( AT_UPLOAD_PRIORITY )){
			  
			  for ( DownloadManager dm: managers ){
				  
				  dm.setUploadPriority( value );
			  }
		  }
		  
		  CategoryManagerImpl.getInstance().saveCategories(this);
	  }

  }
  public boolean
  getBooleanAttribute(
	String		name )
  {
	 String str = getStringAttribute( name );
	 
	 return( str != null && str.equals( "true" ));
  }
  
  public void
  setBooleanAttribute(
	String		name,
	boolean		value )
  {
	  String old = attributes.put( name, value?"true":"false" );
	  
	  if ( old == null || !old.equals( value )){
	  
		  CategoryManagerImpl.getInstance().saveCategories(this);
	  }

  }
  
  public int 
  getTaggableTypes() 
  {
	  return( Taggable.TT_DOWNLOAD );
  }
  
  public String
  getTagName(
    boolean		localize )
  {
	  if ( localize ){
		  if ( type == Category.TYPE_ALL ||  type == Category.TYPE_UNCATEGORIZED){
			  return( MessageText.getString( getTagNameRaw()));
		  }
	  }
	  return( super.getTagName(localize));
  }
	
  public boolean
  supportsTagUploadLimit()
  {
	  return( true );
  }

  public boolean
  supportsTagDownloadLimit()
  {
	  return( true );
  }
	
  public int
  getTagUploadLimit()
  {
	  return( getUploadSpeed());
  }

  public void
  setTagUploadLimit(
		  int		bps )
  {
	  setUploadSpeed( bps );
  }

  public int
  getTagCurrentUploadRate()
  {
	  return( -1 );
  }

  public int
  getTagDownloadLimit()
  {
	  return( getDownloadSpeed());
  }

  public void
  setTagDownloadLimit(
		  int		bps )
  {
	  setDownloadSpeed( bps );
  }

  public int
  getTagCurrentDownloadRate()
  {
	  return( -1 );
  }
  
  public boolean
  getCanBePublicDefault()
  {
	  return( type == Category.TYPE_USER );
  }
  
  public List<DownloadManager>
  getTaggedDownloads()
  {
	 return( getDownloadManagers( AzureusCoreFactory.getSingleton().getGlobalManager().getDownloadManagers()));
  }
  
  public List<Taggable> 
  getTagged() 
  {
	  return( new ArrayList<Taggable>( getTaggedDownloads()));
  }
  
  protected void
  destroy()
  {
	  removeTag();
  }
  
  public int compareTo(Object b)
  {
    boolean aTypeIsUser = type == Category.TYPE_USER;
    boolean bTypeIsUser = ((Category)b).getType() == Category.TYPE_USER;
    if (aTypeIsUser == bTypeIsUser)
      return sName.compareToIgnoreCase(((Category)b).getName());
    if (aTypeIsUser)
      return 1;
    return -1;
  }
  
  public void dump(IndentWriter writer) {
	if ( upload_speed != 0 ){
		writer.println( "up=" + upload_speed );
	}
	if ( download_speed != 0 ){
		writer.println( "down=" + download_speed );
	}
	if ( attributes.size() > 0 ){
		
		writer.println( "attributes: " + attributes );
	}
	}
}
