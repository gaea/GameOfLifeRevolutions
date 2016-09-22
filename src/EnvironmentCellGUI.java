import com.sun.j3d.utils.universe.*; 
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.behaviors.keyboard.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.geometry.*;

import com.sun.j3d.utils.picking.*;
import ncsa.j3d.loaders.ModelLoader;

import javax.media.j3d.TextureUnitState;
import javax.media.j3d.Behavior.*;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.BoundingBox;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;

import java.io.*;
import com.sun.j3d.utils.image.TextureLoader;

import java.awt.event.KeyEvent;
import java.awt.event.*;
import java.awt.*;
import javax.swing.Timer;
import java.util.Enumeration;

import java.net.*;

public class EnvironmentCellGUI extends BranchGroup {

    private GameOfLifeRevolutions golr;
    private TransformGroup vpTrans;
    private BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
    private KeyNavigatorBehavior keyNavCamara;
    private Appearance apparTransparency = new Appearance();
    private Appearance apparBlue = new Appearance();
    private Appearance apparWhiteShadow = new Appearance();
    private Appearance apparBlueShadow = new Appearance();
    private PickCanvas pickCanvas;
    private Box [][] floor;
    private Timer timer;

    public EnvironmentCellGUI(SimpleUniverse simpleU, Canvas3D canvas3D, GameOfLifeRevolutions golr){
		this.golr = golr;
		this.vpTrans = simpleU.getViewingPlatform().getViewPlatformTransform();
		this.pickCanvas = new PickCanvas(canvas3D, this);
		this.pickCanvas.setMode(PickTool.GEOMETRY_INTERSECT_INFO); 
	    this.pickCanvas.setTolerance(0.0f);
		this.floor = new Box[golr.getEnvironmentCell().getWidth()][golr.getEnvironmentCell().getHeigth()];
		
		canvas3D.addMouseListener(new MouseAdapter(){
		    public void mouseClicked(MouseEvent e) {
			setStateCells(e);
		    }
		});
		
		canvas3D.addKeyListener(new KeyListener(){
		    public void keyPressed(KeyEvent keyEvent) {
				if(keyEvent.getKeyCode()==KeyEvent.VK_C){
				    clear();
				}
				if(keyEvent.getKeyCode()==KeyEvent.VK_S){
				    timer.start();
				}
				if(keyEvent.getKeyCode()==KeyEvent.VK_ESCAPE){
				    System.exit(0);
				}
				if(keyEvent.getKeyCode()==KeyEvent.VK_P){
				    timer.stop();
				}
				if(keyEvent.getKeyCode()==KeyEvent.VK_D){
				    if((timer.getDelay()-200)>0){
						timer.setDelay(timer.getDelay()-200);
				    }
				}
				if(keyEvent.getKeyCode()==KeyEvent.VK_A){
				    timer.setDelay(timer.getDelay()+200);
				}
		    }

		    public void keyReleased(KeyEvent keyEvent) {
			
		    }

		    public void keyTyped(KeyEvent keyEvent) {
			
		    }
		});

		addCamera();
		addLight();
		addBackground();
		loadAppearances();
		addMaze();
		compile();
		play();
    }
    
    public void clear(){
		timer.stop();
		for(int i=0; i<golr.getEnvironmentCell().getWidth(); i++){
		    for(int j=0; j<golr.getEnvironmentCell().getHeigth(); j++){
			golr.getEnvironmentCell().getCell(i, j).setState(false);
			floor[i][j].setAppearance(apparTransparency);
		    }
		}
    }
    
    public void setStateCells(MouseEvent e){
		pickCanvas.setShapeLocation(e);
		PickResult result = pickCanvas.pickClosest();
		
		if (result == null){
		    System.out.println("Nothing picked");
		} 
		else{
		    Primitive p = (Primitive)result.getNode(PickResult.PRIMITIVE);
		    System.out.println("picked!!!!!!!!!");
		    if (p != null){
				String posCell [] = ((String)(p.getUserData())).split(",");
				int i = Integer.parseInt(posCell[0]);
				int j = Integer.parseInt(posCell[1]);
				golr.getEnvironmentCell().getCell(i, j).setState(true);
				floor[i][j].setAppearance(apparBlue);
		    }
		}
    }

    public void play(){
		timer = new Timer (1000, new ActionListener (){
		    public void actionPerformed(ActionEvent e){
				golr.play();
				EnvironmentCell envCell = golr.getEnvironmentCell();
				for(int i=0; i<golr.getEnvironmentCell().getWidth(); i++){
				    for(int j=0; j<golr.getEnvironmentCell().getHeigth(); j++){
						if((((envCell.getEnvironment())[i][j])).getState() == true){
						    floor[i][j].setAppearance(apparBlue);
						}
						else{
						    floor[i][j].setAppearance(apparTransparency);
						}
				    }
				}
		    }
		});
	            
		//timer.start();
    }
    
    public void addBackground(){
		Background background = new Background();
		background.setColor(new Color3f(1.0f, 0.870588235f, 0.678431373f));
		background.setApplicationBounds(new BoundingSphere());
		addChild(background);
    }

    public void addLight(){
		Color3f ambientColour = new Color3f(0.2f, 0.2f, 0.2f);
		AmbientLight ambientLightNode = new AmbientLight(ambientColour);
		ambientLightNode.setInfluencingBounds(bounds);

		Color3f lightColour = new Color3f(1.0f, 1.0f, 1.0f);
		Vector3f lightDir = new Vector3f(-1.0f, -1.0f, -0.5f);
		DirectionalLight light = new DirectionalLight(lightColour, lightDir);
		light.setInfluencingBounds(bounds);

		addChild(ambientLightNode);
		addChild(light);
    }

    public void addCamera(){
		Transform3D T3D;
		Vector3f translate = new Vector3f();

		T3D = new Transform3D();
		translate.set(2.5f, 4.5f, 13.0f);

		Transform3D rotViewScene = new Transform3D();
		rotViewScene.rotX(-Math.PI/8.0);

		T3D.setTranslation(translate);
		T3D.mul(rotViewScene);
		vpTrans.setTransform(T3D);

		keyNavCamara = new KeyNavigatorBehavior(vpTrans);
		keyNavCamara.setSchedulingBounds(bounds);
		addChild(keyNavCamara);
    }
      
	public void loadAppearances(){
		/***********************      ColoringAttributes       *********************************/
		ColoringAttributes colorAtri= new ColoringAttributes();
		colorAtri.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
		/***************************************************************************************/

		/***********************      TransparencyAttributes       *********************************/
		TransparencyAttributes ta = new TransparencyAttributes();
		ta.setTransparencyMode( TransparencyAttributes.BLENDED );
		ta.setTransparency(0.7f); 
		/***************************************************************************************/

		/************************************   apparTransparency   ************************************/
		apparTransparency.setCapability(Appearance.ALLOW_MATERIAL_READ);
		apparTransparency.setCapability(Appearance.ALLOW_MATERIAL_WRITE);

		apparTransparency.setTransparencyAttributes(ta);
		/***************************************************************************************/

		/************************************   apparBlue   ************************************/
		apparBlue.setCapability(Appearance.ALLOW_MATERIAL_READ);
		apparBlue.setCapability(Appearance.ALLOW_MATERIAL_WRITE);

		Color3f emissiveColourBlue= new Color3f(0.0f, 0.0f, 0.0f);
		Color3f specularColourBlue= new Color3f(0.5f, 0.5f, 0.5f);
		Color3f ambientColourBlue= new Color3f(0.275f, 0.51f, 0.7f);
		Color3f diffuseColourBlue= new Color3f(0.275f, 0.51f, 0.7f);

		Material materialBlue = new Material(ambientColourBlue, emissiveColourBlue, diffuseColourBlue, specularColourBlue, 20.0f);
		materialBlue.setCapability(Material.ALLOW_COMPONENT_WRITE);
		materialBlue.setCapability(Material.ALLOW_COMPONENT_READ);

		apparBlue.setMaterial(materialBlue);
		apparBlue.setColoringAttributes(colorAtri);
		/***************************************************************************************/
	}
      
	public void addMaze(){
		for(int i=0; i<golr.getEnvironmentCell().getWidth(); i++){
			for(int j=0; j<golr.getEnvironmentCell().getHeigth(); j++){
				Transform3D t3dSpf = new Transform3D();
				t3dSpf.setTranslation( new Vector3d(((float)j*0.2f), 0.0, (float)i*0.2f));
				TransformGroup vpTransSpf = new TransformGroup(t3dSpf);

				Box box = new Box(0.1f, 0.1f, 0.1f, Box.GENERATE_NORMALS | Box.ENABLE_APPEARANCE_MODIFY, apparTransparency);

				box.setUserData(i+","+j);
				vpTransSpf.addChild(box);
				addChild(vpTransSpf);
				floor[i][j] = box;
			}
		}
	}
}