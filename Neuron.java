import java.util.ArrayList;
import java.util.HashMap;

public class Neuron{
    public int neuronName;
    public int nueronPos; //This will be passed a value 0-2 0=input 1=hidden 2=output
    public int neuronType; //this will be a value 1->4 1->4 so 11 or 43 or 14
    public double neuronValue;
    public ArrayList<Integer> connectionList = new ArrayList<>();
    public ArrayList<Double> connectionWeights = new ArrayList<>();
    public HashMap<Integer, Integer> neuronConnectedMap = new HashMap<>();

    public Neuron(int nName, int nPosIHO, int nType){
        this.neuronName=nName;
        this.nueronPos=nPosIHO;
        this.neuronType=nType;
    }

    public void ConnectListWthRanWeight(ArrayList<Integer> neuronList){
        double randomValue = Math.random(); // Gives a number in [0.0, 1.0)
        double randomWeight;
        for(int i=0;i<neuronList.size();i++){
            randomWeight = randomValue * 2 - 1; // Shifts the range to [-1.0, 1.0)
            this.connectionList.add(neuronList.get(i));
            this.connectionWeights.add(randomWeight);
        }
    }

    public void ConnectWthRanWeightRange( ArrayList<Integer> neuronList, double weight){
         double plusminValue = 0.015625;
         for(int i=neuronList.size()-1;i>=0;i--){
            switch(this.neuronConnectedMap.getOrDefault(neuronList.get(i),0)){
                case 0:
                    this.connectionList.add(neuronList.get(i));
                    this.connectionWeights.add((weight-plusminValue) + Math.random() * (2*plusminValue));
                    this.neuronConnectedMap.computeIfAbsent(neuronList.get(i),k->1);
                    i=-1;
                    break;
                default: break;
            }
        }
    }

    public void PrintNeuron(){
        System.out.println("Name: "+this.neuronName);
        System.out.println("Type: "+this.neuronType);
        System.out.println("Value: "+this.neuronValue);
        System.out.println("Input Hidden Output type: "+this.nueronPos);
        System.out.println("Connections: ");
        for(int i=0;i<this.connectionList.size();i++){
            System.out.print(this.connectionList.get(i)+" ");
        }
        System.out.println("\nweights: ");
        for(int i=0;i<this.connectionWeights.size();i++){
            System.out.print(this.connectionWeights.get(i)+" ");
        }
        System.out.println("\n");
    }

    public void GiveNeuronValue(double value){
        this.neuronValue=value;
    }
}