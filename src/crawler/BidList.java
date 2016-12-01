package crawler;

public class BidList {
    private int Type;
    private String Url;
    private String Title;
    private String Refer;
    private String BidNo;
    private String Bstart;
    private String Bexpire;
    private String PDate;
    private String Dept;
    private String Charge;

    public BidList() {
        this.Type = 3;
        this.Url = "NULL";
        this.Title = "NULL";
        this.Refer = "NULL";
        this.BidNo = "NULL";
        this.Bstart = "NULL";
        this.Bexpire = "NULL";
        this.PDate = "NULL";
        this.Dept = "NULL";
        this.Charge = "NULL";
    }

    public int getType() {
        return Type;
    }
    public void setType(int type) {
        Type = type;
    }
    public String getUrl() {
        return Url;
    }
    public void setUrl(String url) {
        Url = url;
    }
    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        Title = title;
        Title = Title.replaceAll("[‘’\'\"]", " ");
    }
    public String getRefer() {
        return Refer;
    }
    public void setRefer(String refer) {
        Refer = refer;
        Refer = Refer.replaceAll("[‘’\'\"]", " ");
    }
    public String getBidNo() {
        return BidNo;
    }
    public void setBidNo(String bidNo) {
        BidNo = bidNo;
        BidNo = BidNo.replaceAll("[‘’\'\"]", " ");
    }
    public String getBstart() {
        return Bstart;
    }
    public void setBstart(String bstart) {
        Bstart = bstart;
        Bstart = Bstart.replaceAll("[‘’\'\"]", " ");
    }
    public String getBexpire() {
        return Bexpire;
    }
    public void setBexpire(String bexpire) {
        Bexpire = bexpire;
        Bexpire = Bexpire.replaceAll("[‘’\'\"]", " ");
    }
    public String getPDate() {
        return PDate;
    }
    public void setPDate(String pDate) {
        PDate = pDate;
        PDate = PDate.replaceAll("[‘’\'\"]", " ");
    }
    public String getDept() {
        return Dept;
    }
    public void setDept(String dept) {
        Dept = dept;
        Dept = Dept.replaceAll("[‘’\'\"]", " ");
    }
    public String getCharge() {
        return Charge;
    }
    public void setCharge(String charge) {
        Charge = charge;
        Charge = Charge.replaceAll("[‘’\'\"]", " ");
    }

    public String getIndex(int index){
        String toGet = null;
        switch(index){
            case 1:
                toGet = getUrl();
                break;
            case 2:
                toGet = getTitle();
                break;
            case 3:
                toGet = getRefer();
                break;
            case 4:
                toGet = getBidNo();
                break;
            case 5:
                toGet = getBstart();
                break;
            case 6:
                toGet = getBexpire();
                break;
            case 7:
                toGet = getPDate();
                break;
            case 8:
                toGet = getDept();
                break;
            case 9:
                toGet = getCharge();
                break;
        }
        return toGet;
    }
}
