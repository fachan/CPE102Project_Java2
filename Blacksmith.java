import java.util.List;
import java.util.function.LongConsumer;

import processing.core.*;

public class Blacksmith
   extends Individual
{
   private int resource_limit;
   private int resource_count = 0;
   private int resource_distance = 1;

   public Blacksmith(String name, Point position, List<PImage> imgs,
      int resource_limit, int rate, int resource_distance)
   {
      super(name, imgs, position, rate);
      this.resource_limit = resource_limit;
      this.resource_count = 0;
      this.resource_distance = resource_distance;
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
      return this.resource_distance;
   }
   
   protected void remove_entity(WorldModel world)
   {
   }
   
   protected int get_animation_rate()
   {
      return -1;
   }
   
   protected LongConsumer create_self_action(WorldModel world, ImageList i_store)
   {
      return null;
   }
   
   protected void schedule_self(WorldModel world, long ticks, 
      ImageList i_store)
   {
   }
   
   public void schedule_entity(WorldModel world, ImageList i_store)
   {
   }
}
