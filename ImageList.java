import java.util.List;

import processing.core.*;

public class ImageList
{
   private List<PImage> images;
   private List<String> names;
   
   public ImageList(List<PImage> images, List<String> names)
   {
      this.images = images;
      this.names = names;
   }
   
   public List<PImage> get_images()
   {
      return this.images;
   }
   
   public List<String> get_names()
   {
      return this.names;
   }
}