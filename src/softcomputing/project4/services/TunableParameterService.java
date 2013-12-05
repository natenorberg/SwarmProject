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
    private final DataSetSource _dataSet;
    private final double _sigmoidAlpha;
    private final int _numIterations;
    private final double _networkLearningRate;
    private ClusteringAlgorithm _clusteringAlgorithm;
    private StopCondition _stopCondition;
    private final int _numIterationsToConverge;
    
    
    //tunable parameters for ACO
	private final int _it_num; //number of iterations
	//size of grid
	private final int _x_size;
	private final int _y_size;
	private final int _ant_num;//number of ants
	private final int _n_patch; //ant visibility (always odd)
	//Dissimilarity measures 
	private final float _gamma;// small --> many clusters, big--> few poorly related clusters 
	private final float _gamma_1;
	private final float _gamma_2;
	
	//parameters for PSO
	private final int _n_particle;
	private final double _intertia;
	private final double _phi_pbest;
	private final double _phi_gbest;

    // Private constructor
    private TunableParameterService()
    {
        // Properties will be initialized in here
        _clusteringAlgorithm = ClusteringAlgorithm.KMeans;
        _dataSet = DataSetSource.Musk;
        _stopCondition = StopCondition.Iterations;
        _numIterations = 300;
        _numIterationsToConverge = 2;

        // Competitive network parameters
        _sigmoidAlpha = 0.5;
        _networkLearningRate = 0.01;
        
        // ACO parameters
        _it_num= 200; //number of iterations
    	//size of grid
    	 _x_size = 100;
    	 _y_size = 100 ;
    	 _ant_num= 30;//number of ants
    	 _n_patch = 3; //ant visibility (always odd)
    	//Dissimilarity measures 
    	_gamma = 2;// small --> many clusters, big--> few poorly related clusters 
    	_gamma_1 =4;
    	_gamma_2 =4;
    	
    	
    	
    	// PSO parameters
    	_n_particle = 10;
    	_intertia = .8;
    	_phi_pbest= .01;
    	_phi_gbest = .9;
    }

    // Gets the singleton instance of this class
    public static TunableParameterService getInstance()
    {
        // Lazy load the instance
        if (_instance == null)
            _instance = new TunableParameterService();

        return _instance;
    }

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

    public int getIterationNum(){
    	return _it_num;
    }
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
    	return _n_patch;
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
}
