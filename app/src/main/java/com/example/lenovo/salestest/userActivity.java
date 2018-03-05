package com.example.lenovo.salestest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.b07.database.helper.DatabaseInsertHelper;
import com.b07.database.helper.DatabaseSelectHelper;
import com.b07.database.helper.DatabaseUpdateHelper;
import com.b07.exceptions.BadInputException;
import com.b07.exceptions.DatabaseInsertException;
import com.b07.inventory.Inventory;
import com.b07.inventory.InventoryImpl;
import com.b07.inventory.Item;
import com.b07.store.Account;
import com.b07.store.ShoppingCart;
import com.b07.users.Customer;
import com.b07.users.User;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class userActivity extends AppCompatActivity {

    private int cusId;
    DatabaseSelectHelper dbSelector;
    DatabaseInsertHelper dbInsertor;
    DatabaseUpdateHelper dbUpdator;
    private Intent intent;
    private ShoppingCart cart;
    private Account account;
    private int accountCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        this.intent = getIntent();
        account = null;
        this.cusId = (int) intent.getSerializableExtra("cusId");

        this.dbSelector = new DatabaseSelectHelper(this);
        this.dbUpdator = new DatabaseUpdateHelper(this);
        this.dbInsertor = new DatabaseInsertHelper(this);

        TextView textAccounts = (TextView) findViewById(R.id.textAccounts);
        StringBuilder sb1 = new StringBuilder();
        List<Account> accountList = null;
        int accountCount = 0;
        Customer customer = null;
        try {
            accountList = dbSelector.getUserAccountsHelper(cusId);
            for (Account e : accountList) {
                if (e.getActive() == 1) {
                    accountCount += 1;
                    int accountId = e.getAccountId();
                    cart = e.getShoppingCart();
                    customer = (Customer) dbSelector.getUserDetailsHelper(cusId);
                    cart.setCustomer(customer);
                    cart.setActiveAccountId(accountId);
                    sb1.append("Account Id : " + accountId + "\n");
                    textAccounts.setText(sb1);
                    this.account = e;
                }
            }
            if (accountCount == 0) {
                textAccounts.setText("You don't have an active account right now. Please contact admin to create an account, or your shopping cart can not be saved.");
                customer = (Customer) this.dbSelector.getUserDetailsHelper(cusId);
                cart = new ShoppingCart(this, customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (BadInputException e) {
            e.printStackTrace();
        }
        //greet customer
        Toast.makeText(this, "Hi, " + customer.getName() + " " + "Welcome to MicroCart"
                , Toast.LENGTH_SHORT).show();
        displayItems();
    }

    protected void displayItems(){
        TextView text = (TextView) findViewById(R.id.textItems);
        List<Item> items = new ArrayList<Item>();
        try {
            items = dbSelector.getAllItemsHelper();
            StringBuilder sb = new StringBuilder();
            sb.append("The following items are in stock:"+ "\n");
            for (Item item : items) {
                int quantity = dbSelector.getInventoryQuantityHelper(item.getId());
                sb.append("Item: " + item.getName() + " Price: $" + item.getPrice() + " Quantity in Stock: " + quantity + "\n");
            }
            text.setText(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (BadInputException e) {
            e.printStackTrace();
        }
    }

    protected void viewMer(View view) {
        displayItems();
    }

    protected void goShoppingCart(View view) {
       showShoppingCart();
    }

    protected void showShoppingCart(){
        try {
            TextView text = (TextView) findViewById(R.id.textItems);
            List<Item> items = new ArrayList<Item>();
            items = cart.getItems();
            StringBuilder sb = new StringBuilder();
            for (Item item : items) {
                sb.append("Item: " + item.getName() + " Quantity: " + cart.getQuantity(item) + "\n");
            }
            BigDecimal total = cart.getTotal();
            BigDecimal discount = cart.getCustomer().getDiscount();
            sb.append("Your total is: " + total + "\n");
            if(total.compareTo(new BigDecimal(0))==1){
                sb.append("You have " + discount + " Discount" + "\n");
                total = total.subtract(discount);
                // total after disc is negative
                if(total.compareTo(new BigDecimal(0))==-1){
                    sb.append("Store credits are applied..." + "\n");
                    sb.append("Your total: " + new BigDecimal(0) + "\n");
                    // add to disc cause total is now negative
                    BigDecimal newDic = total.negate();
                    sb.append("Remaining Store credits: " + newDic  + " will be saved.");
                } else {
                    sb.append("Your total: " + total + "\n");
                    sb.append("Total with tax: " + total.multiply(cart.getTaxRate()).setScale(2,
                            BigDecimal.ROUND_HALF_EVEN) + "\n");
                }

            }

            text.setText(sb.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void addToCart(View view) {
        try {
        EditText nameInputField = (EditText) findViewById(R.id.inputBuyName);
        String name = nameInputField.getText().toString();
        EditText quantityInputField = (EditText) findViewById(R.id.inputBuyQ);
        int quantity =  Integer.parseInt(quantityInputField.getText().toString());
        cart.addItem(name, quantity);
        Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show();
        showShoppingCart();
        } catch (Exception e) {
            Toast.makeText(this, "Sorry it appears something went wrong, " +
                    "please try again with valid inputs", Toast.LENGTH_SHORT).show();
        }

    }

    protected void remove(View view) {
        try {
        EditText nameInputField = (EditText) findViewById(R.id.inputBuyName);
        String name = nameInputField.getText().toString();
        EditText quantityInputField = (EditText) findViewById(R.id.inputBuyQ);
        int quantity =  Integer.parseInt(quantityInputField.getText().toString());
        cart.removeItem(name, quantity);
        Toast.makeText(this, "Removed sucessfully", Toast.LENGTH_SHORT).show();
        showShoppingCart();
        } catch (Exception e) {
            Toast.makeText(this, "Sorry it appears something went wrong, " +
                    "please try again with valid inputs", Toast.LENGTH_SHORT).show();
        }

    }

    protected void checkOut(View view){
        try {
            String total = cart.getTotal().toString();
            if (cart.getTotal().compareTo(new BigDecimal("0")) == 0){
                throw new Exception();
            }
            BigDecimal discount = dbSelector.getDiscountHelper(cusId);
            boolean check = cart.checkOutCustomer(cart);
            System.out.println("##########################CHEK OUT ###############");
            System.out.println(check);
            if(check){
                if (discount.compareTo(new BigDecimal("0")) == 0) {
                    Toast.makeText(this, "You checked out with total: $" + total, Toast.LENGTH_LONG).show();
                } else {
                    if (discount.compareTo(new BigDecimal(total)) >= 0) {
                        Toast.makeText(this, "You checked out with total: $"+ total + " where $" + total + " was paid for with credit", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "You checked out with total: " + total + " where $" + discount.toString() + " was paid for with credit", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(this, "Insufficient items in inventory", Toast.LENGTH_LONG).show();
            }
            int accountId = cart.getActiveAccountId();
            if (check == true) {
                dbUpdator.updateAccountStatusHelper( accountId,false);
            }
            intent.setClassName("com.example.lenovo.salestest",
                    "com.example.lenovo.salestest.userActivity");
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Whoops, Something went wrong." +
                    "Please try again", Toast.LENGTH_LONG).show();
        }
    }

    protected void checkDiscount(View view){
        showCheckDiscount();
    }

    protected void showCheckDiscount(){
        try {
            BigDecimal discount = dbSelector.getDiscountHelper(cusId);
            TextView text = (TextView) findViewById(R.id.textItems);
            text.setText("Your Current Store Credit: $" + discount.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (BadInputException e) {
            e.printStackTrace();
        }
    }


        protected void logOut(View view) {
        if (account != null) {
            try {
                account.refreshCart();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (DatabaseInsertException e) {
                e.printStackTrace();
            } catch (BadInputException e) {
                e.printStackTrace();
            }
        }
        intent.setClassName("com.example.lenovo.salestest",
                "com.example.lenovo.salestest.LogInActivity");
        startActivity(intent);

    }
}
