package volley.haydens.com.volley.model;

import volley.haydens.com.volley.core.GeneralResponse;

public class SampleResponse extends GeneralResponse {

   Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
