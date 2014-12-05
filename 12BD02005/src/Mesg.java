import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mesg  implements Serializable{
	Long number;
	int funct;
	int id = 0;
	Mesg(Long number, int funct){
		this.number = number;
		this.funct = funct;
		id=id+1;
	}
	public int getFunc(){
		return funct;
	}
	public Long getNum(){
		return number;
	}
	public int id(){
		return id;
	}
	public byte[] getBytes() {
        byte[]bytes;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.flush();
            oos.reset();
            bytes = baos.toByteArray();
            oos.close();
            baos.close();
        } catch(IOException e){
            bytes = new byte[] {};
            Logger.getLogger("bsdlog").log(Level.ALL, "unable to write to output stream" + e);
        }
        return bytes;
    }

    public static Mesg fromBytes(byte[] body) {
        Mesg obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(body);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = (Mesg) ois.readObject();
            ois.close();
            bis.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }
}
