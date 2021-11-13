/*
 * Created on 16 Jun 2006
 * Created by Paul Gardner
 * Copyright (C) 2006 Aelitis, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * AELITIS, SAS au capital de 46,603.30 euros
 * 8 Allee Lenotre, La Grille Royale, 78600 Le Mesnil le Roi, France.
 *
 */

package com.aelitis.azureus.core.networkmanager.impl.tcp;

import com.aelitis.azureus.core.networkmanager.ProtocolEndpoint;
import com.aelitis.azureus.core.networkmanager.ProtocolStartpoint;
import com.aelitis.azureus.core.networkmanager.TransportStartpoint;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

public class 
TransportStartpointTCP 
	implements TransportStartpoint, ProtocolStartpoint
{
	private TransportEndpointTCP		ep;

	public
	TransportStartpointTCP(
		TransportEndpointTCP	_ep )
	{
		ep		= _ep;
	}
	
	public ProtocolStartpoint
	getProtocolStartpoint()
	{
		return( this );
	}
	
	public int
	getType()
	{
		return( ProtocolEndpoint.PROTOCOL_TCP );
	}
	
	public InetSocketAddress 
	getAddress() 
	{
		SocketChannel channel = ep.getSocketChannel();
		
		if ( channel != null ){
			
			Socket socket = channel.socket();
			
			if ( socket != null ){
				
				return((InetSocketAddress)socket.getLocalSocketAddress());
			}
		}
		
		return( null );
	}
	
	public String
	getDescription()
	{
		InetSocketAddress address = getAddress();
		
		if ( address == null ){
			
			return( "not connected" );
		}else{
			
			return( address.toString());
		}
	}
}
