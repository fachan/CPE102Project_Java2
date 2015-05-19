public class Point
{
   private final int x;
   private final int y;

   public Point(int x_coord, int y_coord)
   {
      this.x = x_coord;
      this.y = y_coord;
   }

   public int xCoordinate()
   {
      return this.x;
   }

   public int yCoordinate()
   {
      return this.y;
   }

   public int distance_sq(Point that)
   {
      int dx = this.x - that.x;
      int dy = this.y - that.y;
      return (dx * dx) + (dy * dy);
   }

   public boolean adjacent(Point that)
   {
      return ((this.x == that.x && Math.abs(this.y - that.y) == 1) ||
         (this.y == that.y && Math.abs(this.x - that.x) == 1));
   } 
}
