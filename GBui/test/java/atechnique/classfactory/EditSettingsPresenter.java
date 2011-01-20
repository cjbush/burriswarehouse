package atechnique.classfactory;

import atechnique.Main;
import atechnique.game.state.GameManager;
import atechnique.models.interfaces.IEditSettingsModel;
import atechnique.views.interfaces.IEditSettingsListener;
import atechnique.views.interfaces.IEditSettingsView;
import com.jme.system.DisplaySystem;

public class EditSettingsPresenter extends GameStatePresenter implements IEditSettingsListener {
    private IEditSettingsView _view;
    private IEditSettingsModel _model;

    public EditSettingsPresenter(IEditSettingsView view, IEditSettingsModel model) {
        super(view);

        _view = view;
        _model = model;

        _view.addEditSettingsListener(this);

        _view.setResolutionOptions(_model.getResolutionOptions());
        _view.setDepthOptions(_model.getDepthOptions());
        _view.setFrequencyOptions(_model.getFrequencyOptions());

        _view.setResolution(_model.getResolution());
        _view.setDepth(_model.getDepth());
        _view.setFrequency(_model.getFrequency());
        _view.setIsFullScreen(_model.getIsFullScreen());
    }

    @Override
    public void CancelPressed() {
        GameManager.getInstance().popState();
    }

    @Override
    public void SaveSettings() {
        int width = _view.getWidth();
        int height = _view.getHeight();
        int depth = _view.getDepth();
        int frequency = _view.getFrequency();

        if (!DisplaySystem.getDisplaySystem().isValidDisplayMode(width, height, depth, frequency)) {
            _view.showErrorDialog("Video Error", "The selected mode is not supported");
        } else {
            _model.setWidth(width);
            _model.setHeight(height);
            _model.setDepth(depth);
            _model.setFrequency(frequency);
            _model.setIsFullScreen(_view.isFullScreen());
            _model.save();
            Main.changeResolution();
            GameManager.getInstance().popState();
        }
    }
}
