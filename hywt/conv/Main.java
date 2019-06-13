package hywt.conv;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Main {
	private static byte[] applybyte(byte[] b1,byte[] b2) throws Exception {
    	byte[] a=b1;
    	if(b2.length<=24) {
    		for(int i=b2.length;i>0;i--) {
    			int j=b2.length-i;
    			a[23-j]=b2[i-1];
    		}
    		return a;
    	}else {
    		throw new Exception("File extension too long");
    	}
    }
    
	
	public static void toImage(File file) {
		try {
			Window.progressBar.setValue(0);
			Window.progressBar1.setValue(0);
			Window.progressBar1.setString("Parsing... 0/3");
			long size = file.length();
			int width = (int) (Math.sqrt(size) / Math.sqrt(3) + 5);
			int height = (int) (size / (3 * width) + 5)+3;

			System.out.println(width + " / " + height);
			
			//byteStr2(byte3);
			BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			byte[] sizebyte=longToBytes(size);//Detect file size
			System.out.println("Size: "+bytesToHex(sizebyte));
			paint(bImage,1,getRGBInt(sizebyte[5],sizebyte[6],sizebyte[7]));
			paint(bImage,0,getRGBInt(sizebyte[3],sizebyte[3],sizebyte[4]));
			
			
            byte[] extb=new byte[24];
            byte[] ext=applybyte(extb,getFileExtension(file).getBytes(Charset.forName("ASCII")));//Detect file extension
				
            System.out.println("Extension: "+bytesToHex(ext));
            
            byte[][] ext2=divideArray(ext,3);
            
            for(int i=0;i<ext2.length;i++) {
            	paint(bImage,i+2,getRGBInt(ext2[i][0],ext2[i][1],ext2[i][2]));
            }
            
            Window.progressBar.setValue(0);
			byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
			
			Window.progressBar1.setString("Parsing... 1/3");
			Window.progressBar1.setValue(33);

			byte[][] byte3 = divideArray(bytes, 3);
			
			Window.progressBar1.setString("Painting... 2/3");
			Window.progressBar1.setValue(67);
			
			bytes=null;
			System.gc();
			
			float progress=0;
			int progressfull=byte3.length;
			for (int i = 0; i < byte3.length; i++) {
                progress=(float)i/progressfull*100;
                Window.progressBar.setValue((int) progress);
				//System.out.print(byte3[i][1] + " ");
				paint(bImage, i+16, getRGBInt(byte3[i][0], byte3[i][1], byte3[i][2]));
			}
			
			
			ImageIO.write(bImage, "png", new File("./test.png"));
			bImage.flush();
			
			Window.progressBar.setValue(100);
			Window.progressBar1.setString("Done! 3/3");
			Window.progressBar1.setValue(100);
			System.gc();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void paint(BufferedImage bImage, int number, int rgb) {
		int width = bImage.getWidth();
		int x = number % width;
		int y = number / width;
		bImage.setRGB(x, y, rgb);
	}

	public static int getRGBInt(int Red, int Green, int Blue){
	    Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
	    Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
	    Blue = Blue & 0x000000FF; //Mask out anything not blue.

	    return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
	}

	public static byte[][] divideArray(byte[] source, int chunksize) {

		byte[][] ret = new byte[(int) Math.ceil(source.length / (double) chunksize)][chunksize];

		int start = 0;
		float progress=0;
        Window.progressBar.setValue(0);
        //Window.progressBar.setString("Parsing...");
		for (int i = 0; i < ret.length; i++) {
			progress=(float)i/ret.length*100;
            Window.progressBar.setValue((int) progress);
			ret[i] = Arrays.copyOfRange(source, start, start + chunksize);
			start += chunksize;
		}
        Window.progressBar.setValue(100);
		return ret;
	}

	public static String byteStr(byte[] bytes) {
		String out="";
		for (int i = 0; i < bytes.length; i++) {
			out+=bytes[i] + " ";
		}
		return out;
	}
	public static void byteStr2(byte[][] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			System.out.print("["+(bytesToHex(bytes[i])) + "] ");
		}
	}
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	public static byte[] longToBytes(long x) {
	    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	    buffer.putLong(x);
	    return buffer.array();
	}
	public static String getFileExtension(File file) {
	    String name = file.getName();
	    int lastIndexOf = name.lastIndexOf(".");
	    if (lastIndexOf == -1) {
	        return ""; // empty extension
	    }
	    return name.substring(lastIndexOf).replace(".", "");
	}
}