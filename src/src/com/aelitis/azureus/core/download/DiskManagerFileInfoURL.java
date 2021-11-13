/*
 * Created on Feb 11, 2009
 * Created by Paul Gardner
 * 
 * Copyright 2009 Vuze, Inc.  All rights reserved.
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


package com.aelitis.azureus.core.download;

import com.aelitis.azureus.core.util.CopyOnWriteList;
import com.aelitis.azureus.plugins.extseed.ExternalSeedException;
import org.gudy.azureus2.core3.security.SEPasswordListener;
import org.gudy.azureus2.core3.security.SESecurityManager;
import org.gudy.azureus2.core3.util.*;
import org.gudy.azureus2.plugins.disk.*;
import org.gudy.azureus2.plugins.download.Download;
import org.gudy.azureus2.plugins.download.DownloadException;
import org.gudy.azureus2.plugins.utils.PooledByteBuffer;
import org.gudy.azureus2.pluginsimpl.local.utils.PooledByteBufferImpl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class 
DiskManagerFileInfoURL
	implements DiskManagerFileInfo, SEPasswordListener
{
	private URL					url;
	private byte[]				hash;
	
	private File				file;
	
	private Object	lock = new Object();
	
	private URL			redirected_url;
	private int			consec_redirect_fails;

	private boolean		file_cached;
	
	public
	DiskManagerFileInfoURL(
		URL					_url )
	{
		url	= _url;
		
		String url_str = url.toExternalForm();
		
		String id_key = "azcdid=";
		String dn_key = "azcddn=";
		
		int id_pos = url_str.indexOf( id_key );
		int dn_pos = url_str.indexOf( dn_key );

		int	min_pos = id_pos;
		if ( min_pos == -1 ){
			min_pos = dn_pos;
		}else{
			if ( dn_pos != -1 ){
				min_pos = Math.min( min_pos, dn_pos );
			}
		}
		
		if ( min_pos > 0 ){
			
			try{				
				url = new URL( url_str.substring( 0, min_pos-1 ) );
				
			}catch( Throwable e ){
				
				Debug.out( e );
			}
		}
		
		try{
			hash		= new SHA1Simple().calculateHash( ( "DiskManagerFileInfoURL" +  url.toExternalForm()).getBytes( "UTF-8" ));
			
		}catch( Throwable e ){
			
			Debug.out(e);
		}
		
		String file_name;
				
		if ( dn_pos != -1 ){
			
			String dn = url_str.substring( dn_pos + dn_key.length());
			
			dn_pos = dn.indexOf( '&' );
			
			if ( dn_pos != -1 ){
				
				dn = dn.substring( 0, dn_pos );
			}
		
			file_name = UrlUtils.decode( dn );
			
		}else{
			
			String path = url.getPath();
			
			int pos = path.lastIndexOf( "/" );
			
			if ( pos != -1 ){
				
				path = path.substring( pos+1 );
			}
			
			path = path.trim();
			
			if ( url_str.length() > 0 ){
				
				file_name = UrlUtils.decode( path );
				
			}else{
				
				file_name = Base32.encode( hash );
			}
		}
		
		file_name = FileUtil.convertOSSpecificChars( file_name, false );
		
		try{
			file = new File( AETemporaryFileHandler.createTempDir(), file_name );
			
		}catch( Throwable e ){
		
			file_name += ".tmp";
			
			file = new File( AETemporaryFileHandler.getTempDirectory(), file_name );
		}
	}
	
	public URL
	getURL()
	{
		return( url );
	}
	
	public void
	download()
	{
		synchronized( lock ){
			
			if ( file_cached ){
				
				return;
			}
			
			try{
				channel chan = createChannel();
				
				channel.request req = chan.createRequest();
				
				req.setAll();
				
				final FileOutputStream fos = new FileOutputStream( file );
				
				boolean	ok = false;
				
				try{
					req.addListener(
						new DiskManagerListener()
						{
							public void
							eventOccurred(
								DiskManagerEvent	event )
							{
								if ( event.getType() == DiskManagerEvent.EVENT_TYPE_FAILED ){
									
									throw( new RuntimeException( event.getFailure()));
								}
								
								PooledByteBuffer buffer = event.getBuffer();
								
								if ( buffer == null ){
									
									throw( new RuntimeException( "eh?" ));
								}
								
								try{
									
									fos.write( buffer.toByteArray());
									
								}catch( IOException e ){
									
									throw( new RuntimeException( "Failed to write to " + file, e ));
									
								}finally{
									
									buffer.returnToPool();
								}
							}
						});
				
					req.run();
					
					ok = true;
					
				}finally{
					
					try{
						fos.close();
						
					}catch( Throwable e ){
						
						Debug.out( e );
					}
					
					if ( !ok ){
						
						file.delete();
						
					}else{
						
						file_cached = true;
					}
				}
			}catch( Throwable e ){
				
				Debug.out( "Failed to cache file from " + url, e );
			}
		}
	}
	
	public void 
	setPriority(
		boolean b )
	{
	}
	
	public void 
	setSkipped(
		boolean b )
	{
		throw( new RuntimeException( "Not supported" ));
	}
	
	public int 
	getNumericPriority() 
	{
		return( 0 );
	}
	
	public int 
	getNumericPriorty() 
	{
		return( 0 );
	}
	
	public void 
	setNumericPriority(
		int priority) 
	{
		throw( new RuntimeException( "Not supported" ));
	}
	
	public void
	setDeleted(boolean b)
	{
	}	
	
	public void
	setLink(
		File	link_destination )
	{	
		throw( new RuntimeException( "Not supported" ));
	}
	
	public File
	getLink()
	{
		return( null );
	}
		 	
	public int 
	getAccessMode()
	{
		return( READ );
	}
	
	public long 
	getDownloaded()
	{
		return( getLength());
	}
	
	public long
	getLength()
	{
		if ( file_cached ){
			
			long len = file.length();
			
			if ( len > 0 ){
				
				return( len );
			}
		}
		
		return( -1 );
	}
	
	public File 
	getFile()
	{
		return( file );
	}
		
	public File
	getFile(
		boolean	follow_link )
	{
		return( file );
	}
	
	public int
	getIndex()
	{
		return( 0 );
	}
	
	public int 
	getFirstPieceNumber()
	{
		return( 0 );
	}
	
	public long
	getPieceSize()
	{
		return( 32*1024 );
	}
	
	public int 
	getNumPieces()
	{
		return( -1 );
	}
		
	public boolean 
	isPriority()
	{
		return( false );
	}
	
	public boolean 
	isSkipped()
	{
		return( false );
	}
	
	public boolean
	isDeleted()
	{
		return( false );
	}
	
	public byte[] 
	getDownloadHash() 
	
		throws DownloadException 
	{
		return( hash );
	}
	
	public Download 
	getDownload()
	
         throws DownloadException
    {
		throw( new DownloadException( "Not supported" ));
    }
	
	public channel
	createChannel()
	
		throws DownloadException
	{
		return( new channel());
	}
	
	public DiskManagerRandomReadRequest
	createRandomReadRequest(
		long						file_offset,
		long						length,
		boolean						reverse_order,
		DiskManagerListener			listener )
	
		throws DownloadException
	{
		throw( new DownloadException( "Not supported" ));
	}
	
	public PasswordAuthentication
	getAuthentication(
		String		realm,
		URL			tracker )
	{
		return( null );
	}
	
	public void
	setAuthenticationOutcome(
		String		realm,
		URL			tracker,
		boolean		success )
	{
	}
	
	public void
	clearPasswords()
	{
	}

	protected class
	channel
		implements DiskManagerChannel
	{
		private volatile boolean	channel_destroyed;
		private volatile long		channel_position;
		
		public request
		createRequest()
		{
			return( new request());
		}
		
		public DiskManagerFileInfo
		getFile()
		{
			return( DiskManagerFileInfoURL.this );
		}
		
		public long 
		getPosition() 
		{
			return( channel_position );
		}
		
		public boolean 
		isDestroyed() 
		{
			return( channel_destroyed );
		}
		
		public void
		destroy()
		{
			channel_destroyed	= true;
		}
		
		protected class
		request
			implements DiskManagerRequest
		{
			private long		offset;
			private long		length;
			private boolean		do_all_file;
			
			private long		position;
			
			private int			max_read_chunk = 128*1024;;
			
			private volatile boolean	request_cancelled;
			
			private CopyOnWriteList<DiskManagerListener>		listeners = new CopyOnWriteList<DiskManagerListener>();
			
			public void
			setType(
				int			type )
			{
				if ( type != DiskManagerRequest.REQUEST_READ ){
					
					throw( new RuntimeException( "Not supported" ));
				}
			}
			
			public void
			setAll()
			{
				do_all_file = true;
				offset		= 0;
				setLength( -1 );
			}
			
			public void
			setOffset(
				long		_offset )
			{
				offset		= _offset;
			}
			
			public void
			setLength(
				long		_length )
			{
					// length can be -1 here meaning 'to the end'
				
				length		= _length==-1?Long.MAX_VALUE:_length;
			}
				
			public void
			setMaximumReadChunkSize(
				int 	size )
			{
				if ( size > 16*1024 ){
				
					max_read_chunk = size;
				}
			}
			
			public long
			getAvailableBytes()
			{
				return( getRemaining());
			}
						
			public long
			getRemaining()
			{
				return( length==Long.MAX_VALUE?length:(offset + length - position ));
			}
			
			public void
			run()
			{
				try{				
					byte[] buffer = new byte[max_read_chunk];
					
					long	rem		= length;
					long	pos 	= offset;
						
					InputStream	is = null;
					
					try{
						SESecurityManager.setThreadPasswordHandler( DiskManagerFileInfoURL.this );
						
						// System.out.println( "Connecting to " + url + ": " + Thread.currentThread().getId());

							HttpURLConnection	connection;
							int					response;
							
							Set<String>	redirect_urls = new HashSet<String>();
							
redirect_loop:
							while( true ){
								
								URL	target = redirected_url==null?url:redirected_url;
								
								for ( int ssl_loop=0; ssl_loop<2; ssl_loop++ ){
									
									try{
										connection = (HttpURLConnection)target.openConnection();
										
										if ( connection instanceof HttpsURLConnection ){
											
											HttpsURLConnection ssl_con = (HttpsURLConnection)connection;
											
												// allow for certs that contain IP addresses rather than dns names
					  	
										ssl_con.setHostnameVerifier(
											new HostnameVerifier()
											{
												public boolean
												verify(
													String		host,
													SSLSession	session )
												{
													return( true );
												}
											});
									}
									
									connection.setRequestProperty( "Connection", "Keep-Alive" );
									
									if ( !do_all_file ){
									
										connection.setRequestProperty( "Range", "bytes=" + offset + "-" + (offset+length-1));
									}
									
									connection.setConnectTimeout( 20*1000 );
																
									connection.connect();
									
									connection.setReadTimeout( 10*1000 );
																					
									response = connection.getResponseCode();
					
									if (	response == HttpURLConnection.HTTP_ACCEPTED || 
											response == HttpURLConnection.HTTP_OK ||
											response == HttpURLConnection.HTTP_PARTIAL ){
										
										if ( redirected_url != null ){
											
											consec_redirect_fails = 0;
										}
										
										break redirect_loop;
										
									}else if ( 	response == HttpURLConnection.HTTP_MOVED_TEMP ||
												response == HttpURLConnection.HTTP_MOVED_PERM ){
										
											// auto redirect doesn't work from http to https or vice-versa
										
										String	move_to = connection.getHeaderField( "location" );
										
										if ( move_to != null ){
											
											if ( redirect_urls.contains( move_to ) || redirect_urls.size() > 32 ){
												
												throw( new ExternalSeedException( "redirect loop" )); 
											}
											
											redirect_urls.add( move_to );
											
											redirected_url = new URL( move_to );
											
											continue redirect_loop;
										}
									}
									
									if ( redirected_url == null ){
										
										break redirect_loop;
									}
									
										// try again with original URL
									
									consec_redirect_fails++;
									
									redirected_url = null;
								
								}catch( SSLException e ){
									
									if ( ssl_loop == 0 ){
										
										if ( SESecurityManager.installServerCertificates( target ) != null ){
											
												// certificate has been installed
											
											continue;	// retry with new certificate
										}
									}
				
									throw( e );
								}
								
									// don't need another SSL loop
								
								break;
							}
						}
						
						URL final_url = connection.getURL();
						
						if ( consec_redirect_fails < 10 && !url.toExternalForm().equals( final_url.toExternalForm())){
							
							redirected_url = final_url;
						}
			            
						is = connection.getInputStream();
						
						while( rem > 0 ){

							if ( request_cancelled ){
								
								throw( new Exception( "Cancelled" ));
								
							}else if ( channel_destroyed ){
								
								throw( new Exception( "Destroyed" ));
							}
							
							int	len = is.read( buffer );
							
							if ( len == -1 ){
								
								if ( length == Long.MAX_VALUE ){
									
									break;
									
								}else{
									
									throw( new Exception( "Premature end of stream (complete)" ));
								}
							}else if ( len == 0 ){
								
								sendEvent( new event( pos ));
								
							}else{
																
								sendEvent( new event( new PooledByteBufferImpl( buffer, 0, len ), pos, len ));
								
								rem -= len;
								pos	+= len;
							}
						}
					}finally{
						
						SESecurityManager.unsetThreadPasswordHandler();

						// System.out.println( "Done to " + url + ": " + Thread.currentThread().getId() + ", outcome=" + outcome );

						if ( is != null ){
							
							try{
								is.close();
								
							}catch( Throwable e ){
								
							}
						}
					}							
				}catch( Throwable e ){
					
					sendEvent( new event( e ));
				}
			}
			
			public void
			cancel()
			{
				request_cancelled = true;
			}
			
			public void
			setUserAgent(
				String		agent )
			{	
			}
			
			protected void
			sendEvent(
				event		ev )
			{					
				for ( DiskManagerListener l: listeners ){
					
					l.eventOccurred( ev );
				}
			}
			
			public void
			addListener(
				DiskManagerListener	listener )
			{
				listeners.add( listener );
			}
			
			public void
			removeListener(
				DiskManagerListener	listener )
			{
				listeners.remove( listener );
			}
		
			protected class
			event
				implements DiskManagerEvent
			{
				private int					event_type;
				private Throwable			error;
				private PooledByteBuffer	buffer;
				private long				event_offset;
				private int					event_length;
				
				protected
				event(
					Throwable		_error )
				{
					event_type	= DiskManagerEvent.EVENT_TYPE_FAILED;
					error		= _error;
				}
					
				protected 
				event(
					long				_offset )
				{
					event_type		= DiskManagerEvent.EVENT_TYPE_BLOCKED;

					event_offset	= _offset;	
					
					channel_position	= _offset;
				}
				
				protected
				event(
					PooledByteBuffer	_buffer,
					long				_offset,
					int					_length )
				{
					event_type		= DiskManagerEvent.EVENT_TYPE_SUCCESS;
					buffer			= _buffer;
					event_offset	= _offset;
					event_length	= _length;
					
					channel_position	= _offset + _length - 1;
				}
				
				public int
				getType()
				{
					return( event_type );
				}
				
				public DiskManagerRequest
				getRequest()
				{
					return( request.this );
				}
				
				public long
				getOffset()
				{
					return( event_offset );
				}
				
				public int
				getLength()
				{
					return( event_length );
				}
				
				public PooledByteBuffer
				getBuffer()
				{
					return( buffer );
				}
				
				public Throwable
				getFailure()
				{
					return( error );
				}
			}
		}
	}
}
