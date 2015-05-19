import java.util.List;

import processing.core.*;

public abstract class StaticEntity
   extends Entity
{
   public StaticEntity(String name, List<PImage> imgs, Point position)
   {
      super(name, imgs, position);
   }
   
   protected void schedule_self(WorldModel world, long ticks, ImageList i_store)
   {
      this.schedule_action(world, this.create_self_action(world, i_store),
         ticks + this.get_rate());
   }

   protected abstract int get_rate();
   protected abstract int get_animation_rate();
   protected abstract int get_resource_distance();
}
