package ch.deif.meander.map;

import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.TaskValue;
import ch.akuhn.values.Value;
import ch.deif.meander.DigitalElevationModel;
import ch.deif.meander.MapInstance;
import ch.deif.meander.util.MapScheme;

public class ComputeElevationModelTask extends TaskValue<DigitalElevationModel> {

    public ComputeElevationModelTask(Value<MapInstance> mapInstance, Value<MapScheme<Boolean>> hills) {
        super("Creating digital elevation model", mapInstance, hills);
    }

    @Override
    protected DigitalElevationModel computeValue(ProgressMonitor monitor, Arguments arguments) {
        // TODO Auto-generated method stub
        return null;
    }

}
