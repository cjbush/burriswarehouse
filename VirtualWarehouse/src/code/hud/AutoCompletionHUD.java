package code.hud;

import com.jmex.font2d.Font2D;
import com.jmex.font2d.Text2D;

@SuppressWarnings("serial")
public class AutoCompletionHUD extends HUD {
	private Text2D VocollectPrompt;
	private Text2D VocollectResponse;
	
	public AutoCompletionHUD(){
		Font2D f = new Font2D();
		VocollectPrompt = f.createText("Vocollect Prompt: ", 10, 0);
		VocollectPrompt.setLocalTranslation(0, 0, 0);
        this.attachChild(VocollectPrompt);
        
        VocollectResponse = f.createText("Vocollect Response: ", 10, 0);
		VocollectResponse.setLocalTranslation(0, 20, 0);
        this.attachChild(VocollectResponse);
        
        this.setLocalTranslation(90, 100, 0);
        
        
        this.updateGeometricState(0, false);
        this.updateRenderState();
	}
	public void setVocollectResponse(String vocollectResponse) {
		VocollectResponse.setText(vocollectResponse);
	}
	public Text2D getVocollectResponse() {
		return VocollectResponse;
	}
	public void setVocollectPrompt(String vocollectPrompt) {
		VocollectPrompt.setText(vocollectPrompt);
	}
	public Text2D getVocollectPrompt() {
		return VocollectPrompt;
	}
}
