import java.util.List;
import java.util.LinkedList;

public class Pair
{
   private List<Point> move_entity = new LinkedList<Point>();
   private boolean move;
   
   public Pair(List<Point> move_entity, boolean move)
   {
      this.move_entity = move_entity;
      this.move = move;
   }
   
   public List<Point> get_points()
   {
      return this.move_entity;
   }
   
   public boolean move()
   {
      return this.move;
   }
}