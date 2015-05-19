import java.util.List;

import processing.core.*;

public abstract class WorldObject
{
   private String name;
   public List<PImage> imgs;
   private int current_img; //index
   
   public WorldObject(String name, List<PImage> imgs)
   {
      this.name = name;
      this.imgs = imgs;
      this.current_img = 0;
   }

   public String get_name()
   {
      return this.name;
   }
   
   public List<PImage> get_images()
   {
      return this.imgs;
   }
   
   public PImage get_image()
   {
      return this.imgs.get(this.current_img);
   }
   
   public void next_image()
   {
      this.current_img = (this.current_img + 1) % (this.imgs.size());
   }

   public abstract void set_position(Point pt);
   public abstract Point get_position();
   public abstract void schedule_entity(WorldModel world, ImageList i_store);
   protected abstract void remove_entity(WorldModel world);
}
