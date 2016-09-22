public class Cell {

    private boolean state; //true is populated, false is empty
    
    public Cell(boolean state){
       this.state = state;
   }
   
   public void changeState(boolean state){
       this.state = state;
   }
   
   public boolean getState(){
       return state;
   }
   
   public void setState(boolean state){
       this.state = state;
   }
}