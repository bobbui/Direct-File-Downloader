/*
 * Created on 8 Aug 2006
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

package com.aelitis.azureus.util;

import org.gudy.azureus2.plugins.PluginInterface;
import org.gudy.azureus2.plugins.logging.LoggerChannel;
import org.gudy.azureus2.plugins.tracker.Tracker;
import org.gudy.azureus2.plugins.tracker.web.TrackerWebContext;
import org.gudy.azureus2.plugins.tracker.web.TrackerWebPageGenerator;
import org.gudy.azureus2.plugins.tracker.web.TrackerWebPageRequest;
import org.gudy.azureus2.plugins.tracker.web.TrackerWebPageResponse;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LocalResourceHTTPServer
	implements TrackerWebPageGenerator
{
	private static final String my_ip = "127.0.0.1";

	private int my_port;

	private LoggerChannel logger;

	private int resource_id_next;

	private Map published_resources = new HashMap();

	public LocalResourceHTTPServer(PluginInterface _plugin_interface,
			LoggerChannel _logger)

	throws Exception {
		logger = _logger;

		resource_id_next = new Random().nextInt(Integer.MAX_VALUE / 2);

		InetAddress bind_address = InetAddress.getByName(my_ip);

		TrackerWebContext context = _plugin_interface.getTracker().createWebContext(
				"Director:localResource", 0, Tracker.PR_HTTP, bind_address);

		my_port = context.getURLs()[0].getPort();

		if (logger != null) {
			logger.log("Local resource publisher running on " + my_ip + ":" + my_port);
		}

		context.addPageGenerator(this);
	}

	public boolean generate(TrackerWebPageRequest request,
			TrackerWebPageResponse response)

	throws IOException {
		String path = request.getURL();

		File resource;

		synchronized (this) {

			resource = (File) published_resources.get(path);
		}

		if (resource == null) {

			return (false);
		}

		return (response.useFile(resource.getParent(), "/" + resource.getName()));
	}

	public URL publishResource(File resource)

	throws Exception {
		synchronized (this) {

			resource = resource.getCanonicalFile();

			URL result = new URL("http://" + my_ip + ":" + my_port + "/"
					+ resource_id_next++ + "/" + resource.getName());

			published_resources.put(result.getPath(), resource);

			if (logger != null) {
				logger.log("Local resource added: " + resource + " -> " + result);
			}

			return (result);
		}
	}
}
