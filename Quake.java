import java.util.List;
import java.util.function.LongConsumer;

import processing.core.*;

public class Quake
   extends StaticEntity
{
   private int animation_rate;
   private int QUAKE_STEPS = 10;
   private int QUAKE_DURATION = 1100;

   public Quake(String name, Point position, List<PImage> imgs, 
      int animation_rate)
   {
      super(name, imgs, position);
      this.animation_rate = animation_rate;
   }

   protected int get_animation_rate()
   {
      return this.animation_rate;
   }
   
   protected void schedule_self(WorldModel world, long ticks, ImageList i_store)
   {
      //System.out.println("test");
      this.schedule_animation(world, QUAKE_STEPS);
      this.schedule_action(world, this.create_entity_death_action(world, this),
         ticks + QUAKE_DURATION);
   }
   
   private LongConsumer create_entity_death_action(WorldModel world, Entity entity)
   {
      //System.out.println("test");
      LongConsumer[] action = {null};
      action[0] = (long current_ticks) ->
         {
            entity.remove_pending_action(action[0]);
            Point pt = entity.get_position();
            entity.remove_entity(world);
         };
         
      return action[0];
   }

   protected int get_rate()
   {
      return -1;
   }

   protected int get_resource_distance()
   {
      return -1;
   }
   
   protected Point find_open_around()
   {
      return null;
   }
   
   public LongConsumer create_self_action(WorldModel world, ImageList i_store)
   {
      return null;
   }
}
