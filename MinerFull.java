import java.util.List;
import java.util.LinkedList;

import processing.core.*;


public class MinerFull
   extends Miner
{
   public MinerFull(String name, int resource_limit, Point position, 
      int rate, List<PImage> imgs, int animation_rate)
   {
      super(name, resource_limit, position, rate, imgs, animation_rate);
   }
   
   protected Class to_entity()
   {
      return Blacksmith.class;
   }

   
   protected Pair miner_to(WorldModel world, WorldObject smith)
   {
      if (smith instanceof Individual)
      {
         Individual blacksmith = (Blacksmith) smith;
      
         blacksmith.set_resource_count(blacksmith.get_resource_count()
            + this.get_resource_count());
         this.set_resource_count(0);
         return new Pair(new LinkedList<Point>(), true);
      }
      
      return null;
   }


   protected Miner try_transform_miner_status()
   {
      MinerNotFull new_entity = new MinerNotFull(this.get_name(), 
         this.get_resource_limit(), this.get_position(), this.get_rate(),
         this.get_images(), this.get_animation_rate());

      return new_entity;
   }
   
   public void schedule_entity(WorldModel world, ImageList i_store)
   {
   }
}
