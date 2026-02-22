package servlet;

public class DataBean {
    private String date;      // 1列目: 日付
    private int dayOfWeek;    // 2列目: 曜日(1-7)
    private String item1;     // 3列目
    private String item2;     // 4列目
    private String item3;     // 5列目

    // コンストラクタ
    public DataBean(String date, int dayOfWeek, String item1, String item2, String item3) {
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
    }

    // Getter（画面表示などで使用）
    public String getDate() { return date; }
    public int getDayOfWeek() { return dayOfWeek; }
    public String getItem1() { return item1; }
    public String getItem2() { return item2; }
    public String getItem3() { return item3; }

    public String getDayName() {
        String[] days = {"", "日", "月", "火", "水", "木", "金", "土"};
        return days[this.dayOfWeek];
    }
    
    public String toString() {
        return "DataBean{date=" + date + ", dayOfWeek=" + dayOfWeek + 
               ", item1=" + item1 + ", item2=" + item2 + ", item3=" + item3 + "}";
    }
    
    
}
