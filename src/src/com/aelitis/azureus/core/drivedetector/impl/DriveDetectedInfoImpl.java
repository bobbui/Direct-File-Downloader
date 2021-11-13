/*
 * Created on Jul 26, 2009 5:36:43 PM
 * Copyright (C) 2009 Aelitis, All Rights Reserved.
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
 */
package com.aelitis.azureus.core.drivedetector.impl;

import com.aelitis.azureus.core.drivedetector.DriveDetectedInfo;

import java.io.File;
import java.util.Collections;
import java.util.Map;

/**
 * @author TuxPaper
 * @created Jul 26, 2009
 *
 */
public class DriveDetectedInfoImpl
	implements DriveDetectedInfo
{
	File location;
	private Map info;
	
	public DriveDetectedInfoImpl(File location, Map info) {
		this.location = location;
		this.info = info;
	}

	public File getLocation() {
		return location;
	}
	
	public Object getInfo(String key) {
		return info.get(key);
	}
	
	public Map<String, Object> getInfoMap() {
		return Collections.unmodifiableMap(info);
	}

}
