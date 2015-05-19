import java.util.List;
import java.util.LinkedList;
import java.util.function.LongConsumer;

import processing.core.*;

public class OreBlob
   extends MovingEntity
{  
   public OreBlob(String name, Point position, int rate, List<PImage> imgs,
      int animation_rate)
   {
      super(name, imgs, position, rate, animation_rate);
   }

   
   protected Point next_position(WorldModel world, Point dest_pt)
   {
      int horiz = this.sign(dest_pt.xCoordinate() - 
         this.get_position().xCoordinate());
      Point new_pt = new Point(this.get_position().xCoordinate() + horiz, 
         this.get_position().yCoordinate());

      if (horiz == 0 || (world.is_occupied(new_pt) &&
         !(world.get_tile_occupant(new_pt) instanceof Ore)))
      {
         int vert = this.sign(dest_pt.yCoordinate() - 
            this.get_position().yCoordinate());
         new_pt = new Point(this.get_position().xCoordinate(), 
            this.get_position().yCoordinate() + vert);

         if (vert == 0 || (world.is_occupied(new_pt) &&
            !(world.get_tile_occupant(new_pt) instanceof Ore)))
         {
            new_pt = new Point(this.get_position().xCoordinate(), 
               this.get_position().yCoordinate());
         }
      }

      return new_pt;
   }

   
   protected LongConsumer create_self_action(WorldModel world, ImageList i_store)
   {
      LongConsumer[] action = {null};
      action[0] = (long current_ticks) ->
         {
            this.remove_pending_action(action[0]);

            Point entity_pt = this.get_position();
            WorldObject vein = world.find_nearest(entity_pt, Vein.class);
            List<Point> tiles = this.blob_to_vein(world, vein).get_points();
            boolean found = this.blob_to_vein(world, vein).move();

            long next_time = current_ticks + this.get_rate();
            if (found)
            {
               WorldObject quake = Main.create_quake(world, tiles.get(0), 
                  current_ticks, i_store);
               world.add_entity(quake);
               next_time = current_ticks + this.get_rate() * 2;
            }

            this.schedule_action(world, 
               this.create_self_action(world, i_store), next_time);
         };
         
      return action[0];
   }
      

   private Pair blob_to_vein(WorldModel world, WorldObject vein)
   {
      Point entity_pt = this.get_position();
      //List<Point> points = new LinkedList<>();
      
      if (vein == null)
      {
         List<Point> points = new LinkedList<>();
         points.add(entity_pt);
         return new Pair(points, false);
      }
      
      Point vein_pt = vein.get_position();
            
      if (entity_pt.adjacent(vein_pt))
      {
         List<Point> v_points = new LinkedList<>();
         vein.remove_entity(world);
         v_points.add(vein_pt);
         return new Pair(v_points, true);
      }
      
      else
      {
         Point new_pt = this.next_position(world, vein_pt);
         WorldObject old_entity = world.get_tile_occupant(new_pt);
         
         if (old_entity instanceof Ore)
         {
            old_entity.remove_entity(world);
         }
         
         return new Pair(world.move_entity(this, new_pt), false);
      }
   }
   
   
   protected int get_resource_count()
   {
      return -1;
   }

   protected void set_resource_count(int n)
   {
   }

   protected int get_resource_limit()
   {
      return -1;
   }

   protected int get_resource_distance()
   {
      return -1;
   }
}
