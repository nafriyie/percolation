/**
 * 
 * Percolation
 * 
 * @author Ana Paula Centeno
 * @author Haolin (Daniel) Jin
 */

public class Percolation {

	private boolean[][] grid;          // gridSize by gridSize grid of sites; 
	                                   // true = open site, false = closed or blocked site
	private WeightedQuickUnionFind wquFind; // name of object
	private int 		gridSize;      // gridSize by gridSize is the size of the grid/system 
	private int         gridSquared;
	private int         virtualTop;    // virtual top    index on WeightedQuckUnionFind arrays
	private int         virtualBottom; // virtual bottom index on WeightedQuckUnionFind arrays

	/**
	* Constructor.
	* Initializes all instance variables
	*/
	public Percolation ( int n ){
		gridSize 	  = n;
		gridSquared   = gridSize * gridSize;
		wquFind       = new WeightedQuickUnionFind(gridSquared + 2);
		grid          = new boolean[gridSize][gridSize];   // every site is initialized to closed/blocked
		virtualTop    = gridSquared;
		virtualBottom = gridSquared + 1;
	} 

	/**
	* Getter method for GridSize 
	* @return integer representing the size of the grid.
	*/
	public int getGridSize () {
		return gridSize;
	}

	/**
	 * Returns the grid array
	 * @return grid array
	 */
	public boolean[][] getGridArray () {
		return grid;
	}

	/**
	* Open the site at postion (x,y) on the grid to true and add an edge
	* to any open neighbor (left, right, top, bottom) and/or top/bottom virtual sites
	* Note: diagonal sites are not neighbors
	*
	* @param row grid row
	* @param col grid column
	* @return void
	*/
	public void openSite (int row, int col) {

		// WRITE YOUR CODE HERE

		int index = (gridSize * row) + col;
		if ( grid[row][col] == false ){ // checks if site is closed
			grid[row][col] = true; // set site to open
		}
			if ( row == 0 ) { // checks if opened site is in top row ** should it just be row instead of grid[row]
				if( grid[row][col] == true ) {
					// use union find to connect site with virtual top
					wquFind.union(index, virtualTop); // col is wrong, should be the index number

					//** shouldn't be (x,y) other known as (row,col)--should be (x,x) or union(index,index)
				}
			}
			if ( row == gridSize - 1 ) { // checks if opened site is in the bottom row
				if (grid[row][col] == true) {
					// use union find to connect site with virtual bottom
				wquFind.union(index, virtualBottom); // col is wrong, should be the index number
				}
				
			}

			// connect to adjacent grids - recheck this part of method, I think it causes a problem. Try to draw a diagram for it
			if ( col < gridSize - 1) { // right
				if (grid[row][col + 1] == true){ // if statement added to check if adjacent site is connected
					wquFind.union(index + 1,index);
				}
			}
			if ( col > 0 ) { // left
				if(grid[row][col - 1] == true) { // if statement added to check if adjacent site is connected
					wquFind.union(index - 1, index);
				}
			}
			if ( row < gridSize - 1 ) { // bottom
				if (grid[row + 1][col] == true) { // if statement added to check if adjacent site is connected
					wquFind.union(index + gridSize,index);
				}
			}
			if ( row > 0 ) { // top
				if (grid[row - 1][col] == true) { // if statement added to check if adjacent site is connected
					wquFind.union(index - gridSize,index);
				}
			}

		 else {
			return;
		}

		return;  
	}

	/**
	* Check if the system percolates (any top and bottom sites are connected by open sites)
	+
	* @return true if system percolates, false otherwise
	*/
	public boolean percolationCheck () {
		// WRITE YOUR CODE HERE
		System.out.println(wquFind.find(virtualTop) + " " + wquFind.find(virtualBottom));
		if (wquFind.find(virtualBottom) == wquFind.find(virtualTop)) { // is something about this wrong?
			return true;
		} else {
			return false;
		}
		 // update this line, it is only here so the code compiles
	}

	/**
	 * Iterates over the grid array openning every site. 
	 * Starts at [0][0] and moves row wise 
	 * @param probability
	 * @param seed
	 */
	public void openAllSites (double probability, long seed) {

		// Setting the same seed before generating random numbers ensure that
		// the same numbers are generated between different runs
		StdRandom.setSeed(seed); // DO NOT remove this line

		// WRITE YOUR CODE HERE, DO NOT remove the line above

			for (int i = 0; i < gridSize; i++) {
				for (int j = 0; j < gridSize; j++) {
					if (StdRandom.uniform() <= probability) { // StdRandom.union
						openSite(i,j);
					}
				}
			}
		
		
	} 

	/**
	* Open up a new window and display the current grid using StdDraw library.
	* The output will be colored based on the grid array. Blue for open site, black for closed site.
	* @return: void 
	*/
	public void displayGrid () {
		double blockSize = 0.9 / gridSize;
		double zeroPt =  0.05+(blockSize/2), x = zeroPt, y = zeroPt;

		for ( int i = gridSize-1; i >= 0; i-- ) {
			x = zeroPt;
			for ( int j = 0; j < gridSize; j++) {
				if ( grid[i][j] ) {
					StdDraw.setPenColor( StdDraw.BOOK_LIGHT_BLUE );
					StdDraw.filledSquare( x, y ,blockSize/2);
					StdDraw.setPenColor( StdDraw.BLACK);
					StdDraw.square( x, y ,blockSize/2);		
				} else {
					StdDraw.filledSquare( x, y ,blockSize/2);
				}
				x += blockSize; 
			}
			y += blockSize;
		}
	}

	/**
	* Main method, for testing only, feel free to change it.
	*/
	public static void main ( String[] args ) {

		double p = 0.47; //og = p = 0.47
		Percolation pl = new Percolation(5);

		/* 
		 * Setting a seed before generating random numbers ensure that
		 * the same numbers are generated between runs.
		 *
		 * If you would like to reproduce Autolab's output, update
		 * the seed variable to the value Autolab has used.
		 */
		long seed = System.currentTimeMillis();
		pl.openAllSites(p, seed);

		System.out.println("The system percolates: " + pl.percolationCheck());
		pl.displayGrid();
	}
}