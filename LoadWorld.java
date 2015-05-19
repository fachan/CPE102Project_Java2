import java.io.*;
import java.util.Scanner;

import processing.core.*;

public class LoadWorld extends PApplet
{
   private static final int MIN_ARGS = 3;
   private static final int WORLD_IDX = 2;
   
   private static final int TILE_WIDTH = 32;
   private static final int TILE_HEIGHT = 32;
   
   private static final int PROPERTY_KEY = 0;
   
   private static final String BGND_KEY = "background";
   private static final int BGND_NUM_PROPERTIES = 4;
   private static final int BGND_NAME = 1;
   private static final int BGND_COL = 2;
   private static final int BGND_ROW = 3;

   private static final String MINER_KEY = "miner";
   private static final int MINER_NUM_PROPERTIES = 7;
   private static final int MINER_NAME = 1;
   private static final int MINER_LIMIT = 4;
   private static final int MINER_COL = 2;
   private static final int MINER_ROW = 3;
   private static final int MINER_RATE = 5;
   private static final int MINER_ANIMATION_RATE = 6;

   private static final String OBSTACLE_KEY = "obstacle";
   private static final int OBSTACLE_NUM_PROPERTIES = 4;
   private static final int OBSTACLE_NAME = 1;
   private static final int OBSTACLE_COL = 2;
   private static final int OBSTACLE_ROW = 3;

   private static final String ORE_KEY = "ore";
   private static final int ORE_NUM_PROPERTIES = 5;
   private static final int ORE_NAME = 1;
   private static final int ORE_COL = 2;
   private static final int ORE_ROW = 3;
   private static final int ORE_RATE = 4;

   private static final String SMITH_KEY = "blacksmith";
   private static final int SMITH_NUM_PROPERTIES = 7;
   private static final int SMITH_NAME = 1;
   private static final int SMITH_COL = 2;
   private static final int SMITH_ROW = 3;
   private static final int SMITH_LIMIT = 4;
   private static final int SMITH_RATE = 5;
   private static final int SMITH_REACH = 6;

   private static final String VEIN_KEY = "vein";
   private static final int VEIN_NUM_PROPERTIES = 6;
   private static final int VEIN_NAME = 1;
   private static final int VEIN_RATE = 4;
   private static final int VEIN_COL = 2;
   private static final int VEIN_ROW = 3;
   private static final int VEIN_REACH = 5;

   
   private static boolean checkArgs(String [] args)
   {
      return (args.length >= MIN_ARGS);
   }
   
   
   private void process_file(Scanner in, WorldModel world, 
      ImageList images, boolean run)
   {
      while (in.hasNextLine())
      {
         String [] properties = in.nextLine().split("\\s");
         if (properties != null)
         {
            if (properties[PROPERTY_KEY].equals(BGND_KEY))
            {
               add_background(world, properties, images);
            }
         
            else
            {
               add_entity(world, properties, images, run);
            }
         }
      }
   }


   public void load_world(String [] args, WorldModel world, ImageList images, 
      boolean run)
   {
      try
      {
         if (checkArgs(args))
         {
            Scanner in = new Scanner(new FileInputStream(args[WORLD_IDX]));
            process_file(in, world, images, run);
         }

         else
         {
            System.err.println("File(s) missing");
         }
      }

      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }
   
   
   public void add_background(WorldModel world, String [] properties,
         ImageList i_store)
      {
         ImageStore images_store = new ImageStore();

         if (properties.length >= BGND_NUM_PROPERTIES)
         {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]), 
               Integer.parseInt(properties[BGND_ROW]));
            String name = properties[BGND_NAME];
            
            world.set_background(pt, new Background(name,
               images_store.get_images(i_store, name, TILE_WIDTH, TILE_HEIGHT)));
         }
      }
      
      
   public void add_entity(WorldModel world, String[] properties, 
      ImageList i_store, boolean run)
   {
      WorldObject new_entity = create_from_properties(properties, i_store);
      if (new_entity != null)
      {
         world.add_entity(new_entity);
         if (run)
         {
            new_entity.schedule_entity(world, i_store);
         }
      }
   }


   private WorldObject create_from_properties(String[] properties, ImageList i_store)
   {
      String key = properties[PROPERTY_KEY];

      if (properties != null)
      {
         if (key.equals(MINER_KEY))
         {
            return create_miner(properties, i_store);
         }

         else if (key.equals(VEIN_KEY))
         {
            return create_vein(properties, i_store);
         }

         else if (key.equals(ORE_KEY))
         {
            return create_ore(properties, i_store);
         }

         else if (key.equals(SMITH_KEY))
         {
            return create_blacksmith(properties, i_store);
         }

         else if (key.equals(OBSTACLE_KEY))
         {
            return create_obstacle(properties, i_store);
         }
      }

      return null;
   }


   private MovingEntity create_miner(String[] properties, 
         ImageList i_store)
   {
      ImageStore image_store = new ImageStore();

      if (properties.length == MINER_NUM_PROPERTIES)
      {
         Miner miner = new MinerNotFull(properties[MINER_NAME],
            Integer.parseInt(properties[MINER_LIMIT]),
            new Point(Integer.parseInt(properties[MINER_COL]), 
            Integer.parseInt(properties[MINER_ROW])),
            Integer.parseInt(properties[MINER_RATE]),
            image_store.get_images(i_store, properties[PROPERTY_KEY],
            TILE_WIDTH, TILE_HEIGHT),
            Integer.parseInt(properties[MINER_ANIMATION_RATE]));
         return miner;
      }

      else
      {
         return null;
      }
   }


   private StaticEntity create_vein(String[] properties, ImageList i_store)
   {
      ImageStore image_store = new ImageStore();

      if (properties.length == VEIN_NUM_PROPERTIES)
      {
         StaticEntity vein = new Vein(properties[VEIN_NAME], 
            Integer.parseInt(properties[VEIN_RATE]),
            new Point(Integer.parseInt(properties[VEIN_COL]), 
            Integer.parseInt(properties[VEIN_ROW])),
            image_store.get_images(i_store, properties[PROPERTY_KEY],
            TILE_WIDTH, TILE_HEIGHT),
            Integer.parseInt(properties[VEIN_REACH]));
         return vein;
      }

      else
      {
         return null;
      }
   }


   private StaticEntity create_ore(String[] properties, ImageList i_store)
   {
      ImageStore image_store = new ImageStore();

      if (properties.length == ORE_NUM_PROPERTIES)
      {
         StaticEntity ore = new Ore(properties[ORE_NAME],
            image_store.get_images(i_store, properties[PROPERTY_KEY],
            TILE_WIDTH, TILE_HEIGHT),
            new Point(Integer.parseInt(properties[ORE_COL]), 
            Integer.parseInt(properties[ORE_ROW])),
            Integer.parseInt(properties[ORE_RATE]));
         return ore;
      }

      else
      {
         return null;
      }
   }


   private Individual create_blacksmith(String[] properties, ImageList i_store)
   {
      ImageStore image_store = new ImageStore();

      if (properties.length == SMITH_NUM_PROPERTIES)
      {
         Individual smith = new Blacksmith(properties[SMITH_NAME],
            new Point(Integer.parseInt(properties[SMITH_COL]), 
            Integer.parseInt(properties[SMITH_ROW])),
            image_store.get_images(i_store, properties[PROPERTY_KEY],
            TILE_WIDTH, TILE_HEIGHT),
            Integer.parseInt(properties[SMITH_LIMIT]), 
            Integer.parseInt(properties[SMITH_RATE]),
            Integer.parseInt(properties[SMITH_REACH]));
         return smith;
      }

      else
      {
         return null;
      }
   }


   private WorldObject create_obstacle(String[] properties, ImageList i_store)
   {
      ImageStore image_store = new ImageStore();

      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         return new Obstacle(properties[OBSTACLE_NAME],
            new Point(Integer.parseInt(properties[OBSTACLE_COL]), 
            Integer.parseInt(properties[OBSTACLE_ROW])),
            image_store.get_images(i_store, properties[PROPERTY_KEY],
            TILE_WIDTH, TILE_HEIGHT));
      }

      else
      {
         return null;
      }
   }   
}