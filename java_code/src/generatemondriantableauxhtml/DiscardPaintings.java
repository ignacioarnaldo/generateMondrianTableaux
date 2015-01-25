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
 * This class implements the methods necessary to discard "bad" paintings. 
 * @author Ignacio Arnaldo
 */
public class DiscardPaintings {
    
    String FILE_PATH_ALL, FILE_PATH_GOOD, FILE_PATH_BAD;
    
    PrintWriter printWriter_good, printWriter_bad;
    String[] splitRowOptions, splitColOptions;
    String WHITE_CODE = "1";
    String RED_CODE = "2";
    String BLUE_CODE = "3";
    String YELLOW_CODE = "4";
    
    /**
     * Constructor
     * @param afilePathAll
     * @param aPathGood
     * @param aPathBad
     * @throws IOException 
     */
    public DiscardPaintings(String afilePathAll, String aPathGood, String aPathBad) throws IOException{
        
        FILE_PATH_ALL = afilePathAll;
        FILE_PATH_GOOD = aPathGood;
        FILE_PATH_BAD = aPathBad;
        
        splitRowOptions = new String[2];
        splitRowOptions[0] = "1"; // one two
        splitRowOptions[1] = "2"; // two one
        
        // three ways of splitting a row
        splitColOptions = new String[3];
        splitColOptions[0] = "1"; // one one one
        splitColOptions[1] = "2"; // one two
        splitColOptions[2] = "3"; // two one
        
        
        BufferedWriter bw_good = new BufferedWriter(new FileWriter(FILE_PATH_GOOD));
        printWriter_good = new PrintWriter(bw_good);
        
        BufferedWriter bw_bad = new BufferedWriter(new FileWriter(FILE_PATH_BAD));
        printWriter_bad = new PrintWriter(bw_bad);
    }
    
    /**
     * This method iterates over all the painting in the specified file
     * and discards them if the satisfy one of the following conditions:
     * 1) it has 4 cells and 0 white cells
     * 2) it has 5-6 cells and 0-1 white cells
     * 3) only has white+1 color
     * 4) it has 5-6 cells and white+2 colors
     * 5) consequent cells have the same color
     * 6) cells of same size on top of each other have the same color
     * @throws FileNotFoundException 
     */
    public void classifyPaintings() throws FileNotFoundException{
        try (Scanner sc = new Scanner(new FileReader(FILE_PATH_ALL))) {
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String[] tokens = line.split(",");
                if(discardCondition1(tokens)){
                    printWriter_bad.write(line + "\n");
                }else if(discardCondition2(tokens)){
                    printWriter_bad.write(line + "\n");
                }else if(discardCondition3(tokens)){
                    printWriter_bad.write(line + "\n");
                }else if(discardCondition4(tokens)){
                    printWriter_bad.write(line + "\n");
                }else if(discardCondition5(tokens)){
                    printWriter_bad.write(line + "\n");
                }else if(discardCondition6(tokens)){
                    printWriter_bad.write(line + "\n");
                }else{// TABLEAU IS GOOD!
                    printWriter_good.write(line + "\n");
                }
            }
            sc.close();
        }
        printWriter_good.close();
        printWriter_bad.close();
    }
    
    /**
     * Condition number 1 to discard painting
     * 1) it has 4 cells and 0 white cells
     * @param tokens
     * @return 
     */
    public boolean discardCondition1(String[] tokens){
        boolean verifiedCondition = false;
        if( (tokens[1].equals(splitColOptions[1])||tokens[1].equals(splitColOptions[2])) && (tokens[2].equals(splitColOptions[1])||tokens[2].equals(splitColOptions[2])) ){
            int countWhites = 0;
            for(int i=3;i<6;i++){
                if(tokens[i].equals(WHITE_CODE))countWhites++;
            }
            if(countWhites==0) verifiedCondition = true;
        }
        return verifiedCondition;
    }
    
    /**
     * Condition number 2 to discard painting
     * 2) it has 5-6 cells and 0-1 white cells
     * @param tokens
     * @return 
     */
    public boolean discardCondition2(String[] tokens){
        boolean verifiedCondition = false;
        if( tokens[1].equals(splitColOptions[0]) || tokens[2].equals(splitColOptions[0]) ){
            int countWhites = 0;
            for(int i=3;i<8;i++){
                if(tokens[i].equals(WHITE_CODE))countWhites++;
            }
            if(countWhites<=1) verifiedCondition = true;
        }
        return verifiedCondition;
    }
    
    /**
     * Condition number 3 to discard painting
     * 3) only has white+1 color
     * @param tokens
     * @return 
     */
    public boolean discardCondition3(String[] tokens){
        boolean verifiedCondition = false;
        ArrayList<String> seenColors = new ArrayList<>();
        for(int i=3;i<8;i++){
            if(tokens[i].equals(RED_CODE) || tokens[i].equals(BLUE_CODE) || tokens[i].equals(YELLOW_CODE)){//red - blue - yellow
                if(!seenColors.contains(tokens[i])){
                    seenColors.add(tokens[i]);
                }
            }
        }
        if (seenColors.size()<=1){
            verifiedCondition = true;
        }
        return verifiedCondition;
    }
    
    /**
     * Condition number 4 to discard painting
     * 4) it has 5-6 cells and white+2 colors
     * @param tokens
     * @return 
     */
    public boolean discardCondition4(String[] tokens){
        boolean verifiedCondition = false;
        if( tokens[1].equals(splitColOptions[0]) || tokens[2].equals(splitColOptions[0]) ){
            ArrayList<String> seenColors = new ArrayList<>();
            for(int i=3;i<8;i++){
                if(tokens[i].equals(RED_CODE) || tokens[i].equals(BLUE_CODE) || tokens[i].equals(YELLOW_CODE)){//red - blue - yellow
                    if(!seenColors.contains(tokens[i])){
                        seenColors.add(tokens[i]);
                    }
                }
            }
            if (seenColors.size()<=2){
                verifiedCondition = true;
            }
        }
        return verifiedCondition;
    }
    
    /**
     * Condition number 5 to discard painting
     * 5) consequent cells have the same color
     * @param tokens
     * @return 
     */
    public boolean discardCondition5(String[] tokens){
        boolean verifiedCondition = false;
        // FIRST LINE
        if(tokens[1].equals(splitColOptions[0])){
            if(tokens[3].equals(tokens[4]) || tokens[4].equals(tokens[5])){
                verifiedCondition = true;
            }
        }else{
            if(tokens[3].equals(tokens[4])){
                verifiedCondition = true;
            }
        }
        if(verifiedCondition)return verifiedCondition;
        // SECOND LINE
        if(tokens[1].equals(splitColOptions[0])){
            if(tokens[2].equals(splitColOptions[0])){
                if(tokens[6].equals(tokens[7]) || tokens[7].equals(tokens[8])){
                    verifiedCondition = true;
                }
            }else{
                if(tokens[6].equals(tokens[7])){
                    verifiedCondition = true;
                }
            }
        }else{
            if(tokens[2].equals(splitColOptions[0])){
                if(tokens[5].equals(tokens[6]) || tokens[6].equals(tokens[7])){
                    verifiedCondition = true;
                }
            }else{
                if(tokens[5].equals(tokens[6])){
                    verifiedCondition = true;
                }
            }
        }
        return verifiedCondition;
    }
   
    /**
     * Condition number 6 to discard painting
     * 6) cells of same size on top of each other have the same color  
     * @param tokens
     * @return 
     */
    public boolean discardCondition6(String[] tokens){
        boolean verifiedCondition = false;
        // FIRST LINE
        if(tokens[1].equals(splitColOptions[0])){
            if(tokens[2].equals(splitColOptions[0])){
                if(tokens[3].equals(tokens[6]) || tokens[4].equals(tokens[7]) || tokens[5].equals(tokens[8])){
                    verifiedCondition = true;
                }
            }else if(tokens[2].equals(splitColOptions[1])){
                if(tokens[3].equals(tokens[6]) || tokens[4].equals(tokens[7]) || tokens[5].equals(tokens[7])){
                    verifiedCondition = true;
                }
            }else if(tokens[2].equals(splitColOptions[2])){
                if(tokens[3].equals(tokens[6]) || tokens[4].equals(tokens[6]) || tokens[5].equals(tokens[7])){
                    verifiedCondition = true;
                }
            }    
        }else if(tokens[1].equals(splitColOptions[1])){
            if(tokens[2].equals(splitColOptions[0])){
                if(tokens[3].equals(tokens[5]) || tokens[4].equals(tokens[6]) || tokens[4].equals(tokens[7])){
                    verifiedCondition = true;
                }
            }else if(tokens[2].equals(splitColOptions[1])){
                if(tokens[3].equals(tokens[5]) || tokens[4].equals(tokens[6]) ){
                    verifiedCondition = true;
                }
            }else if(tokens[2].equals(splitColOptions[2])){
                if(tokens[3].equals(tokens[5]) || tokens[4].equals(tokens[5]) || tokens[4].equals(tokens[6]) ){
                    verifiedCondition = true;
                }
            }
        }else if(tokens[1].equals(splitColOptions[2])){
            if(tokens[2].equals(splitColOptions[0])){
                if(tokens[3].equals(tokens[5]) || tokens[3].equals(tokens[6]) || tokens[4].equals(tokens[7]) ){
                    verifiedCondition = true;
                }
            }else if(tokens[2].equals(splitColOptions[1])){
                if(tokens[3].equals(tokens[5]) || tokens[3].equals(tokens[6]) || tokens[4].equals(tokens[6]) ){
                    verifiedCondition = true;
                }
            }else if(tokens[2].equals(splitColOptions[2])){
                if(tokens[3].equals(tokens[5]) || tokens[4].equals(tokens[6]) ){
                    verifiedCondition = true;
                }
            }
        }
            
        return verifiedCondition;
    }
    
    /**
     * Entry point - main method
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException{
        if(args.length==3){
            String FILE_PATH_ALL = args[0];
            String FILE_PATH_GOOD = args[1];
            String FILE_PATH_BAD = args[2];           
            DiscardPaintings dp = new DiscardPaintings(FILE_PATH_ALL,FILE_PATH_GOOD,FILE_PATH_BAD);
            dp.classifyPaintings();
        }else{
            System.err.println();
            System.err.println("ERROR: Wrong number of arguments");
            System.err.println();
            System.err.println("USAGE:");
            System.err.println();
            System.err.println("java -jar discardPaintings.jar path_to_all_paintings dest_path_to_good des_path_to_bad");
            System.err.println();
        }
        
    }
}