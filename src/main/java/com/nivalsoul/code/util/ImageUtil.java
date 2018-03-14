package com.nivalsoul.code.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.stream.ImageInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: ImageUtil </p>
 * <p>Description: 使用JDK原生态类生成图片缩略图和裁剪图片 </p>
 */
public class ImageUtil {
	
	private static Logger log = LoggerFactory.getLogger(ImageUtil.class);
    
    private static String DEFAULT_PREVFIX = "thumb_";
    private static Boolean DEFAULT_FORCE = false;
    
    /**
     * <p>Title: thumbnailImage</p>
     * <p>Description: 根据图片路径生成缩略图 </p>
     * @param imgFile      原图片
     * @param w            缩略图宽
     * @param h            缩略图高
     * @param prevfix      生成缩略图的前缀
     * @param force        是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
     * @param thumbFile    缩略图， 为null则将图片保存在原目录并加上前缀
     */
    public static void thumbnailImage(File imgFile, int w, int h, String prevfix, boolean force, File thumbFile){
        if(imgFile.exists()){
            try {
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames());
                String suffix = null;
                // 获取图片后缀
                if(imgFile.getName().indexOf(".") > -1) {
                    suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1);
                }// 类型和图片后缀全部小写，然后判断后缀是否合法
                if(suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()) < 0){
                    log.error("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
                    return ;
                }
                log.debug("target image's size, width:{}, height:{}.",w,h);
                Image img = ImageIO.read(imgFile);
                if(!force){
                    // 根据原图与要求的缩略图比例，找到最合适的缩略图比例
                    int width = img.getWidth(null);
                    int height = img.getHeight(null);
                    if((width*1.0)/w < (height*1.0)/h){
                        if(width > w){
                            h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w/(width*1.0)));
                            log.debug("change image's height, width:{}, height:{}.",w,h);
                        }
                    } else {
                        if(height > h){
                            w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h/(height*1.0)));
                            log.debug("change image's width, width:{}, height:{}.",w,h);
                        }
                    }
                }
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.getGraphics();
                g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
//                g.drawImage(img.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING), 0, 0, null);
                g.dispose();
                String p = imgFile.getPath();
                // 默认将图片保存在原目录并加上前缀
                if(thumbFile == null)
                	thumbFile = new File(p.substring(0,p.lastIndexOf(File.separator)) 
                    		+ File.separator + prevfix +imgFile.getName());
                ImageIO.write(bi, suffix, thumbFile);
                bi.flush();
            } catch (IOException e) {
               log.error("generate thumbnail image failed.",e);
            }
        }else{
            log.warn("the image is not exist.");
        }
    }
    
    public static void thumbnailImage(String imagePath, int w, int h, 
    		String prevfix, boolean force, String thumbFilePath){
        File imgFile = new File(imagePath);
        File thumbFile = null;
        if(thumbFilePath != null)
        	thumbFile = new File(thumbFilePath);
        thumbnailImage(imgFile, w, h, prevfix, force, thumbFile);
    }
    
    public static void thumbnailImage(String imagePath, int w, int h, boolean force, String thumbFilePath){
        thumbnailImage(imagePath, w, h, DEFAULT_PREVFIX, force, thumbFilePath);
    }
    
    public static void thumbnailImage(String imagePath, int w, int h, String thumbFilePath){
        thumbnailImage(imagePath, w, h, DEFAULT_FORCE, thumbFilePath);
    }
    
    public static void main(String[] args) {
		ImageUtil.thumbnailImage("imgs/Tulips.jpg", 100, 150, null);
    }
    
    public static void resizePNG(String fromFile, String toFile, int outputWidth, int outputHeight,boolean proportion) {
		try {
			File f2 = new File(fromFile);

			BufferedImage bi2 = ImageIO.read(f2);
			int newWidth;
			int newHeight;
			// 判断是否是等比缩放
			if (proportion == true) {
				// 为等比缩放计算输出的图片宽度及高度
				double rate1 = ((double) bi2.getWidth(null)) / (double) outputWidth + 0.1;
				double rate2 = ((double) bi2.getHeight(null)) / (double) outputHeight + 0.1;
				// 根据缩放比率大的进行缩放控制
				double rate = rate1 < rate2 ? rate1 : rate2;
				newWidth = (int) (((double) bi2.getWidth(null)) / rate);
				newHeight = (int) (((double) bi2.getHeight(null)) / rate);
			} else {
				newWidth = outputWidth; // 输出的图片宽度
				newHeight = outputHeight; // 输出的图片高度
			}
			BufferedImage to = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = to.createGraphics();
			to = g2d.getDeviceConfiguration().createCompatibleImage(newWidth, newHeight, Transparency.TRANSLUCENT);
			g2d.dispose();
			g2d = to.createGraphics();
			Image from = bi2.getScaledInstance(newWidth, newHeight, bi2.SCALE_AREA_AVERAGING);
			g2d.drawImage(from, 0, 0, null);
			g2d.dispose();
			ImageIO.write(to, "png", new File(toFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    
    
    
    public static BufferedImage subsampleImage(ImageInputStream inputStream, int x, int y) throws IOException {
		BufferedImage resampledImage = null;

		Iterator<ImageReader> readers = ImageIO.getImageReaders(inputStream);

		if (!readers.hasNext()) {
			throw new IOException("No reader available for supplied image stream.");
		}

		ImageReader reader = readers.next();

		ImageReadParam imageReaderParams = reader.getDefaultReadParam();
		reader.setInput(inputStream);

		Dimension d1 = new Dimension(reader.getWidth(0), reader.getHeight(0));
		Dimension d2 = new Dimension(x, y);
		int subsampling = (int) scaleSubsamplingMaintainAspectRatio(d1, d2);
		imageReaderParams.setSourceSubsampling(subsampling, subsampling, 0, 0);

		reader.addIIOReadProgressListener(new IIOReadProgressListener() {
			public void imageComplete(ImageReader source) {
				System.out.println("image complete " + source);
			}

			public void imageProgress(ImageReader source, float percentageDone) {
				System.out.println("image progress " + source + ": " + percentageDone + "%");
			}

			public void imageStarted(ImageReader source, int imageIndex) {
				System.out.println("image #" + imageIndex + " started " + source);
			}

			public void readAborted(ImageReader source) {
				System.out.println("read aborted " + source);
			}

			public void sequenceComplete(ImageReader source) {
				System.out.println("sequence complete " + source);
			}

			public void sequenceStarted(ImageReader source, int minIndex) {
				System.out.println("sequence started " + source + ": " + minIndex);
			}

			public void thumbnailComplete(ImageReader source) {
				System.out.println("thumbnail complete " + source);
			}

			public void thumbnailProgress(ImageReader source, float percentageDone) {
				System.out.println("thumbnail started " + source + ": " + percentageDone + "%");
			}

			public void thumbnailStarted(ImageReader source, int imageIndex, int thumbnailIndex) {
				System.out.println("thumbnail progress " + source + ", " + thumbnailIndex + " of " + imageIndex);
			}
		});
		resampledImage = reader.read(0, imageReaderParams);
		reader.removeAllIIOReadProgressListeners();

		return resampledImage;
	}

	public static long scaleSubsamplingMaintainAspectRatio(Dimension d1, Dimension d2) {
		long subsampling = 1;

		if (d1.getWidth() > d2.getWidth()) {
			subsampling = Math.round(d1.getWidth() / d2.getWidth());
		} else if (d1.getHeight() > d2.getHeight()) {
			subsampling = Math.round(d1.getHeight() / d2.getHeight());
		}

		return subsampling;
	}

}