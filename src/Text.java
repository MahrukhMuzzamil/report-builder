package Buisness;

import java.awt.*;

public class Text {
    String text;
    private Font font;

    public void setText(String t){
        text=t;
    }
    public String getText(){
        return text;
    }
    public void setFont(Font f){
        font=f;
    }
    public Font getFont(){
        return font;
    }
}
