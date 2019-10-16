
import java.io.Serializable;
//Score object with user input name and their pet count as attributes
public class Score implements Serializable {
	private String myname;
	private int totalPoints;

	public Score(String myname, int totalPoints) {
		this.myname = myname;
		this.totalPoints = totalPoints;
	}

	public int getPoints() {
		return totalPoints;
	}
	public String getName() {
		return myname;
	}
}