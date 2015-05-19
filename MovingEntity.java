import java.util.List;

import processing.core.*;


public abstract class MovingEntity
   extends Individual
{
   private int animation_rate;

   
   public MovingEntity(String name, List<PImage> imgs, Point position, int rate, 
      int animation_rate)
   {
      super(name, imgs, position, rate);
      this.animation_rate = animation_rate;
   }

   
   protected int get_animation_rate()
   {
      return this.animation_rate;
   }

   
   protected void schedule_self(WorldModel world, long ticks, ImageList i_store)
   {
      this.schedule_action(world, this.create_self_action(world, i_store),
         ticks + this.get_rate());
      this.schedule_animation(world, 0);
   }
   
   
   protected Point next_position(WorldModel world, Point dest_pt)
   {
      int horiz = sign(dest_pt.xCoordinate() - 
         this.get_position().xCoordinate());
      Point new_pt = new Point(this.get_position().xCoordinate() + horiz,
         this.get_position().yCoordinate());
      
      if (horiz == 0 || world.is_occupied(new_pt))
      {
         int vert = sign(dest_pt.yCoordinate() - 
            this.get_position().yCoordinate());
         new_pt = new Point(this.get_position().xCoordinate(), 
            this.get_position().yCoordinate() + vert);
         
         if (vert == 0 || world.is_occupied(new_pt))
         {
            new_pt = new Point(this.get_position().xCoordinate(),
               this.get_position().yCoordinate());
         }
      }
      
      return new_pt;
   }
   
   
   protected int sign(int x)
   {
      if (x < 0)
      {
         return -1;
      }
      
      else if (x > 0)
      {
         return 1;
      }
      
      return 0;
   }
   
   
   /*private int find_nearest_entity(Point pt)
   {
      return pt.distance_sq(this.get_position());
   }*/

   
   protected abstract int get_resource_limit();
   protected abstract void set_resource_count(int n);
   protected abstract int get_resource_count();
}
