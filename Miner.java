import java.util.List;
import java.util.LinkedList;
import java.util.function.LongConsumer;
import java.util.function.Supplier;

import processing.core.*;

public abstract class Miner
   extends MovingEntity
{
   private int resource_limit;
   private int resource_count;

   public Miner(String name, int resource_limit, Point position, 
      int rate, List<PImage> imgs, int animation_rate)
   {
      super(name, imgs, position, rate, animation_rate);
      this.resource_limit = resource_limit;

      if (this instanceof MinerFull)
      { 
         this.resource_count = resource_limit;
      }
 
      else
      {
         this.resource_count = 0;
      }
   }

   protected int get_resource_limit()
   {
      return this.resource_limit;
   }

   protected void set_resource_count(int n)
   {
      this.resource_count = n;
   }

   protected int get_resource_count()
   {
      return this.resource_count;
   }

   protected int get_resource_distance()
   {
      return -1;
   }

   
   protected LongConsumer create_self_action(WorldModel world, ImageList i_store)
   {
      LongConsumer[] action = {null};
      action[0] = (long current_ticks) ->
         {
            this.remove_pending_action(action[0]);
        
            Point entity_pt = this.get_position();
            WorldObject to_entity = world.find_nearest(entity_pt, this.to_entity());

            boolean found = this.miner_to_entity(world, to_entity).move();

            Miner new_entity = this;

            if (found)
            {
               new_entity = this.try_transform_miner(world,
                   this::try_transform_miner_status);
            }

            new_entity.schedule_action(world, 
               new_entity.create_self_action(world, i_store),
               current_ticks + new_entity.get_rate());
         };
      
      return action[0];
   }
   

   private Pair miner_to_entity(WorldModel world, WorldObject entity)
   {
      Point entity_pt = this.get_position();
      List<Point> points = new LinkedList<Point>();
      
      if (entity == null)
      {
         points.add(entity_pt);
         return new Pair(points, false);
      }
      
      Point to_pt = entity.get_position();
      
      if (entity_pt.adjacent(to_pt))
      {
         return this.miner_to(world, entity);
      }
      
      else
      {
         Point new_pt = this.next_position(world, to_pt);
         return new Pair(world.move_entity(this, new_pt), false);
      }
   }
   
   
   private Miner try_transform_miner(WorldModel world, 
      Supplier<Miner> transform)
   {
      Miner new_entity = transform.get();

      if (this != new_entity)
      {
         world.clear_pending_actions(this);
         world.remove_entity_at(this.get_position());
         world.add_entity(new_entity);
         new_entity.schedule_animation(world, 0);
      }
      
      return new_entity;
   }
   

   protected abstract Class to_entity();
   protected abstract Miner try_transform_miner_status(); 
   protected abstract Pair miner_to(WorldModel w, WorldObject ore);
}
