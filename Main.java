import java.util.List;
import java.util.Random;

import processing.core.*;


public class Main extends PApplet
{
   private boolean RUN_AFTER_LOAD = true;
   
   private ImageList i_store;
   private List<PImage> background_img;
   private LoadWorld full_world;
   private WorldModel world;
   private WorldView view;
   private Background default_background;
   private int num_cols;
   private int num_rows;

   private String DEFAULT_IMAGE_NAME = "background_default";
   
   private int WORLD_WIDTH_SCALE = 2;
   private int WORLD_HEIGHT_SCALE = 2;

   private int SCREEN_WIDTH = 640;
   private int SCREEN_HEIGHT = 480;
   private static int TILE_WIDTH = 32;
   private static int TILE_HEIGHT = 32;
   
   private String file = "Main";
   private String image_file = "imagelist";
   private String entity_file = "gaia.sav";

   private static int BLOB_ANIMATION_RATE_SCALE = 50;
   private static int BLOB_ANIMATION_MIN = 1;
   private static int BLOB_ANIMATION_MAX = 3;

   private static int ORE_CORRUPT_MIN = 20000;
   private static int ORE_CORRUPT_MAX = 30000;

   private static int QUAKE_ANIMATION_RATE = 100;

   //private int VEIN_SPAWN_DELAY = 500;
   private static int VEIN_RATE_MIN = 8000;
   private static int VEIN_RATE_MAX = 17000;

   private static ImageStore image_store = new ImageStore();
   private boolean left;
   private boolean right;
   private boolean up;
   private boolean down;
 
   
   private static Random random = new Random();
   private static int random_blob = random.nextInt((BLOB_ANIMATION_MAX - 
      BLOB_ANIMATION_MIN + 1) + BLOB_ANIMATION_MIN) * 
      BLOB_ANIMATION_RATE_SCALE;
   private static int random_ore = random.nextInt((ORE_CORRUPT_MAX - 
      ORE_CORRUPT_MIN + 1) + ORE_CORRUPT_MIN);
   private static int random_quake = random.nextInt((VEIN_RATE_MAX -
      VEIN_RATE_MIN + 1) + VEIN_RATE_MIN);
   

   public static void main(String[] args)
   {
      PApplet.main("Main");
   }

   public Background create_default_background(List<PImage> img)
   {
      return new Background(DEFAULT_IMAGE_NAME, img);
   }
   
   public void setup()
   {
      size(SCREEN_WIDTH, SCREEN_HEIGHT);
      
      num_cols = SCREEN_WIDTH / TILE_WIDTH * WORLD_WIDTH_SCALE;
      num_rows = SCREEN_HEIGHT / TILE_HEIGHT * WORLD_HEIGHT_SCALE;
      
      
      i_store = image_store.load_images(new String[] {file, image_file, 
         entity_file}, TILE_WIDTH, TILE_HEIGHT);
      
      background_img = image_store.get_images(i_store, DEFAULT_IMAGE_NAME, 
         TILE_WIDTH, TILE_HEIGHT);

      default_background = create_default_background(background_img);
      
      world = new WorldModel(num_rows, num_cols, default_background);

      view = new WorldView(20, 15, world, TILE_WIDTH, TILE_HEIGHT, this);
      
      full_world = new LoadWorld();
      full_world.load_world(new String[]{file, image_file, entity_file},
         world, i_store, RUN_AFTER_LOAD);
   }

   private long next_time = 0;
   public void draw()
   {     
      long time = System.currentTimeMillis();

      view.update_view(shift());
      if (time > next_time)
      {  
         world.update_on_time(time);
         next_time += 100;
      }
   }
   
   
   public int [] shift()
   {
      int [] delta = new int[2];
      
      if (left)
      {
         left = false;
         delta[0] = -1;
         delta[1] = 0;
         return delta;
      }
      
      else if (right)
      {
         right = false;
         delta[0] = 1;
         delta[1] = 0;
         return delta;
      }
      
      else if (up)
      {
         up = false;
         delta[0] = 0;
         delta[1] = -1;
         return delta;
      }
      
      else if (down)
      {
         down = false;
         delta[0] = 0;
         delta[1] = 1;
         return delta;
      }
      
      delta[0] = 0;
      delta[1] = 0;
      return delta;
   }
   
   
   public void keyPressed()
   {  
      if (key == CODED)
      {
         if (keyCode == LEFT)
         {
            left = true;
         }
         
         else if (keyCode == RIGHT)
         {
            right = true;
         }
         
         else if (keyCode == UP)
         {
            up = true;
         }
         
         else if (keyCode == DOWN)
         {
            down = true;
         }
      }
   }
   
   public static Entity create_blob(WorldModel world, String name, Point pt, int rate, 
         long ticks, ImageList i_store)
   {
      Entity blob = new OreBlob(name, pt, rate, 
         image_store.get_images(i_store, "blob", TILE_WIDTH, TILE_HEIGHT),
         random_blob);
      blob.schedule_self(world, ticks, i_store);
      
      return blob;
   }
   
   
   public static Entity create_ore(WorldModel world, String name, Point pt, long ticks, 
      ImageList i_store)
   {
      Entity ore = new Ore(name, image_store.get_images(i_store, "ore",
         TILE_WIDTH, TILE_HEIGHT), pt, random_ore);
      ore.schedule_self(world, ticks, i_store);

      return ore;
   }


   public static Entity create_quake(WorldModel world, Point pt, long ticks, 
      ImageList i_store)
   {
      Entity quake = new Quake("quake", pt, image_store.get_images(i_store, 
         "quake", TILE_WIDTH, TILE_HEIGHT), QUAKE_ANIMATION_RATE);
      quake.schedule_self(world, ticks, i_store);
      
      return quake;
   }

   
   public static Entity create_vein(WorldModel world, String name, Point pt, 
      long ticks, ImageList i_store)
   {
      Entity vein = new Vein("vein" + name, random_quake, pt, 
         image_store.get_images(i_store, "vein", TILE_WIDTH, TILE_HEIGHT), 1);
      
      return vein;
   }
}
