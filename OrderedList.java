import java.util.List;
import java.util.LinkedList;
import java.util.function.LongConsumer;


public class OrderedList
{
   private List<ListItem> list;
   
   public OrderedList()
   {
      this.list = new LinkedList<ListItem>();
   }

   public int get_size()
   {
      return this.list.size();
   }
   
   public void insert(LongConsumer item, long ord)
   {
      int size = this.list.size();
      int idx = 0;
      
      while (idx < size && this.list.get(idx).get_ord() < ord)
      {
         idx+= 1;
      }

      this.list.add(idx, new ListItem(item, ord));
   }
   
   
   public void remove(LongConsumer item)
   {
      int size = this.list.size();
      int idx = 0;
      
      while (idx < size && this.list.get(idx).get_item() != item)
      {
         idx++;
      }
      
      if (idx < size)
      {
         this.list.remove(idx);
      }
   }
   
   
   public ListItem head()
   {
      if (this.list != null)
      {
         return this.list.get(0);
      }
      
      else
      {
         return null;
      }
   }
   
   
   public ListItem pop()
   {
      if (this.list != null)
      {
         ListItem pop = list.get(0);
         this.list.remove(0);
         return pop;
      }
      
      else
      {
         return null;
      }
   }
}