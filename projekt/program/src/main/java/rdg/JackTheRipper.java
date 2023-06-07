package rdg;

/**
 *
 * @author Rastislav Urbanek
 */
public class JackTheRipper {
    private int winnerId;
    private int allDefeated;
    private int maleDefeated;
    private int femaleDefeated;
    private Integer week;
    private Integer month;

    public int getAllDefeated() { return allDefeated; }
    public void setAllDefeated(int allDefeated) { this.allDefeated = allDefeated; }
    public Integer getWeek() { return week; }
    public void setWeek(Integer week) { this.week = week; }
    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }
    public int getWinnerId() { return winnerId; }
    public void setWinnerId(int winnerId) { this.winnerId = winnerId; }
    public int getMaleDefeated() { return maleDefeated; }
    public void setMaleDefeated(int maleDefeated) { this.maleDefeated = maleDefeated; }
    public int getFemaleDefeated() { return femaleDefeated; }
    public void setFemaleDefeated(int femaleDefeated) { this.femaleDefeated = femaleDefeated; }

}
