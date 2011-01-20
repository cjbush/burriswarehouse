package atechnique.views.concrete.gbui;

import atechnique.views.interfaces.IStartServerListener;
import atechnique.views.interfaces.IStartServerView;
import com.jmex.bui.BButton;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.AbsoluteLayout;
import com.jmex.bui.util.Point;

import java.util.ArrayList;
import java.util.List;

public class StartServerView extends GbuiGameState implements IStartServerView {
//	private JLabel _lblNickname;
//	private JTextField _txtNickname;
//	private JLabel _lblServerPort;
//	private JTextField _txtServerPort;
//	private JLabel _lblAllegiance;
    //	private JComboBox _cboAllegiances;
    private BButton _btnCancel;
    private BButton _btnOk;

    private IStartServerListener _startServerListener;

    public StartServerView() {
        super("StartServerView");

        _window = new BWindow(BuiSystem.getStyle(), new AbsoluteLayout());
        _window.setSize(400, 300);

        // Nickname label
//		_lblNickname = new JLabel();
//		GridBagConstraints c = new GridBagConstraints();
//		c.gridx = 0;
//		c.gridy = 0;
//		contentPane.add(_lblNickname);

        // Nickname textbox
//		_txtNickname = new JTextField();
//		_txtNickname.setPreferredSize(new Dimension(128, 16));
//		_txtNickname.setMinimumSize(new Dimension(256, 64));
//		_txtNickname.addKeyListener(new KeyListener() {
//			@Override
//			public void keyPressed(KeyEvent arg0) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void keyReleased(KeyEvent arg0) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void keyTyped(KeyEvent arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//		c = new GridBagConstraints();
//		c.gridx = 1;
//		c.gridy = 0;
//		contentPane.add(_txtNickname);

        // Server Port label
//		_lblServerPort = new JLabel();
//		c = new GridBagConstraints();
//		c.gridx = 0;
//		c.gridy = 1;
//		contentPane.add(_lblServerPort);

        // Server Port textbox
//		_txtServerPort = new JTextField();
//		_txtServerPort.setPreferredSize(new Dimension(256, 64));
//		c = new GridBagConstraints();
//		c.gridx = 1;
//		c.gridy = 1;
//		contentPane.add(_txtServerPort);

        // Allegiance label
//		_lblAllegiance = new JLabel();
//		c = new GridBagConstraints();
//		c.gridx = 0;
//		c.gridy = 2;
//		contentPane.add(_lblAllegiance);

        // Allegiances combo-box
//		_cboAllegiances = new JComboBox();
//		c = new GridBagConstraints();
//		c.gridx = 1;
//		c.gridy = 2;
//		contentPane.add(_cboAllegiances);

        int buttonWidth = _window.getWidth() / 4;
        int buttonHeight = 32;

        // Cancel button
        _btnCancel = new BButton("");
        _btnCancel.setPreferredSize(buttonWidth, buttonHeight);
        _btnCancel.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (_startServerListener != null) {
                    _startServerListener.cancelPressed();
                }
            }
        });
        _window.add(_btnCancel, new Point(10, 20));

        // OK button
        _btnOk = new BButton("");
        _btnOk.setPreferredSize(buttonWidth, buttonHeight);
        _btnOk.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (_startServerListener != null) {
                    _startServerListener.okPressed();
                }
            }
        });
        _window.add(_btnOk, new Point(_window.getWidth() - 10 - buttonWidth, 20));

        _window.center();
    }

    public List<String> getTranslationTags() {
        ArrayList<String> translationTags = new ArrayList<String>();
        translationTags.add("StartServer.Nickname"); // Nickname:
        translationTags.add("StartServer.ServerPort");    // Server Port:
        translationTags.add("StartServer.Allegiances");    // Allegiance:
        translationTags.add("StartServer.Cancel"); // Cancel
        translationTags.add("StartServer.CancelDescription"); // Back to Select Campaign
        translationTags.add("StartServer.Ok"); // OK
        translationTags.add("StartServer.OkDescription"); // Start selected scenario

        return translationTags;
    }

    public void setTranslationPhrases(List<String> translationPhrases) {
//		_lblNickname.setText(translationPhrases.get(0));
//		_lblServerPort.setText(translationPhrases.get(1));
//		_lblAllegiance.setText(translationPhrases.get(2));
        _btnCancel.setText(translationPhrases.get(3));
        _btnCancel.setTooltipText(translationPhrases.get(4));
        _btnOk.setText(translationPhrases.get(5));
        _btnOk.setTooltipText(translationPhrases.get(6));

//		centerOnDesktop();
    }

    public void setAllegiances(List<String> allegiances) {
//		ComboBoxModel listModel = new ComboBoxModel();
//		for (String allegiance : allegiances) {
//			_cboAllegiances.addItem(allegiance);
//		}
//		_cboAllegiances.setModel(listModel);
//		_cboAllegiances.
//		// Select first in list
//		_cboAllegiances.setSelectedIndex(0);

//		centerOnDesktop();
    }

    public void setNickname(String nickname) {
//		_txtNickname.setText(nickname);

//		centerOnDesktop();
    }

    public void setPort(int port) {
//		_txtServerPort.setText(new Integer(port).toString());

//		centerOnDesktop();
    }

    public void addStartServerListener(IStartServerListener startServerListener) {
        _startServerListener = startServerListener;
    }
}
