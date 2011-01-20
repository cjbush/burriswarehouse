//
// $Id: BBasicMessage.java,v 1.3 2007/05/02 21:34:01 vivaldi Exp $
//
// BUI - a user interface library for the JME 3D engine
// Copyright (C) 2005, Michael Bayne, All Rights Reserved
//
// This library is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as published
// by the Free Software Foundation; either version 2.1 of the License, or
// (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
package com.jmex.bui.dragndrop;

import com.jmex.bui.BComponent;
import com.jmex.bui.event.BEvent;
import com.jmex.bui.event.EventListener;
import com.jmex.bui.event.MouseEvent;
import com.jmex.bui.icon.BIcon;
import com.jmex.bui.icon.BlankIcon;

/** @author ivicaz */
public class BDragListener implements EventListener {
	private Object dragObject;
	private IconRequest iconRequest;
	private BComponent source;

	public BDragListener(BComponent source, Object dragObject) {
		this(source, dragObject, new BlankIcon(14, 14));
	}

	public BDragListener(BComponent source, Object dragObject, BIcon iconRequest) {
		this(source, dragObject, new SimpleIconRequest(iconRequest));
	}

	public BDragListener(BComponent source, Object dragObject, IconRequest iconRequest) {
		this.source = source;
		this.dragObject = dragObject;
		this.iconRequest = iconRequest;
	}

	public void eventDispatched(BEvent event) {
		if (event instanceof MouseEvent) {
			MouseEvent e = (MouseEvent) event;
			if (e.getType() == MouseEvent.MOUSE_ENTERED) {
				BDragNDrop dnd = BDragNDrop.instance();
				if (iconRequest.getIcon() != null) {
					dnd.setDragIconDisplacement(-iconRequest.getIcon().getWidth() / 2, -iconRequest.getIcon().getHeight() / 2);
					dnd.setPotentialDrag(source, dragObject, iconRequest.getIcon());
				}
			} else if (e.getType() == MouseEvent.MOUSE_MOVED) {
				BDragNDrop dnd = BDragNDrop.instance();
				if (iconRequest.getIcon() != null) {
					dnd.setDragIconDisplacement(-iconRequest.getIcon().getWidth() / 2, -iconRequest.getIcon().getHeight() / 2);
					dnd.setPotentialDrag(source, dragObject, iconRequest.getIcon());
				}
			} else if (e.getType() == MouseEvent.MOUSE_EXITED) {
				BDragNDrop.instance().removePotentialDrag(source);
			}
		}
	}

	public static interface IconRequest {
		BIcon getIcon();
	}

	private static class SimpleIconRequest implements IconRequest {
		private BIcon icon;

		private SimpleIconRequest(BIcon icon) {
			this.icon = icon;
		}

		public BIcon getIcon() {
			return icon;
		}
	}
}
