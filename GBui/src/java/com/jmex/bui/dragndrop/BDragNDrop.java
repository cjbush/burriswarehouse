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
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.event.BEvent;
import com.jmex.bui.event.EventListener;
import com.jmex.bui.event.MouseEvent;
import com.jmex.bui.icon.BIcon;
import com.jmex.bui.util.Point;

import java.util.HashSet;
import java.util.Set;

/** @author ivicaz */
public final class BDragNDrop implements EventListener {
  private static final BDragNDrop INSTANCE = new BDragNDrop();

  public static BDragNDrop instance() {
    return INSTANCE;
  }

  private Set<DragNDropListener> listeners;
  /**
   * Because Bcomponents that have ActionListener are eating mouse events,
   * we're unable to catch mouse down/up on those components. Therefore,
   * whenever mouse enters the component, we create a potential for drag,
   * which is kept until mouse enters another component (or no component).
   * If the user then presses mouse button, potential drag becomes actual drag
   * and drag & drop process goes on.
   */
  private BDragEvent potentialBDrag;
  private BIcon potentialDragIcon;
  private BDragEvent currentDraggingEventB;
  private BDragIconWindow BDragIconWindow;
  private Point dragIconDisplacement;

  private BDragNDrop() {
    BuiSystem.getRootNode().addGlobalEventListener(this);
    BDragIconWindow = new BDragIconWindow();
    dragIconDisplacement = new Point(0, 0);
  }

  public void addDragNDropListener(DragNDropListener listener) {
    if (listeners == null)
      listeners = new HashSet<DragNDropListener>();
    listeners.add(listener);
  }

  public void removeDragNDropListener(DragNDropListener listener) {
    if (listeners != null)
      listeners.remove(listener);
  }

  private void detachNotifierWindow() {
    if (BuiSystem.getRootNode().getAllWindows().contains(BDragIconWindow))
      BuiSystem.getRootNode().removeWindow(BDragIconWindow);
  }

  public boolean isDragging() {
    return currentDraggingEventB != null;
  }

  private void fireDragInitiated() {
    if (listeners != null)
      for (DragNDropListener listener : listeners)
        listener.dragInitiated(this, currentDraggingEventB);
  }

  private void fireDropped(BDropEvent eventB) {
    if (listeners != null)
      for (DragNDropListener listener : listeners)
        listener.dropped(this, eventB);
  }

  public void setPotentialDrag(BComponent source, Object dragObject, BIcon dragIcon) {
    if(potentialBDrag != null && potentialBDrag.getSource().equals(source)) // Skip constant object creation
      return;
    potentialBDrag = new BDragEvent(source, dragObject);
    potentialDragIcon = dragIcon;
  }

  public void removePotentialDrag(BComponent source) {
    if (potentialBDrag != null && potentialBDrag.getSource().equals(source))
      potentialBDrag = null;
  }

  public Point getDragIconDisplacement() {
    return dragIconDisplacement;
  }

  public void setDragIconDisplacement(int x, int y) {
    dragIconDisplacement.x = x;
    dragIconDisplacement.y = y;
  }

  public void eventDispatched(BEvent event) {
    if (event instanceof MouseEvent) {
      MouseEvent e = (MouseEvent) event;
      if (leftButtonPressed(e) && potentialBDrag != null) {
        startDrag(potentialBDrag);
        updateIconWindowLocation(e);
      } else if (leftButtonReleased(e) && isDragging()) {
        drop(e.getX(), e.getY());
      } else if (isDragging() && mouseIsMovingWithButtonDown(e)) {
        updateIconWindowLocation(e);
      }
    }
  }

  private boolean leftButtonReleased(MouseEvent e) {
    return e.getType() == MouseEvent.MOUSE_RELEASED && e.getButton() == MouseEvent.BUTTON1;
  }

  private boolean leftButtonPressed(MouseEvent e) {
    return e.getType() == MouseEvent.MOUSE_PRESSED && e.getButton() == MouseEvent.BUTTON1;
  }

  private boolean mouseIsMovingWithButtonDown(MouseEvent e) {
    return e.getType() == MouseEvent.MOUSE_DRAGGED;
  }

  private void updateIconWindowLocation(MouseEvent e) {
    BDragIconWindow.setLocation(e.getX() + dragIconDisplacement.x, e.getY() + dragIconDisplacement.y);
  }

  private void startDrag(BDragEvent eventB) {
    currentDraggingEventB = eventB;
    attachNotifierWindowToMouse();
    fireDragInitiated();
  }

  private void attachNotifierWindowToMouse() {
    if (BDragIconWindow != null)
      BDragIconWindow.setIcon(potentialDragIcon);
    BuiSystem.getRootNode().addWindow(BDragIconWindow);
  }

  private void drop(int x, int y) {
    detachNotifierWindow();
    BComponent hitComponent = notifyHitComponent(x, y);

    // Set me to null only if component below is not draggable
    // Determine that by figuring if component set itself as potential drag
    // This is necesarry precaution in case we drop on invalid target, like
    // outside of window
    if (potentialBDrag == null || !potentialBDrag.getSource().equals(hitComponent))
      potentialBDrag = null;
    currentDraggingEventB = null;
  }

  private BComponent notifyHitComponent(int x, int y) {
    Iterable<BWindow> allWindows = BuiSystem.getRootNode().getAllWindows();
    for (BWindow allWindow : allWindows) {
      BComponent hitComponent = allWindow.getHitComponent(x, y);
      if (hitComponent != null) {
        BDropEvent dropEvent = new BDropEvent(hitComponent, currentDraggingEventB);
        hitComponent.dispatchEvent(dropEvent);
        fireDropped(dropEvent);
        return hitComponent;
      }
    }
    BDropEvent dropEvent = new BDropEvent(BDropEvent.NOTHING, currentDraggingEventB);
    fireDropped(dropEvent);
    // Don't crash here because we need ability not to hit anything
    return null;
  }

  public interface DragNDropListener {
    void dragInitiated(BDragNDrop container, BDragEvent eventB);

    void dropped(BDragNDrop container, BDropEvent eventB);
  }
}
