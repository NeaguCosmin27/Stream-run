package Listeners;

import javax.swing.tree.DefaultTreeModel;

public interface ClientConnectionsTableListener {
     public void disconnect(int index);
     public void getTreeModel(DefaultTreeModel treeModel);
     public void getIndex(int index);
}
