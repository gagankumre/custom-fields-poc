package in.clear.http.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

@MappedSuperclass
public class OEntity<T> {

    @Transient
    T originalObj;

    @Transient
    public T getOriginalObj(){
        return this.originalObj;
    }

    @PostLoad
    public void onLoad(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String serialized = mapper.writeValueAsString(this);
            this.originalObj = (T) mapper.readValue(serialized, this.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
