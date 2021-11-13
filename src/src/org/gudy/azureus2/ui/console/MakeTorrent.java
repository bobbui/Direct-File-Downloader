/*
 * File    : MakeTorrent.java
 * Created : 16 nov. 2003
 * By      : Olivier
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
 
package org.gudy.azureus2.ui.console;

import org.gudy.azureus2.core3.torrent.TOTorrent;
import org.gudy.azureus2.core3.torrent.TOTorrentCreator;
import org.gudy.azureus2.core3.torrent.TOTorrentFactory;
import org.gudy.azureus2.core3.torrent.TOTorrentProgressListener;
import org.gudy.azureus2.core3.util.TorrentUtils;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * @author Olivier
 *
 */
public class MakeTorrent implements TOTorrentProgressListener {

  private boolean verbose;
  
  private static final String[] validKeys = {"comment","announce-list","target","force_piece_size_pow2","verbose"};
  
  public void reportCurrentTask(String task_description) {
    if(verbose) {
      System.out.println(task_description);
    }
  }

  public void reportProgress(int percent_complete) {    
    if(verbose) {
      System.out.print("\r" + percent_complete + "%    ");
    }
  }
  
  public MakeTorrent(String file,URL url,Map parameters) {
    File fSrc = new File(file);
    
    String torrentName = (String) parameters.get("target");
    if(torrentName == null)
    torrentName = file + ".torrent";    
    File fDst = new File(torrentName);
    
    if(parameters.get("verbose") != null)
      verbose = true;
    
    TOTorrent torrent = null;
    String pieceSizeStr = (String) parameters.get("force_piece_size_pow2");
    if(pieceSizeStr != null) {
      try {     
        long pieceSize = 1l << Integer.parseInt(pieceSizeStr);
        TOTorrentCreator creator = TOTorrentFactory.createFromFileOrDirWithFixedPieceLength(fSrc,url,pieceSize);
        creator.addListener( this );
        torrent = creator.create();
      }catch(Exception e) {
        e.printStackTrace();
        return;
      }
    } else {
      try {
      	TOTorrentCreator creator = TOTorrentFactory.createFromFileOrDirWithComputedPieceLength(fSrc,url);
        creator.addListener( this );
        torrent = creator.create();
      } catch(Exception e) {
        e.printStackTrace();
        return;
      }
    }
    
    String comment = (String) parameters.get("comment");
    if(comment != null) {
      torrent.setComment(comment);
    }
    
    String announceList = (String) parameters.get("announce-list");
    if(announceList != null) {
      StringTokenizer st = new StringTokenizer(announceList,"|");
      List list = new ArrayList();
      List urls = new ArrayList();
      while(st.hasMoreTokens()) {
        String _url = st.nextToken();
        urls.add(_url);        
      }
      list.add(urls);            
      
      TorrentUtils.listToAnnounceGroups(list, torrent);
    }
    
    try {
      torrent.serialiseToBEncodedFile(fDst);
    } catch(Exception e) {
      e.printStackTrace();
    }
    
  }
  
  public static void main(String args[]) {
    if(args.length < 2) {
      usage();
      System.exit(0);
    }
    Map parameters = new HashMap();
    for(int i = 2 ; i < args.length ; i++) {
      boolean ok = parseParameter(args[i],parameters);
      if(!ok) System.exit(-1);
    }
    File f = new File(args[1]);
    if(!f.exists()) {
      System.out.println(args[1] + " is not a valid file / directory");
      System.exit(-1);
    }
    URL url = null;
    try {
      url = new URL(args[0]);
    } catch(Exception e) {
      System.out.println(args[0] + " is not a valid url");
      System.exit(-1);
    }
    new MakeTorrent(args[1],url,parameters);    
  }
  
  public static void usage() {
    System.out.println("Usage :");
    System.out.println("MakeTorrent <trackerurl> <file|dir> [options]");
    System.out.println("Options :");
    System.out.println("--comment=<comment>            Adds a comment to the torrent");
    System.out.println("--force_piece_size_pow2=<pow2> Specifies the piece size to use");
    System.out.println("--target=<target file>         Specifies a target torrent file");
    System.out.println("--verbose                      Verbose");
    System.out.println("--announce-list=url1[|url2|...] Use a list of trackers");
  }
  
  public static boolean parseParameter(String parameter,Map parameters) {
    if(parameter == null)
      return false;
    if(parameter.equalsIgnoreCase("--v") || parameter.equalsIgnoreCase("--verbose")) {
      parameters.put("verbose",new Integer(1));      
    }
    if(parameter.startsWith("--")) {
      try {    
        StringTokenizer st = new StringTokenizer(parameter.substring(2),"=");
        String key = st.nextToken();
        String value = "";
        String sep = "";
        while(st.hasMoreTokens()) {
          value += sep + st.nextElement();
          sep = "=";
        }
        boolean valid = false;
        for(int i = 0 ; i < validKeys.length ;i++) {
          if(validKeys[i].equalsIgnoreCase(key)) {
            valid = true;
            break;
          }
        }
        if(!valid) {
          System.out.println("Invalid parameter : " + key);
          return false;
        }
        parameters.put(key,value);
        return true;      
      } catch(Exception e) {
        System.out.println("Cannot parse " + parameter);
        return false;
      }
    }
    System.out.println("Cannot parse " + parameter);
    return false;
  }

}
