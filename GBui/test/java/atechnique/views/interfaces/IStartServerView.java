package atechnique.views.interfaces;

import java.util.List;

public interface IStartServerView extends IGameStateView {
    void setNickname(String nickname);

    void setPort(int port);

    void setAllegiances(List<String> allegiances);

    void addStartServerListener(IStartServerListener startServerListener);
}
