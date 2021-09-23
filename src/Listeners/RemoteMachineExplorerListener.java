package Listeners;

public interface RemoteMachineExplorerListener {
	public void getSelectedPath(String selectedPath);
	public void getHierarchicalPath(String hierarchicalPath);
	public void expandSelectedFolder(String path);
}
