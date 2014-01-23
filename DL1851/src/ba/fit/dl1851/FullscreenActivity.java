package ba.fit.dl1851;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {

	Button btnViewPictures;
	Button btnCreatePicture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen);

		// Buttons
		btnViewPictures = (Button) findViewById(R.id.btnViewPictures);
		btnCreatePicture = (Button)findViewById(R.id.btnCreatePicture);

		//final View controlsView = findViewById(R.id.fullscreen_content_controls);
		//final View contentView = findViewById(R.id.fullscreen_content);

		// view products click event
		btnViewPictures.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching All pictures Activity
				Intent i = new Intent(getApplicationContext(), AllPicturesActivity.class);
                startActivity(i);
			}
		});
		
		btnCreatePicture.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent i = new Intent(getApplicationContext(), NewPictureActivity.class);
                startActivity(i);
 
            }
        });
    }
}