/**
 * Copyright (C) 2006 Aelitis, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * AELITIS, SAS au capital de 63.529,40 euros
 * 8 Allee Lenotre, La Grille Royale, 78600 Le Mesnil le Roi, France.
 *
 */

package com.aelitis.azureus.core.messenger;

import com.aelitis.azureus.core.messenger.browser.BrowserMessageDispatcher;
import com.aelitis.azureus.core.messenger.browser.listeners.BrowserMessageListener;

import java.util.Collection;
import java.util.Map;

/**
 * @author TuxPaper
 * @created Oct 9, 2006
 *
 */
public interface ClientMessageContext
{
	public abstract void addMessageListener(BrowserMessageListener listener);

	public abstract void removeMessageListener(String listenerId);

	public abstract void removeMessageListener(BrowserMessageListener listener);

	public abstract Object getBrowserData(String key);

	public abstract void setBrowserData(String key, Object value);

	/**
	 * Sends a message to the JavaScript message dispatcher in the page.
	 * 
	 * @param key identifies the listener to receive the message
	 * @param op identifies the operation to perform
	 */
	public abstract boolean sendBrowserMessage(String key, String op);

	/**
	 * Sends a message to the JavaScript message dispatcher in the page.
	 * 
	 * @param key identifies the listener to receive the message
	 * @param op identifies the operation to perform
	 * @param params optional message parameters
	 */
	public abstract boolean sendBrowserMessage(String key, String op, Map params);

	public abstract boolean executeInBrowser(final String javascript);

	/**
	 * Displays a debug message tagged with the context ID.
	 * 
	 * @param message sent to the debug log
	 */
	public abstract void debug(String message);

	/**
	 * Displays a debug message and exception tagged with the context ID.
	 * 
	 * @param message sent to the debug log
	 * @param t exception to log with message
	 */
	public abstract void debug(String message, Throwable t);

	public BrowserMessageDispatcher getDispatcher();
	
	/**
	 * @param key
	 * @param op
	 * @param params
	 * @return
	 *
	 * @since 3.0.1.5
	 */
	boolean sendBrowserMessage(String key, String op, Collection params);

	/**
	 * @param dispatcher
	 *
	 * @since 3.0.5.3
	 */
	void setMessageDispatcher(BrowserMessageDispatcher dispatcher);
	
	void setTorrentURLHandler( torrentURLHandler handler );
	
	public interface
	torrentURLHandler
	{
		public void
		handleTorrentURL(
			String		url );
	}

	/**
	 * @param contentNetwork
	 *
	 * @since 4.0.0.5
	 */
	void setContentNetworkID(long id);

	/**
	 * @return
	 *
	 * @since 4.0.0.5
	 */
	long getContentNetworkID();
}