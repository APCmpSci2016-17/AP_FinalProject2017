import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SaveChars {
	public static ArrayList<CharData> data = new ArrayList<CharData>();

	public static void main(String[] args) throws IOException {
		
		save();
		//load();
		System.out.println(data.size());
	}
	
	public static void save() throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("D:\\charData.dat"));
		out.writeObject(data);
		out.close();
	}
	
	@SuppressWarnings("unchecked")
	public static void load() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("D:\\charData.dat"));
			data = (ArrayList<CharData>) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("No save file found! \n");
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
