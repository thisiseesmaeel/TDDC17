import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class TutorialController extends Controller {
	
	DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US); 
    public SpringObject object;

    ComposedSpringObject cso;

    /* These are the agents senses (inputs) */
	DoubleFeature x; /* Positions */
	DoubleFeature y;
	DoubleFeature vx; /* Velocities */
	DoubleFeature vy;
	DoubleFeature angle; /* Angle */

    /* Example:
     * x.getValue() returns the vertical position of the rocket 
     */

	/* These are the agents actuators (outputs)*/
	RocketEngine leftRocket;
	RocketEngine middleRocket;
	RocketEngine rightRocket;

    /* Example:
     * leftRocket.setBursting(true) turns on the left rocket 
     */
	
	public void init() {
		cso = (ComposedSpringObject) object;
		x = (DoubleFeature) cso.getObjectById("x");
		y = (DoubleFeature) cso.getObjectById("y");
		vx = (DoubleFeature) cso.getObjectById("vx");
		vy = (DoubleFeature) cso.getObjectById("vy");
		angle = (DoubleFeature) cso.getObjectById("angle");

		leftRocket = (RocketEngine) cso.getObjectById("rocket_engine_left");
		rightRocket = (RocketEngine) cso.getObjectById("rocket_engine_right");
		middleRocket = (RocketEngine) cso.getObjectById("rocket_engine_middle");
	}

    public void tick(int currentTime) {

    	/* TODO: Insert your code here */
    	int threshold = -100;

//    		System.out.println("x position is: " + this.x.getValue());
//    		System.out.println("y position is: " + this.y.getValue());
    	//System.out.println("vx is: " + this.vx.getValue());
    	//System.out.println("vy is: " + this.vy.getValue());
    	//System.out.println("angle is: " + this.angle.getValue());   

    	//System.out.println("Reward => " + StateAndReward.getRewardAngle(this.angle.getValue(), this.vx.getValue(), this.y.getValue()));
    	System.out.println(" SENSORS: a=" +  df.format(this.angle.getValue()) + " vx=" +  df.format(this.vx.getValue()) + 
				" vy=" +  df.format(this.vy.getValue()) + " P_STATE: " +  StateAndReward.getStateHover(this.angle.getValue(), this.vx.getValue(), this.vy.getValue()) + 
				" P_REWARD: " +  df.format(StateAndReward.getRewardHover(this.angle.getValue(), this.vx.getValue(), this.vy.getValue())));
    	
    }

}
