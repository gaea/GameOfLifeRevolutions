import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.universe.*; 
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import javax.swing.SwingUtilities;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import javax.swing.*;
import java.awt.event.*;
import java.awt.EventQueue;
import java.awt.*;
import java.net.URL;
import java.util.*;

public class GameOfLifeRevolutionsGUI extends JFrame {

	private GameOfLifeRevolutions golr = new GameOfLifeRevolutions(30, 30);

	public GameOfLifeRevolutionsGUI() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		catch ( Exception e ) {
			javax.swing.JOptionPane.showMessageDialog(this, "Imposible modificar el tema visual", "Lookandfeel inválido.",
				javax.swing.JOptionPane.ERROR_MESSAGE);
		}

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Game Of Life Revolutions");
		this.setMinimumSize(new Dimension(900, 700));
		this.setResizable(false);
		
		java.awt.Dimension pantalla = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		java.awt.Dimension dVentana = this.getSize();
		this.setLocation((pantalla.width - dVentana.width) / 2,(pantalla.height - dVentana.height) / 2);
		this.setLayout(new BorderLayout());
		
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

		Canvas3D canvas3D = new Canvas3D(config);

		SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
		simpleU.getViewingPlatform().setNominalViewingTransform();

		View view = simpleU.getViewer().getView();
		view.setTransparencySortingPolicy(View.TRANSPARENCY_SORT_GEOMETRY);

		ViewingPlatform viewingPlatform = simpleU.getViewingPlatform();
		OrbitBehavior orbit = new OrbitBehavior(canvas3D, OrbitBehavior.REVERSE_ALL);
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
		orbit.setSchedulingBounds(bounds);
		viewingPlatform.setViewPlatformBehavior(orbit);
		
		EnvironmentCellGUI envCell = new EnvironmentCellGUI(simpleU, canvas3D, golr);
		simpleU.addBranchGraph(envCell);
		
		this.add("Center", canvas3D);
		
		this.pack();
		this.setVisible(true);
	}

	public static void main(String args[]){
		new GameOfLifeRevolutionsGUI();
	}
}