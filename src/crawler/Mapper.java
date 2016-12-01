package crawler;

import java.util.ArrayList;
import java.util.List;

public class Mapper {
    private int Type = 0;
    private String TypeS = "구분업무";
    private String Url = "";
    private String Title = "공고명공고제목공고이름제목입찰건명입찰건업무공고입찰명공사명사업명제목";
    private String Refer = "링크LinklinkLINK";
    private String BidNo = "번호공고번호-차수고시공고번호입찰번호입찰공고번호입찰공고 번호";
    private String Bstart = "개찰일자입찰일자입찰일시발주시기개찰일시";
    private String Bexpire = "입찰서마감일시마감일시입찰마감일시입찰일등록기한입찰서 마감일시";
    private String PDate = "등록일작성일등록일자공고일게시일자게재일자게재일시게시일시";
    private String Dept = "의뢰부서공고기관담당부서계약부서입찰기관사업부서부서명관서명발주처명";
    private String Charge = "작성자";
    private String Date = "";

    private ArrayList<BidList> bidListArray; // 쓰레드로 쏘기 위해서 입찰정보 객체들을 담아 놓을 bidListArray
    //private BidList bidList; // 넘어온 이중 ArrayList의 Row에 해당하는 입찰정보 건수들을 동적으로 할당받기 위한 bidList 배열
    private List<String> Category; // 세호로부터 받을 카테고리 ArrayList
    private ArrayList<String> mappedCategory;
    private String Temp = "";
    private List<List<String>> Table; // 세호로부터 받을 Table
    private Parser parser;

    public void setMapper(Parser parser) {
        this.parser = parser;
        this.Category = parser.category;
        this.Table = parser.article;
        this.Refer = parser.getName();
        this.Type = parser.getType();
        mappedCategory = new ArrayList<String>();
    }

    public Mapper(){
        bidListArray = new ArrayList<BidList>();
    }

    public ArrayList<String> getCategory(){
        for(int i = 0; i < mappedCategory.size(); i++){
            System.out.println(mappedCategory.get(i));
        }
        return mappedCategory;
    }

    public ArrayList<BidList> mapping()
    {
        BidList[] tempBL = new BidList[Table.size()];
        ArrayList<BidList> bidListArr = new ArrayList();

        for(int i = 0;i < Table.size() - 1;i++) {
            BidList temBL = new BidList();
            temBL.setUrl(parser.getURL());
            temBL.setRefer(Refer);
            bidListArr.add(temBL);
        }

        for(int j = 0; j < Category.size(); j++) {
            mappingData(bidListArr,j);
        }

        mappedCategory.add("Refer");
        mappedCategory.add("Url");
        mappedCategory.add("TypeS");
        return bidListArr;
    }

    public int toInteger(String type){
        int typeNum = Type;
        if(Type == 3){
            if(type.trim().contains("공사")){
                typeNum = 1;
            }else if(type.trim().contains("용역")){
                typeNum = 2;
            }else if(type.trim().contains("물품")){
                typeNum = 0;
            }
        }else if(Type == 7){
            if(type.trim().contains("공사")){
                typeNum = 5;
            }else if(type.trim().contains("용역")){
                typeNum = 6;
            }else if(type.trim().contains("물품")){
                typeNum = 4;
            }
        }else if(Type == -1){
            if(type == null){
                typeNum = 3;
            }
        }
        return typeNum;
    }

    public void mappingData(ArrayList<BidList> bidListArr, int j) {
        boolean isAdded = false;
        if(TypeS.contains(Category.get(j))){
            for(int i = 0; i < Table.size() - 1; i++){
                bidListArr.get(i).setType(toInteger(Table.get(i).get(j)));
            }
        }

        if(Title.contains(Category.get(j))) {
            for(int i = 0; i < mappedCategory.size(); i++) {
                if(mappedCategory.get(i).contains("Title"))
                isAdded = true;
            }
            if(!isAdded)
                mappedCategory.add("Title");
            for(int i = 0;i < Table.size() - 1;i++) {
                bidListArr.get(i).setTitle(Table.get(i).get(j));
            }
            isAdded= false;
        }

        if(BidNo.contains(Category.get(j))) {
            for(int i = 0; i < mappedCategory.size(); i++) {
                if(mappedCategory.get(i).contains("BidNo"))
                    isAdded = true;
            }
            if(!isAdded) mappedCategory.add("BidNo");
            for(int i=0;i<Table.size() - 1;i++) {
                bidListArr.get(i).setBidNo(Table.get(i).get(j));
            }
            isAdded = false;
        }

        if(Bstart.contains(Category.get(j)))
        {
            for(int i = 0; i < mappedCategory.size(); i++) {
                if(mappedCategory.get(i).contains("Bstart"))
                    isAdded = true;
            }
            if(!isAdded)
                mappedCategory.add("Bstart");
            for(int i=0;i<Table.size() - 1;i++)
            {
                System.out.println(Table.get(i).get(j));
                bidListArr.get(i).setBstart(Table.get(i).get(j));
            }
            isAdded = false;
        }

        if(Bexpire.contains(Category.get(j)))
        {
            for(int i = 0; i < mappedCategory.size(); i++) {
                if(mappedCategory.get(i).contains("Bexpire"))
                    isAdded = true;
            }
            if(!isAdded)
                mappedCategory.add("Bexpire");
            for(int i=0;i<Table.size() - 1;i++)
            {
                System.out.println(Table.get(i).get(j));
                bidListArr.get(i).setBexpire(Table.get(i).get(j));
            }
            isAdded = false;
        }

        if(PDate.contains(Category.get(j)))
        {
            for(int i = 0; i < mappedCategory.size(); i++) {
                if(mappedCategory.get(i).contains("PDate"))
                    isAdded = true;
            }
            if(!isAdded)
                mappedCategory.add("PDate");
            for(int i=0;i<Table.size() - 1;i++)
            {
                System.out.println(Table.get(i).get(j));
                bidListArr.get(i).setDept(Table.get(i).get(j));
            }
            isAdded = false;
        }

        if(Dept.contains(Category.get(j)))
        {
            for(int i = 0; i < mappedCategory.size(); i++) {
                if(mappedCategory.get(i).contains("Dept"))
                    isAdded = true;
            }
            if(!isAdded)
                mappedCategory.add("Dept");
            for(int i=0;i<Table.size() - 1;i++)
            {
                System.out.println(Table.get(i).get(j));
                bidListArr.get(i).setDept(Table.get(i).get(j));
            }
            isAdded = false;
        }

        if(Charge.contains(Category.get(j))) {
            for(int i = 0; i < mappedCategory.size(); i++) {
                if(mappedCategory.get(i).contains("Charge"))
                    isAdded = true;
            }
            if(!isAdded) mappedCategory.add("Charge");
            for(int i=0;i<Table.size() - 1;i++) {
                System.out.println(Table.get(i).get(j));
                bidListArr.get(i).setCharge(Table.get(i).get(j));
            }
            isAdded = false;
        }
    }

    public ArrayList<BidList> getBidList(){
        return bidListArray;
    }

}
