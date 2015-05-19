import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import processing.core.*;

public class ImageStore extends PApplet
{
   private static final String DEFAULT_IMAGE_NAME = "background_default";
   private final int DEFAULT_IMAGE_COLOR = color(128, 128, 128, 0);
   
   private static final int MIN_ARGS = 2;
   private static final int IMGS_IDX = 1;
   private static final int IMG_IDX = 1;
   private static final int NAME_IDX = 0;
   
   private static final int COLOR_MASK = 0xffffff;

   
   private static boolean checkArgs(String [] args)
   {
      return (args.length >= MIN_ARGS);
   }
   
   
   private ImageList process_file(Scanner in, int tile_width, 
      int tile_height)
   {
      List<PImage> imgs = new ArrayList<PImage>();
      List<String> names = new ArrayList<String>();
      boolean default_present = false;
      
      while (in.hasNextLine())
      {
         String [] file = in.nextLine().split("\\s");
         if (file != null)
         {
            for (int i = 0; i < file.length; i++)
            {
               PImage img = loadImage(file[IMG_IDX]);
               imgs.add(img);
               names.add(file[NAME_IDX]);
               
               if (file.length == 6)
               {
                  int r = Integer.parseInt(file[2]);
                  int g = Integer.parseInt(file[3]);
                  int b = Integer.parseInt(file[4]);
                  int a = Integer.parseInt(file[5]);
                  setAlpha(img, color(r, g, b), a);
               }
  
               if (file[NAME_IDX].equals(DEFAULT_IMAGE_NAME))
               {
                  default_present = true;
               }
            }
         }
      }
      
      if (!default_present)
      {
         PImage default_image = createImage(tile_width, tile_height, RGB);
         default_image.loadPixels();
         
         for (int i = 0; i < default_image.pixels.length; i++)
         {
            default_image.pixels[i] = DEFAULT_IMAGE_COLOR;
         }
         
         imgs.add(default_image);
         names.add(DEFAULT_IMAGE_NAME);
      }

      return new ImageList(imgs, names);
   }
   
   
   public ImageList load_images(String [] args, int tile_width, int tile_height)
   {
      try
      {
         if (checkArgs(args))
         {
            Scanner in = new Scanner(new FileInputStream(args[IMGS_IDX]));
            return process_file(in, tile_width, tile_height);
         }

         else
         {
            System.err.println("File(s) missing");
            return null;
         }
      }

      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
         return null;
      }
   }
   

   public List<PImage> get_images(ImageList images, String name, int tile_width, 
      int tile_height)
   {
      List<PImage> imagelist = new ArrayList<PImage>();
      
      for (int i = 0; i < images.get_names().size(); i++)
      {
         if (name.equals(images.get_names().get(i)))
         {
            imagelist.add(images.get_images().get(i));
         }
      }

      return imagelist;
   }
   
   
   private PImage setAlpha(PImage img, int maskColor, int alpha)
   {
      int alpha_value = alpha << 24;
      int non_alpha = maskColor & COLOR_MASK;
      img.format = PApplet.ARGB;
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         if ((img.pixels[i] & COLOR_MASK) == non_alpha)
         {
            img.pixels[i] = alpha_value | non_alpha;
         }
      }
      img.updatePixels();
      return img;
   }
}