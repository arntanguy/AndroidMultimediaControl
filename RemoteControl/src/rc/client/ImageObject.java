package rc.client;

public class ImageObject {
	private int launcher;
	private String name;
	
	public ImageObject(String name, int launcher) {
		this.name = name;
		this.launcher = launcher;
	}

	public int getLauncherRessource() {
		return launcher;
	}

	public void setLauncherRessource(int launcher) {
		this.launcher = launcher;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
