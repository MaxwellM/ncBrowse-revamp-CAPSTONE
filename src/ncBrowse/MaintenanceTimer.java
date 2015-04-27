package ncBrowse;

public class MaintenanceTimer extends Thread {
	ButtonMaintainer mMaintainer;
	long mDelay;
	
	public MaintenanceTimer(ButtonMaintainer maintainer, long delay) {
		mMaintainer = maintainer;
		mDelay = delay;
	}
	
	public void startMaintainer() {
		if (isAlive()) {
			super.resume();
		}
		else
			start();	
	}
	
	public void endMaintainer() {
		if (isAlive()) {
			stop();
		}
	}
	
	public void run() {
		while (true) {
			mMaintainer.maintainButtons();
			try {
				Thread.sleep(mDelay);
			}
			catch (InterruptedException ignored) {
			}
		}
	}
}


