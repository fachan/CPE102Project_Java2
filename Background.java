import java.util.List;

import processing.core.*;

public class Background
   extends WorldObject
{
   public Background(String name, List<PImage> imgs)
   {
      super(name, imgs);
   }

   public Point get_position()
   {
      return null;
   }

   public void set_position(Point point)
   {
   }
   
   protected void remove_entity(WorldModel world)
   {
   }
   
   public void schedule_entity(WorldModel world, ImageList i_store)
   {
   }
}
