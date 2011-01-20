/*
 * MahjonggGameState.java
 *
 *  Copyright (c) 2007 Daniel Gronau
 *
 *  This file is part of Monkey Mahjongg.
 *
 *  Monkey Mahjongg is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Monkey Mahjongg is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package atechnique.classfactory;

import atechnique.game.state.ATechniqueGameState;
import atechnique.game.state.GameManager;
import com.jme.bounding.BoundingBox;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.CombinerFunctionRGB;
import com.jme.image.Texture.CombinerOperandRGB;
import com.jme.image.Texture.CombinerScale;
import com.jme.image.Texture.CombinerSource;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.controls.binding.MouseButtonBinding;
import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.Spatial.TextureCombineMode;
import com.jme.scene.Text;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.FogState.DensityFunction;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureManager;
import com.jmex.game.state.BasicGameState;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.FaultFractalHeightMap;
import com.jmex.terrain.util.ProceduralTextureGenerator;
import org.lex.game.Demo;
import org.lex.game.StrategicHandlerDefaults;
import org.lex.input.StrategicCameraEffects;
import org.lex.input.StrategicHandler;
import org.lex.input.effects.HeightMap;
import org.lex.input.mouse.MouseBindingManager;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author Pirx
 */
public class InGameState extends BasicGameState implements ATechniqueGameState {
    private Node _statNode;
    private TerrainPage _terrain;
    private LightState _lightState;
    private boolean viewLocked;
    private boolean followingRotation;
    private boolean _runnerMoving = true;
    private Spatial _runner;
    private Vector3f rDirection = new Vector3f();
    private float rSpeed = 50;
    private Quaternion rTurn = new Quaternion();
    private float tSpeed = 10;
    private KeyBindingManager _keyboard;

    private String cmdToggleFilters = "toggleFilters";
    private String cmdNextCursor = "nextCursor";
    private String cmdChangeLocation = "changeLocation";
    private String cmdFollowObject = "followObject";
    private String cmdFollowObjectRotation = "followObjectRotation";
    private String cmdFollowObjectAligned = "followObjectAligned";
    private String cmdToggleRunnerMove = "toggleRunnerMove";
    private String cmdToggleHeightOffset = "toggleHeightOffset";
    private String cmdToggleHandler = "toggleHandler";
    private String cmdFlyToOrigin = "flyToOrigin";
    private String cmdPause = "pause";

    private MouseBindingManager _mouseBinding;

    private String mbLeftClick = "leftClick";

    private StrategicHandler _strategicHandler;
    private StrategicCameraEffects _camEffects;

    protected Camera _cam;

    private Text _infoFilter;
    private Text _infoFollow;
    private Text _infoTimer;
    private StringBuffer sb = new StringBuffer();
    private boolean filtersEnabled;

    private float modifiedHeight = 50;

    private Quad _minimap;
    private float minimapWidth = 200;
    private float minimapHeight = 200;
    private Vector2f mapLocation = new Vector2f();

    private float _handlerTime;
    private DecimalFormat format = new DecimalFormat("0.00000");

    private boolean _isPaused;

//	private NetworkingServerGameState _server = null;
//	private NetworkingClientGameState _client = null;

    public InGameState(Camera camera) {
        super("ATechnique");
        _cam = camera;
        _cam.setFrustumPerspective(60.0f, (float) DisplaySystem.getDisplaySystem().getWidth() / (float) DisplaySystem.getDisplaySystem().getHeight(), 1, 1500);
        _cam.setParallelProjection(false);
        _cam.update();

        /**
         * Create a ZBuffer to display pixels closest to the camera above
         * farther ones.
         */
        ZBufferState buf = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
        buf.setEnabled(true);
        buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        rootNode.setRenderState(buf);

        /** Set up a basic, default light. */
        PointLight light = new PointLight();
        light.setDiffuse(new ColorRGBA(0.75f, 0.75f, 0.75f, 0.75f));
        light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        light.setLocation(new Vector3f(100, 100, 100));
        light.setEnabled(true);

        /** Attach the light to a lightState and the lightState to rootNode. */
        _lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
        _lightState.setEnabled(true);
        _lightState.attach(light);
        rootNode.setRenderState(_lightState);

        setupStats();

        GameTaskQueueManager.getManager().getQueue(
                GameTaskQueue.UPDATE).enqueue(new Callable<Object>() {
            public Object call() throws Exception {
                setupTerrain();
                initSkybox();
                setupFollowObject();
                setupControls();
                setupHandler();
                setupMisc();
                rootNode.updateRenderState();

                return null;
            }
        });
    }

    private Skybox getSkybox(String... textures) {
        Skybox sb = new Skybox("skybox", 500, 500, 500);
        sb.setTexture(Skybox.Face.North, TextureManager.loadTexture(
                InGameState.class.getClassLoader().getResource(textures[0]),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear,
                Image.Format.GuessNoCompression, 1, true));
        sb.setTexture(Skybox.Face.West, TextureManager.loadTexture(
                InGameState.class.getClassLoader().getResource(textures[1]),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear,
                Image.Format.GuessNoCompression, 1, true));
        sb.setTexture(Skybox.Face.South, TextureManager.loadTexture(
                InGameState.class.getClassLoader().getResource(textures[2]),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear,
                Image.Format.GuessNoCompression, 1, true));
        sb.setTexture(Skybox.Face.East, TextureManager.loadTexture(
                InGameState.class.getClassLoader().getResource(textures[3]),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear,
                Image.Format.GuessNoCompression, 1, true));
        sb.setTexture(Skybox.Face.Up, TextureManager.loadTexture(
                InGameState.class.getClassLoader().getResource(textures[4]),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear,
                Image.Format.GuessNoCompression, 1, true));
        sb.setTexture(Skybox.Face.Down, TextureManager.loadTexture(
                InGameState.class.getClassLoader().getResource(textures[5]),
                Texture.MinificationFilter.BilinearNearestMipMap,
                Texture.MagnificationFilter.Bilinear,
                Image.Format.GuessNoCompression, 1, true));

        return sb;
    }

    private void initSkybox() {
        Skybox skybox = getSkybox("atechnique/images/sky/dg_north.png",
                                  "atechnique/images/sky/dg_south.png",
                                  "atechnique/images/sky/dg_east.png",
                                  "atechnique/images/sky/dg_west.png",
                                  "atechnique/images/sky/dg_up.png",
                                  "atechnique/images/sky/dg_down.png");
        rootNode.attachChild(skybox);
    }

    private void setupStats() {
        _statNode = new Node("Stats node");
        _statNode.setCullHint(Spatial.CullHint.Never);
        _statNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);
        rootNode.attachChild(_statNode);

        _statNode.updateGeometricState(0.0f, true);
        _statNode.updateRenderState();
    }

    public void setupTerrain() {
        final Node terrainNode = new Node();
        rootNode.attachChild(terrainNode);

        // this piece is pretty much copied from the jme terrain test
        FaultFractalHeightMap heightMap = new FaultFractalHeightMap(257, 32, 0, 255, 0.75f);
        Vector3f terrainScale = new Vector3f(10, 1, 10);
        heightMap.setHeightScale(0.001f);
        _terrain = new TerrainPage("Terrain", 33,
                                   heightMap.getSize(), terrainScale,
                                   heightMap.getHeightMap());

        _terrain.setDetailTexture(1, 16);
        terrainNode.attachChild(_terrain);

        ProceduralTextureGenerator pt = new ProceduralTextureGenerator(heightMap);
        pt.addTexture(new ImageIcon(Demo.class.getClassLoader().getResource(
                "jmetest/data/texture/grassb.png")), -128, 0, 128);
        pt.addTexture(new ImageIcon(Demo.class.getClassLoader().getResource(
                "jmetest/data/texture/dirt.jpg")), 0, 128, 255);
        pt.addTexture(new ImageIcon(Demo.class.getClassLoader().getResource(
                "jmetest/data/texture/highest.jpg")), 128, 255, 384);
        pt.createTexture(512);

        TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        ts.setEnabled(true);
        Texture t1 = TextureManager.loadTexture(
                pt.getImageIcon().getImage(),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear,
                true);
        ts.setTexture(t1, 0);

        Texture t2 = TextureManager.loadTexture(
                Demo.class.getClassLoader().getResource(
                        "jmetest/data/texture/Detail.jpg"),
                Texture.MinificationFilter.Trilinear,
                Texture.MagnificationFilter.Bilinear);

        ts.setTexture(t2, 1);
        t2.setWrap(Texture.WrapMode.Repeat);

        t1.setApply(ApplyMode.Combine);
        t1.setCombineFuncRGB(CombinerFunctionRGB.Modulate);
        t1.setCombineSrc0RGB(CombinerSource.CurrentTexture);
        t1.setCombineOp0RGB(CombinerOperandRGB.SourceColor);
        t1.setCombineSrc1RGB(CombinerSource.PrimaryColor);
        t1.setCombineOp1RGB(CombinerOperandRGB.SourceColor);
        t1.setCombineScaleRGB(CombinerScale.One);

        t2.setApply(ApplyMode.Combine);
        t2.setCombineFuncRGB(CombinerFunctionRGB.AddSigned);
        t2.setCombineSrc0RGB(CombinerSource.CurrentTexture);
        t2.setCombineOp0RGB(CombinerOperandRGB.SourceColor);
        t2.setCombineSrc1RGB(CombinerSource.Previous);
        t2.setCombineOp1RGB(CombinerOperandRGB.SourceColor);
        t2.setCombineScaleRGB(CombinerScale.One);
        terrainNode.setRenderState(ts);

        FogState fs = DisplaySystem.getDisplaySystem().getRenderer().createFogState();
        fs.setDensity(0.0015f);
        fs.setEnabled(true);
        fs.setColor(new ColorRGBA(0.5f, 0.55f, 0.5f, 0.5f));
        fs.setDensityFunction(DensityFunction.Exponential);
        fs.setQuality(FogState.Quality.PerVertex);
        terrainNode.setRenderState(fs);

        terrainNode.lock();
        terrainNode.lockBranch();

        CullState cs = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
        cs.setCullFace(CullState.Face.Back);
        cs.setEnabled(true);
        terrainNode.setRenderState(cs);

        _lightState.detachAll();

        DirectionalLight dl = new DirectionalLight();
        dl.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
        dl.setDirection(new Vector3f(1, -0.5f, 1));
        dl.setEnabled(true);
        _lightState.attach(dl);

        DirectionalLight dr = new DirectionalLight();
        dr.setEnabled(true);
        dr.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
        dr.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        dr.setDirection(new Vector3f(0.5f, -0.5f, 0).normalizeLocal());
        _lightState.attach(dr);
    }

    public void setupFollowObject() {
        _runner = new Box("Runner", new Vector3f(), 10, 10, 20);
        _runner.setModelBound(new BoundingBox());
        _runner.updateModelBound();

        _runner.addController(new Controller() {
            private static final long serialVersionUID = 1L;
            private Vector3f runnerDirection = Vector3f.UNIT_Z.negate();

            public void update(float time) {
                if (!_runnerMoving) {
                    return;
                }

                _runner.getLocalRotation().mult(runnerDirection, rDirection);
                _runner.getLocalTranslation().addLocal(
                        rDirection.multLocal(rSpeed * time));

                rTurn.fromAngleNormalAxis(tSpeed * time * FastMath.DEG_TO_RAD, Vector3f.UNIT_Y);
                _runner.getLocalRotation().multLocal(rTurn);

                Vector3f pos = _runner.getLocalTranslation();
                float height = _terrain.getHeight(pos);
                _runner.setLocalTranslation(pos.x, height, pos.z);
            }
        });

        rootNode.attachChild(_runner);
    }

    public void setupControls() {
        _keyboard = KeyBindingManager.getKeyBindingManager();

        _keyboard.add(cmdToggleFilters, KeyInput.KEY_F);
        _keyboard.add(cmdNextCursor, KeyInput.KEY_C);
        _keyboard.add(cmdChangeLocation, KeyInput.KEY_L);
        _keyboard.add(cmdFollowObject, KeyInput.KEY_Z);
        _keyboard.add(cmdFollowObjectRotation, KeyInput.KEY_X);
        _keyboard.add(cmdFollowObjectAligned, KeyInput.KEY_V);
        _keyboard.add(cmdToggleRunnerMove, KeyInput.KEY_SPACE);
        _keyboard.add(cmdToggleHeightOffset, KeyInput.KEY_H);
        _keyboard.add(cmdToggleHandler, KeyInput.KEY_TAB);
        _keyboard.add(cmdFlyToOrigin, KeyInput.KEY_NUMPAD0);

        // Pause the Application with ESC
        _keyboard.add(cmdPause, KeyInput.KEY_ESCAPE);

        _mouseBinding = MouseBindingManager.get();
        _mouseBinding.addCommand(mbLeftClick, MouseButtonBinding.LEFT_BUTTON);
    }

    //	@SuppressWarnings("unchecked")
    public void setupHandler() {
        HeightMap heightMap = new HeightMap() {
            public float getHeightAt(Vector3f location) {
                float height = _terrain.getHeight(location);
                if (Float.isNaN(height)) {
                    height = 0;
                }
                return height;
            }
        };

        _strategicHandler = new StrategicHandler(_cam, ClassFactory.getMouseManager(), heightMap);
        _camEffects = new StrategicCameraEffects(_strategicHandler);
        _strategicHandler.setCameraEffects(_camEffects);
        ClassFactory.getInputHandler().addToAttachedHandlers(_strategicHandler);

        _strategicHandler.updateProperties(StrategicHandlerDefaults.getCustomConfig());

        Map<String, String> cursors = new HashMap<String, String>();
        cursors.put(StrategicHandler.CURSOR_ROTATE, "atechnique/cursors/goldenarrow_rotate/default.cursor");
        cursors.put(StrategicHandler.CURSOR_SCROLL_UP, "atechnique/cursors/goldenscroll_up/default.cursor");
        cursors.put(StrategicHandler.CURSOR_SCROLL_UPLEFT, "atechnique/cursors/goldenscroll_upleft/default.cursor");
        _strategicHandler.updateCursors(cursors);

//		strategicHandler.getScrollingPlane().setScrollLimits(minX, maxX, minY, maxY);
    }

    public void setupMisc() {
        Text info = Text.createDefaultTextLabel("infoKeys1");
        info.setTextureCombineMode(TextureCombineMode.Replace);
        info.setLocalTranslation(0, DisplaySystem.getDisplaySystem().getHeight() - 40, 0);
        info.print("Press F to enable/disable input filters and " + "C to switch cursors.");
        _statNode.attachChild(info);

        _infoFilter = Text.createDefaultTextLabel("InfoFilter");
        _infoFilter.setTextureCombineMode(TextureCombineMode.Replace);
        _infoFilter.setLocalTranslation(0, DisplaySystem.getDisplaySystem().getHeight() - 55, 0);
        filtersEnabled = true;
        _infoFilter.print("Input Filters ENABLED");
        _statNode.attachChild(_infoFilter);

        info = Text.createDefaultTextLabel("infoKeys2");
        info.setTextureCombineMode(TextureCombineMode.Replace);
        info.setLocalTranslation(0, DisplaySystem.getDisplaySystem().getHeight() - 85, 0);
        info.print("Z - followObject, X - followRotation, V - lockView and " +
                   " SPACEBAR - move/stop.");
        _statNode.attachChild(info);

        _infoFollow = Text.createDefaultTextLabel("infoFollow");
        _infoFollow.setTextureCombineMode(TextureCombineMode.Replace);
        _infoFollow.setLocalTranslation(0, DisplaySystem.getDisplaySystem().getHeight() - 100, 0);
        _statNode.attachChild(_infoFollow);

        _infoTimer = Text.createDefaultTextLabel("infoTimer");
        _infoTimer.setTextureCombineMode(TextureCombineMode.Replace);
        _infoTimer.setLocalTranslation(0, DisplaySystem.getDisplaySystem().getHeight() - 115, 0);
        _statNode.attachChild(_infoTimer);

        _minimap = new Quad("myQuad", minimapWidth, minimapHeight);
        _minimap.setLocalTranslation(minimapWidth / 2 + 5, minimapHeight / 2 + 20, 0);
        _minimap.setRenderQueueMode(Renderer.QUEUE_ORTHO);
        _minimap.setCullHint(CullHint.Never);
        _minimap.setLightCombineMode(LightCombineMode.Off);
        _minimap.setTextureCombineMode(TextureCombineMode.Off);
        _minimap.setDefaultColor(new ColorRGBA(ColorRGBA.white));
        _minimap.getDefaultColor().a = 0.5f;
        BlendState as = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        as.setBlendEnabled(true);
        as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        as.setTestEnabled(true);
        as.setTestFunction(BlendState.TestFunction.GreaterThan);
        as.setEnabled(true);
        _minimap.setRenderState(as);
        _statNode.attachChild(_minimap);

        info = Text.createDefaultTextLabel("infoKeys3");
        info.setTextureCombineMode(TextureCombineMode.Replace);
        info.setLocalTranslation(minimapWidth + 10, 60, 0);
        info.print("Left-click on the minimap to fly to location,");
        _statNode.attachChild(info);

        info = Text.createDefaultTextLabel("infoKeys4");
        info.setTextureCombineMode(TextureCombineMode.Replace);
        info.setLocalTranslation(minimapWidth + 10, 45, 0);
        info.print("TAB to toggle fps contoller on/off,");
        _statNode.attachChild(info);

        info = Text.createDefaultTextLabel("infoKeys5");
        info.setTextureCombineMode(TextureCombineMode.Replace);
        info.setLocalTranslation(minimapWidth + 10, 30, 0);
        info.print("H - height offset and L or NUMPAD_0 - go to origin.");
        _statNode.attachChild(info);
    }

    @Override
    public void update(float tpf) {
        if (!_isPaused) {
            super.update(tpf);

            // TODO: can do picking here and change cursor depending on what the mouse is hovering over (pick result)

            updateInput();

            if (_strategicHandler.isEnabled()) {
                /*
                     * Remember to update your cursor every frame. You will want to
                     * do this, because after you do mouse picking you can choose
                     * different cursors based on the result of the mouse pick.
                     *
                     * Example: you can set a different cursor when the mouse is
                     * hovering over an enemy vs the default cursor.
                    */
                String cursorToDisplay = ClassFactory.getCursor(ClassFactory.currentCursorIndex);
                _strategicHandler.useCursor(cursorToDisplay);
            }

            ClassFactory.getInputHandler().update(tpf);

            sb.delete(0, sb.length());
            sb.append("FollowObject = ");
            sb.append(_strategicHandler.getFollowObject() != null ? "ON" : "OFF");
            sb.append(". FollowRotaion = ");
            sb.append(followingRotation ? "ON" : "OFF");
            sb.append(". LockView = ");
            sb.append(viewLocked ? "ON" : "OFF");
            sb.append(".");
            _infoFollow.print(sb);

            sb.delete(0, sb.length());
            sb.append("Handler update took = ");
            sb.append(format.format(_handlerTime));
            sb.append("seconds.");
            _infoTimer.print(sb);
        }
    }

    protected void updateInput() {
        long start = System.nanoTime();
        _handlerTime = (System.nanoTime() - start) * 10e-9f;

        if (_keyboard.isValidCommand("pause", true)) {
            GameManager.getInstance().pushState(ClassFactory.getInGamePausePresenter());
        }

        if (_keyboard.isValidCommand(cmdToggleFilters, false)) {
            filtersEnabled = !filtersEnabled;

            if (filtersEnabled) {
                _strategicHandler.updateProperties(
                        StrategicHandlerDefaults.getFilterConfig());
                _infoFilter.print("Input Filters ENABLED");
            } else {
                _strategicHandler.updateProperties(
                        StrategicHandlerDefaults.getNoFilterConfig());
                _infoFilter.print("Input Filters DISABLED");
            }
        }

        if (_keyboard.isValidCommand(cmdNextCursor, false)) {
            ClassFactory.currentCursorIndex++;
            if (ClassFactory.currentCursorIndex >= ClassFactory.cursorCount()) {
                ClassFactory.currentCursorIndex = 0;
            }
        }

        if (_keyboard.isValidCommand(cmdChangeLocation, false)) {
            _strategicHandler.setWorldTranslation(0, 0, 0);
        }
        if (_keyboard.isValidCommand(cmdToggleHeightOffset, false)) {
            float height = _strategicHandler.getScrollingPlane().getHeight();
            if (height == modifiedHeight) {
                height = 0;
            } else {
                height = modifiedHeight;
            }

            _strategicHandler.getScrollingPlane().setHeight(height);
        }
        if (_keyboard.isValidCommand(cmdToggleRunnerMove, false)) {
            _runnerMoving = !_runnerMoving;
        }

        boolean followObject = (_strategicHandler.getFollowObject() != null);
        if (_keyboard.isValidCommand(cmdFollowObject, false)) {
            if (followingRotation || viewLocked) {
                followObject = true;
            } else {
                followObject = !followObject;
            }

            followingRotation = false;
            viewLocked = false;

            _strategicHandler.setHorizontalRotationEnabled(!viewLocked);
            _strategicHandler.setFollowObject(followObject ? _runner : null,
                                              followingRotation, viewLocked, 0);
        } else if (_keyboard.isValidCommand(cmdFollowObjectRotation, false)) {
            if (!followingRotation || viewLocked) {
                followObject = true;
            } else {
                followObject = !followObject;
            }

            if (followObject) {
                followingRotation = true;
                viewLocked = false;
            } else {
                followingRotation = false;
                viewLocked = false;
            }

            _strategicHandler.setHorizontalRotationEnabled(!viewLocked);
            _strategicHandler.setFollowObject(followObject ? _runner : null,
                                              followingRotation, viewLocked, 0);
        } else if (_keyboard.isValidCommand(cmdFollowObjectAligned, false)) {
            if (!viewLocked) {
                followObject = true;
            } else {
                followObject = !followObject;
            }

            if (followObject) {
                followingRotation = true;
                viewLocked = true;
            } else {
                followingRotation = false;
                viewLocked = false;
            }

            _strategicHandler.setFollowObject(followObject ? _runner : null,
                                              followingRotation, viewLocked, 0);
            _strategicHandler.setHorizontalRotationEnabled(!viewLocked);
        }

        if (_keyboard.isValidCommand(cmdFlyToOrigin, false)) {
            if (!_strategicHandler.isEnabled()) {
                _strategicHandler.setEnabled(true);
                _strategicHandler.getMouseManager().getMouse().enable();
            }
            _camEffects.flyToLocation(Vector3f.ZERO);
        }

        if (_mouseBinding.isValidCommand(mbLeftClick, false)) {
            Vector2f mousePosition =
                    _strategicHandler.getMouseManager().getMousePosition();

            mapLocation.x = (mousePosition.x - _minimap.getLocalTranslation().x
                             + minimapWidth / 2) / minimapWidth;
            mapLocation.y = (mousePosition.y - _minimap.getLocalTranslation().y
                             + minimapHeight / 2) / minimapHeight;

            if (mapLocation.x >= 0 && mapLocation.x <= 1
                && mapLocation.y >= 0 && mapLocation.y <= 1) {
                mapLocation.x = mapLocation.x * 2500 - 1250;
                mapLocation.y = mapLocation.y * 2500 - 1250;

                if (!_strategicHandler.isEnabled()) {
                    _strategicHandler.setEnabled(true);
                    _strategicHandler.getMouseManager().getMouse().enable();
                }
                _camEffects.flyToLocation(mapLocation);
            }
        }
    }

    @Override
    public void render(float tpf) {
        super.render(tpf);

        DisplaySystem.getDisplaySystem().getRenderer().draw(_statNode);
    }

    @Override
    public void enter() {
        super.setActive(true);
    }

    @Override
    public void exit() {
        super.setActive(false);
    }

    @Override
    public void pause() {
        // Stop any animated movement
        _isPaused = true;

        // Don't respond to any mouse input
        _strategicHandler.setEnabled(false);
    }

    @Override
    public void resume() {
        // Resume any animated movement
        _isPaused = false;
        _strategicHandler.setEnabled(true);
    }
}