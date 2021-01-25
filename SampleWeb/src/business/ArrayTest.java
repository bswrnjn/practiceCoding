package business;

public class ArrayTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int temp;
		int[] arr ;
		arr = new int[]{5,7,1,8};
		
		for(int i=0;i<arr.length-1;i++){
			System.out.println(arr[i]);
			if(arr[i]>arr[i+1]){
				temp=arr[i];
				System.out.println(temp);
			}
		}

	}

}
