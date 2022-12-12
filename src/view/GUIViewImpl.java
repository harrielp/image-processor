package view;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.ButtonCommands;
import controller.GUIController;
import controller.commands.Load;
import model.ImageProcessorModel;

/**
 * A GUI view for out program -> visualizes the functions of our image processor and takes in a
 * type ImageProcessorModel to call the controller after a new image is loaded.
 */
public class GUIViewImpl extends JFrame implements GUIView {
  private ImageProcessorModel model; //the view takes in a model of type ImageProcessorModel
  private int numberOfClicks; //the number of times the load button is clicked
  private Load load; //the new Load command that is created when the user chooses a new image

  private JPanel mainPanel;
  private JPanel histogramPanel;

  private JLabel imgLabel; //represents the image to be displayed
  private JLabel loadDisplay; //displayed file path
  private JLabel msgLabel; //the displayed message as a result of renderMessage

  //all the command buttons
  private JButton imgLoad;
  private JButton imgSave;
  private JButton imgBrighten;
  private JButton imgDarken;
  private JButton imgHorizontalFlip;
  private JButton imgVerticalFlip;
  private JButton imgRedComponent;
  private JButton imgGreenComponent;
  private JButton imgBlueComponent;
  private JButton imgLuma;
  private JButton imgValue;
  private JButton imgIntensity;
  private JButton imgBlur;
  private JButton imgSharpen;
  private JButton imgSepia;
  private JButton imgGrayscale;
  private JButton imgRestart;
  private JButton imgDownscale;

  /**
   * A constructor for the view that initializes the GUI. Renders all needed panels,
   * messages, and buttons.
   *
   * @param model of type ImageProcessorModel that is used to process the Load class and
   *              the GUI controller
   * @throws IllegalArgumentException when the model is null
   */
  public GUIViewImpl(ImageProcessorModel model) throws IllegalArgumentException {
    super();

    this.model = model;

    if (model == null) {
      throw new IllegalArgumentException("Model in the view cannot be null.");
    }

    //initialize the main panel
    this.setTitle("Image Processing Model");
    this.setSize(1400, 1000);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

    //scroll bar around main panel
    JScrollPane mainScrollPane = new JScrollPane(mainPanel);
    add(mainScrollPane);

    //have a panel for possible messages
    msgLabel = new JLabel("––Messages will go here––");

    //file open and load
    imgLoad = new JButton("Open and Load an Image");
    imgLoad.setActionCommand("Open file");
    //displays the chosen image --> see the chooseAndModifyFile method below
    imgLoad.addActionListener(this::chooseAndModifyFile);
    loadDisplay = new JLabel("File path will appear here");

    //file save
    imgSave = new JButton("Save the Image");

    //create a panel that will have both load and save options
    JPanel loadAndSavePanel = new JPanel();
    loadAndSavePanel.setBorder(BorderFactory.createTitledBorder("Load and Save Images:"));
    loadAndSavePanel.setLayout(new GridLayout(2, 0, 0, 10));
    loadAndSavePanel.add(msgLabel);
    loadAndSavePanel.add(imgLoad);
    loadAndSavePanel.add(loadDisplay);
    loadAndSavePanel.add(imgSave);

    //create a histogram panel
    histogramPanel = new JPanel();
    histogramPanel.setBorder(BorderFactory.createTitledBorder("RGB and Intensity Histogram:"));
    histogramPanel.setPreferredSize(new Dimension(300, 300));

    //create a panel for displaying an image
    JPanel imagePanel = new JPanel();
    imagePanel.setBorder(BorderFactory.createTitledBorder("Working on:"));
    imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));
    imagePanel.add(histogramPanel);

    imagePanel.add(loadAndSavePanel);
    mainPanel.add(imagePanel);

    //initialize the label to which all the images will be placed as
    imgLabel = new JLabel();

    //allow the user to scroll in the image panel
    JPanel imageScrollPanel = new JPanel();
    imageScrollPanel.setLayout(new BoxLayout(imageScrollPanel, BoxLayout.Y_AXIS));
    imageScrollPanel.add(imgLabel);
    JScrollPane imageScrollPane = new JScrollPane(imageScrollPanel);
    imageScrollPane.setPreferredSize(new Dimension(500, 500));
    imagePanel.add(imageScrollPane);

    //create all command buttons
    imgBrighten = new JButton("Brighten");
    imgDarken = new JButton("Darken");
    imgHorizontalFlip = new JButton("Horizontal Flip");
    imgVerticalFlip = new JButton("Vertical Flip");
    imgRedComponent = new JButton("Red Component");
    imgGreenComponent = new JButton("Green Component");
    imgBlueComponent = new JButton("Blue Component");
    imgLuma = new JButton("Luma");
    imgValue = new JButton("Value");
    imgIntensity = new JButton("Intensity");
    imgBlur = new JButton("Blur");
    imgSharpen = new JButton("Sharpen");
    imgSepia = new JButton("Sepia Tone");
    imgGrayscale = new JButton("Grayscale Transformation");
    imgRestart = new JButton("Restart");
    imgDownscale = new JButton("Downscale");

    //show all commands on a panel representing possible modification choices
    JPanel commandsPanel = new JPanel();
    commandsPanel.setBorder(BorderFactory.createTitledBorder("Image Modifications:"));
    commandsPanel.setLayout(new GridLayout(15, 0, 1, 1));
    commandsPanel.add(imgBrighten);
    commandsPanel.add(imgDarken);
    commandsPanel.add(imgHorizontalFlip);
    commandsPanel.add(imgVerticalFlip);
    commandsPanel.add(imgRedComponent);
    commandsPanel.add(imgGreenComponent);
    commandsPanel.add(imgBlueComponent);
    commandsPanel.add(imgLuma);
    commandsPanel.add(imgValue);
    commandsPanel.add(imgIntensity);
    commandsPanel.add(imgBlur);
    commandsPanel.add(imgSharpen);
    commandsPanel.add(imgSepia);
    commandsPanel.add(imgGrayscale);
    commandsPanel.add(imgDownscale);
    commandsPanel.add(imgRestart);
    mainPanel.add(commandsPanel);
  }

  /**
   * Allows the user to choose an image to display and edit on the image panel. Creates and runs
   * a new Load command to the ImageProcessor model and runs the GUI controller whenever a
   * new image is chosen. Also displays the histogram whenever an image is loaded or when there
   * is a change in the model (when the user modifies the image through the model).
   *
   * @param e of type ActionEvent to apply to the specified Load button.
   */
  protected void chooseAndModifyFile(ActionEvent e) {

    numberOfClicks++;

    //we will only allow the user to load one image at a time, so if the user clicks the
    //"Open and Load an Image" button twice, they will get a pop-up message and won't be able
    //to perform the action:
    if (numberOfClicks > 1) {
      JOptionPane.showMessageDialog(mainPanel, "CANNOT LOAD AN IMAGE TWICE, "
              + "IF YOU WANT TO MODIFY ANOTHER IMAGE, PRESS \"RESTART\".");
      return;
    }

    //allows the user to choose a file but is limited to only images (ppm, png, pmb, etc.)
    if (e.getSource() == imgLoad) {
      JFileChooser fileChooser = new JFileChooser(".");
      FileNameExtensionFilter filter =
              new FileNameExtensionFilter("PNG, PPM, BMP, JPEG, and JPG Images",
                      "png", "ppm", "jpg", "bmp", "jpeg");
      fileChooser.setFileFilter(filter);
      int result = fileChooser.showOpenDialog(getParent());
      if (result == JFileChooser.APPROVE_OPTION) {
        try {
          File file = fileChooser.getSelectedFile();
          BufferedImage picture = ImageIO.read(file);

          //displays the chosen image to the panel
          imgLabel.setIcon(new ImageIcon(picture));

          //displays the file path to the user
          loadDisplay.setText(file.getAbsolutePath());

          //creates a new Load command each time a file is chosen and loads the image
          load = new Load(file.getAbsolutePath(), "name");
          this.setNewLoadCommand(load);
          load.run(model);

          //runs the controller everytime a new file is chosen since a new image has to be
          //modified every single time
          GUIController cont = new GUIController(model, this);
          cont.runGUI();

          //allows the GUI to display the histogram every time there is a change
          histogramPanel.add(new HistogramGraphics(model));

        } catch (IOException ie) {
          ie.printStackTrace();
          JOptionPane.showMessageDialog(mainPanel, "ERROR, CANNOT READ FILE.");
        }
      }
    }
  }

  @Override
  public void setNewLoadCommand(Load cmd) {
    this.load = cmd;
  }

  @Override
  public Load getNewLoadCommand() {
    return this.load;
  }

  @Override
  public void renderImage(BufferedImage img) {
    this.imgLabel.setIcon(new ImageIcon(img));
  }

  @Override
  public void renderMessage(String msg) {
    this.msgLabel.setText(msg);
  }

  @Override
  public void applyActionEventsToButtons(ButtonCommands commands) {
    //JUST LIKE TURTLE GRAPHICAL VIEW
    //JButton.addActionListener(ActionEvent e -> commands.blah());
    imgSave.addActionListener(l -> commands.save());
    imgBrighten.addActionListener(l -> commands.brighten());
    imgDarken.addActionListener(l -> commands.darken());
    imgHorizontalFlip.addActionListener(l -> commands.flipHori());
    imgVerticalFlip.addActionListener(l -> commands.flipVert());
    imgRedComponent.addActionListener(l -> commands.redComponent());
    imgGreenComponent.addActionListener(l -> commands.greenComponent());
    imgBlueComponent.addActionListener(l -> commands.blueComponent());
    imgLuma.addActionListener(l -> commands.lumaGrayscale());
    imgValue.addActionListener(l -> commands.valueGrayscale());
    imgIntensity.addActionListener(l -> commands.intensityGrayscale());
    imgBlur.addActionListener(l -> commands.blur());
    imgSharpen.addActionListener(l -> commands.sharpen());
    imgSepia.addActionListener(l -> commands.sepia());
    imgGrayscale.addActionListener(l -> commands.grayscaleColorTrans());
    imgRestart.addActionListener(l -> commands.restart());
    imgDownscale.addActionListener(l -> commands.downscale());
  }
}