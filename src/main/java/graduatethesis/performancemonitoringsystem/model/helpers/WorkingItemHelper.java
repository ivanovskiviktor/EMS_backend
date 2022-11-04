package graduatethesis.performancemonitoringsystem.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkingItemHelper implements Comparable<WorkingItemHelper> {

    private Long id;

    private String name;

    private String label;

    private Boolean presentInEmployeeTrackingForm;

    @Override
    public int compareTo(WorkingItemHelper o) {
        if(o != null){
            if(o.getLabel().equals(this.getLabel())){
                return 1;
            }
        }
        return 0;
    }
}
