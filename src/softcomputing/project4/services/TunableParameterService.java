package softcomputing.project4.services;

import softcomputing.project4.enums.ClusteringAlgorithm;
import softcomputing.project4.enums.DataSetSource;
import softcomputing.project4.enums.StopCondition;

/**
 * This class is a central location to change all of the tunable parameters for the project
 * This class is passed in through dependency injection to all classes that need it
 */
public class TunableParameterService
{
    private static TunableParameterService _instance;
    private final int _numRuns;
    private final DataSetSource _dataSet;
    private final double _sigmoidAlpha;
    private final int _numIterations;
    private final double _networkLearningRate;
    private final boolean _createOutputCsv;
    private ClusteringAlgorithm _clusteringAlgorithm;
    private StopCondition _stopCondition;
    private final int _numIterationsToConverge;

    // Print parameters
    private final boolean _printIntraClusterDistance;
    private final boolean _printInterClusterDistance;
    private final boolean _printDaviesBouldinIndex;
    private final boolean _saveAsSampleRun;
    
    //tunable parameters for ACO
	//size of grid
	private final int _x_size;
	private final int _y_size;
	private final int _ant_num;//number of ants
	private final int _visibility; //ant visibility
	//Dissimilarity measures 
	private final float _gamma;// small --> many clusters, big--> few poorly related clusters 
	private final float _gamma_1;
	private final float _gamma_2;
	
	//parameters for PSO
	private final int _n_particle;
	private final double _intertia;
	private final double _phi_pbest;
	private final double _phi_gbest;
	private final double _delta;

    // Private constructor
    private TunableParameterService()
    {
        // Properties will be initialized in here
        _clusteringAlgorithm = ClusteringAlgorithm.AntColony;
        _dataSet = DataSetSource.BreastCancer;
        _numRuns = 1;

        _stopCondition = StopCondition.Iterations;
        _numIterations = 300;
        _numIterationsToConverge = 2;

        // Print parameters
        _printIntraClusterDistance = true;
        _printInterClusterDistance = true;
        _printDaviesBouldinIndex = true;
        _saveAsSampleRun = true;

		_createOutputCsv = true;

        // Competitive network parameters
        _sigmoidAlpha = 0.5;
        _networkLearningRate = 0.15;
        
        // ACO parameters
    	//size of grid
    	 _x_size = 35;
    	 _y_size = 35 ;
    	 _ant_num= 100;//number of ants
    	 _visibility = 2; //final ant visibility 
    	//Dissimilarity measures 
    	_gamma = (float).5;// small --> many clusters, big--> few poorly related clusters 
    	_gamma_1 =(float).7;
    	_gamma_2 =(float) .8;
    	
    	
    	
    	// PSO parameters
    	_n_particle = 10;
    	_intertia = 5;//randomized
    	_phi_pbest= .06;
    	_phi_gbest = .7;
    	_delta = .3;//defines vMax for clamping
    }

    // Gets the singleton instance of this class
    public static TunableParameterService getInstance()
    {
        // Lazy load the instance
        if (_instance == null)
            _instance = new TunableParameterService();

        return _instance;
    }

    // Gets the number of runs for the experiment
    public int getNumRuns() { return _numRuns; }

    // Gets the algorithm type used in this run
    public ClusteringAlgorithm getClusteringAlgorithm() {
        return _clusteringAlgorithm;
    }

    // Gets the data set used in this run
    public DataSetSource getDataSet(){
        return _dataSet;
    }

    // Gets the stop condition
    public StopCondition getStopCondition() {
        return _stopCondition;
    }

    // Gets the number of iterations
    public int getNumberOfIterations() {
        return _numIterations;
    }

    // Gets the number of iterations that need to have the same classification to be considered converged
    public int getNumIterationsToConverge() {
        return _numIterationsToConverge;
    }

    // Getters for print booleans
    public boolean getPrintIntraClusterDistance() { return _printIntraClusterDistance; }
    public boolean getPrintInterClusterDistance() { return _printInterClusterDistance; }
    public boolean getPrintDaviesBouldinIndex() { return _printDaviesBouldinIndex; }
    public boolean getSaveAsSampleRun() { return  _saveAsSampleRun; }

    // Gets whether or not to create output csv files to create graphs
	public boolean getCreateOutputCsv() { return _createOutputCsv; }

    // Gets the alpha value used to stretch out sigmoid functions
    public double getSigmoidAlpha()
    {
        return _sigmoidAlpha;
    }

    /**
     * Gets the learning rate for competitive neural network
     * @return learning rate
     */
    public double getNetworkLearningRate() {
        return _networkLearningRate;
    }
    //Getters for ACO
    public int getXSize(){
    	return _x_size;
    }
    public int getYSize(){
    	return _y_size;
    }
    public int getAntNum(){
    	return _ant_num;
    }
    public int getAntVisibility(){
    	return _visibility;
    }
    public float getGamma(){
    	return _gamma;
    }
    public float getGamma1(){
    	return _gamma_1;
    }
    public float getGamma2(){
    	return _gamma_2;
    }
    //Getters for PSO
    public int getParticleNum(){
    	return _n_particle;
    }
    public double getintertia(){
    	return _intertia;
    }
    public double getPhiPbest(){
    	return _phi_pbest;
    }
    public double getPhiGbest(){
    	return _phi_gbest;
    }
    public double getDelta(){
    	return _delta;
    }
}
