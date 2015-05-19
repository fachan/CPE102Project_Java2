import java.util.List;
import java.util.LinkedList;
import java.util.function.LongConsumer;

import processing.core.*;


public class WorldModel
{
   private Grid background;
   private int num_rows;
   private int num_cols;
   private Grid occupancy;
   private List<WorldObject> entities;
   private OrderedList action_queue;
   PApplet screen;
   

   public WorldModel(int num_rows, int num_cols, WorldObject background)
   {
      this.background = new Grid(num_cols, num_rows, background);
      this.num_rows = num_rows;
      this.num_cols = num_cols;
      this.occupancy = new Grid(num_cols, num_rows, null);
      this.entities = new LinkedList<WorldObject>();
      this.action_queue = new OrderedList();
   }

   
   public int get_rows()
   {
      return this.num_rows;
   }
   
   
   public int get_cols()
   {
      return this.num_cols;
   }
   
   
   public PImage get_background_image(Point pt)
   {
      if (this.within_bounds(pt))
      {
         WorldObject entity = this.background.get_cell(pt);
         return entity.get_image();
      }
      
      return null;
   }


   public WorldObject get_background(Point pt)
   {
      if (this.within_bounds(pt))
      {
         return this.background.get_cell(pt);
      }

      return null;
   }

   
   public void set_background(Point pt, WorldObject bgnd)
   {
      if (this.within_bounds(pt))
      {
         this.background.set_cell(pt, bgnd);
      }
   }

   
   public boolean is_occupied(Point pt)
   {
      return (this.within_bounds(pt) && 
         (this.occupancy.get_cell(pt) != null));
   }

  
   public WorldObject get_tile_occupant(Point pt)
   {
      if (this.within_bounds(pt))
      {
         return this.occupancy.get_cell(pt);
      }

      return null;
   }


   public boolean within_bounds(Point pt)
   {
      int x = pt.xCoordinate();
      int y = pt.yCoordinate();

      return (x >= 0 && x < this.num_cols && y >= 0 && y < this.num_rows);
   }

  
   public List<WorldObject> get_entities()
   {
      return this.entities;
   }

   
   public void add_entity(WorldObject entity)
   {
      Point pt = entity.get_position();

      if (this.within_bounds(pt))
      {
         WorldObject old_entity = this.occupancy.get_cell(pt);

         if (old_entity != null && (old_entity instanceof Entity || 
            old_entity instanceof Obstacle))
         {
           	if (old_entity instanceof Entity)
        	   {
               Entity old = (Entity) old_entity;
               old.clear_pending_actions();
           	}
        	
           	else
        	   {
        		   Obstacle old = (Obstacle) old_entity;
            }
         } 
           
         this.occupancy.set_cell(pt, entity);
         this.entities.add(entity);
      }
   } 


   public List<Point> move_entity(WorldObject entity, Point pt)
   {
      List<Point> tiles = new LinkedList<Point>();

      if (this.within_bounds(pt))
      {
         Point old_pt = entity.get_position();
         this.occupancy.set_cell(old_pt, null);
         tiles.add(old_pt);
         this.occupancy.set_cell(pt, entity);
         tiles.add(pt);
         entity.set_position(pt);
      }

      return tiles;
   }

  
   public void remove_entity(WorldObject entity)
   {
      this.remove_entity_at(entity.get_position());
   }

   
   public void remove_entity_at(Point pt)
   {
      if (this.within_bounds(pt) &&
         this.occupancy.get_cell(pt) != null)
      {
         WorldObject entity = this.occupancy.get_cell(pt);
         entity.set_position(new Point(-1, -1));
         this.entities.remove(entity);
         this.occupancy.set_cell(pt, null);
      }
   }

   public WorldObject find_nearest(Point pt, Class type)
   {
      List<WorldObject> entities = new LinkedList<WorldObject>();
      List<Integer> distances = new LinkedList<Integer>();
  
      for (WorldObject e : this.entities)
      {
         if (type.isInstance(e))
         {
            entities.add(e);
            distances.add(pt.distance_sq(e.get_position()));
         }
      }

      return nearest_entity(entities, distances);
   }
   
   
   public void schedule_action(LongConsumer action, long time)
   {
      this.action_queue.insert(action, time);
   }
   

   public void unschedule_action(LongConsumer action)
   {
      this.action_queue.remove(action);
   }
   
   
   public void update_on_time(long ticks)
   {
      ListItem next = this.action_queue.head();

      while (next != null && next.get_ord() < ticks)
      {
         this.action_queue.pop();
         next.get_item().accept(ticks);  // invoke action function
         next = this.action_queue.head();
      }
   }
   
   
   public void clear_pending_actions(Entity entity)
   {
      for (LongConsumer action : entity.get_pending_actions())
      {
         this.unschedule_action(action);
      }
      
      entity.clear_pending_actions();
   }
   
   //originally a separate function
   private static WorldObject nearest_entity(List<WorldObject> entities,
      List<Integer> dists)
   {
      WorldObject nearest = null;

      if (dists.size() > 0)
      {
         WorldObject current_entity = entities.get(0);
         Integer current_dist = dists.get(0);

         for (int i = 0; i < entities.size(); i++)
         {
            if (dists.get(i) < current_dist)
            {
               current_entity = entities.get(i);
               current_dist = dists.get(i);
            } 
         }

         nearest = current_entity;
      }

      return nearest;
   }
}

