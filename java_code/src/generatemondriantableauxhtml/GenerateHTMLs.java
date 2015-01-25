/**
 * Copyright (c) 2015 Ignacio Arnaldo
 * 
 * Licensed under the MIT License.
 * 
 * See the "LICENSE" file for a copy of the license.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.  
 *
 */

package generatemondriantableauxhtml;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class generates the html version of the paintings given their
 * representation as a set of 9 variables X1,X2,...,X9
 * @author Ignacio Arnaldo
 */
public class GenerateHTMLs {
    
    String paintings_file_path, dest_path;
    String[] splitRowOptions, splitColOptions;
    //String WHITE_CODE = "1";
    String RED_CODE = "2";
    String BLUE_CODE = "3";
    String YELLOW_CODE = "4";
    
    /**
     * Constructor
     * @param aFilePath
     * @param aDestPath 
     */
    public GenerateHTMLs(String aFilePath, String aDestPath){
        paintings_file_path = aFilePath;
        dest_path = aDestPath;
        splitRowOptions = new String[2];
        splitRowOptions[0] = "1"; // one two
        splitRowOptions[1] = "2"; // two one
        
        // three ways of splitting a row
        splitColOptions = new String[3];
        splitColOptions[0] = "1"; // one one one
        splitColOptions[1] = "2"; // one two
        splitColOptions[2] = "3"; // two one
    }

    /**
     * Print row (first or second) of the tableau
     * @param printWriter
     * @param isThick
     * @param splitStrategy
     * @param colors 
     */
    public void printRow(PrintWriter printWriter, boolean isThick, String splitStrategy, ArrayList<String> colors){
        if(!isThick){
            printWriter.println("\t\t<div id= \"onethirdrow\">");
            if(splitStrategy.equals(splitColOptions[0])){
                printWriter.println("\t\t\t<div id= \"onethirdsquarebox\" class= \"" + colors.get(0)+ " right\"></div>");
                printWriter.println("\t\t\t<div id= \"vertlineonethirdthick\" class= \"black right\"></div>");
                printWriter.println("\t\t\t<div id= \"onethirdsquarebox\" class= \"" + colors.get(1)+ " right\"></div>");
                printWriter.println("\t\t\t<div id= \"vertlineonethirdthick\" class= \"black right\"></div>");
                printWriter.println("\t\t\t<div id= \"onethirdsquarebox\" class= \"" + colors.get(2)+ " right\"></div>");
            }else if(splitStrategy.equals(splitColOptions[1])){
                printWriter.println("\t\t\t<div id= \"onethirdsquarebox\" class= \"" + colors.get(0)+ " right\"></div>");
                printWriter.println("\t\t\t<div id= \"vertlineonethirdthick\" class= \"black right\"></div>");
                printWriter.println("\t\t\t<div id= \"twothirdsbyonethirdsbox\" class= \"" + colors.get(1)+ " right\"></div>");
            }else if(splitStrategy.equals(splitColOptions[2])){
                printWriter.println("\t\t\t<div id= \"twothirdsbyonethirdsbox\" class= \"" + colors.get(0)+ " right\"></div>");
                printWriter.println("\t\t\t<div id= \"vertlineonethirdthick\" class= \"black right\"></div>");
                printWriter.println("\t\t\t<div id= \"onethirdsquarebox\" class= \"" + colors.get(1)+ " right\"></div>");
            }
            printWriter.println("\t\t</div>");
        }else if(isThick){
            printWriter.println("\t\t<div id= \"twothirdsrow\">");
            if(splitStrategy.equals(splitColOptions[0])){
                printWriter.println("\t\t<div id= \"onethirdbytwothirdsbox\" class= \"" + colors.get(0)+ " right\"></div>");
                printWriter.println("\t\t\t<div id= \"vertlinetwothirdsthick\" class= \"black right\"></div>");
                printWriter.println("\t\t\t<div id= \"onethirdbytwothirdsbox\" class= \"" + colors.get(1)+ " right\"></div>");
                printWriter.println("\t\t\t<div id= \"vertlinetwothirdsthick\" class= \"black right\"></div>");
                printWriter.println("\t\t\t<div id= \"onethirdbytwothirdsbox\" class= \"" + colors.get(2)+ " right\"></div>");
            }else if(splitStrategy.equals(splitColOptions[1])){
                printWriter.println("\t\t\t<div id= \"onethirdbytwothirdsbox\" class= \"" + colors.get(0)+ " right\"></div>");
                printWriter.println("\t\t\t<div id= \"vertlinetwothirdsthick\" class= \"black right\"></div>");
                printWriter.println("\t\t\t<div id= \"twothirdsquarebox\" class= \"" + colors.get(1)+ " right\"></div>");
            }else if(splitStrategy.equals(splitColOptions[2])){
                printWriter.println("\t\t\t<div id= \"twothirdsquarebox\" class= \"" + colors.get(0)+ " right\"></div>");
                printWriter.println("\t\t\t<div id= \"vertlinetwothirdsthick\" class= \"black right\"></div>");
                printWriter.println("\t\t\t<div id= \"onethirdbytwothirdsbox\" class= \"" + colors.get(1)+ " right\"></div>");
            }
            printWriter.println("\t\t</div>");
        }
        
    }
    
    /**
     * Translate color, from 1,2,3,4 to white,red,blue,yellow 
     * @param colorCode
     * @return 
     */
    private String translateColor(String colorCode){
        String colorName = "white";
        if(colorCode.equals(RED_CODE)){
            colorName = "red";
        }else if(colorCode.equals(BLUE_CODE)){
            colorName = "blue";
        }if(colorCode.equals(YELLOW_CODE)){
            colorName = "yellow";
        }
        return colorName;
    }
        
    /**
     * print html header and footer, and determine the appropriate parameters
     * to draw the rows/cols of the painting and their color
     * @param tokens
     * @param id
     * @throws IOException 
     */
    public void printHTML(String[] tokens, int id) throws IOException{
        BufferedWriter bw = new BufferedWriter(new FileWriter(dest_path + "/" + id + ".html"));
        try (PrintWriter printWriter = new PrintWriter(bw)) {
            printWriter.println("<!DOCTYPE html>");
            printWriter.println("<html>");
            printWriter.println("<head>");
            printWriter.println("\t<title>" + id + "</title>");
            printWriter.println("\t<meta charset=\"utf-8\">");
            printWriter.println("\t<link href=\"styles/styles.css\" media=\"screen\" rel=\"stylesheet\" type=\"text/css\"/>");
            printWriter.println("</head>");
            printWriter.println("<body>");
            printWriter.println("\t<div id= \"canvas\">");
            boolean thickRow = false;
            if(tokens[0].equals(splitRowOptions[0])){
                thickRow = false;
            }else if(tokens[0].equals(splitRowOptions[1])){
                thickRow = true;
            }
            int indexColors = 3;
            ArrayList<String> colors = new ArrayList<>();
            
            if(tokens[1].equals(splitColOptions[0])){//3 first colors
                for(int i=0;i<3;i++){
                    String color = translateColor(tokens[indexColors]);
                    colors.add(color);
                    indexColors++;
                }
            }else if(tokens[1].equals(splitColOptions[1]) || tokens[1].equals(splitColOptions[2])){// 2 first colors
                for(int i=0;i<2;i++){
                    String color = translateColor(tokens[indexColors]);
                    colors.add(color);
                    indexColors++;
                }
            }
            printRow(printWriter,thickRow,tokens[1],colors);
            printWriter.println("\t\t<div id= \"horthickline\" class= \"black\"></div>");
            thickRow = !thickRow;
            colors = new ArrayList<>();
            if(tokens[2].equals(splitColOptions[0])){//3 first colors
                for(int i=0;i<3;i++){
                    String color = translateColor(tokens[indexColors]);
                    colors.add(color);
                    indexColors++;
                }
            }else if(tokens[2].equals(splitColOptions[1]) || tokens[2].equals(splitColOptions[2])){// 2 first colors
                for(int i=0;i<2;i++){
                    String color = translateColor(tokens[indexColors]);
                    colors.add(color);
                    indexColors++;
                }
            }
            printRow(printWriter,thickRow,tokens[2],colors);
            
            
            // END
            printWriter.println("\t</div>");
            printWriter.println("</body>");
            printWriter.println("</html>");
        }
    }
    
    /**
     * Iterate over the paintings and print each of them in the specified folder
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void printAll() throws FileNotFoundException, IOException{
        int painting_id = 0;
        try (Scanner sc = new Scanner(new FileReader(paintings_file_path))) {
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String[] tokens = line.split(",");
                printHTML(tokens,painting_id);
                painting_id++;
            }
            sc.close();
        }
    }
    
    /**
     * Entry Point, receives a file with encoded paintings (a set of 9 variables X1,...,X9)
     * and generates the corresponding htmls and stores them in the specified folder.
     * @param args
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void main(String args[]) throws FileNotFoundException, IOException{
        if(args.length==2){
            String paintings_path = args[0];
            String dest_path = args[1];
            GenerateHTMLs gh = new GenerateHTMLs(paintings_path,dest_path);
            gh.printAll();
        }else{
            System.err.println();
            System.err.println("ERROR: Wrong number of arguments");
            System.err.println();
            System.err.println("USAGE:");
            System.err.println();
            System.err.println("java -jar generateHTMLs.jar path_to_paintings dest_folder");
            System.err.println();            
        }
    }
}
