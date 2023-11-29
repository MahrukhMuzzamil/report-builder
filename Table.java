package Buisness;

public class Table {
    private String[][] tableData;
    int rows;
    int col;


    public Table(int row, int c){
        rows=row;
        col=c;
        tableData=new String[rows][col];
    }
    public int getRows(){
        return rows;
    }
    public int getCols(){
        return col;
    }
    public void setRows(int r){
        rows=r;
    }
    public void setCols(int c){
        col=c;
    }
    public String[][] getTableData(){
        return tableData;
    }
    public void setTableData(int r, int c, String d){
        tableData[r][c]=d;
    }
}
