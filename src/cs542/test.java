package cs542;



public class test {
	public static void main(String[] args) {
	byte[] value = new byte[1024*1024];
	for(int i=0;i<1024*1024;i++){
		value[i]=7;
	}
	valueStore.add(1,value);
	valueStore.add(3,value);
	byte[]data=valueStore.get(1);
	for(int i=0;i<100;i++){
		System.out.println(data[i]+" ");
	}
	
	valueStore.delete(1);
	valueStore.delete(3);
	}
	
	


	
}

