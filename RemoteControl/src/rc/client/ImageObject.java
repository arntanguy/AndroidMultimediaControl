package rc.client;

public class ImageObject {
	private int enabledLauncher;
	private String name;
	private boolean enabled;
	
	public ImageObject(String name, int enabledLauncher, int disabledLauncher) {
		this.name = name;
		this.enabledLauncher = enabledLauncher;
	}

	public int getLauncherRessource() {
		return enabledLauncher;
	}

	public void setLauncherRessource(int launcher) {
		this.enabledLauncher = launcher;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
