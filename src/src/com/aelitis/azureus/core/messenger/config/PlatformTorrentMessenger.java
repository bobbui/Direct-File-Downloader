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

package com.aelitis.azureus.core.messenger.config;

import org.gudy.azureus2.core3.torrent.TOTorrent;

import java.util.Map;

/**
 * @author TuxPaper
 * @created Oct 13, 2006
 *
 */
public class PlatformTorrentMessenger
{
	public static String LISTENER_ID = "torrent";

	public static String OP_STREAMCOMPLETE = "stream-complete";

	public static void streamComplete(TOTorrent torrent, long waitTime,
			int maxSeekAheadSecs, int numRebuffers, int numHardRebuffers) {
	}

	public static void streamComplete(TOTorrent torrent, Map info) {
	}
}
