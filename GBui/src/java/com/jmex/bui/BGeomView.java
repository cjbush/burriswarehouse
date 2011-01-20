//
// $Id: BGeomView.java,v 1.2 2007/04/27 19:46:29 vivaldi Exp $
//
// BUI - a user interface library for the JME 3D engine
// Copyright (C) 2005-2006, Michael Bayne, All Rights Reserved
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

package com.jmex.bui;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.Renderer;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;
import com.jmex.bui.util.Insets;
import com.jmex.bui.util.Rectangle;
import org.lwjgl.opengl.GL11;

/**
 * Displays 3D geometry (a {@link Spatial}) inside a normal user interface.
 *
 * @author Michael Bayne
 * @author StandTrooper
 */
public class BGeomView extends BComponent {
    /**
     * Creates a view with no configured geometry. Geometry can be set later with {@link #setGeometry}.
     */
    public BGeomView() {
        this(null);
    }

    /**
     * Creates a view with the specified {@link Spatial} to be rendered.
     *
     * @param geom Spatial
     */
    public BGeomView(final Spatial geom) {
        _geom = geom;
    }

    /**
     * Returns the camera used when rendering our geometry.
     *
     * @return _camera Camera
     */
    public Camera getCamera() {
        if (_camera == null) {
            _camera = createCamera(DisplaySystem.getDisplaySystem());
        }

        return _camera;
    }

    /**
     * Configures the spatial to be rendered by this view.
     *
     * @param geom Spatial
     */
    public void setGeometry(final Spatial geom) {
        _geom = geom;
    }

    /**
     * Returns the geometry rendered by this view.
     *
     * @return _geom Spatial
     */
    public Spatial getGeometry() {
        return _geom;
    }

    /**
     * Called every frame (while we're added to the view hierarchy) by the {@link BRootNode}.
     *
     * @param frameTime float
     */
    public void update(final float frameTime) {
        if (_geom != null) {
            _geom.updateGeometricState(frameTime, true);
        }
    }

    /**
     * Documentation Inherited
     *
     * @see BComponent#wasAdded
     */
    @Override
    protected void wasAdded() {
        super.wasAdded();
        _root = getWindow().getRootNode();
        _root.registerGeomView(this);
    }

    /**
     * Documentation Inherited
     *
     * @see BComponent#wasRemoved
     */
    @Override
    protected void wasRemoved() {
        super.wasRemoved();
        _root.unregisterGeomView(this);
        _root = null;
    }

    /**
     * Documentation Inherited
     *
     * @param renderer Renderer
     * @see BComponent#renderComponent
     */
    @Override
    protected void renderComponent(final Renderer renderer) {
        super.renderComponent(renderer);
        if (_geom == null) {
            return;
        }

        applyDefaultStates();
        Camera cam = renderer.getCamera();

        int x = getAbsoluteX();
        int y = getAbsoluteY();

        boolean useOrtho = (_geom.getRenderQueueMode() == Renderer.QUEUE_ORTHO);

        try {
            if (!useOrtho) {
                renderer.unsetOrtho();

                // create or resize our camera if necessary
                DisplaySystem display = DisplaySystem.getDisplaySystem();
                int swidth = display.getWidth(),
                        sheight = display.getHeight();
                boolean updateDisplay = false;
                if (_camera == null || _swidth != swidth || _sheight != sheight) {
                    _swidth = swidth;
                    _sheight = sheight;
                    if (_camera == null) {
                        _camera = createCamera(display);
                    } else {
                        _camera.resize(_swidth, _sheight);
                    }
                    updateDisplay = true;
                }

                // set up our camera viewport if it has changed
                Insets insets = getInsets();

                int ax = x + insets.left;
                int ay = y + insets.bottom;
                int width = _width - insets.getHorizontal();
                int height = _height - insets.getVertical();

                if (updateDisplay || _cx != ax || _cy != ay ||
                    _cwidth != width || _cheight != height) {
                    _cx = ax;
                    _cy = ay;
                    _cwidth = width;
                    _cheight = height;
                    float left = _cx / _swidth, right = left + _cwidth / _swidth;
                    float bottom = _cy / _sheight;
                    float top = bottom + _cheight / _sheight;
                    _camera.setViewPort(left, right, bottom, top);
                    _camera.setFrustumPerspective(
                            45.0f, _width / (float) _height, 1, 1000);
                }

                // clear the z buffer over the area to which we will be drawing
                boolean scissored = intersectScissorBox(_srect, ax, ay, width, height);
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                restoreScissorState(scissored, _srect);

                // now set up the custom camera and render our geometry
                renderer.setCamera(_camera);
                _camera.apply();
                _camera.update();
            }

            // actually render the geometry
            renderer.draw(_geom);
        } finally {
            if (!useOrtho) {
                // restore the camera
                renderer.setCamera(cam);
                cam.apply();
                cam.update();
                renderer.setOrtho();

                // we need to restore the GL translation as that got wiped out when we left and
                // re-entered ortho mode
                GL11.glTranslatef(x, y, 0);
            }
        }
    }

    /**
     * Called to create and configure the camera that we'll use when rendering our geometry.
     *
     * @param ds DisplaySystem
     * @return camera Camera
     */
    protected Camera createCamera(final DisplaySystem ds) {
        // create a standard camera and frustum
        final Camera camera = ds.getRenderer().createCamera(_swidth, _sheight);
        camera.setParallelProjection(false);

        // put and point it somewhere sensible by default
        final Vector3f loc = new Vector3f(0.0f, 0.0f, 25.0f);
        final Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
        final Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
        final Vector3f dir = new Vector3f(0.0f, 0f, -1.0f);

        camera.setFrame(loc, left, up, dir);

        return camera;
    }

    protected BRootNode _root;
    protected Camera _camera;
    protected Spatial _geom;
    protected int _swidth, _sheight;
    protected float _cx, _cy, _cwidth, _cheight;

    protected Rectangle _srect = new Rectangle();
}
