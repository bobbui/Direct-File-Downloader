package org.gudy.azureus2.plugins.ui;

import org.gudy.azureus2.plugins.ui.toolbar.UIToolBarActivationListener;
import org.gudy.azureus2.plugins.ui.toolbar.UIToolBarEnablerBase;

import java.util.Map;

/**
 * 
 * @author Vuze
 *
 */
public interface UIPluginViewToolBarListener
	extends UIToolBarActivationListener, UIToolBarEnablerBase
{
	/**
	 * Fill in list with the toolbar ids and states you wish to set
	 * 
	 * @param list
	 * @since 4.6.0.5
	 */
	public void refreshToolBarItems(Map<String, Long> list);
}
