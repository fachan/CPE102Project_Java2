import java.util.List;
import java.util.ArrayList;
import java.util.function.LongConsumer;

import processing.core.*;


public abstract class Entity
   extends WorldObject
{
   private Point position;
   private List<LongConsumer> pending_actions = new ArrayList<LongConsumer>();

   
   public Entity(String name, List<PImage> imgs, Point position)
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
   
 
   public void schedule_entity(WorldModel world, ImageList i_store)
   {
   }
   
   
   protected void schedule_action(WorldModel world, 
      LongConsumer action, long time)
   {
      this.add_pending_action(action);
      world.schedule_action(action, time);
   }
   
   
   protected void schedule_animation(WorldModel world, int repeat_count)
   {
      this.schedule_action(world, this.create_animation_action(world, 
         repeat_count), this.get_animation_rate());
   }
   
   
   public LongConsumer create_animation_action(WorldModel world, 
      int repeat_count)
   {  
      LongConsumer[] action = {null};
      action[0] = (long current_ticks) ->
         {
            this.remove_pending_action(action[0]);
         
            this.next_image();
         
            if (repeat_count != 1)
            {
               this.schedule_action(world, this.create_animation_action(world, 
                  Math.max(repeat_count - 1, 0)), current_ticks + 
                  this.get_animation_rate());
            }
         };
      
      return action[0];
   }

   
   protected void remove_pending_action(LongConsumer action)
   {
      if (this instanceof Entity)
      {
         this.pending_actions.remove(action);
      }
   }
   
   
   protected void add_pending_action(LongConsumer action)
   {
      if (this instanceof Entity)
      {
         this.pending_actions.add(action);
      }
   }
   
   
   protected List<LongConsumer> get_pending_actions()
   {
      if (this instanceof Entity)
      {
         return this.pending_actions;
      }
      
      else
      {
         return new ArrayList<LongConsumer>();
      }
   }
   
   
   protected void clear_pending_actions()
   {
      if (this instanceof Entity)
      {
         this.pending_actions = new ArrayList<LongConsumer>();
      }
   }
   
   
   protected void remove_entity(WorldModel world)
   {
      for (LongConsumer action : this.get_pending_actions())
      {
         world.unschedule_action(action);
      }
      
      this.clear_pending_actions();
      world.remove_entity(this);
   }

   
   protected abstract int get_rate();
   protected abstract int get_animation_rate();
   protected abstract LongConsumer create_self_action(WorldModel world, 
      ImageList i_store);
   protected abstract void schedule_self(WorldModel world, long ticks, 
      ImageList i_store);
}
