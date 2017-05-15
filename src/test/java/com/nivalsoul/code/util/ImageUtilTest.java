package com.nivalsoul.code.util;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.stream.ImageInputStream;

public class ImageUtilTest {

	public static void main(String[] args) throws IOException {
		long start = new Date().getTime();
		
		String imagePath = "E:/d.jpg";
		String outFile = "E:/thumb_d2.jpg";
		//ImageUtil.thumbnailImage(imagePath, 800, 600, null);
		
		RenderedImage subsampleImage = ImageUtil.subsampleImage(
				ImageIO.createImageInputStream(new File(imagePath)), 800, 600);
		ImageIO.write(subsampleImage, "jpg", new File(outFile));

		System.out.println(new Date().getTime()-start);
	}
	
	

}
