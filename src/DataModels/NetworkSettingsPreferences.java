package DataModels;

import java.util.prefs.Preferences;

public class NetworkSettingsPreferences {
	
	private Preferences networkingPreferences;
	
	public NetworkSettingsPreferences() {
		networkingPreferences = Preferences.userRoot().node("PeerNetworkingPreferences");
		networkingPreferences.put("DefaulPublictServerIP", "188.26.15.205");
		networkingPreferences.put("DefaulPublictServerPort", "7345");
		networkingPreferences.put("DefaultInternalServerIP", "192.168.100.7");
		networkingPreferences.put("DefaultInternalIPPort", "7344");
	}
	
	public void setExternalConnectionData(String IP, String port) {
		networkingPreferences.put("DefaultPublicServerIP", IP);
		networkingPreferences.put("DefaultPublicServerPort", port);
	}
	
	public String getDefaultPublicIP() {
		return networkingPreferences.get("DefaultPublicServerIP", "");
	}
	
	public String getDefaultPublicPort() {
		return networkingPreferences.get("DefaultPublicServerPort", "");
	}
	
	public String getDefaultInternalIP() {
		return networkingPreferences.get("DefaultInternalServerIP", "");
	}
	
	public String getDefaultInternalPort() {
		return networkingPreferences.get("DefaultInternalIPPort", "");
	}

}
