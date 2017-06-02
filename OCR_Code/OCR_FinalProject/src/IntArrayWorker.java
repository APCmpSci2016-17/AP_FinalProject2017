/**
 * AP CmpSci 2016-2017
 * pixLab
 * Works with pixel arrays to edit images
 * Class to practice working with integer arrays
 * @author Connor Boham
 * @version August 20th, 2016
 **/
public class IntArrayWorker
{
  /** two dimensional matrix */
  private int[][] matrix = null;
  
  /** set the matrix to the passed one
    * @param theMatrix the one to use
    */
  public void setMatrix(int[][] theMatrix)
  {
    matrix = theMatrix;
  }
  
  /** find the number of times a passed integer is found in the array
   * @param value - the value of the passed integer
   * @return count - the number of times it appears in the array
   */
  public int getCount(int value){
	  int count = 0;
	  for(int i = 0; i < matrix.length; i++) {
		  for (int j = 0; j < matrix[0].length; j++){
			  if (matrix[i][j] == value){
				  count += 1;
			  }
		  }
	  }
	  return count;
  }
  /** find the largest value in the array
   * @return largest - the largest value
   */
  public int getLargest(){
	  int largest = 0;
	  for(int i = 0; i < matrix.length; i++) {
		  for (int j = 0; j < matrix[0].length; j++){
			  if (matrix[i][j] > largest){
				 largest = matrix[i][j];
			  }
		  }
	  }
	  return largest;
  }
  /** Method to return the sum of the values in a column
   * 
   * @param column - the passed specified column
   * @return sum - the total of the values in the specified column
   */
  public int getColTotal(int column){
	  int sum = 0;
	  for (int i = 0; i < matrix.length; i++){
		 sum += matrix[i][column];
	  }
	  return sum;
  }
  
  /**
   * Method to return the total 
   * @return the total of the values in the array
   */
  public int getTotal()
  {
    int total = 0;
    for (int row = 0; row < matrix.length; row++)
    {
      for (int col = 0; col < matrix[0].length; col++)
      {
        total = total + matrix[row][col];
      }
    }
    return total;
  }
  
  /**
   * Method to return the total using a nested for-each loop
   * @return the total of the values in the array
   */
  public int getTotalNested()
  {
    int total = 0;
    for (int[] rowArray : matrix)
    {
      for (int item : rowArray)
      {
        total = total + item;
      }
    }
    return total;
  }
  
  /**
   * Method to fill with an increasing count
   */
  public void fillCount()
  {
    int numCols = matrix[0].length;
    int count = 1;
    for (int row = 0; row < matrix.length; row++)
    {
      for (int col = 0; col < numCols; col++)
      {
        matrix[row][col] = count;
        count++;
      }
    }
  }
  
  /**
   * print the values in the array in rows and columns
   */
  public void print()
  {
    for (int row = 0; row < matrix.length; row++)
    {
      for (int col = 0; col < matrix[0].length; col++)
      {
        System.out.print( matrix[row][col] + " " );
      }
      System.out.println();
    }
    System.out.println();
  }
  
  
  /** 
   * fill the array with a pattern
   */
  public void fillPattern1()
  {
    for (int row = 0; row < matrix.length; row++)
    {
      for (int col = 0; col < matrix[0].length; 
           col++)
      {
        if (row < col)
          matrix[row][col] = 1;
        else if (row == col)
          matrix[row][col] = 2;
        else
          matrix[row][col] = 3;
      }
    }
  }
 
}