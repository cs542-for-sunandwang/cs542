package cs542;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;


public class KeyValueStore{
	private static final int MetaSize=1024*1024;
	private final static int MaxDataEnd=5*1024*1024;
	
	private static HashMap<Integer, int[]> index = new HashMap<Integer,int[]>();
	
	@SuppressWarnings("unchecked")
	public static void ReadMeta(){
    	try
        {
           FileInputStream fis = new FileInputStream("CS542.meta");
           ObjectInputStream ois = new ObjectInputStream(fis);
           index = (HashMap<Integer, int[]>) ois.readObject();
           ois.close();
           fis.close();
           
        }catch(IOException ioe)
        {
           ioe.printStackTrace();
           return;
        }catch(ClassNotFoundException c)
        {
           System.out.println("Class not found");
           c.printStackTrace();
           return;
        }
		
	}
	
	public static void WriteMeta(){
   	 	try
   	 	{
            FileOutputStream fos = new FileOutputStream("CS542.meta");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(index);
            oos.close();
            fos.close();
            //System.out.printf("Serialized HashMap data is saved in db\n");

   	 	}catch(IOException ioe)
   	 		{
   	 			ioe.printStackTrace();
   	 		}
	}
	
	private static byte[] ReadDB(int addr, int length){
		RandomAccessFile out1;
		byte[] value= new byte[length];
		try {
			out1 = new RandomAccessFile("CS542.db","rw");
			out1.seek(addr-1);
			
			out1.read(value,0, length);
			out1.close();
			//return value;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;

	}
	
	private static void WriteDB(byte[] value, int addr){
		byte[] value1= new byte[1024*1024*4+1];
		value1=ReadDB(1048577, 4194304+1);
		
		try {

			for(int i=addr-1048576;i<addr-1048576+value.length;i++){
				value1[i]=value[i-addr+1048576];
			}
			RandomAccessFile out1=new RandomAccessFile("CS542.db","rw");
			out1.seek(1048577);
			out1.write(value1);
			out1.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void Put(int key, byte[] value){
		// read metadata into hashmap index
		ReadMeta();
		//System.out.println(index.isEmpty());
		if(index.isEmpty()){
			
			int[] addr = new int[2];
			addr[0]=value.length;
			addr[1]=(MetaSize+1);
			
			index.put(key,addr);
			//System.out.println(index.isEmpty());
			WriteMeta();
			//put value at address at k
			WriteDB(value, addr[1]);
			System.out.println("Successfully put " +key+" into db..");
			}
		else{	
			if(index.containsKey(key)){
				System.out.println("Key "+key+" existed! Now replacing it with new value.");
				Remove(key);
				ReadMeta();
			}
			int[] startpos= new int[index.size()];
			int[] endpos=new int[index.size()];
			int i=0;

			for(int k: index.keySet()){
				//System.out.println(index.get(k)[0]);
				//System.out.println(index.get(k)[1]);
				startpos[i]=index.get(k)[1];
				endpos[i]=index.get(k)[0]+index.get(k)[1]-1;
				i++;
			}
			Arrays.sort(startpos);
			Arrays.sort(endpos);
			//for(int q=0;q<endpos.length;q++){
				//System.out.println(endpos[q]);
			//}
			/*
			for(int w=0;w<startpos.length;w++)
				System.out.println(startpos[w]);
			for(int s=0;s<endpos.length;s++)
				System.out.println(endpos[s]);
			*/
			//
			//System.out.println(endpos[endpos.length-1]);
			// len <the first vacant address, the length of space>

			
			if(endpos[endpos.length-1]<MaxDataEnd && (MaxDataEnd-endpos[endpos.length-1])>=value.length){
				int[] addr = new int[2];
				addr[0]=value.length;
				addr[1]=(endpos[endpos.length-1]+1);
				
				index.put(key,addr);
				WriteMeta();
				//put value at address at k
				WriteDB(value, addr[1]);
				System.out.println("Successfully put " +key+" into db..");
			}
			//else if(endpos[endpos.length-1]<MaxDataEnd && (MaxDataEnd-endpos[endpos.length-1])<value.length){
				//System.out.println("Fail. Maximum data size exceeded.");
			//}
			

			else {
				/*
				for(int k: len.keySet()){
					System.out.println(len.get(k));
					System.out.println(k);
				}
				*/
				HashMap<Integer, Integer> len = new HashMap<Integer,Integer>();
				len.put(endpos[endpos.length-1]+1, MaxDataEnd-endpos[endpos.length-1]);
				for(int j=0;j<startpos.length-1;j++){
					int leng=startpos[j+1]-endpos[j]-1;
					if(leng!=0)
						len.put(endpos[j]+1, leng);
				}
				boolean flag=true;
				for(int k: len.keySet()){
					
					if(len.get(k)>=value.length){
						flag=false;
						// addr contains address and value length regarding to a certain key
						int[] addr = new int[2];
						addr[0]=value.length;
						addr[1]=(k);
						index.put(key,addr);
						WriteMeta();
						//put value at address at k
						WriteDB(value, k);
						System.out.println("Successfully put " +key+" into db...");
						break;
					}					
				}
				if(flag)
					System.out.println("Put value "+ key+" to database fail: not enough space.");
			}
				
					
				
			
			}
	}
	
	public static byte[] Get(int key){
		ReadMeta();
		
		//System.out.println(index.isEmpty());
		
		if(index.containsKey(key)){
			int addr=index.get(key)[1];
			return ReadDB(addr, index.get(key)[0]);
		}
		else
			System.out.println("Cannot get data: key not existed.");
			return null;
	}
	
	public static void Remove(int key){
		ReadMeta();
		if(index.containsKey(key)){
			// delete information in metadata
			index.remove(key);
			WriteMeta();
			//delete value in DB
			System.out.println("Deleted "+key +" in DB..");
		}
		else
			System.out.println("Key "+ key+" not found.");
	}
	
	public static void layout(int mega){
		byte[] value= new byte[1024*1024*4+20];
		value=ReadDB(1048577, 4194304+20);
		for(int i=0+mega*1048576;i<30+mega*1048576;i++){
			System.out.print(value[i]+" ");
		}
		System.out.println();
		for(int i=1048566+mega*1048576;i<1048596+mega*1048576;i++){
			System.out.print(value[i]+" ");
		}
		System.out.println();
	}
	public static void keyStatus(){
		for(int k: index.keySet()){
			System.out.print(k);
			System.out.print("\t");
			System.out.print(index.get(k)[0]);
			System.out.print("\t");
			System.out.print((index.get(k)[1]-1)/(1048576/2));
			System.out.println();
		}
	}
}

