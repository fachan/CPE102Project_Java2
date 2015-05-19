import java.util.List;

import processing.core.*;

public class Obstacle
   extends WorldObject
{
   private Point position;

   public Obstacle(String name, Point position, List<PImage> imgs)
   {
      super(name, imgs);
      this.position = position;
   }

   public void set_position(Point point)
   {
      this.position = point;
   }

   public Point get_position()
   {
      return this.position;
   }
   
   protected void remove_entity(WorldModel world)
   {
   }
   
   public void schedule_entity(WorldModel world, ImageList i_store)
   {
   }
}
