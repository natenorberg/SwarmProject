package softcomputing.project4.services;

import softcomputing.project4.enums.ClusteringAlgorithm;
import softcomputing.project4.enums.DataSetSource;

/**
 * This class is a central location to change all of the tunable parameters for the project
 * This class is passed in through dependency injection to all classes that need it
 */
public class TunableParameterService
{
    private static TunableParameterService _instance;
    private final DataSetSource _dataSet;
    private final double _sigmoidAlpha;
    private ClusteringAlgorithm _clusteringAlgorithm;
    
    
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

    // Private constructor
    private TunableParameterService()
    {
        // Properties will be initialized in here
        _clusteringAlgorithm = ClusteringAlgorithm.KMeans;
        _dataSet = DataSetSource.Placeholder;

        // Competitive network parameters
        _sigmoidAlpha = 0.5;
        
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

    public double getSigmoidAlpha()
    {
        return _sigmoidAlpha;
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
}
