public class Grid
{
 /*  private final int EMPTY = 0;
   private final int GATHERER = 1;
   private final int GENERATOR = 2;
   private final int RESOURCE = 3;*/
   
   private int width;
   private int height;
   private WorldObject[][] cells;

   public Grid(int width, int height, WorldObject occupancy_value)
   {
      this.width = width;
      this.height = height;
      this.cells = new WorldObject[height][width];;

      for (int row = 0; row < this.height; row++)
      {
         for (int col = 0; col < this.width; col++)
         {
            this.cells[row][col] = occupancy_value;
         }
      }
   }

   public void set_cell(Point point, WorldObject value)
   {
      this.cells[point.yCoordinate()][point.xCoordinate()] = value;
   }

   public WorldObject get_cell(Point point)
   {
      return (this.cells[point.yCoordinate()][point.xCoordinate()]);
   }
}
