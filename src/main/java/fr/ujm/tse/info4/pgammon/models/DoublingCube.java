// 
//
//  @ Project : Project Gammon
//  @ File : DoublingCube.java
//  @ Date : 12/12/2012
//  @ Authors : DONG Chuan, BONNETTO Benjamin, FRANCON Adrien, POTHELUNE Jean-Michel
//
//

package fr.ujm.tse.info4.pgammon.models;

public class DoublingCube
{
    private int value;
    
    public DoublingCube()
    {
        value = 1;
    }
    
    public DoublingCube(int value)
    {
        this.value = value;
    }
    
    public int doubleValue()
    {
        value = value * 2;
        return value;
    }
    
    public int getValue()
    {
        return value;
    }
}
