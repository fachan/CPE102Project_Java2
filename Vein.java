import java.util.List;
import java.util.LinkedList;
import java.util.function.LongConsumer;

import processing.core.*;

public class Vein
   extends StaticEntity
{
   private int rate;
   private int resource_distance;
   private static final long current_time = System.currentTimeMillis();

   public Vein(String name, int rate, Point position, List<PImage> imgs,
      int resource_distance)
   {
      super(name, imgs, position);
      this.rate = rate;
      this.resource_distance = resource_distance; 
   }

   protected int get_rate()
   {
      return this.rate;
   }

   protected int get_animation_rate()
   {
      return -1;
   }

   protected int get_resource_distance()
   {
      return this.resource_distance;
   }

   public void schedule_entity(WorldModel world, ImageList i_store)
   {
      this.schedule_self(world, 0, i_store);
   }
   
   
   private Point find_open_around(WorldModel world)
   {
      for (int dy = -this.get_resource_distance(); 
         dy < this.get_resource_distance() + 1; dy++)
      {
         for (int dx = -this.get_resource_distance();
            dx < this.get_resource_distance() + 1; dx++)
         {
            Point new_pt = new Point(this.get_position().xCoordinate() + dx,
               this.get_position().yCoordinate() + dy);
            
            if (world.within_bounds(new_pt) && !(world.is_occupied(new_pt)))
            {
               return new_pt;
            }
         }
      }
      
      return null;
   }

   
   protected LongConsumer create_self_action(WorldModel world, ImageList i_store)
   {
      LongConsumer[] action = {null};
      action[0] = (long current_ticks) ->
         {
            this.remove_pending_action(action[0]);
 
            Point open_pt = this.find_open_around(world);
            List<Point> tiles = new LinkedList<Point>();
            
            if (open_pt != null)
            {
               Entity ore = Main.create_ore(world, "ore - " + this.get_name() + 
                  " - " + String.valueOf(current_ticks), open_pt, 
                  current_ticks, i_store);
               world.add_entity(ore);
               tiles.add(open_pt);
            }
            
            this.schedule_action(world, this.create_self_action(world, i_store),
               current_ticks + this.get_rate());
         };

      return action[0];
   }
}
