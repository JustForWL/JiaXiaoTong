package com.example.jiaxiaotong.bar;

public class DataElement {  
    
    public DataElement(String name, int value, int color) {  
        this.itemName = name;  
        this.value = value;  
        this.color = color;  
    }  
    public String getItemName() {  
        return itemName;  
    }  
    public void setItemName(String itemName) {  
        this.itemName = itemName;  
    }  
    public int getValue() {  
        return value;  
    }  
    public void setValue(int value) {  
        this.value = value;  
    }  
      
    public void setColor(int color) {  
        this.color = color;  
    }  
      
    public int getColor() {  
        return this.color;  
    }  
      
    private String itemName;  
    private int color;  
    private int value;  
}  
