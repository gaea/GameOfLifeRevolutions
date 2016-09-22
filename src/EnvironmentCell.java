public class EnvironmentCell {

	private Cell environment[][];
	private int width;
	private int heigth;

	public EnvironmentCell(int width, int heigth){
		this.width = width;
		this.heigth = heigth;
		environment = new Cell [width][heigth];

		for(int i=0; i<width; i++){
			for(int j=0; j<width; j++){
				environment[i][j] = new Cell(false);
			}
		}

		//printEnvironment();
	}

    public boolean checkNeighborsForPopulatedCell(int w, int h){ //  if cell survives return true, if die false
    	int neighbors = checkNeighbors(w,h);

    	if(neighbors == 2 || neighbors == 3){
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    public boolean checkNeighborsForEmptyCell(int w, int h){ //  if cell survives return true, if die false
    	int neighbors = checkNeighbors(w,h);

    	if(neighbors ==  3){
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    public int checkNeighbors(int w, int h){
    	int neighbors = 0;
    	for(int i=w-1; i<w+2; i++){
    		for(int j=h-1; j<h+2; j++){
    			if(i>=0 && i<width && j>=0 && j<heigth){
    				if(getCell(i,j).getState() == true){
    					if(!(i==w && j==h)){
    						neighbors++;
    					}
    				}
    			}
    		}
    	}
    	return neighbors;
    }

    public Cell[][] getEnvironment(){
    	return environment;
    }
    
    public Cell getCell(int width, int heigth){
    	return environment[width][heigth];
    }
    
    public void setCell(Cell cell, int width, int heigth){
    	environment[width][heigth] = cell;
    }
    
    public void setEnvironment(Cell environment[][]){
    	this.environment = environment;
    }
    
    public int getWidth(){
    	return width;
    }
    
    public void setWidth(int width){
    	this.width = width;
    }
    
    public int getHeigth(){
    	return heigth;
    }
    
    public void setHeigth(int heigth){
    	this.heigth = heigth;
    }
    
    public void printEnvironment(){

    	for(int i=0; i<width; i++){
    		System.out.print("| ");
    		for(int j=0; j<width; j++){
    			if(environment[i][j].getState() == true){
    				System.out.print(" 1 ");
    			}
    			else{
    				System.out.print(" 0 ");
    			}
    		}
    		System.out.println(" |");
    	}
    	System.out.println("----------------------------------------------------------------------------------------------");
    }
}