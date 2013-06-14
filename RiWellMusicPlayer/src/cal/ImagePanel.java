package cal;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * @author Kay Wellinger
 */
public class ImagePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private BufferedImage image;

    public ImagePanel(){
    	super();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image != null)
        	g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
    }

    public void setImage(BufferedImage img){
    	image = img;
    	repaint();
    }
    
    public void clearImage(){
    	image = null;
    }
    
    public BufferedImage scaleImage(Image img, int newWidth, int newHeight){
		//Skalierte kopie holen... ist normalerweise Toolbox image daher...
    	Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
		//... Buffered image erstellen
		BufferedImage buffimg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_4BYTE_ABGR);
		//und den ganzen mist rüber zeichnen. ist etwas langsamer aber schöner so
		Graphics g = buffimg.getGraphics();
		g.drawImage(scaledImage, 0, 0, null);
		buffimg.flush();
		
		return buffimg;
    }
}
