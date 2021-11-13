/*
 * Created on Jan 27, 2009
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


package com.aelitis.azureus.core.devices;

import com.aelitis.azureus.core.devices.DeviceManager.UnassociatedDevice;

import java.io.File;
import java.net.InetAddress;


public interface 
DeviceMediaRenderer
	extends Device, TranscodeTarget
{	
	public static final int RS_PS3		= 1;
	public static final int RS_XBOX		= 2;
	public static final int RS_ITUNES	= 3;
	public static final int RS_WII		= 4;
	public static final int RS_BROWSER	= 5;
	public static final int RS_OTHER	= 6;

		/**
		 * THIS WILL CHANGE!!!
		 * @return	RS_<x>
		 */

	public int
	getRendererSpecies();

	public boolean
	canFilterFilesView();
	
	public void
	setFilterFilesView(
		boolean	filter );
	
	public boolean
	getFilterFilesView();
	
		// copy to device
	
	public boolean
	canCopyToDevice();
	
	public boolean
	getAutoCopyToDevice();
		
	public void
	setAutoCopyToDevice(
		boolean		auto );
	
	public int
	getCopyToDevicePending();
	
	public boolean
	canAutoStartDevice();
	
	public boolean
	getAutoStartDevice();
	
	public void
	setAutoStartDevice(
		boolean		auto );
	
		// copy to folder
	
	public boolean
	canCopyToFolder();
	
	public void
	setCanCopyToFolder(
		boolean		can );
	
	public File
	getCopyToFolder();
	
	public void
	setCopyToFolder(
		File		file );
	
	public int
	getCopyToFolderPending();
	
	public boolean
	getAutoCopyToFolder();
		
	public void
	setAutoCopyToFolder(
		boolean		auto );
	
	public void
	manualCopy()
	
		throws DeviceManagerException;
	
		// associate
	
	public boolean
	canAssociate();
	
	public void
	associate(
		UnassociatedDevice	assoc );
	
	public boolean
	canShowCategories();
	
	public void
	setShowCategories(
		boolean	b );
	
	public boolean
	getShowCategories();
	
	public boolean
	isRSSPublishEnabled();
	
	public void
	setRSSPublishEnabled(
		boolean		enabled );
	
	public InetAddress
	getAddress();
	
	public boolean
	canRestrictAccess();
	
	public String
	getAccessRestriction();
	
	public void
	setAccessRestriction(
		String		str );
}
