import java.util.function.LongConsumer;

public class ListItem
{
   private LongConsumer item;
   private long ord;
   
   public ListItem(LongConsumer item, long ord)
   {
      this.item = item;
      this.ord = ord;
   }
   
   public LongConsumer get_item()
   {
      return this.item;
   }
   
   public long get_ord()
   {
      return this.ord;
   }
}