package com.jmex.bui;

import com.jmex.bui.dragndrop.*;
import com.jmex.bui.enumeratedConstants.TitleOptions;
import com.jmex.bui.headlessWindows.BTitledWindow;
import com.jmex.bui.icon.BIcon;

/**
 * @author ivicaz
 */
public class DraggingPlayground extends AbstractBuiTest {
    protected void addGui() {
        setupWindow1(50, 200);
        setupWindow2(450, 200);
        initializeDnd();
    }

    private void initializeDnd() {
        BDragNDrop dnd = BDragNDrop.instance();
        dnd.addDragNDropListener(new BDragNDrop.DragNDropListener() {
            public void dragInitiated(BDragNDrop container, BDragEvent event) {
            	if (((BButton)event.getSource()).getIcon() == null) {
            		// allow me to cancel the drag, since it doesn't make sense.
            	}
                System.out.println("Drag initiated: " + event);
            }

            public void dropped(BDragNDrop container, BDropEvent event) {
                System.out.println("Dropped: " + event);
            }
        });
    }

    private void setupWindow1(int x, int y) {
        BTitledWindow win = new BTitledWindow("le window 1",
                                              new BTitleBar("lala", "Lala", TitleOptions.MIN_MAX_CLOSE),
                                              new BStatusBar("ohlala"),
                                              BuiSystem.getStyle());
        win.setLocation(x, y);
        win.setSize(320, 240);

        BButton button = new BButton(getImageIcon("rsrc/test/gold.jpg"), "");
        button.addListener(new BDragListener(button, new GetIcon(button)/**, getImageIcon("rsrc/test/gold.jpg")*/));
        button.addListener(new SwitchIconDropTarget());
        win.getComponentArea().add(button);

        BuiSystem.addWindow(win);
    }

    private void setupWindow2(int x, int y) {
        BTitledWindow win = new BTitledWindow("le window 2",
                                              new BTitleBar("lala", "Lala 2", TitleOptions.MIN_MAX_CLOSE),
                                              new BStatusBar("ohlala 2"),
                                              BuiSystem.getStyle());
        win.setLocation(x, y);
        win.setSize(320, 240);

        BButton button = new BButton("Empty");
        button.addListener(new BDragListener(button, new GetIcon(button)));
        button.addListener(new SwitchIconDropTarget());
        win.getComponentArea().add(button);

        BuiSystem.addWindow(win);
    }

    private class SwitchIconDropTarget extends BDropListener {
        protected void drop(BDropEvent dropEvent) {
            Object dropEventSource = dropEvent.getSource();

            if (dropEventSource instanceof BButton) {
                BButton dropButton = (BButton) dropEventSource;
                BDragEvent dragEvent = dropEvent.getDragEvent();
                GetIcon getIcon = (GetIcon) dragEvent.getDraggedObject();
                dropButton.setText("");
                dropButton.setIcon(getIcon.call());

                BButton dragButton = (BButton) dragEvent.getSource();
                dragButton.setText("Empty");
                dragButton.setIcon(null);
            }
        }
    }
    
    private class GetIcon {
        private BButton button;

        private GetIcon(BButton button) {
            this.button = button;
        }

        public BIcon call() {
            return button.getIcon();
        }
    }

    public static void main(String[] args) {
        new DraggingPlayground().start();
    }
}
