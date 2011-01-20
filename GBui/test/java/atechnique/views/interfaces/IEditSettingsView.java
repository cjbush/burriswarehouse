package atechnique.views.interfaces;

import java.util.List;

public interface IEditSettingsView extends IGameStateView {
    void addEditSettingsListener(IEditSettingsListener editSettingsListener);

    void setResolutionOptions(List<String> resolutions);

    void setDepthOptions(List<String> depthValues);

    void setFrequencyOptions(List<String> frequencyOptions);

    void setResolution(String resolution);

    void setDepth(String colorBits);

    void setFrequency(String frequency);

    void setIsFullScreen(boolean isFullScreen);

    int getWidth();

    int getHeight();

    int getDepth();

    int getFrequency();

    boolean isFullScreen();

    void showErrorDialog(String title, String message);
}
