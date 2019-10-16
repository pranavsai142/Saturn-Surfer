
import java.util.Comparator;
//compares scores to each other
public class ScoreComparator implements Comparator<Score> {
	public int compare(Score score1, Score score2) {

		int sc1 = score1.getPoints();
		int sc2 = score2.getPoints();

		if (sc1 > sc2){
			return -1;
		}else if (sc1 < sc2){
			return +1;
		}else{
			return 0;
		}
	}
}
