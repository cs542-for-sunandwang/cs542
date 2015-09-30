package cs542;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;



public class valueStore {
	//Use HashMap to store key and value
	
    //private static HashMap<Integer, byte[]> store;
    private static HashMap<Integer, byte[]> store = new HashMap<Integer,byte[]>();
 
    /**
     * Read out data from cs542.db in project directory
     * data includes metadata and actual data
     * 
     */
    
    @SuppressWarnings("unchecked")
	private static void read(){
    	try
        {
           FileInputStream fis = new FileInputStream("cs542.db");
           ObjectInputStream ois = new ObjectInputStream(fis);
           store = (HashMap<Integer, byte[]>) ois.readObject();
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
        //System.out.println("Deserialized HashMap..");
    	
    }
    private static void write(){
    	/*
    	try {
    		storetofile.writeObject("/cs542.db", store);
		} catch (IOException e) {			
			e.printStackTrace();
		}  */
    	 try
         {
                FileOutputStream fos = new FileOutputStream("cs542.db");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(store);
                oos.close();
                fos.close();
                //System.out.printf("Serialized HashMap data is saved in db\n");
         }catch(IOException ioe)
          {
                ioe.printStackTrace();
          }
    	 /*
    	 for(int key: store.keySet()){
    		       //System.out.println(key + ": " + store.get(key));
    		       String sss=Arrays.toString(store.get(key));
    		       System.out.println(sss);
    		       
    	 }
    	 */
    	 /*
         Set<Entry<Integer, byte[]>> set = store.entrySet();
         Iterator<Entry<Integer, byte[]>> iterator = set.iterator();
         while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            System.out.print("key: "+ mentry.getKey() + " & Value: ");
            System.out.println(mentry.getValue());
         }
         */
    }
    
    public static void add(int key, byte[] obj){
    	read();
        //if key already exists
    	if(store!=null&&store.containsKey(key)){
    		System.out.println("Key exists");
    	}
    	else{
    		store.put(key, obj);
    		write();
    		System.out.println("key "+key+" added to db..");
    	}
    }
 
    /**
     * Get a specific object regarding key
     * @param key The key to search
     * @return The object retrieve, or null if nothing found
     */
    public static byte[] get(int key){
    	read();
        if(store.containsKey(key)){
            return store.get(key);
        }else{
        	System.out.println("key not found.");
            return null;
        }
    }
 
    /**
     * Delete an entry from the memory store
     * @param key The key to delete
     * @return The delete value result (true if the object has been found, false in other case)
     */
    public static void delete(int key){
    	read();
        if(store.containsKey(key)){
            store.remove(key);
            write();
            System.out.println("key "+key+" deleted.");
        }
        else{
        	System.out.println("key not found.");
        }
    }
    
}


