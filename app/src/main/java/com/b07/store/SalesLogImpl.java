package com.b07.store;

import android.content.Context;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.exceptions.BadInputException;
import com.b07.inventory.Item;

/**
 * The saleslog containing all sales in the store.
 */
public class SalesLogImpl implements SalesLog, Serializable {
  // initalize list of saleslog.
  private DatabaseSelectHelper selectHelper;
  private List<Sale> salesLog = new ArrayList<>();
  private Context context;

  public SalesLogImpl(Context context){
      this.context = context;
  }
  /**
   * add sale to the log
   * 
   * @param sale commited sale
   */
  public void addSale(Sale sale) {
    this.salesLog.add(sale);
  }

  /**
   * return the log to the user
   * 
   * @return list of all sales.
   */
  public List<Sale> getLog() {
    // Get a list of sales in the log given as a copy not the original.
    List<Sale> logCopy = new ArrayList<>();
    for (Sale sale : this.salesLog) {
      logCopy.add(sale);
    }
    return logCopy;
  }

    /**
     * Get The map storing the itemized information in the log.
     * @return HashMap<Item, Integer> map of all items in saleslog
     */
   public HashMap<Item, Integer> getItemizedMap(){
     HashMap<Item, Integer> map = new HashMap<>();
     // Fill the new hasmap with the nedded helper.
     try {
         selectHelper = new DatabaseSelectHelper(context);
         return  selectHelper.returnItemizedSales();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (BadInputException e) {
      e.printStackTrace();
    }
     return map;
   }

    /**
     *  Get the indexed item in the log.
     * @param i index of log
     * @return the index of teh item in the log
     */
  public Sale get(int i) {
    return salesLog.get(i);
  }
}
