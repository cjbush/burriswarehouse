package atechnique.models.interfaces;

import java.util.List;

public interface IEditSettingsModel {
    List<String> getResolutionOptions();

    List<String> getDepthOptions();

    List<String> getFrequencyOptions();

    String getResolution();

    String getDepth();

    String getFrequency();

    boolean getIsFullScreen();

    void setWidth(int width);

    void setHeight(int height);

    void setDepth(int colorBits);

    void setFrequency(int frequency);

    void setIsFullScreen(boolean isFullScreen);

    void save();
}
