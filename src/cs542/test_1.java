package cs542;

public class test_1 {
	public static void main(String[] args) {

	byte[] A = new byte[1024*1024];
	for(int i=0;i<1024*1024;i++){
		A[i]=1;
	}
	byte[]  B = new byte[1024*1024];
	for(int i=0;i<1024*1024;i++){
		B[i]=2;
	}
	byte[]  C = new byte[1024*1024];
	for(int i=0;i<1024*1024;i++){
		C[i]=3;
	}
	byte[]  D = new byte[1024*1024];
	for(int i=0;i<1024*1024;i++){
		D[i]=4;
	}
	byte[]  E = new byte[1024*512];
	for(int i=0;i<1024*512;i++){
		E[i]=5;
	}
	byte[]  F = new byte[1024*1024];
	for(int i=0;i<1024*1024;i++){
		F[i]=6;
	}
	byte[]  G = new byte[1024*1024];
	for(int i=0;i<1024*1024;i++){
		G[i]=7;
	}
	byte[]  H = new byte[1024*1024];
	for(int i=0;i<1024*1024;i++){
		H[i]=8;
	}
	//KeyValueStore.WriteMeta();
	//System.out.println(value.length);
	KeyValueStore.WriteMeta();
	KeyValueStore.Put(1,A);
	//KeyValueStore.layout(0);
	KeyValueStore.Put(2,B);
	//KeyValueStore.layout(1);
	KeyValueStore.Put(3,C);
	//KeyValueStore.layout(2);
	KeyValueStore.Put(4, D);
	//KeyValueStore.layout(3);
	KeyValueStore.Remove(2);
	KeyValueStore.Put(5, E);
	KeyValueStore.keyStatus();
	KeyValueStore.Put(6, F); //Fails
	KeyValueStore.Remove(3);
	KeyValueStore.Put(7, G); //success
	KeyValueStore.keyStatus();
	KeyValueStore.Remove(5);
	KeyValueStore.Put(8, H);
	
	byte[]data1=KeyValueStore.Get(3);
	byte[]data=KeyValueStore.Get(7);
	KeyValueStore.keyStatus();
	byte[]data2=KeyValueStore.Get(4);
	
	for(int i=0;i<10;i++){
		System.out.print(data[i]+" ");
	}
	System.out.println();
	for(int i=0;i<10;i++){
		System.out.print(data2[i]+" ");
	}
	System.out.println();
	KeyValueStore.Put(1, H);
	KeyValueStore.keyStatus();
	KeyValueStore.layout(0);
	}

}
