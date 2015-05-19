import java.util.List;

import processing.core.*;

public abstract class Individual
   extends Entity
{
   private int rate;

   public Individual(String name, List<PImage> imgs, Point position, int rate)
   {
      super(name, imgs, position);
      this.rate = rate;
   }

   protected int get_rate()
   {
      return this.rate;
   }

   protected abstract int get_resource_limit();
   protected abstract void set_resource_count(int n);
   protected abstract int get_resource_count();
   protected abstract int get_resource_distance();
}

