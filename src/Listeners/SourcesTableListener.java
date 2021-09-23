package Listeners;

import javax.swing.tree.DefaultTreeModel;

public interface SourcesTableListener {
     public void closeSource(int index);
     public void getTreeModel(DefaultTreeModel treeModel);
}
