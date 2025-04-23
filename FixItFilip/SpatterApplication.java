//------------------------------------------------
//CMSC-3320 Technical Computing Using Java		     
//Fix It Filip Program 8					                             
//Group 1												                      
//Group Names: 										                    
//-Margo Bonal,      bon8330@pennwest.edu			
//-Luke Ruffing,     ruf96565@pennwest.edu 		
//-Ethan Janovich,   jan60248@pennwest.edu			
//-----------------------------------------------
//package SpatterApplication;
//----------LIBRARIES-----------------
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import EDU.emporia.mathbeans.*;
import EDU.emporia.mathtools.*;
//import javafx.geometry.Point2D;
//import javafx.scene.shape.Ellipse;
import java.util.*;
//-----------END LIBRARIES-----------

//-------------------------------CLASS SPATTERAPPLICATION------------------------------------
public class SpatterApplication extends JFrame implements WindowListener, ActionListener 
{
//-----------------------------GLOBAL VARIABLES--------------------------------
  final double gravity=4.9;
  final double wallDistance=6;
  double t=0;
  double x1=0, y1=4;
  double oldx1=x1, oldy1=y1;
  double x2=1, y2=5;
  double oldx2=x2, oldy2=y2;
  double spatterWidth=0, spatterLength=0;
  boolean dragging1=false;
  boolean dragging2=false;
  javax.swing.Timer animationTimer;
  boolean move=false;
  private boolean isStandalone = false;
  JPanel jPanel1 = new JPanel();
  MathGrapher graph = new MathGrapher();
  MathGrapher dropShapeGraph = new MathGrapher();
  SymbolicParametricCurve bloodPath = new SymbolicParametricCurve();
  SymbolicParametricCurve directionVector = new SymbolicParametricCurve();
  SymbolicParametricCurve wall = new SymbolicParametricCurve();
  JLabel jLabel1 = new JLabel();
  JButton trackButton = new JButton();
  JLabel floorOrWallLabel = new JLabel();
  Ellipse spatterEllipse = new Ellipse();
  MathTextField widthMathTextField = new MathTextField();
  MathTextField lengthMathTextField = new MathTextField();
  JLabel widthLabel = new JLabel();
  JLabel lengthLabel = new JLabel();
  MathTextField angleMathTextField = new MathTextField();
  JLabel jLabel2 = new JLabel();
  JButton resetButton = new JButton();

  //-------ADDITIONS: TEXTFIELDS----------------
  MathTextField initA = new MathTextField();
  MathTextField initL= new MathTextField();
  JLabel WLabel = new JLabel();
  JLabel LLabel = new JLabel();
  MathTextField initV= new MathTextField();
  JLabel VLabel = new JLabel();
//-----------------------------END GLOBAL VARIABLES-----------------------------

//----------------GET PROPERTIES-------------------------------------------------
  //Get a parameter value
private String getProperty(Properties property, String key, String def)
{
	String temp;
	try{
		temp = property.getProperty(key);
		if(temp.equals(""))
			temp = def;
	}
	catch (NullPointerException e)
	{
		temp = def;
	}
	return temp;
}
//----------------END GET PROPERTIES-------------------------------------------------

  //Get a parameter value
//-----------------------------MAIN---------------------------------------------------
  public static void main(String[] args)
  {
	try {
 		SpatterApplication s = new SpatterApplication(); 
    }
     catch(Exception e) {
      e.printStackTrace();
    }
 }
//-----------------------------END MAIN------------------------------------------------

//------------------------------------FRAME INITIALIZATION----------------------------------
  //Component initialization
  public SpatterApplication() throws Exception {
	  animationTimer = new javax.swing.Timer(1, this);
    // this.setSize(new Dimension(660,440));
    this.setSize(new Dimension(680,440));
    jPanel1.setLayout(null);
    graph.setTraceEnabled(false);
    graph.setF(bloodPath);
    graph.setG(directionVector);
    graph.setGridLines(EDU.emporia.mathbeans.MathGrapher.GRIDOFF);
    graph.setToolTipText("Drag left hand point to adjust height, right hand point to adjust " +
    "direction and velocity");
    // graph.setXMax(6.0);
    graph.setXMax(7.0);
    graph.setXMin(0.0);
    graph.setYMax(9.0);
    graph.setYMin(0.0);
    graph.setBounds(new Rectangle(140, 5, 364, 390));
    // graph.setBounds(new Rectangle(140, 5, 400, 390));

    graph.addMouseMotionListener(new SpatterApplication_graph_mouseMotionAdapter(this));
    graph.addMouseListener(new SpatterApplication_graph_mouseAdapter(this));
    dropShapeGraph.setTraceEnabled(false);
    dropShapeGraph.setAxesColor(Color.lightGray);
    dropShapeGraph.setGridColor(Color.lightGray);
    dropShapeGraph.setTitleEnabled(false);
    dropShapeGraph.setXLabel("");
    dropShapeGraph.setXMax(5.0);
    dropShapeGraph.setXMin(-5.0);
    dropShapeGraph.setYLabel("");
    dropShapeGraph.setYMax(5.0);
    dropShapeGraph.setYMin(-5.0);
    //dropShapeGraph.setBounds(new Rectangle(507, 7, 131, 124));
    dropShapeGraph.setBounds(new Rectangle(507, 7, 140, 124));


    bloodPath.setYFormula("1");
    directionVector.setXFormula("0");

    bloodPath.setTMax(20.0);
    bloodPath.setTMin(0.0);
    directionVector.setTMax(1.0);
    directionVector.setTMin(0.0);
    wall.setXFormula(""+wallDistance);
    wall.setYFormula("t");
    wall.setTMin(0.0);

    jLabel1.setFont(new java.awt.Font("Dialog", 1, 16));
    jLabel1.setText("Blood Spatter");
    jLabel1.setBounds(new Rectangle(7, 17, 133, 38));
    
    trackButton.setBounds(new Rectangle(23, 81, 101, 39));
    trackButton.setText("Trace path");
    trackButton.addActionListener(new SpatterApplication_trackButton_actionAdapter(this));

    floorOrWallLabel.setHorizontalAlignment(SwingConstants.CENTER);
    floorOrWallLabel.setText("Press Trace path ");
    floorOrWallLabel.setBounds(new Rectangle(517, 135, 114, 28));
    
    widthMathTextField.setMaxNumberOfCharacters(8);
    widthMathTextField.setEditable(false);
    widthMathTextField.setFont(new java.awt.Font("Dialog", 0, 14));
    widthMathTextField.setHorizontalAlignment(SwingConstants.CENTER);
    widthMathTextField.setMargin(new Insets(1, 1, 1, 1));
    widthMathTextField.setRequestFocusEnabled(true);
    widthMathTextField.setText("");
    widthMathTextField.setBounds(new Rectangle(520, 207, 110, 30));

    lengthMathTextField.setBounds(new Rectangle(521, 271, 110, 30));
    lengthMathTextField.setMaxNumberOfCharacters(8);
    lengthMathTextField.setEditable(false);
    lengthMathTextField.setFont(new java.awt.Font("Dialog", 0, 14));
    lengthMathTextField.setHorizontalAlignment(SwingConstants.CENTER);
    lengthMathTextField.setText("");

    widthLabel.setText("width (in mm):");
    widthLabel.setBounds(new Rectangle(521, 184, 111, 25));
    lengthLabel.setBounds(new Rectangle(520, 248, 112, 25));
    lengthLabel.setText("height (in mm):");

    angleMathTextField.setBounds(new Rectangle(521, 355, 110, 30));
    angleMathTextField.setText("");
    angleMathTextField.setRequestFocusEnabled(true);
    angleMathTextField.setMargin(new Insets(1, 1, 1, 1));
    angleMathTextField.setHorizontalAlignment(SwingConstants.CENTER);
    angleMathTextField.setFont(new java.awt.Font("Dialog", 0, 14));
    angleMathTextField.setEditable(false);
    angleMathTextField.setMaxNumberOfCharacters(10);

    jLabel2.setText("Angle of impact:");
    jLabel2.setBounds(new Rectangle(520, 328, 111, 24));

  
    // LEFT SIDE TEXTFIELDS
    // MathTextField initA = new MathTextField();
    initA.setBounds(new Rectangle(23, 207, 110, 30));
    initA.setText("");
    initA.setRequestFocusEnabled(true);
    initA.setMargin(new Insets(1, 1, 1, 1));
    initA.setHorizontalAlignment(SwingConstants.CENTER);
    initA.setFont(new java.awt.Font("Dialog", 0, 14));
    initA.setEditable(false);
    initA.setMaxNumberOfCharacters(10);

    JLabel WLabel = new JLabel();
    WLabel.setText("Initial Height (m):");
    WLabel.setBounds(new Rectangle(23, 184, 130, 20));

    // --------------------------

    // MathTextField initL = new MathTextField();
    initL.setBounds(new Rectangle(23, 271, 110, 30));
    initL.setText("");
    initL.setRequestFocusEnabled(true);
    initL.setMargin(new Insets(1, 1, 1, 1));
    initL.setHorizontalAlignment(SwingConstants.CENTER);
    initL.setFont(new java.awt.Font("Dialog", 0, 14));
    initL.setEditable(false);
    initL.setMaxNumberOfCharacters(10);

    JLabel LLabel = new JLabel();
    LLabel.setText("Initial Angle:");
    LLabel.setBounds(new Rectangle(23, 248, 130, 20));
    // --------------------------

    // MathTextField initV = new MathTextField();
    initV.setBounds(new Rectangle(23, 355, 110, 30));
    initV.setText("");
    initV.setRequestFocusEnabled(true);
    initV.setMargin(new Insets(1, 1, 1, 1));
    initV.setHorizontalAlignment(SwingConstants.CENTER);
    initV.setFont(new java.awt.Font("Dialog", 0, 14));
    initV.setEditable(false);
    initV.setMaxNumberOfCharacters(10);

    JLabel VLabel = new JLabel();
    VLabel.setText("Initial velocity: (m/s)");
    VLabel.setBounds(new Rectangle(23, 332, 130, 20));

    this.add(WLabel);
    this.add(initA);

    this.add(LLabel);
    this.add(initL);

    this.add(VLabel);
    this.add(initV);


    resetButton.addActionListener(new SpatterApplication_resetButton_actionAdapter(this));
    resetButton.setText("reset");
    resetButton.addActionListener(new SpatterApplication_resetButton_actionAdapter(this));
    resetButton.setBounds(new Rectangle(23, 142, 101, 39));

    this.getContentPane().add(jPanel1, BorderLayout.CENTER);
    this.setResizable(false);
    setVisible(true);			//make it visible
    validate();				//validate the layout
    addWindowListener(this);
    setTitle("Spatter Application");

    jPanel1.add(graph, null);
    jPanel1.add(jLabel1, null);
    jPanel1.add(trackButton, null);
    jPanel1.add(dropShapeGraph, null);
    jPanel1.add(floorOrWallLabel, null);
    jPanel1.add(widthLabel, null);
    jPanel1.add(lengthLabel, null);
    jPanel1.add(lengthMathTextField, null);
    jPanel1.add(widthMathTextField, null);
    jPanel1.add(angleMathTextField, null);
    jPanel1.add(jLabel2, null);
    jPanel1.add(resetButton, null);
    graph.setPointRadius(4);
    graph.updateGraph();
   	dropShapeGraph.removeAll();

    // Set initial values on launch
    initV.setMathValue(places1(Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1)))); // velocity
    //initL.setMathValue(places1(angle(0) * 180 / Math.PI)); // angle in degrees
    initA.setMathValue(places1(y1)); // height in meters
    //initL.setMathValue(places1(10*spatterEllipse.getYRadius()));
    initL.setMathValue(places1(Math.atan2(y2 - y1, x2 - x1) * 180 / Math.PI));// angle in degrees


    repaint();
  }
//------------------------------------END FRAME INITIALIZATION-------------------------------
  //Get Application information
  public String getApplicationInfo()
  {
    return "Application Information";
  }
  //Get parameter info
  public String[][] getParameterInfo()
  {
    return null;
  }

public void stop()
{
	this.removeWindowListener(this);
}

public void windowClosing(WindowEvent e)
{
	stop();
	dispose();
	System.exit(0);
}
//-------------------------------WINDOW METHODS---------------------------------------------------
public void windowClosed(WindowEvent e){}
public void windowOpened(WindowEvent e){}
public void windowActivated(WindowEvent e){}
public void windowDeactivated(WindowEvent e){}
public void windowIconified(WindowEvent e){}
public void windowDeiconified(WindowEvent e){}
//-------------------------------END WINDOW METHODS-------------------------------------------------

//-------------------------------ACTION PERFORMED---------------------------------------------------
    public void actionPerformed( ActionEvent e)
     {
      Point2D p = bloodPath.getPoint(t);
       graph.plotPoint(p.getX(),p.getY());
       t += 0.02;
       graph.updateGraph();
       if(t>wallDistance/(x2-x1))
       {
         animationTimer.stop();
         floorOrWallLabel.setText("Wall spatter shape");
         lengthLabel.setText("Height (in mm):");
         spatterEllipse.setXRadius((1+Math.random())/2);
         spatterEllipse.setYRadius(spatterEllipse.getXRadius()/Math.cos(angle(t)));
         dropShapeGraph.addGraph(spatterEllipse, Color.RED);
         widthMathTextField.setMathValue(places1(10*spatterEllipse.getXRadius()));
         lengthMathTextField.setMathValue(places1(10*spatterEllipse.getYRadius()));
         angleMathTextField.setMathValue(places1(90-angle(t)*180/Math.PI));
       }
       
       if(t>(((y2-y1)+Math.sqrt((y1-y2)*(y1-y2)+4*gravity*y1)))/(2*gravity))
       {
         animationTimer.stop();
         floorOrWallLabel.setText("Floor spatter shape");
         lengthLabel.setText("Length (in mm):");
         spatterEllipse.setXRadius((1+Math.random())/2);
         spatterEllipse.setYRadius(spatterEllipse.getXRadius()/Math.sin(angle(t)));
         dropShapeGraph.addGraph(spatterEllipse, Color.RED);
         widthMathTextField.setMathValue(places1(10*spatterEllipse.getXRadius()));
         lengthMathTextField.setMathValue(places1(10*spatterEllipse.getYRadius()));
         angleMathTextField.setMathValue(places1(angle(t)*180/Math.PI));

       }
  }
//-------------------------------END ACTION PERFORMED-------------------------------------------------
//-------------------------------GRAPH MOUSE PRESSED-------------------------------------------------
  void graph_mousePressed(MouseEvent e) {
    int xMouse=e.getX();
    int yMouse=e.getY();
    int distance1Squared = (xMouse-graph.xMathToPixel(x1))*
    (xMouse-graph.xMathToPixel(x1))+ (yMouse-graph.yMathToPixel(y1))*
    (yMouse-graph.yMathToPixel(y1));
    if (distance1Squared < 9) dragging1 = true;
    int distance2Squared = (xMouse-graph.xMathToPixel(x2))*
    (xMouse-graph.xMathToPixel(x2))+ (yMouse-graph.yMathToPixel(y2))*
    (yMouse-graph.yMathToPixel(y2));
    if (distance2Squared < 9 && distance1Squared >=9) dragging2 = true;
  }
//-------------------------------END GRAPH MOUSE PRESSED---------------------------------------------
//-------------------------------GRAPH MOUSE RELEASED------------------------------------------------
  void graph_mouseReleased(MouseEvent e)
  {
    dragging1 = false;
    dragging2 = false;
  }
//-------------------------------END GRAPH MOUSE RELEASED-------------------------------------------
//-------------------------------ROUNDING----------------------------------------------------------
  public double places1(double x)
{
    x=10*x;
    x=Math.round(x);
    x=(double) x/10;
    return x;
}

public double places2(double x)
{
    x=100*x;
    x=Math.round(x);
    x=(double) x/100;
    return x;
}
//-------------------------------END ROUNDING------------------------------------------------------
//-------------------------------GETTERS-----------------------------------------------------------
public double x(double t){return ((x2-x1)*t);}

public double y(double t){return (y1+(y2-y1)*t-gravity*t*t);}

public double angle(double t){return -Math.atan((y(t)-y(t-0.04))/(x(t)-x(t-0.04)));}
//-------------------------------END GETTERS-------------------------------------------------------

//-------------------------------REPAINT----------------------------------------------------------
  public void repaint()
  {
    graph.removeAllPoints();
       graph.removeAll();
            graph.addPoint(x1,y1,Color.magenta);
            graph.addPoint(x2,y2,Color.magenta);
            try{
                directionVector.setXFormula(x2+"*t");
                directionVector.setYFormula(y1+"+"+"("+y2+"-"+y1+")*"+"t");
            }catch(Graphable_error e){}
          if(x1==x2)
          {
          }
          else
          {
          }
          try{
               bloodPath.setXFormula("("+x2+"-"+x1+")*t");
               bloodPath.setYFormula(y1+"+("+y2+"-"+y1+")*t-"+gravity+"*t*t");
          }catch(Graphable_error e){
              }
          graph.addGraph(directionVector, Color.MAGENTA);
          graph.addGraph(bloodPath, Color.RED);
          graph.addGraph(wall, Color.BLUE);
          graph.updateGraph();
  }
//-------------------------------END REPAINT------------------------------------------------------

//-------------------------------GRAPH MOUSE DRAGGED-------------------------------------------------
  void graph_mouseDragged(MouseEvent e)
{
    double xmin = graph.getXMin();
    double xmax = graph.getXMax();
    double ymin = graph.getYMin();
    double ymax = graph.getYMax();

    if (dragging1)
    {
        oldy1 = y1;
        oldy2 = y2;
        double newY1 = graph.yPixelToMath(e.getY());

        newY1 = Math.max(ymin, Math.min(ymax, newY1));

        y1 = newY1;
        y2 = oldy2 - oldy1 + y1;
        
        y2 = Math.max(ymin, Math.min(ymax, y2));

        repaint();
    }

    if (dragging2)
    {
        oldx2 = x2;
        oldy2 = y2;
        double newX2 = graph.xPixelToMath(e.getX());
        double newY2 = graph.yPixelToMath(e.getY());

        newX2 = Math.max(xmin, Math.min(xmax, newX2));
        newY2 = Math.max(ymin, Math.min(ymax, newY2));

        x2 = newX2;
        y2 = newY2;

        repaint();
    }

    // Set initial values on launch
    initV.setMathValue(places1(Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1)))); // velocity
    //initL.setMathValue(places1(angle(0) * 180 / Math.PI)); // angle in degrees
    initA.setMathValue(places1(y1)); // height in meters
    //initL.setMathValue(places1(10*spatterEllipse.getYRadius()));
    //initL.setMathValue(places1(-angle(t)*180/Math.PI));
    initL.setMathValue(places1(Math.atan2(y2 - y1, x2 - x1) * 180 / Math.PI));// angle in degrees
}
//-------------------------------END GRAPH MOUSE DRAGGED---------------------------------------------

//-------------------------------TRACK BUTTON-ACTION PERFORMED--------------------------------------
  void trackButton_actionPerformed(ActionEvent e) {
    dropShapeGraph.removeGraph(spatterEllipse);
    t=0;
    animationTimer.start();
  }
//-------------------------------END TRACK BUTTON-ACTION PERFORMED-----------------------------------

//-------------------------------RESET BUTTON-ACTION PERFORMED---------------------------------------------------------
  void resetButton_actionPerformed(ActionEvent e) {
  	animationTimer = null;
	  animationTimer = new javax.swing.Timer(1, this);
  	t=0;
  	x1=0;
  	y1=4;
  	oldx1=x1;
  	oldy1=y1;
  	x2=1;
  	y2=5;
  	oldx2=x2;
  	oldy2=y2;
  	spatterWidth=0;
  	spatterLength=0;

	try {
	    bloodPath.setYFormula("1");
    	directionVector.setXFormula("0");
	    wall.setXFormula(""+wallDistance);
	    wall.setYFormula("t");
	}
	catch (Graphable_error r)
	{
	}

    floorOrWallLabel.setText("Press Trace path ");
    lengthLabel.setText("height (in mm):");
    widthMathTextField.setText("");
    lengthMathTextField.setText("");
    angleMathTextField.setText("");

    //reset the inital text fields
    //initA.setText("");
    //initL.setText("");
    //initV.setText("");

    // reset initial values on launch
    initV.setMathValue(places1(Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1)))); // velocity
    //initL.setMathValue(places1(angle(0) * 180 / Math.PI)); // angle in degrees
    initA.setMathValue(places1(y1)); // height in meters
    //initL.setMathValue(places1(10*spatterEllipse.getYRadius()));
    //initL.setMathValue(places1(-angle(t)*180/Math.PI));
    initL.setMathValue(places1(Math.atan2(y2 - y1, x2 - x1) * 180 / Math.PI));

    graph.setPointRadius(4);
    graph.updateGraph();
    dropShapeGraph.removeGraph(spatterEllipse);
    repaint();


  }
  //-------------------------------END RESET BUTTON-ACTION PERFORMED---------------------------------------------------------

}
//-------------------------------END CLASS SPATTERAPPLICATION------------------------------------


//-------------------------------GRAPH MOUSE ADAPTER-----------------------------------------------
class SpatterApplication_graph_mouseAdapter extends java.awt.event.MouseAdapter
{
  SpatterApplication adaptee;

  SpatterApplication_graph_mouseAdapter(SpatterApplication adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mousePressed(MouseEvent e)
  {
    adaptee.graph_mousePressed(e);
  }
  public void mouseReleased(MouseEvent e)
  {
    adaptee.graph_mouseReleased(e);
  }
}
//-------------------------------END GRAPH MOUSE ADAPTER---------------------------------------------
//-------------------------------GRAPH MOUSE ADAPTER-----------------------------------------------
class SpatterApplication_graph_mouseMotionAdapter extends java.awt.event.MouseMotionAdapter
{
  SpatterApplication adaptee;

  SpatterApplication_graph_mouseMotionAdapter(SpatterApplication adaptee)
  {
    this.adaptee = adaptee;
  }
  public void mouseDragged(MouseEvent e) {
    adaptee.graph_mouseDragged(e);
  }
}
//-------------------------------END GRAPH MOUSE MOTION ADAPTER-------------------------------------

//-------------------------------TRACK BUTTON-------------------------------------------------------
class SpatterApplication_trackButton_actionAdapter implements java.awt.event.ActionListener {
  SpatterApplication adaptee;

  SpatterApplication_trackButton_actionAdapter(SpatterApplication adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.trackButton_actionPerformed(e);
  }
}
//--------------------------------END TRACK BUTTON----------------------------------------------------

//-------------------------------RESET BUTTON---------------------------------------------------------
class SpatterApplication_resetButton_actionAdapter implements java.awt.event.ActionListener {
  SpatterApplication adaptee;

  SpatterApplication_resetButton_actionAdapter(SpatterApplication adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.resetButton_actionPerformed(e);
  }
}
//-------------------------------END RESET BUTTON-------------------------------------------------------