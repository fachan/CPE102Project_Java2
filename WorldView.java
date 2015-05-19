import java.awt.Rectangle;
import processing.core.*;


public class WorldView
{  
   private Rectangle viewport;
   private WorldModel world;
   private int tile_width;
   private int tile_height;
   private int num_rows;
   private int num_cols;
   private PApplet screen;
   
   
   public WorldView(int view_cols, int view_rows, WorldModel world,
      int tile_width, int tile_height, PApplet screen)
   {
      this.viewport = new Rectangle(0, 0, view_cols, view_rows);
      this.screen = screen;
      this.world = world;
      this.tile_width = tile_width;
      this.tile_height = tile_height;
      this.num_rows = world.get_rows();
      this.num_cols = world.get_cols();
   }
   
   
   private boolean view_contains(Point pt)
   {
      return (pt.xCoordinate() >= this.viewport.x &&
         pt.xCoordinate() < (this.viewport.x + this.viewport.width) &&
         pt.yCoordinate() >= this.viewport.y && 
         pt.yCoordinate() < (this.viewport.y + this.viewport.height));
   }   
   
   
   public void draw_background()
   {   
      for (int y = 0; y < this.viewport.height; y++)
      {
         for (int x = 0; x < this.viewport.width; x++)
         {
            Point w_pt = this.viewport_to_world(new Point(x, y));
            PImage img = this.world.get_background_image(w_pt);
            screen.image(img, x * this.tile_width, y * this.tile_height);
         }
      }
   }
   
   
   private void draw_entities()
   {
      for (WorldObject entity : this.world.get_entities())
      {
         if (this.view_contains(new Point(entity.get_position().xCoordinate(),
            entity.get_position().yCoordinate())))
         {
            Point v_pt = this.world_to_viewport(entity.get_position());
            screen.image(entity.get_image(), v_pt.xCoordinate() * this.tile_width, 
               v_pt.yCoordinate() * this.tile_height);
         }
      }
   }
   
   private void draw_viewport()
   {
      this.draw_background();
      this.draw_entities();
   }
   
 
   public Point viewport_to_world(Point pt)
   {
      return new Point(pt.xCoordinate() + this.viewport.x,
         pt.yCoordinate() + this.viewport.y);
   }
   

   private Point world_to_viewport(Point pt)
   {
      return new Point(pt.xCoordinate() - this.viewport.x,
         pt.yCoordinate() - this.viewport.y);
   }
   
   
   private Rectangle create_shifted_viewport(int [] delta, int num_rows, 
      int num_cols)
   {
      int new_x = clamp(this.viewport.x + delta[0], 0,
         num_cols - this.viewport.width);
      int new_y = clamp(this.viewport.y + delta[1], 0, 
         num_rows - this.viewport.height);
         
      return new Rectangle(new_x, new_y, this.viewport.width, 
         this.viewport.height);
   }
   
   
   public void update_view(int [] view_delta)
   {
      this.viewport = this.create_shifted_viewport(view_delta,
         this.num_rows, this.num_cols);
     
      this.draw_viewport();
      screen.updatePixels();
   }

   
   private static int clamp(int v, int low, int high)
   {
      return Math.min(high, Math.max(v, low));
   }
}