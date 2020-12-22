package scoremng;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table
public class Score {
	public Score () {}
	
	public Score (Integer id, String player, Integer score, Timestamp scoretime) {
		super();
		
		this.id = id;
		
		this.player = player;
		
		this.score = score;
		
		this.scoretime = scoretime;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private String player;
	
	@Column
	private Integer score;
	
	@Column
	private Timestamp scoretime;
	
	public Integer getId() {
		return id;
	}
	
	public void setId (Integer id) {
		this.id = id;
	}
	
	public String getPlayer() {
		return player;
	}
	
	public void setPlayer (String player) {
		this.player = player;
	}
	
	public Integer getScore () {
		return score;
	}
	
	public void setScore (Integer score) {
		this.score = score;
	}
	
	public Timestamp getScoretime () {
		return scoretime;
	}
	
	public void setScoretime (Timestamp scoretime) {
		this.scoretime = scoretime;
	}
}
