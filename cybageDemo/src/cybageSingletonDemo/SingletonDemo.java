package cybageSingletonDemo;

public class SingletonDemo {
	
	int x;

	static SingletonDemo ref = null;
	
	/*@Override
	public String toString() {
		return "SingletonDemo [x=" + x + "]";
	}*/
	
	private SingletonDemo(){
		//this.x=x;
		
	}
	
	public static SingletonDemo getInstance(){
		if(ref==null){
			ref = new SingletonDemo();
		}
		return ref;
	}

}
