package scoremng;

import java.sql.Timestamp;

public class PlayerStats {

	public PlayerStats () {}
	
	public PlayerStats (Integer max, Timestamp maxTime, Integer min, Timestamp minTime, float avg) {
		super();
		
		this.max = max;
		this.maxTime = maxTime;
		this.min = min;
		this.minTime = minTime;
		this.avg = avg;
	}
	
	private Integer max;
	
	private Timestamp maxTime;
	
	private Integer min;
	
	private Timestamp minTime;
	
	private float avg;
	
	public Integer getMax () {
		return max;
	}
	
	public void setMax (Integer max) {
		this.max = max;
	}
	
	public Timestamp getMaxTime() {
		return maxTime;
	}
	
	public void setMaxTime(Timestamp maxTime) {
		this.maxTime = maxTime;
	}
	
	public Integer getMin() {
		return min;
	}
	
	public void setMin (Integer min) {
		this.min = min;
	}
	
	public Timestamp getMinTime() {
		return minTime;
	}
	
	public void setMinTime(Timestamp minTime) {
		this.minTime = minTime;
	}
	
	public float getAvg () {
		return avg;
	}
	
	public void setAvg (float avg) {
		this.avg = avg;
	}
}
