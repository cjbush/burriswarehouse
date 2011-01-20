package atechnique.game.state;

import java.util.Stack;

//will provide static access to different menu instances and will also handle the state changes between them
public class GameManager {
    private static GameManager _instance = new GameManager();

    private Stack<ATechniqueGameState> _states = new Stack<ATechniqueGameState>();

    protected void finalize() throws Throwable {
        // clean up all the states
        while (!_states.empty()) {
            _states.pop().exit();
        }
    }

    public static GameManager getInstance() {
        return _instance;
    }

    public void start(ATechniqueGameState state) {
        changeState(state);
    }

    public void changeState(ATechniqueGameState state) {
        while (!_states.empty()) {
            _states.pop().exit();
        }

        // store and init the new state
        _states.push(state);
        state.enter();
    }

    // Push the current state onto the stack (which will cause it to pause) and
    // switch to the new state (enter)
    // When the new state does a "popState" (exit), the current state will be
    // resumed (resume)
    public void pushState(ATechniqueGameState state) {
        // pause current state
        if (!_states.empty()) {
            _states.peek().pause();
        }

        // store and init the new state
        _states.push(state);
        state.enter();
    }

    public void popState() {
        // cleanup the current state
        if (!_states.empty()) {
            _states.pop().exit();
        }

        // resume previous state
        if (!_states.empty()) {
            _states.peek().resume();
        }
    }
}
