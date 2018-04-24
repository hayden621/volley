package volley.haydens.com.volley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.List;

import volley.haydens.com.volley.core.GeneralResponse;
import volley.haydens.com.volley.core.GenericApiHelper;
import volley.haydens.com.volley.model.Data;
import volley.haydens.com.volley.model.SampleResponse;

public class MainActivity extends AppCompatActivity implements SampleApiHelper.SampleApiCallbackListener{

    TextView firstNameTextView;
    TextView lastNameTextView;

    private static final int SAMPLE_API_RESPONSE_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstNameTextView = findViewById(R.id.firstNameTextView);
        lastNameTextView = findViewById(R.id.lasNameTextView);

        SampleApiHelper.getInstance().call(this,
                Request.Method.GET,
                getResources().getString(R.string.sample_endpoint),
                null,
                SampleResponse.class,
                SAMPLE_API_RESPONSE_CODE,
                this);
    }

    @Override
    public void onApiResponse(int responseCode, GeneralResponse response) {
        switch (responseCode) {
            case SAMPLE_API_RESPONSE_CODE:
                handleSampleResponse(response);
                break;
        }
    }

    @Override
    public void onApiError(int responseCode, String errorMsg) {
        switch (responseCode) {
            case SAMPLE_API_RESPONSE_CODE:
                break;
        }
    }

    private void handleSampleResponse(GeneralResponse response) {
        if(response instanceof SampleResponse) {
            SampleResponse sampleResponse = (SampleResponse) response;

            firstNameTextView.setText("First name:" + sampleResponse.getData().getFirst_name());
            lastNameTextView.setText("Last name:" + sampleResponse.getData().getLast_name());
        }
    }
}
