package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageViewResizable extends ImageView {
	
    public ImageViewResizable() {
        setPreserveRatio(true);
    }
    
    public ImageViewResizable(Image img) {
    	super(img); 
    	setPreserveRatio(true); 
    }

    @Override
    public double minWidth(double height) {
        return 1;
    }

    @Override
    public double prefWidth(double height) {
        Image I=getImage();
        if (I==null) return minWidth(height);
        return I.getWidth();
    }

    @Override
    public double maxWidth(double height) {
        return 20000;
    }

    @Override
    public double minHeight(double width) {
        return 1;
    }

    @Override
    public double prefHeight(double width) {
        Image I=getImage();
        if (I==null) return minHeight(width);
        return I.getHeight();
    }

    @Override
    public double maxHeight(double width) {
        return 20000;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(double width, double height) {
        setFitWidth(width);
        setFitHeight(height);
    }
}

