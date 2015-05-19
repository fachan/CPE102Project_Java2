import java.util.List;
import java.util.LinkedList;

import processing.core.*;

public class MinerNotFull
   extends Miner
{
   public MinerNotFull(String name, int resource_limit, Point position,
      int rate, List<PImage> imgs, int animation_rate)
   {
      super(name, resource_limit, position, rate, imgs, animation_rate);
   }
   
   public void schedule_entity(WorldModel world, ImageList i_store)
   {
      this.schedule_self(world, 0, i_store);
   }

   protected Class to_entity()
   {
      return Ore.class;
   }
   
   
   protected Pair miner_to(WorldModel world, WorldObject ore)
   {
      if (ore instanceof StaticEntity)
      {
         StaticEntity o = (Ore) ore;
      
         Point ore_pt = o.get_position();
         this.set_resource_count(1 + this.get_resource_count());
         o.remove_entity(world);
         
         List<Point> ore_list = new LinkedList<Point>();
         ore_list.add(ore_pt);
         return new Pair(ore_list, true);
      }
      
      return null;
   }
   

   protected Miner try_transform_miner_status()
   {
      if (this.get_resource_count() < this.get_resource_limit())
      {
         return this;
      }
      
      else
      {
         MinerFull new_entity = new MinerFull(this.get_name(), 
            this.get_resource_limit(), this.get_position(),
            this.get_rate(), this.get_images(), this.get_animation_rate());

         return new_entity;
      }
   }   
}
