package atechnique.models.concrete;

import atechnique.classfactory.ClassFactory;
import atechnique.models.interfaces.IEditSettingsModel;
import com.jme.system.GameSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;

public class EditSettingsModel implements IEditSettingsModel {
    private GameSettings _gameSettings;
    private List<String> _resolutionOptions;
    private List<String> _depthOptions;
    private List<String> _frequencyOptions;

    public EditSettingsModel() {
        _gameSettings = ClassFactory.getGameSettings();

        _resolutionOptions = new ArrayList<String>();
        _resolutionOptions.add("640x480");
        _resolutionOptions.add("800x600");
        _resolutionOptions.add("1024x768");
        _resolutionOptions.add("1280x1024");
        _resolutionOptions.add("1600x1200");
        _resolutionOptions.add("1440x900");

        _depthOptions = new ArrayList<String>();
        _depthOptions.add("16");
        _depthOptions.add("24");
        _depthOptions.add("32");

        _frequencyOptions = new ArrayList<String>();
        _frequencyOptions.add("60");
        _frequencyOptions.add("70");
        _frequencyOptions.add("72");
        _frequencyOptions.add("75");
        _frequencyOptions.add("85");
        _frequencyOptions.add("100");
        _frequencyOptions.add("120");
        _frequencyOptions.add("140");
    }

    @Override
    public List<String> getResolutionOptions() {
        return _resolutionOptions;
    }

    @Override
    public List<String> getDepthOptions() {
        return _depthOptions;
    }

    @Override
    public List<String> getFrequencyOptions() {
        return _frequencyOptions;
    }

    @Override
    public String getResolution() {
        int width = _gameSettings.getWidth();
        int height = _gameSettings.getHeight();

        return Integer.toString(width) + "x" + Integer.toString(height);
    }

    @Override
    public String getDepth() {
        return Integer.toString(_gameSettings.getDepth());
    }

    @Override
    public String getFrequency() {
        return Integer.toString(_gameSettings.getFrequency());
    }

    @Override
    public boolean getIsFullScreen() {
        return _gameSettings.isFullscreen();
    }

    @Override
    public void setWidth(int width) {
        _gameSettings.setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        _gameSettings.setHeight(height);
    }

    @Override
    public void setDepth(int colorBits) {
        _gameSettings.setDepth(colorBits);
    }

    @Override
    public void setFrequency(int frequency) {
        _gameSettings.setFrequency(frequency);
    }

    @Override
    public void setIsFullScreen(boolean isFullScreen) {
        _gameSettings.setFullscreen(isFullScreen);
    }

    @Override
    public void save() {
        try {
            ClassFactory.getPreferences().flush();
        } catch (BackingStoreException e) {
            // Logger.getLogger("global").log(Level.SEVERE, null, ex);
        }
    }
}
