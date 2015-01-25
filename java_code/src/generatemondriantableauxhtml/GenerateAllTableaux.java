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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class exhaustively generates all the combinations of splits and colors
 * that can be coded with the following strategy:
 * 
 * perform row split
 *      or one-two (1)
 *      or two-one (2)
 * for each row generate column split
 *      whether one-one-one (X2,X3 = 1)
 *      or one-two (X2,X3 = 2)
 *      or two-one (X2,X3 = 3)
 * for each cell generate color
 *      whether white,red,blue,yellow (X4,X5,X6,X7,X8,X9 = 1,2,3,4)
 * 
 * @author Ignacio Arnaldo
 */
public class GenerateAllTableaux {
    int[] splitRowOptions, splitColOptions,splitRowTwoThirdsOptions;
    String[] colors;
    PrintWriter printWriter;
    String FILE_PATH;
    String DUMMY_VALUE = "5";
    int TABLEAU_SIZE = 9;
    
    /**
     * Constructor
     * @param aFILE_PATH
     * @throws IOException 
     */
    public GenerateAllTableaux(String aFILE_PATH) throws IOException{
        
        FILE_PATH = aFILE_PATH;
        
        // three ways of splitting the rows
        splitRowOptions = new int[2];
        splitRowOptions[0] = 1; // one two
        splitRowOptions[1] = 2; // two one
        
        // three ways of splitting a row
        splitColOptions = new int[3];
        splitColOptions[0] = 1; // one one one
        splitColOptions[1] = 2; // one two
        splitColOptions[2] = 3; // two one
        
        // in the case of a two one split of the rows, the thick road can be re-split
        splitRowTwoThirdsOptions = new int[2];
        splitRowTwoThirdsOptions[0] = 1; // one one
        splitRowTwoThirdsOptions[1] = 2; // two
        
        // four colors
        colors = new String[4];
        colors[0] = "1";//"white";
        colors[1] = "2";//"red";
        colors[2] = "3";//"blue";
        colors[3] = "4";//"yellow";
        
        BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH));
        printWriter = new PrintWriter(bw);
        
    }
    
    
    /**
     * Generate all the combinations of colors for the defined structure
     * @param addedColors
     * @param countCells
     * @param genTableau 
     */
    public void generateAllColors(int addedColors,int countCells, String genTableau){
        if(addedColors<countCells){
            for (String color : colors) {
                generateAllColors(addedColors+1, countCells, genTableau + "," + color);
            }
        }else{
            String[] tokens = genTableau.split(",");
            int length = tokens.length;
            int toAddDummyValues = TABLEAU_SIZE - length;
            for(int i=0;i<toAddDummyValues;i++){
                genTableau = genTableau + "," + DUMMY_VALUE;
            }
            printWriter.write(genTableau + "\n");
        }
    }
    
    /**
     * Generate the possible column splits
     * @param srOption
     * @param indexRow
     * @param numRows
     * @param genTableau 
     */
    public void generateAllColSplits(int srOption,int indexRow, int numRows,String genTableau){
        if(indexRow<numRows){
            for(int i=0;i<splitColOptions.length;i++){
                generateAllColSplits(srOption,indexRow+1,numRows, (genTableau + "," + splitColOptions[i]));
            }
        }else{
            int countCells = 0;
            String[] tokens = genTableau.split(",");
            if(tokens[1].equals("1")){
                countCells +=3;
            }else{
                countCells +=2;
            }
            if(tokens[2].equals("1")){
                countCells +=3;
            }else{
                countCells +=2;
            }
            generateAllColors(0,countCells,genTableau);
        }
    }
    
    /**
     * Generate the two possible row splits
     */
    public void generateAllSplits(){
        String genTableau = "";
        for(int i=0;i<splitRowOptions.length;i++){
            int numRows = 2;
            generateAllColSplits(splitRowOptions[i],0,numRows,(genTableau + splitRowOptions[i]));
        }
    }
    
    /**
     * Entry point 
     * @param args
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        if(args.length==1){
            String FILE_PATH = args[0];
            GenerateAllTableaux gt = new GenerateAllTableaux(FILE_PATH);
            // generate all the possible tableaux
            gt.generateAllSplits();
            gt.printWriter.flush();
            gt.printWriter.close();
        }else{
            System.err.println();
            System.err.println("ERROR: Wrong number of arguments");
            System.err.println();
            System.err.println("USAGE:");
            System.err.println();
            System.err.println("java -jar allPaintings.jar dest_paitings");
            System.err.println();
        }
    }   
}