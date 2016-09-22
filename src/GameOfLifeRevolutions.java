import javax.swing.*;

public class GameOfLifeRevolutions {

	private EnvironmentCell environment;
	
	public GameOfLifeRevolutions(int width, int heigth){
		environment = new EnvironmentCell(width, heigth);
		loadPopulatedCells();
		//play();
	}
	
	public void loadPopulatedCells(){
		environment.getCell(10,10).setState(true);
		environment.getCell(10,11).setState(true);
		environment.getCell(10,12).setState(true);
		environment.getCell(10,13).setState(true);
		environment.getCell(10,14).setState(true);
		environment.getCell(10,15).setState(true);
		environment.getCell(10,16).setState(true);
		environment.getCell(10,17).setState(true);
		environment.getCell(10,18).setState(true);
		environment.getCell(10,19).setState(true);
		//environment.printEnvironment();
	}
	
	public void play(){
		EnvironmentCell environmentTemp;
		environmentTemp = new EnvironmentCell(environment.getWidth(), environment.getHeigth());
		for(int i=0; i<environment.getWidth(); i++){
			for(int j=0; j<environment.getHeigth(); j++){
				if(environment.getCell(i,j).getState()){
					if(environment.checkNeighborsForPopulatedCell(i,j) == true){
						environmentTemp.getCell(i,j).setState(true);
					}
					else{
						environmentTemp.getCell(i,j).setState(false);
					}
				}
				else{
					if(environment.checkNeighborsForEmptyCell(i,j)){
						environmentTemp.getCell(i,j).setState(true);
					}
				}
			}
		}
		environment = environmentTemp;
		//environment.printEnvironment();
	}
	
	public EnvironmentCell getEnvironmentCell(){
		return environment;
	}

	public static void main(String arg[]){
		new GameOfLifeRevolutions(30, 30);
	}
}