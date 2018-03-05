package br.com.fiap.listacompraappa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Cecilia_2 on 04/03/2018.
 */

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

    }

    public void sairSobre(final View v) {
        this.finish();
    }

}
