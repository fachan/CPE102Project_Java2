import java.util.List;
import java.util.function.LongConsumer;

import processing.core.*;

public class Ore
   extends StaticEntity
{
   private int rate = 5000;
   private int BLOB_RATE_SCALE = 4;
   
   public Ore(String name, List<PImage> imgs, Point position, int rate)
   {
      super(name, imgs, position);
      this.rate = rate;
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
      return -1;
   }
   
   protected Point find_open_around()
   {
      return null;
   }
   
   protected LongConsumer create_self_action(WorldModel world, 
      ImageList i_store)
   {
      LongConsumer[] action = {null};
      action[0] = (current_ticks) ->
         {
            this.remove_pending_action(action[0]);
            Entity blob = Main.create_blob(world, this.get_name() + " -- blob",
            this.get_position(), this.get_rate() / BLOB_RATE_SCALE, 
            current_ticks, i_store);

            this.remove_entity(world);
            world.add_entity(blob);
         };
         
      return action[0];
   }
   
   public void schedule_entity(WorldModel world, ImageList i_store)
   {
      this.schedule_self(world, 0, i_store);
   }
}
