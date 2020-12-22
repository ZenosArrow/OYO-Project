package scoremng;

import java.util.List;

public interface IScoreService {
	Number createScore(Score score);
	Score getScoreById(Integer id);
	void deleteScore(Integer id);
	List<Score> getScoreList (String beforeDate, String afterDate, String players);
	List<Object> getPlayerHistory (String player);
}
