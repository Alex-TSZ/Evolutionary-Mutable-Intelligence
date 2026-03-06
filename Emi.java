Testing
//So lets take what I have just learned and make a new type of AI from the ground up this might not be super good but sounds fun.
//DNA has 4 letters to it that are always in groups of 2 that match up A->T and G->C

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Emi{
    static Random rand = new Random();
    static ArrayList<Integer> DNA = new ArrayList<>();
    static ArrayList<Neuron> brain=new ArrayList<>();
    static HashMap<Integer, ArrayList<Integer>> neuronIndexMap = new HashMap<>();
    static HashMap<Integer, ArrayList<Integer>> taskNeuronIndexMap = new HashMap<>();
    static int task = 0;
    //This is for testing 
    static int lastDNAPos=2;

    //A pair of dna will look like 1->2 2->1 3->4 4->3
    //(3-x)+(floor(x/3)*4) seems simple and should work
    public static void GenerateDNAPair(){
        int randomDNA = rand.nextInt(4) +1; // range to [1, 4]
        //This is the next part but broken down so it makes more sense
        //int pair  = (3 - randomDNA) + (int)(Math.floor(randomDNA / 3.0) * 4); 1->2 2->1 3->4 4->3
        //int dnaStrand = (randomDNA << 16) | (pair & 0xFFFF); This is bit shifting the two ints into one int given the value never goes above 4.
        //DNA.add(dnaStrand);
        //System.out.println(randomDNA+" <-> "+pair);
        DNA.add((randomDNA << 16) | ((3 - randomDNA) + (int)(Math.floor(randomDNA / 3.0) * 4) & 0xFFFF));
    }

    public static void GenerateDNAPairFromGiven(int[] dnaString){
        for(int i=0;i<dnaString.length;i++){
            DNA.add((dnaString[i] << 16) | ((3 - dnaString[i]) + (int)(Math.floor(dnaString[i] / 3.0) * 4) & 0xFFFF));
        }
    }

    public static void ReadDNA(){
        int leftDNA;
        int rightDNA;
        for(int i=0;i<DNA.size();i++){
            leftDNA = (DNA.get(i) >>> 16) & 0xFFFF;
            rightDNA = DNA.get(i) & 0xFFFF;
            System.out.println(leftDNA + " <-> "+rightDNA);
        }
    }

    public static void ReadLeftDNA(){
        System.out.println("--------Left---------");
        int leftDNA;
        for(int i=0;i<DNA.size();i++){
            leftDNA = (DNA.get(i) >>> 16) & 0xFFFF;
            System.out.println(leftDNA);
        }
    }

    public static void ReadRightDNA(){
        System.out.println("--------Right---------");
        int rightDNA;
        for(int i=0;i<DNA.size();i++){
            rightDNA = DNA.get(i) & 0xFFFF;
            System.out.println(rightDNA);
        }
    }

    public static void CreateNeuron(int neuronName, int nueronType, int nueronPos){
        Neuron temp;
        switch(nueronPos){
            case 0: 
                temp = new Neuron(neuronName,0,nueronType);
                //The key that is set to 0 should be determined based off from the actual task being train not hard coded.
                taskNeuronIndexMap.computeIfAbsent(task, k->new ArrayList<>()).add(brain.size());
                brain.add(temp);
                break;
            case 2:
                temp = new Neuron(neuronName,2,nueronType); 
                brain.add(temp);
                break;
            default: 
                temp = new Neuron(neuronName,1,nueronType);
                brain.add(temp);
                break;
        }
    }

    public static void CreateConnection(int connectingNueronName, int connectingNueronType, int connectToNueronName, int connectToNueronType){
        ///*
        switch(connectingNueronType){
            case 0: 
                connectingNueronType=2;
                break;
            case 2: 
                connectingNueronType=0;
                break;
            default:
                break;
        }
        switch(connectToNueronType){
            case 0: 
                connectToNueronType=2;
                break;
            case 2: 
                connectToNueronType=0;
                break;
            default:
                break;
        }
        //*/
        
        System.out.println("NA: "+neuronIndexMap.get(connectingNueronName*10+connectingNueronType)+" NB: "+neuronIndexMap.get(connectToNueronName*10+connectToNueronType));
        if(neuronIndexMap.get(connectingNueronName*10+connectingNueronType)==null){
            return;
        }
        if(neuronIndexMap.get(connectToNueronName*10+connectToNueronType)==null){
            return;
        }
        ArrayList<Integer> neuronsA = neuronIndexMap.get(connectingNueronName*10+connectingNueronType);
        ArrayList<Integer> neuronsB = neuronIndexMap.get(connectToNueronName*10+connectToNueronType);

        int nName1Value = brain.get(neuronsA.get(0)).neuronName;
        int nName2Value = brain.get(neuronsB.get(0)).neuronName;
        int nName1ValuePos1 = nName1Value/100;
        int nName2ValuePos1 = nName2Value/100;
        int nName1ValuePos2 = (nName1Value%100)/10;
        int nName2ValuePos2 = (nName2Value%100)/10;
        int nName1ValuePos3 = nName1Value%10;
        int nName2ValuePos3 = nName2Value%10;

        System.out.println("N1P1: "+nName1ValuePos1+" N1P2: "+nName1ValuePos2+" N1P3: "+nName1ValuePos3+" N2P1: "+nName2ValuePos1+" N2P2: "+nName2ValuePos2+" N2P3: "+nName2ValuePos3);
        double connectionMidValue = Math.pow(-1,nName1ValuePos1/3)*.5+Math.pow(-1,nName1ValuePos2/3)*.25+Math.pow(-1,nName1ValuePos3/3)*.125+Math.pow(-1,nName2ValuePos1/3)*.0625+Math.pow(-1,nName2ValuePos2/3)*.03125+Math.pow(-1,nName2ValuePos3/3)*.015625;
        for(int i=0;i<neuronsA.size();i++){
            brain.get(neuronsA.get(i)).ConnectWthRanWeightRange(neuronsB,connectionMidValue);
        }
        System.out.println("Weight value:"+connectionMidValue);
        System.out.println("index 1: "+(connectingNueronName*10+connectingNueronType)+ "index 1: "+(connectToNueronName*10+connectToNueronType));
        System.out.println("Nueron 1: "+brain.get(neuronsA.get(0)).neuronName+" - Neuron Type: "+connectingNueronName+" - IHO Type:"+connectingNueronType +"|| Nueron 2: "+brain.get(neuronsB.get(0)).neuronName+" - Neuron Type: " +connectToNueronName+" - IHO Type:"+connectToNueronType);
        
    }

    public static void PrintBrain(){
        for(int i=0;i<brain.size();i++){
            brain.get(i).PrintNeuron();
        }
    }

    public static void CreateBrainLeftSide(){
        int leftDNAPos1;
        int leftDNAPos2;
        int leftDNAPos3;
        int savedNeuronTypeValue=-1;
        int savedType=-1;
        int type;
        int create_connect;
        int neuronTypeValue;
        int neuronNameValue;
        int connect=0; //To connect two neurons first we have to have two connect calls so this will keep track.
        //This is for testing i=2
        for(int i=lastDNAPos;i<DNA.size();i+=3){
            leftDNAPos1 = (DNA.get(i-2) >>> 16) & 0xFFFF;
            leftDNAPos2 = (DNA.get(i-1) >>> 16) & 0xFFFF;
            leftDNAPos3 = (DNA.get(i) >>> 16) & 0xFFFF;
            type = (leftDNAPos1+leftDNAPos2+leftDNAPos3)%4;
            //System.out.println("-------1------"+type);
            switch(type){
                case 0: 
                    type=0;
                    break;
                case 3: 
                    type=2;
                    break;
                default:
                    type=1;
                    break;
            }
            //System.out.println("-------2------"+type);
            create_connect=(leftDNAPos1+leftDNAPos3)%2;
            neuronTypeValue = leftDNAPos2*10+leftDNAPos3;
            neuronNameValue=leftDNAPos1*100+neuronTypeValue;
            System.out.println(leftDNAPos1+" "+leftDNAPos2+" "+leftDNAPos3+ " Type: "+ type+ " Create/Connection:"+create_connect);
            switch(create_connect+(create_connect*connect)){
                case 0:
                    neuronIndexMap.computeIfAbsent(neuronTypeValue*10+type, k->new ArrayList<>()).add(brain.size());
                    CreateNeuron(neuronNameValue,neuronTypeValue,type);
                    break;
                case 1: 
                    connect=1;
                    savedNeuronTypeValue=neuronTypeValue;
                    savedType=type;
                    break;
                case 2: 
                    CreateConnection(savedNeuronTypeValue,savedType, neuronTypeValue, type);
                    savedNeuronTypeValue=-1;
                    savedType=-1;
                    connect=0;
                    break;
                default: break;
            }
            //This is for testing 
            lastDNAPos+=3;
        }
        
    }

    public static void CreateBrainRightSide(){

    }
    //double[] inputs
    public static void PassValues(double[] inputs){
        //make a hash table that tells what problem uses what input nuerons.
        int indexSize=inputs.length;
        if(taskNeuronIndexMap.get(task)==null){
            return;
        }
        ArrayList<Integer> inputNeuronsPos = taskNeuronIndexMap.get(task);
        if(inputNeuronsPos.size()<=inputs.length){
            indexSize=inputNeuronsPos.size();
        }
        for(int i=0;i<indexSize;i++){
            brain.get(inputNeuronsPos.get(i)).GiveNeuronValue(inputs[i]);
        }
    }

    public static void ForwardPropagation(){
        
    }

    public static void PrintFullNeuronIndexMap(){
        for (HashMap.Entry<Integer, ArrayList<Integer>> entry : neuronIndexMap.entrySet()) {
            System.out.println("Key " + entry.getKey() + " -> " + entry.getValue());
        }
    }

    public static void PrintFullInputNeuronIndexMap(){
        for (HashMap.Entry<Integer, ArrayList<Integer>> entry : taskNeuronIndexMap.entrySet()) {
            System.out.println("Key " + entry.getKey() + " -> " + entry.getValue());
        }
    }

    public static void main(String[] args){
        int[] testDNA = {2,2,4, 2,1,4, 1,2,4, 3,1,4, 2,2,4, 2,1,4, 1,2,4, 3,1,4,};
        //int[] testDNA = {2,2,4};
        //int[] testDNA2 = {2,2,4};
        double[] testInput = {1,2,3};
        /*
        for(int i=0;i<150;i++){
            GenerateDNAPair();
        }
        //*/

        GenerateDNAPairFromGiven(testDNA);
        ReadDNA();
        //ReadLeftDNA();
        //ReadRightDNA();
        CreateBrainLeftSide();
        PassValues(testInput);
        PrintBrain();
        PrintFullNeuronIndexMap();
        PrintFullInputNeuronIndexMap();

    }
}
/*
\frac{1}{1+e^{-a\left(x-b\right)}}+\frac{axe^{-a\left(x+c\right)}}{f\left(1+e^{-a\left(x\right)}\right)^{2}}
*/