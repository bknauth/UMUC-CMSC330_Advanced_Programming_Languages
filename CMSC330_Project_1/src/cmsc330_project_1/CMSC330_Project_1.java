//CMSC330_BenjaminKnauth_Project1_15September2014
package cmsc330_project_1;
import java.util.Scanner;
import java.util.LinkedList;
import java.io.File;
import javax.swing.*;
import java.awt.*;

public class CMSC330_Project_1  extends JFrame{
    static String token;
    static Scanner input;
    static LinkedList<Panel> panels = new LinkedList<Panel>();//This holds Panels so they can be referenced without being named.  Enables Panels within Panels.
    static JFrame frame = new JFrame();
    public static void main(String[] args) throws Exception{
        File file = new File("input2.txt");
        input = new Scanner(file);
        System.out.println(gui(input));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    static boolean gui(Scanner input){//Fills in frame name, size, calls layout, calls widgets, terminates with "End."    
        token = input.next().trim();
        while(token.equals("")){
            token = input.next().trim();
        }
        if(token.equals("Window")){
            token = input.next().trim();
            while(token.equals("")){
                token = input.next().trim();
            }            
            if(token.charAt(0) == '"' && token.charAt(token.length() - 1) == '"'){
                frame.setTitle(token.substring(1, token.length() - 1));//Sets title of Window.
                token = input.next().trim();
                while(token.equals("")){//Lots of these.  Erases white spaces.
                    token = input.next().trim();
                }
                if(token.charAt(0) == '(' && token.charAt(token.length() - 1) == ',' && isInteger(token.substring(1, token.length() - 2))){
                    int frameWidth = Integer.parseInt(token.substring(1, token.length() - 1));//Width
                    token = input.next().trim();
                    while(token.equals("")){
                        token = input.next().trim();
                    }
                    if(isInteger(token.substring(0, token.length() - 2)) && token.charAt(token.length() - 1) == ')'){
                        int frameHeight = Integer.parseInt(token.substring(0, token.length() - 1));//Height.
                        frame.setSize(frameWidth,frameHeight);//Size of Window.                        
                        token = input.next().trim();
                        while(token.equals("")){
                            token = input.next().trim();
                        }
                        if(layout(token, input, "Frame")){//Layout
                            token = input.next().trim().trim();                            
                            while(token.equals("")){
                                token = input.next().trim();
                            }
                            if(token.equals(":")){
                                token = input.next().trim();
                                while(token.equals("")){                                    
                                    token = input.next().trim();
                                }
                                if(widgets(token, input, "Frame")){//Widgets.
                                    token = input.next().trim();
                                    while(token.equals("")){
                                        token = input.next().trim();
                                    }
                                    if(token.equals("End.")){//End of .txt file.
                                        System.out.println("gui() pass");
                                        return true;//gui() returns true if the .txt file compiles correctly.
                                    }
                                }
                            }                            
                        }
                    }
                }
            }
        }
        System.out.println("Problem gui()");//gui() returns false if .txt file compiles incorrectly.
        return false;
    }
    
    static boolean layout(String token, Scanner input, String containerType){//containerType is either "Frame" or "Panel", which gets passed to layout_types.  This method only ensures proper formatting.
        if(token.equals("Layout")){
            input.useDelimiter(":");
            token = input.next().trim();
            while(token.equals("")){
                token = input.next().trim();
            }
            if(token.equals("Flow") || token.substring(0, 4).equals("Grid")){                
                input.useDelimiter(" |\n");
                if(layout_type(token, input, containerType)){
                    System.out.println("layout() pass");
                    return true;
                }
            }            
        }
        System.out.println("Problem layout()");
        return false;
    }
    
    static boolean layout_type(String token, Scanner input, String containerType){//First determines if Layout is Flow or Grid, then whether containerType is Frame or Panel. If Grid, commaCounter() is used to determine if 2-arg or 4-arg.
        String type = token.substring(0, 4);
        switch(type){
            case "Flow":
                switch(containerType){
                    case "Frame":
                        if(token.equals("Flow")){
                            frame.setLayout(new FlowLayout());
                            System.out.println("layout_type flow Pass");
                            return true;
                        }
                    case "Panel":
                        if(token.equals("Flow")){
                            panels.getLast().setLayout(new FlowLayout());
                            System.out.println("layout_type flow Pass");
                            return true;
                        }
                    default:
                        System.out.println("layout_type flow fail");
                        return false;
                }
            case "Grid":
                int count;
                Scanner subInput;
                switch(containerType){                   
                    case "Frame":
                        count = countCommas(token);//This is used to account for possible [] in grammer.
                        subInput = new Scanner(token.substring(5));
                        if(count == 3){
                            subInput.useDelimiter(",");
                            token = subInput.next();//Height.
                            if(isInteger(token)){
                                int height = Integer.parseInt(token);
                                token = subInput.next().trim();//Width.
                                if(isInteger(token)){
                                    int width = Integer.parseInt(token);
                                    token = subInput.next().trim();//Horizontal gap.
                                    if(isInteger(token)){
                                        int horiz = Integer.parseInt(token);
                                        subInput.useDelimiter("\\)");
                                        token = subInput.next().trim();//Vertical gap.                                        
                                        token = token.substring(1).trim();
                                        if(isInteger(token)){
                                            int vert = Integer.parseInt(token);
                                            subInput.close();
                                            frame.setLayout(new GridLayout(height, width, horiz, vert));
                                            System.out.println("layout_type Grid c3 pass");
                                            return true;
                                        }                                
                                    }
                                }
                            }
                            System.out.println("Problem layout_type");
                            return false;
                        }
                        else if(count == 1){
                            subInput.useDelimiter(",");
                            token = subInput.next();//Height.
                            if(isInteger(token)){
                                int height = Integer.parseInt(token);
                                subInput.useDelimiter("//)");
                                token = subInput.next().trim();//Width.                                
                                token = token.substring(1).trim();
                                if(isInteger(token)){
                                    int width = Integer.parseInt(token);
                                    subInput.close();
                                    frame.setLayout(new GridLayout(height, width));
                                    System.out.println("layout_type Grid c1 pass");
                                    return true;
                                }
                            }                
                        }
                        else{
                            System.out.println("Problem layout_type");
                            return false;
                        }
                    case "Panel"://Set layout_type for a Panel.
                        count = countCommas(token);//This is used to account for possible [] in grammer.
                        subInput = new Scanner(token.substring(5));
                        if(count == 3){
                            subInput.useDelimiter(",");
                            token = subInput.next();//Height.
                            if(isInteger(token)){
                                int height = Integer.parseInt(token);
                                token = subInput.next().trim();//Width.
                                if(isInteger(token)){
                                    int width = Integer.parseInt(token);
                                    token = subInput.next().trim();//Horizontal gap.
                                    if(isInteger(token)){
                                        int horiz = Integer.parseInt(token);
                                        subInput.useDelimiter("\\)");
                                        token = subInput.next().trim();//Vertical gap.
                                        token = token.substring(1).trim();
                                        if(isInteger(token)){
                                            int vert = Integer.parseInt(token);
                                            subInput.close();
                                            panels.getLast().setLayout(new GridLayout(height, width, horiz, vert));
                                            System.out.println("layout_type Grid c3 pass");
                                            return true;
                                        }                                
                                    }
                                }
                            }
                            System.out.println("Problem layout_type");
                            return false;
                        }
                        else if(count == 1){                            
                            subInput.useDelimiter(",");
                            token = subInput.next().trim();//Height.                            
                            if(isInteger(token)){
                                int height = Integer.parseInt(token);
                                subInput.useDelimiter("\\)");
                                token = subInput.next().trim();//Width.
                                token = token.substring(1).trim();
                                if(isInteger(token)){
                                    int width = Integer.parseInt(token);
                                    subInput.close();
                                    panels.getLast().setLayout(new GridLayout(height, width));
                                    System.out.println("layout_type Grid c1 pass");
                                    return true;
                                }
                            }                
                        }
                        else{
                            System.out.println("Problem layout_type");
                            return false;
                        }
                    }
                
            default:
                System.out.println("Problem layout_type");
                return false;
        }
    }
    
    static boolean widgets(String token, Scanner input, String containerType){//Sends token to widget, then determines if next token is widget or "End;"  Acts recursively if .txt feeds more widgets.
        if(widget(token, input, containerType)){
            while(input.hasNext("")){
                token = input.next().trim();
            }
            if(input.hasNext("Button") || input.hasNext("Group:") || input.hasNext("Label") || input.hasNext("Panel") || input.hasNext("Textfield")){
                token = input.next().trim();
                while(token.equals("")){
                    token = input.next().trim();
                }
                widgets(token, input, containerType);
            }
        }
        return true;
    }
    
    static boolean widget(String token, Scanner input, String containerType){//First determines if widget is being added to a frame or panel using containerType.  Then uses token to determine which widget is being added.
        switch(containerType){
            case "Frame":        
                switch(token){
                    case "Button":
                        token = input.next().trim();
                        while(token.equals("")){
                            token = input.next().trim();
                        }
                        if(token.charAt(0) == '"' && token.charAt(token.length() - 1) == ';' && token.charAt(token.length() - 2) == '"'){
                            String bString = token.substring(1, token.length() - 2);
                            frame.add(new JButton(bString));
                            System.out.println("widget() Button Pass");                    
                            return true;
                        }
                    case "Group:":
                        token = input.next().trim();
                        while(token.equals("")){
                            token = input.next().trim();
                        }
                        if(radio_buttons(token, input, "Frame")){
                            token = input.next().trim();
                            while(token.equals("")){
                                token = input.next().trim();
                            }
                            if(token.equals("End;")){
                                System.out.println("widget() Group Pass");
                                return true;
                            }
                        }
                    case "Label":
                        token = input.next().trim();
                        while(token.equals("")){
                            token = input.next().trim();
                        }
                        if(token.charAt(0) == '"' && token.charAt(token.length() - 1) == ';' && token.charAt(token.length() - 2) == '"'){
                            String lString = token.substring(1, token.length() - 2);
                            frame.add(new JLabel(lString));
                            System.out.println("widget() Label Pass");
                            return true;
                        }
                    case "Panel":                        
                        panels.add(new Panel());
                        frame.add(panels.getLast());
                        token = input.next().trim();
                        while(token.equals("")){
                            token = input.next().trim();
                        }
                        if(layout(token, input, "Panel")){
                            token = input.next().trim();
                            while(token.equals("")){
                                token = input.next().trim();
                            }
                            if(token.equals(":")){                        
                                token = input.next().trim();
                                while(token.equals("")){
                                    token = input.next().trim();
                                }
                                if(widgets(token, input, "Panel")){
                                    token = input.next().trim();
                                    while(token.equals("")){
                                        token = input.next().trim();
                                    }
                                    if(token.equals("End;")){
                                        System.out.println("widget() Panel Pass");
                                        return true;
                                    }
                                }
                            }    
                        }
                    case "Textfield":
                        token = input.next().trim();                        
                        while(token.equals("")){
                            token = input.next().trim();
                        }                
                        if(isInteger(token.substring(0, token.length() - 1))){
                            int tfSize = Integer.parseInt(token.substring(0, token.length() - 1));
                            frame.add(new JTextField(tfSize));
                            System.out.println("widget() Textfield Pass");
                            return true;
                        }            
                }
            case "Panel":
                switch(token){
                    case "Button":
                        token = input.next().trim();
                        while(token.equals("")){
                            token = input.next().trim();
                        }
                        if(token.charAt(0) == '"' && token.charAt(token.length() - 1) == ';' && token.charAt(token.length() - 2) == '"'){
                            String bString = token.substring(1, token.length() - 2);
                            panels.getLast().add(new JButton(bString));
                            System.out.println("widget() Button Pass");                    
                            return true;
                        }
                    case "Group:":                        
                        token = input.next().trim();
                        while(token.equals("")){
                            token = input.next().trim();
                        }
                        if(radio_buttons(token, input, "Panel")){
                            token = input.next().trim();
                            while(token.equals("")){
                                token = input.next().trim();
                            }
                            if(token.equals("End;")){
                                System.out.println("widget() Group Pass");
                                return true;
                            }
                        }
                    case "Label":
                        token = input.next().trim();
                        while(token.equals("")){
                            token = input.next().trim();
                        }
                        if(token.charAt(0) == '"' && token.charAt(token.length() - 1) == ';' && token.charAt(token.length() - 2) == '"'){
                            String lString = token.substring(1, token.length() - 2);
                            panels.getLast().add(new JLabel(lString));
                            System.out.println("widget() Label Pass");
                            return true;
                        }
                    case "Panel":
                        panels.add(new Panel());
                        panels.get(panels.size() - 2).add(panels.get(panels.size() - 1));
                        token = input.next().trim();
                        while(token.equals("")){
                                token = input.next().trim();
                        }
                        if(layout(token, input, "Panel")){
                            token = input.next().trim();
                            while(token.equals("")){
                                token = input.next().trim();
                            }
                            if(token.equals(":")){                        
                                token = input.next().trim();
                                while(token.equals("")){
                                    token = input.next().trim();
                                }
                                if(widgets(token, input, "Panel")){
                                    token = input.next().trim();
                                    while(token.equals("")){
                                        token = input.next().trim();
                                    }
                                    if(token.equals("End;")){
                                        System.out.println("widget() Panel Pass");
                                        return true;
                                    }
                                }
                            }    
                        }
                    case "Textfield":
                        if(input.hasNext()){
                            token = input.next().trim();
                            while(token.equals("")){
                                token = input.next().trim();
                            }
                        }
                        while(token.equals("") && input.hasNext()){
                            token = input.next().trim();
                        }                
                        if(isInteger(token.substring(0, token.length() - 1))){
                            int tfSize = Integer.parseInt(token.substring(0, token.length() - 1));
                            panels.getLast().add(new JTextField(tfSize));
                            System.out.println("widget() Textfield Pass");
                            return true;
                        }            
                }
        }
    
        System.out.println("Problem widget " + token);
        return false;
    }
    
    static boolean radio_buttons(String token, Scanner input, String containerType){//Only used to act recursively if next token is Radio.
        if(radio_button(token, input, containerType)){
            while(input.hasNext("")){
                token = input.next().trim();
            }
            if(input.hasNext("Radio")){
                token = input.next().trim();
                radio_buttons(token, input, containerType);
            }
        }
        return true;
    }
    
    static boolean radio_button(String token, Scanner input, String containerType){//Determines if RadioButton is being added to frame or panel, then adds RadioButton.
        switch(containerType){
            case "Frame":
                if(token.equals("Radio")){
                    token = input.next().trim();
                    while(token.equals("")){
                        token = input.next().trim();
                    }
                    if(token.charAt(0) == '"' && token.charAt(token.length() - 2) == '"' && token.charAt(token.length() - 1) == ';'){
                        String rbString = token.substring(1, token.length() - 2);
                        frame.add(new JRadioButton(rbString));
                        System.out.println("radio_button() Pass");
                        return true;
                    }
                }
            case "Panel":
                System.out.println("Test1: " + token);
                if(token.equals("Radio")){                       
                    token = input.next().trim();
                    while(token.equals("")){
                        token = input.next().trim();
                    }
                    if(token.charAt(0) == '"' && token.charAt(token.length() - 2) == '"' && token.charAt(token.length() - 1) == ';'){
                        String rbString = token.substring(1, token.length() - 2);
                        panels.getLast().add(new JRadioButton(rbString));
                        System.out.println("radio_button() Pass");
                        return true;
                    }
                }            
        }
        System.out.println("Problem radio_button");
            return false;
    }
    
    public static boolean isInteger(String subString) {//Determines if token is an integer before converting a parsed String into an integer.
        try{ 
            Integer.parseInt(subString); 
        } 
        catch(NumberFormatException e){ 
            return false; 
        }
        return true;
    }
    
    public static int countCommas(String grid){//Used to determine if a Grid is 2-arg or 4-arg.
        int count = 0;
        for(int i = 0; i < grid.length(); i++){
            if(grid.charAt(i) == ',')
                count++;
        }
        return count;
    }
}
