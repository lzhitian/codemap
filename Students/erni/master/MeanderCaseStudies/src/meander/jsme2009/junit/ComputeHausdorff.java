package meander.jsme2009.junit;

import java.util.ArrayList;
import java.util.Collection;

import ch.deif.meander.HausdorffDistance;
import ch.deif.meander.Map;
import ch.deif.meander.MapBuilder;
import ch.deif.meander.Serializer;
import ch.deif.meander.Serializer.MSELocation;
import ch.deif.meander.Serializer.MSEProject;
import ch.deif.meander.Serializer.MSERelease;

public class ComputeHausdorff implements Runnable {

    private Collection<Map> maps;

    public ComputeHausdorff(Collection<Map> maps) {
        super();
        this.maps = maps;
    }

    public ComputeHausdorff(Serializer ser) {
        this.maps = new ArrayList<Map>();
        MSEProject proj = ser.model().all(MSEProject.class).iterator().next();
        Map map = null;
        for (MSERelease rel: proj.releases) {
            MapBuilder builder = Map.builder();
            for (MSELocation each: rel.locations) {
                builder.location(each.x, each.y, each.height);
            }
            Map each = builder.build();
            each.name = rel.name;
            map = each;
            maps.add(map);
        }
    }

    public void run() {
        Map prev = null;
        HausdorffDistance hausdorff = new HausdorffDistance();
        for (Map each: maps) {
            if (prev != null) {
                System.out.println("\t\t\t\t" + hausdorff.distance(each, prev));
            }
            System.out.println(each.name);
            prev = each;
        }
    }
    
    
    
}