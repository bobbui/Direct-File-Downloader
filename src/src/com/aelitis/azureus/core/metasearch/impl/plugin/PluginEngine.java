/*
 * Created on Jun 20, 2008
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


package com.aelitis.azureus.core.metasearch.impl.plugin;

import com.aelitis.azureus.core.metasearch.*;
import com.aelitis.azureus.core.metasearch.impl.EngineImpl;
import com.aelitis.azureus.core.metasearch.impl.MetaSearchImpl;
import org.gudy.azureus2.core3.util.AESemaphore;
import org.gudy.azureus2.core3.util.Debug;
import org.gudy.azureus2.plugins.utils.search.SearchInstance;
import org.gudy.azureus2.plugins.utils.search.SearchObserver;
import org.gudy.azureus2.plugins.utils.search.SearchProvider;
import org.gudy.azureus2.plugins.utils.search.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class 
PluginEngine
	extends EngineImpl
{
	private static int[][] FIELD_MAP = {
		
		{ SearchResult.PR_CATEGORY,				Engine.FIELD_CATEGORY },
		{ SearchResult.PR_COMMENTS,				Engine.FIELD_COMMENTS },
		{ SearchResult.PR_CONTENT_TYPE,			Engine.FIELD_CONTENT_TYPE },
		{ SearchResult.PR_DETAILS_LINK,			Engine.FIELD_CDPLINK },
		{ SearchResult.PR_DOWNLOAD_BUTTON_LINK,	Engine.FIELD_DOWNLOADBTNLINK },
		{ SearchResult.PR_DOWNLOAD_LINK,		Engine.FIELD_TORRENTLINK },
		{ SearchResult.PR_DRM_KEY,				Engine.FIELD_DRMKEY },
		{ SearchResult.PR_LEECHER_COUNT,		Engine.FIELD_PEERS },
		{ SearchResult.PR_NAME,					Engine.FIELD_NAME },
		{ SearchResult.PR_PLAY_LINK,			Engine.FIELD_PLAYLINK },
		{ SearchResult.PR_PRIVATE,				Engine.FIELD_PRIVATE },
		{ SearchResult.PR_PUB_DATE,				Engine.FIELD_DATE },
		{ SearchResult.PR_SEED_COUNT,			Engine.FIELD_SEEDS },
		{ SearchResult.PR_SIZE,					Engine.FIELD_SIZE },
		{ SearchResult.PR_SUPER_SEED_COUNT,		Engine.FIELD_SUPERSEEDS },
		{ SearchResult.PR_VOTES,				Engine.FIELD_VOTES },
	};
		
		
	public static EngineImpl
	importFromBEncodedMap(
		MetaSearchImpl		meta_search,
		Map					map )
	
		throws IOException
	{
		return( new PluginEngine( meta_search, map ));
	}
	
	private SearchProvider			provider;
	
	public
	PluginEngine(
		MetaSearchImpl		_meta_search,
		long				_id,
		SearchProvider		_provider )
	{
		super( _meta_search, Engine.ENGINE_TYPE_PLUGIN, _id, 0, 1.0f, (String)_provider.getProperty( SearchProvider.PR_NAME ));
		
		provider	= _provider;
		
		setSource( ENGINE_SOURCE_LOCAL );
	}
	
	protected
	PluginEngine(
		MetaSearchImpl		_meta_search,
		Map					_map )
	
		throws IOException
	{
		super( _meta_search, _map );
		
			// recovery from when incorrectly defaulted to 0.0
		
		if ( getRankBias() == 0.0f ){
			
			setRankBias( 1.0f );
		}
		
		setSource( ENGINE_SOURCE_LOCAL );
	}
	
	public Map 
	exportToBencodedMap()
	
		throws IOException
	{
		return( exportToBencodedMap( false ));
	}
	
	public Map 
	exportToBencodedMap(
		boolean	generic )
	
		throws IOException 
	{
		Map	res = new HashMap();
				
		super.exportToBencodedMap( res, generic );
		
		return( res );
	}
	
	public void
	setProvider(
		SearchProvider		_provider )
	{
		provider	= _provider;
	}
	
	public SearchProvider
	getProvider()
	{
		return( provider );
	}
	
	protected boolean
	useAccuracyForRank()
	{
		if ( provider == null ){
			
			return( false );
		}
		
		Boolean val = (Boolean)provider.getProperty( SearchProvider.PR_USE_ACCURACY_FOR_RANK );
		
		if ( val == null ){
			
			return( false );
		}
		
		return( val.booleanValue());
	}
	public boolean
	isActive()
	{
		return( provider != null && super.isActive());
	}
	
	public String 
	getNameEx() 
	{
		return( super.getName() + ": (plugin)" );
	}
	
	public String 
	getDownloadLinkCSS() 
	{
		if ( provider == null ){
			
			return( null );
		}
		
		return((String)provider.getProperty( SearchProvider.PR_DOWNLOAD_LINK_LOCATOR ));
	}
	
	public boolean 
	supportsField(
		int		field )
	{
		if ( provider == null ){
			
			return( false );
		}
		
		int[] supports = (int[])provider.getProperty( SearchProvider.PR_SUPPORTS_RESULT_FIELDS );
		
		if ( supports == null ){
			
			return( true );
		}
		
		for (int i=0;i<FIELD_MAP.length;i++){
			
			int[]	entry = FIELD_MAP[i];
			
			if ( entry[1] == field ){
				
				for (int j=0;j<supports.length;j++){
					
					if ( supports[j] == entry[0] ){
						
						return( true );
					}
				}
				
				break;
			}
		}
		
		return( false );
	}
	
	public boolean
	supportsContext(
		String	context_key )
	{
		return( false );
	}
	
	public boolean 
	isShareable() 
	{
		return( false );
	}
	
	public String 
	getIcon() 
	{
		if ( provider == null ){
			
			return( null );
		}
		
		return((String)provider.getProperty( SearchProvider.PR_ICON_URL ));
	}
	
	public String 
	getReferer() 
	{
		if ( provider == null ){
			
			return( null );
		}
		
		return((String)provider.getProperty( SearchProvider.PR_REFERER ));
	}
	
	protected Result[] 
	searchSupport(
		SearchParameter[] 		params, 
		Map						searchContext,
		final int 				desired_max_matches,
		final int				absolute_max_matches,
		String 					headers, 
		final ResultListener 	listener )
	
		throws SearchException 
	{
		if ( provider == null ){
			
			provider = getMetaSearch().resolveProvider( this );
			
			if ( provider == null ){
				
				return( new Result[0]  );
			}
		}
		
		Map search_parameters = new HashMap();
		
		String	term = null;
		
		for (int i=0;i<params.length;i++){
			
			SearchParameter param = params[i];
			
			String pattern 	= param.getMatchPattern();
			String value	= param.getValue();
			
			if ( pattern.equals( "s" )){
				
				term = value;
				
				search_parameters.put( SearchProvider.SP_SEARCH_TERM, value );
				
			}else if ( pattern.equals( "m" )){
			
				search_parameters.put( SearchProvider.SP_MATURE, new Boolean(value ));

			}else{
				
				Debug.out( "Unrecognised search parameter '" + pattern + "=" + value + "' ignored" );
			}
		}
			
		final String f_term = term;
		
		try{
			final List<PluginResult>	results = new ArrayList<PluginResult>();
			
			final AESemaphore	sem = new AESemaphore( "waiter" );

			provider.search( 
				search_parameters,
				new SearchObserver()
				{
					private boolean	complete = false;
					
					public void 
					resultReceived(
						SearchInstance 		search,
						SearchResult 		result )
					{
						PluginResult p_result = new PluginResult( PluginEngine.this, result, f_term );
						
						synchronized( this ){
							
							if ( complete ){
								
								return;
							}
							
							results.add( p_result );
						}
						
						if ( listener != null ){
							
							listener.resultsReceived( PluginEngine.this, new Result[]{ p_result });
						}
						
						synchronized( this ){

							if ( absolute_max_matches >= 0 && results.size() >= absolute_max_matches ){
								
								complete = true;
								
								sem.release();
							}
						}
					}
					
					public void 
					cancelled() 
					{
						sem.release();
					}
					
					public void 
					complete() 
					{
						sem.release();
					}
					
					public Object 
					getProperty(
						int property ) 
					{
						if ( property == PR_MAX_RESULTS_WANTED ){
							
							return( new Long( desired_max_matches ));
						}
						
						return( null );
					}
				});
			
			sem.reserve();
			
			if ( listener != null ){
				
				listener.resultsComplete( this );
			}
			
			return((Result[])results.toArray(new Result[results.size()]));
			
		}catch( Throwable e ){
			
			throw( new SearchException( "Search failed", e ));
		}
	}
}
