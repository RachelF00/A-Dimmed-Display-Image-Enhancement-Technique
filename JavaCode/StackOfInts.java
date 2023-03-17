/* 
 * authors: Aditi Majumder and Sandy Irani
 * University of California, Irvine
 * Last update: 07/11/08
 */

// Stack of Ints 
public class StackOfInts
{
    int size;
    int[] data;
    final int INIT_SIZE = 100;

    
    //Constructor for objects of class StackOfShorts
    public StackOfInts()
    {
        size = 0;
        data = new int[ INIT_SIZE ];
    }

    //double the size of an array
    private void resize()
    {
        int[] newData = new int[ data.length * 2 ];
        for ( int i = 0; i < data.length; i++ )
            newData[i] = data[i];
        data = newData;
    }
    
    public boolean isEmpty()
    {
        return ( size==0 );
    }
    
    //pop from a stack
    public int pop()
    {
        if ( size > 0 )
        {
            size--;
            return data[size];
        }
        else
        {
            System.out.println("Popping from an empty stack.");
            return( -1 );
        }
    }
    
    //push data into a stack
    public void push( int newItem )
    {
        if ( size == data.length )
            resize();
        
        data[size] = newItem;
        size++;
        return;
    }
}
